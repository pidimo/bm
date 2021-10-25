package com.piramide.elwis.cmd.uimanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.uimanager.*;
import com.piramide.elwis.dto.uimanager.*;
import com.piramide.elwis.utils.UIManagerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: StyleSheetCmd.java 12517 2016-02-26 21:21:47Z miguel $
 */
public class StyleSheetCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {

        if ("read".equals(getOp())) {
            read();
        }
        if ("create".equals(getOp())) {
            create(ctx);
        }
        if ("update".equals(getOp())) {
            update(ctx);
        }
        if ("delete".equals(getOp())) {
            delete();
        }

    }

    public void read() {
        Map res = new HashMap();
        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        Integer styleSheetType = Integer.valueOf(paramDTO.get("styleSheetType").toString());

        Object companyStyle = paramDTO.get("companyStyle");

        StyleSheet styleSheet;
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);
        try {
            //if is company style
            if (companyStyle != null && companyStyle.equals(UIManagerConstants.COMPANY_STYLE_KEY)) {
                styleSheet = styleSheetHome.findByCompanyId(companyId, styleSheetType);
            } else {
                styleSheet = styleSheetHome.findByUserIdAndCompanyId(userId, companyId, styleSheetType);
            }

        } catch (FinderException fe) {
            log.debug("StyleSheet not found..............." + fe);
            styleSheet = null;
        }

        if (styleSheet != null) {
            StyleSheetDTO styleSheetDTO = new StyleSheetDTO();
            DTOFactory.i.copyToDTO(styleSheet, styleSheetDTO);
            resultDTO.putAll(styleSheetDTO);
            res.putAll(styleSheetDTO);

            Collection styles = styleSheet.getStyles();
            Collection styleDtos = new ArrayList();
            for (Iterator iterator = styles.iterator(); iterator.hasNext();) {
                Style style = (Style) iterator.next();
                Map mapStyle = new HashMap();
                StyleDTO styleDTO = new StyleDTO();
                DTOFactory.i.copyToDTO(style, styleDTO);
                mapStyle.putAll(styleDTO);

                Collection styleAttributes = style.getStyleAttributes();
                Collection styleAttributeDtos = new ArrayList();
                for (Iterator iterator2 = styleAttributes.iterator(); iterator2.hasNext();) {
                    StyleAttribute styleAttribute = (StyleAttribute) iterator2.next();
                    StyleAttributeDTO attributeDTO = new StyleAttributeDTO();
                    DTOFactory.i.copyToDTO(styleAttribute, attributeDTO);
                    styleAttributeDtos.add(attributeDTO);
                }
                mapStyle.put("attributeData", styleAttributeDtos);
                styleDtos.add(mapStyle);
            }

            res.put("styleData", styleDtos);
        }

        resultDTO.put("styleSheet", res);
    }

    public void create(SessionContext ctx) {

        Map xmlStyleSheetMap = (Map) paramDTO.get(UIManagerConstants.XMLSTYLESHEET_KEY);
        Map sections = (Map) paramDTO.get("sectionMap");
        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        Integer styleSheetType = Integer.valueOf(paramDTO.get("styleSheetType").toString());

        Object companyStyle = paramDTO.get("companyStyle");

        //elements changes
        Map elementChangeMap = getChangeElements(sections);
        elementChangeMap = findFathersInElementChangeMap(elementChangeMap, companyId, null);
        Map elementTempMap = new LinkedHashMap();

        StyleSheet styleSheet;
        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);
        try {
            //if is company style
            if (companyStyle != null && companyStyle.equals(UIManagerConstants.COMPANY_STYLE_KEY)) {
                userId = null;
                styleSheet = styleSheetHome.findByCompanyId(companyId, styleSheetType);
            } else {
                styleSheet = styleSheetHome.findByUserIdAndCompanyId(userId, companyId, styleSheetType);
            }
        } catch (FinderException fe) {
            styleSheet = null;
        }
        if (styleSheet != null) {
            //exist a style sheet for this user
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("Common.error.concurrency"); // concurrency message
            return;
        }

        if (thereChanges(sections) && styleSheet == null) {
            //create style sheet
            StyleSheetDTO styleSheetDTO = new StyleSheetDTO();
            styleSheetDTO.put("userId", userId);
            styleSheetDTO.put("companyId", companyId);
            styleSheetDTO.put("styleSheetType", styleSheetType);

            styleSheet = (StyleSheet) ExtendedCRUDDirector.i.create(styleSheetDTO, resultDTO, true);
            //StyleSheet styleSheet = (StyleSheet)ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE,styleSheetDTO,resultDTO,false,true,false,false);

            for (Iterator iterator = xmlStyleSheetMap.values().iterator(); iterator.hasNext();) {
                StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
                List elements = sectionWrapperDTO.getElements();

                for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                    StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();
                    String elementName = elementWrapperDTO.getElementName();

                    if (elementChangeMap.containsKey(elementName)) {
                        elementWrapperDTO = (StyleElementWrapperDTO) elementChangeMap.get(elementName);
                        elementWrapperDTO = lightlyCreate(elementWrapperDTO, styleSheet, companyId, ctx);
                    } else {

                        StyleElementWrapperDTO fatherElementWrapperDTO = getElementFather(elementChangeMap, elementName);
                        if (fatherElementWrapperDTO != null) {
                            //log.debug("hijo de...................................."+fatherElementWrapperDTO.getElementName());

                            //update attributes of child
                            elementWrapperDTO = updateAttributesOfTheChild(fatherElementWrapperDTO, elementWrapperDTO, companyId, null);

                            elementWrapperDTO = lightlyCreate(elementWrapperDTO, styleSheet, companyId, ctx);

                            //set to element changes map
                            elementChangeMap.put(elementName, elementWrapperDTO);

                            //verif if this child have oother childs
                            verifByMoreChilds(elementWrapperDTO, elementTempMap, elementChangeMap, styleSheet, companyId, ctx, CRUDDirector.OP_CREATE, false);

                        } else {
                            //add to temp element
                            elementTempMap.put(elementName, elementWrapperDTO);
                        }
                    }
                }
            }
            //set forward to update the page
            resultDTO.setForward("Change");
        }
    }

    public void update(SessionContext ctx) {
        Map xmlStyleSheetMap = (Map) paramDTO.get(UIManagerConstants.XMLSTYLESHEET_KEY);
        Map sections = (Map) paramDTO.get("sectionMap");
        Integer styleSheetId = Integer.valueOf(paramDTO.get("styleSheetId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        boolean restorePredetermined = false;
        if (paramDTO.get("restorePredetermined") != null) {
            restorePredetermined = true;
        }

        //elements changes
        Map elementChangeMap = getChangeElements(sections);
        elementChangeMap = findFathersInElementChangeMap(elementChangeMap, companyId, styleSheetId);
        Map elementTempMap = new LinkedHashMap();

        //update style sheet
        StyleSheetDTO styleSheetDTO = new StyleSheetDTO();
        styleSheetDTO.put("styleSheetId", styleSheetId);
        styleSheetDTO.put("version", paramDTO.get("version"));

        //check version
        StyleSheet styleSheet = (StyleSheet) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE, styleSheetDTO, resultDTO, true, true, false, false);

        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            if (styleSheet == null) { //bean deleted
                resultDTO.addResultMessage("Common.error.concurrency"); // concurrency message
            }
            return;
        } else {

            for (Iterator iterator = xmlStyleSheetMap.values().iterator(); iterator.hasNext();) {
                StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
                List elements = sectionWrapperDTO.getElements();

                for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                    StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                    String elementName = elementWrapperDTO.getElementName();
                    if (elementChangeMap.containsKey(elementName)) {
                        //update
                        elementWrapperDTO = (StyleElementWrapperDTO) elementChangeMap.get(elementName);
                        elementWrapperDTO = lightlyUpdate(elementWrapperDTO, styleSheet, companyId, ctx, restorePredetermined);
                    } else {
                        StyleElementWrapperDTO fatherElementWrapperDTO = getElementFather(elementChangeMap, elementName);
                        if (fatherElementWrapperDTO != null) {

                            //update attributes of child
                            elementWrapperDTO = updateAttributesOfTheChild(fatherElementWrapperDTO, elementWrapperDTO, companyId, styleSheetId);

                            //update
                            elementWrapperDTO = lightlyUpdate(elementWrapperDTO, styleSheet, companyId, ctx, restorePredetermined);

                            //set to element changes map
                            elementChangeMap.put(elementName, elementWrapperDTO);

                            //verif if this child have oother childs
                            verifByMoreChilds(elementWrapperDTO, elementTempMap, elementChangeMap, styleSheet, companyId, ctx, CRUDDirector.OP_UPDATE, restorePredetermined);

                        } else {
                            //add to temp element
                            elementTempMap.put(elementName, elementWrapperDTO);
                        }
                    }
                }
            }

            //verif if have styles to delete
            styleSheet = (StyleSheet) EJBFactory.i.callFinder(new StyleSheetDTO(), "findByPrimaryKey", new Object[]{styleSheetId});
            if (styleSheet.getStyles().isEmpty()) {
                delete();
            }
            //set forward to update the page
            resultDTO.setForward("Change");
        }

    }

    public void delete() {

        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        Integer styleSheetType = Integer.valueOf(paramDTO.get("styleSheetType").toString());

        Object companyStyle = paramDTO.get("companyStyle");

        StyleSheetHome styleSheetHome = (StyleSheetHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLESHEET);
        Collection collection = null;
        try {
            //if is company style
            if (companyStyle != null && companyStyle.equals(UIManagerConstants.COMPANY_STYLE_KEY)) {
                collection = styleSheetHome.findAllByCompanyId(companyId, styleSheetType);
            } else {
                collection = styleSheetHome.findAllByUserIdAndCompanyId(userId, companyId, styleSheetType);
            }
        } catch (FinderException fe) {
            collection = new ArrayList();
            log.debug("StyleSheet not found..............." + fe);
        }

        if (!collection.isEmpty()) {

            Object[] objects = collection.toArray();
            for (int i = 0; i < objects.length; i++) {
                StyleSheet styleSheet = (StyleSheet) objects[i];

                StyleSheetDTO styleSheetDTO = new StyleSheetDTO();
                styleSheetDTO.put("styleSheetId", styleSheet.getStyleSheetId());
                ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_DELETE, styleSheetDTO, resultDTO);
            }

            //set forward to update the page
            resultDTO.setForward("Change");
        }

    }

    private StyleElementWrapperDTO lightlyCreate(StyleElementWrapperDTO elementWrapperDTO, StyleSheet styleSheet, Integer companyId, SessionContext ctx) {

        Integer styleSheetId = styleSheet.getStyleSheetId();
        String elementClass = elementWrapperDTO.getElementClass();

        boolean styleClassIsCreated = false;
        List attributes = elementWrapperDTO.getAttributes();
        Integer styleId = null;

        for (int i = 0; i < attributes.size(); i++) {
            StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) attributes.get(i);

            if (!attributeWrapperDTO.getAttributeValue().equals(attributeWrapperDTO.getAttributeNewValue()) &&
                    !attributeWrapperDTO.getIsDefault() &&
                    !attributeWrapperDTO.getAttributeNewValue().equals(UIManagerConstants.DEFAULT_KEY)) {

                if (!styleClassIsCreated) {
                    //create style
                    StyleCmd styleCmd = new StyleCmd();
                    styleCmd.putParam("op", CRUDDirector.OP_CREATE);
                    styleCmd.putParam("name", elementClass);
                    styleCmd.putParam("styleSheetId", styleSheetId);
                    styleCmd.putParam("companyId", companyId);
                    styleCmd.executeInStateless(ctx);
                    styleId = (Integer) styleCmd.getResultDTO().get("styleId");

                    styleClassIsCreated = true;
                }

                if (styleId != null) {
                    //create attribute
                    StyleAttributeCmd styleAttributeCmd = new StyleAttributeCmd();
                    styleAttributeCmd.putParam("op", CRUDDirector.OP_CREATE);
                    styleAttributeCmd.putParam("name", attributeWrapperDTO.getAttributeName());
                    styleAttributeCmd.putParam("value", composeAttributeValue(attributeWrapperDTO));
                    styleAttributeCmd.putParam("styleId", styleId);
                    styleAttributeCmd.putParam("companyId", companyId);
                    styleAttributeCmd.executeInStateless(ctx);
                    Integer attributeId = (Integer) styleAttributeCmd.getResultDTO().get("attributeId");

                    //put in the wrapper
                    attributeWrapperDTO.setAttributeId(attributeId);
                    //replace this object
                    attributes.set(i, attributeWrapperDTO);
                }
            }
        }

        //overwrite attributes
        overwriteAttributes(elementWrapperDTO, styleSheet, companyId, ctx);

        //update element wrapper
        elementWrapperDTO.setStyleId(styleId);
        elementWrapperDTO.setAttributes(attributes);

        return elementWrapperDTO;
    }


    private StyleElementWrapperDTO lightlyUpdate(StyleElementWrapperDTO elementWrapperDTO, StyleSheet styleSheet, Integer companyId, SessionContext ctx, boolean restorePredetermined) {

        Integer styleSheetId = styleSheet.getStyleSheetId();
        String elementClass = elementWrapperDTO.getElementClass();

        boolean haveStyleId = (elementWrapperDTO.getStyleId() != null && elementWrapperDTO.getStyleId().intValue() != 0);
        List attributes = elementWrapperDTO.getAttributes();
        Integer styleId = elementWrapperDTO.getStyleId();

        for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext();) {
            StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();
            boolean haveAttributeId = (attributeWrapperDTO.getAttributeId() != null && attributeWrapperDTO.getAttributeId().intValue() != 0);

            if (restorePredetermined ||
                    attributeWrapperDTO.getAttributeXmlValue().equals(attributeWrapperDTO.getAttributeNewValue()) ||
                    attributeWrapperDTO.getIsDefault() ||
                    attributeWrapperDTO.getAttributeNewValue().equals(UIManagerConstants.DEFAULT_KEY)) {

                if (haveAttributeId && !attributeWrapperDTO.getOverwrite()) { //do not remove if is overwrite attribute
                    //remove attribute
                    StyleAttributeCmd styleAttributeCmd = new StyleAttributeCmd();
                    styleAttributeCmd.putParam("op", CRUDDirector.OP_DELETE);
                    styleAttributeCmd.putParam("attributeId", attributeWrapperDTO.getAttributeId());
                    styleAttributeCmd.executeInStateless(ctx);

                    //put in the wrapper
                    attributeWrapperDTO.setAttributeId(null);
                }

            } else if (!attributeWrapperDTO.getAttributeValue().equals(attributeWrapperDTO.getAttributeNewValue())) {
                if (haveAttributeId) {
                    //update attribute
                    StyleAttributeCmd styleAttributeCmd = new StyleAttributeCmd();
                    styleAttributeCmd.putParam("op", CRUDDirector.OP_UPDATE);
                    styleAttributeCmd.putParam("attributeId", attributeWrapperDTO.getAttributeId());
                    //styleAttributeCmd.putParam("name", attributeWrapperDTO.getAttributeName());
                    styleAttributeCmd.putParam("value", composeAttributeValue(attributeWrapperDTO));
                    styleAttributeCmd.executeInStateless(ctx);

                } else {

                    if (!haveStyleId) {
                        //create style
                        StyleCmd styleCmd = new StyleCmd();
                        styleCmd.putParam("op", CRUDDirector.OP_CREATE);
                        styleCmd.putParam("name", elementClass);
                        styleCmd.putParam("styleSheetId", styleSheetId);
                        styleCmd.putParam("companyId", companyId);
                        styleCmd.executeInStateless(ctx);
                        styleId = (Integer) styleCmd.getResultDTO().get("styleId");
                        haveStyleId = true;
                    }
                    if (styleId != null) {
                        //create attribute
                        StyleAttributeCmd styleAttributeCmd = new StyleAttributeCmd();
                        styleAttributeCmd.putParam("op", CRUDDirector.OP_CREATE);
                        styleAttributeCmd.putParam("name", attributeWrapperDTO.getAttributeName());
                        styleAttributeCmd.putParam("value", composeAttributeValue(attributeWrapperDTO));
                        styleAttributeCmd.putParam("styleId", styleId);
                        styleAttributeCmd.putParam("companyId", companyId);
                        styleAttributeCmd.executeInStateless(ctx);
                        Integer attributeId = (Integer) styleAttributeCmd.getResultDTO().get("attributeId");

                        //put in the wrapper
                        attributeWrapperDTO.setAttributeId(attributeId);
                    }
                }
            }
        }

        //overwrite attributes
        overwriteAttributes(elementWrapperDTO, styleSheet, companyId, ctx);

        //verif if have attributes to delete
        if (haveStyleId && styleId != null) {
            Style style = (Style) EJBFactory.i.callFinder(new StyleDTO(), "findByPrimaryKey", new Object[]{styleId});
            if (style.getStyleAttributes().isEmpty()) {
                //remove style
                StyleCmd styleCmd = new StyleCmd();
                styleCmd.putParam("op", CRUDDirector.OP_DELETE);
                styleCmd.putParam("styleId", styleId);
                styleCmd.executeInStateless(ctx);
                styleId = null;
            }
        }

        //update in wrapper
        elementWrapperDTO.setStyleId(styleId);
        elementWrapperDTO.setAttributes(attributes);

        return elementWrapperDTO;
    }


    /**
     * verif if there changes make for the user
     *
     * @param sections map of sections with style class
     * @return true or false
     */
    private boolean thereChanges(Map sections) {
        log.debug("Executing method thereChanges...................................");
        for (Iterator iterator = sections.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
            List elements = sectionWrapperDTO.getElements();

            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();
                List attributes = elementWrapperDTO.getAttributes();

                for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext();) {
                    StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();
                    if (!attributeWrapperDTO.getAttributeValue().equals(attributeWrapperDTO.getAttributeNewValue()) &&
                            !attributeWrapperDTO.getIsDefault() &&
                            !attributeWrapperDTO.getAttributeNewValue().equals(UIManagerConstants.DEFAULT_KEY)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * compose the attribute value if have arguments
     *
     * @param attributeWrapperDTO
     * @return String , the attribute value
     */
    private String composeAttributeValue(StyleAttributeWrapperDTO attributeWrapperDTO) {
        String res;
        String arguments = attributeWrapperDTO.getAttributeArguments().trim();
        if (attributeWrapperDTO.getIsUrl() && attributeWrapperDTO.getBackgroundKey().equals(UIManagerConstants.IS_MOSAIC_KEY)) {
            res = "url(" + UIManagerConstants.CONTEXT_PATH + attributeWrapperDTO.getAttributeNewValue() + ")";
        } else {
            res = attributeWrapperDTO.getAttributeNewValue();
        }
        if (arguments.length() > 0) {
            if (attributeWrapperDTO.getAttributeName().equals(UIManagerConstants.ATTRIBUTE_FONT_FAMILY)) {
                res = res + ", " + arguments;
            } else if (!(attributeWrapperDTO.getIsUrl() && attributeWrapperDTO.getBackgroundKey().equals(UIManagerConstants.IS_COLOR_KEY))) {
                res = res + UIManagerConstants.SEPARATOR_KEY + arguments;
            }
        }
        return res;
    }

    /**
     * get the element father of this elementName
     *
     * @param elementChangeMap the elements wath changer, possible father
     * @param elementName      name of an element, possible child
     * @return StyleElementWrapperDTO, element father of elementName
     */
    private StyleElementWrapperDTO getElementFather(Map elementChangeMap, String elementName) {

        StyleElementWrapperDTO result = null;
        for (Iterator iterator = elementChangeMap.values().iterator(); iterator.hasNext();) {
            StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator.next();

            //list of childs
            List childs = elementWrapperDTO.getElementChild();
            for (Iterator iterator2 = childs.iterator(); iterator2.hasNext();) {
                ElementChildWrapperDTO childWrapperDTO = (ElementChildWrapperDTO) iterator2.next();

                if (childWrapperDTO.getChildName().equals(elementName)) {
                    log.debug("have father ...");
                    result = elementWrapperDTO;
                    return result;
                }
            }
        }
        return result;
    }

    /**
     * update the data of the child
     *
     * @param fatherElementWrapperDTO father
     * @param childElementWrapperDTO  child
     * @param companyId
     * @param styleSheetId
     * @return StyleElementWrapperDTO , with the data updates
     */
    private StyleElementWrapperDTO updateAttributesOfTheChild(StyleElementWrapperDTO fatherElementWrapperDTO, StyleElementWrapperDTO childElementWrapperDTO, Integer companyId, Integer styleSheetId) {

        StyleHome styleHome = (StyleHome) EJBFactory.i.getEJBLocalHome(UIManagerConstants.JNDI_STYLE);
        Style style = null;
        if (styleSheetId != null) {
            try {
                style = styleHome.findByStyleSheetIdAndCompanyIdAndName(styleSheetId, companyId, childElementWrapperDTO.getElementClass());
            } catch (FinderException fe) {
                log.debug("Style not found...." + fe);
                if (FinderException.class.equals(fe.getClass())) {
                    //remove style duplicated
                    removeDuplicatedStyleClass(styleSheetId, childElementWrapperDTO.getElementClass());
                }
            }
        }

        List fatherAttributes = fatherElementWrapperDTO.getAttributes();
        List childAttributes = childElementWrapperDTO.getAttributes();
        for (Iterator iterator = fatherAttributes.iterator(); iterator.hasNext();) {
            StyleAttributeWrapperDTO fatherAttributeDTO = (StyleAttributeWrapperDTO) iterator.next();
            //boolean flagContain = false;
            for (int i = 0; i < childAttributes.size(); i++) {
                StyleAttributeWrapperDTO childAttributeDTO = (StyleAttributeWrapperDTO) childAttributes.get(i);
                if (childAttributeDTO.getIsInherit() && fatherAttributeDTO.getAttributeName().equals(childAttributeDTO.getAttributeName())) {

                    //set attributes of the father
                    childAttributeDTO.setAttributeXmlValue(fatherAttributeDTO.getAttributeXmlValue());
                    childAttributeDTO.setAttributeValue(fatherAttributeDTO.getAttributeValue());
                    childAttributeDTO.setAttributeNewValue(fatherAttributeDTO.getAttributeNewValue());
                    childAttributeDTO.setIsDefault(fatherAttributeDTO.getIsDefault());
                    childAttributeDTO.setIsUrl(fatherAttributeDTO.getIsUrl());
                    childAttributeDTO.setAttributeArguments(fatherAttributeDTO.getAttributeArguments());
                    childAttributeDTO.setBackgroundKey(fatherAttributeDTO.getBackgroundKey());

                    //set iDs
                    if (style != null) {
                        for (Iterator iterator3 = style.getStyleAttributes().iterator(); iterator3.hasNext();) {
                            StyleAttribute styleAttribute = (StyleAttribute) iterator3.next();
                            if (styleAttribute.getName().equals(childAttributeDTO.getAttributeName())) {
                                childAttributeDTO.setAttributeId(styleAttribute.getAttributeId());
                            }
                        }
                    }
                    //replace the attribute in list
                    childAttributes.set(i, childAttributeDTO);
                }
            }
        }

        if (style != null) {
            childElementWrapperDTO.setStyleId(style.getStyleId());
        }
        //update list of attributess
        childElementWrapperDTO.setAttributes(childAttributes);

        return childElementWrapperDTO;
    }

    /**
     * verify if this element have more childs
     *
     * @param elementWrapperDTO    the element
     * @param elementTempMap       Map with elements that not changed
     * @param elementChangeMap     Map with the elements that changed
     * @param styleSheet
     * @param companyId
     * @param ctx                  SessionContext
     * @param op                   operation (create, update)
     * @param restorePredetermined flag to restore default values
     */
    private void verifByMoreChilds(StyleElementWrapperDTO elementWrapperDTO, Map elementTempMap, Map elementChangeMap, StyleSheet styleSheet, Integer companyId, SessionContext ctx, String op, boolean restorePredetermined) {

        Integer styleSheetId = styleSheet.getStyleSheetId();
        List childs = (List) elementWrapperDTO.getElementChild();
        for (Iterator iterator3 = childs.iterator(); iterator3.hasNext();) {
            ElementChildWrapperDTO childWrapperDTO = (ElementChildWrapperDTO) iterator3.next();
            String childName = childWrapperDTO.getChildName();
            //verif in back
            if (elementTempMap.containsKey(childName)) {
                StyleElementWrapperDTO childElementWrapperDTO = (StyleElementWrapperDTO) elementTempMap.get(childName);

                if (op.equals(CRUDDirector.OP_CREATE)) {
                    childElementWrapperDTO = updateAttributesOfTheChild(elementWrapperDTO, childElementWrapperDTO, companyId, null);
                    childElementWrapperDTO = lightlyCreate(childElementWrapperDTO, styleSheet, companyId, ctx);
                }
                if (op.equals(CRUDDirector.OP_UPDATE)) {
                    childElementWrapperDTO = updateAttributesOfTheChild(elementWrapperDTO, childElementWrapperDTO, companyId, styleSheetId);
                    childElementWrapperDTO = lightlyUpdate(childElementWrapperDTO, styleSheet, companyId, ctx, restorePredetermined);
                }

                //set to element changes map
                elementChangeMap.put(childElementWrapperDTO.getElementName(), childElementWrapperDTO);

                //remove of elementTempMap
                elementTempMap.remove(childElementWrapperDTO.getElementName());

                //recursive
                verifByMoreChilds(childElementWrapperDTO, elementTempMap, elementChangeMap, styleSheet, companyId, ctx, op, restorePredetermined);
            }
        }

    }

    /**
     * find father inside of the Map with elements that changed
     *
     * @param elementChangeMap Map with the elements that changed
     * @param companyId
     * @return Map elementChangeMap with the change updates
     */
    private Map findFathersInElementChangeMap(Map elementChangeMap, Integer companyId, Integer styleSheetId) {

        for (Iterator iterator = elementChangeMap.values().iterator(); iterator.hasNext();) {
            StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator.next();
            List childs = elementWrapperDTO.getElementChild();
            for (Iterator iterator2 = childs.iterator(); iterator2.hasNext();) {
                ElementChildWrapperDTO childWrapperDTO = (ElementChildWrapperDTO) iterator2.next();
                String childName = childWrapperDTO.getChildName();

                if (elementChangeMap.containsKey(childName)) {
                    StyleElementWrapperDTO childElementWrapperDTO = (StyleElementWrapperDTO) elementChangeMap.get(childName);
                    childElementWrapperDTO = updateAttributesOfTheChild(elementWrapperDTO, childElementWrapperDTO, companyId, styleSheetId);

                    //put the child updated (rewrite)
                    elementChangeMap.put(childName, childElementWrapperDTO);

                    //update my childs if exist
                    elementChangeMap = updateMyChilds(childElementWrapperDTO, elementChangeMap, companyId, styleSheetId);
                }
            }

        }
        return elementChangeMap;
    }

    /**
     * update the childs of this element if have
     *
     * @param elementWrapperDTO element
     * @param elementChangeMap  Map with the elements that changed
     * @param companyId
     * @return Map elementChangeMap , with your childs updates
     */
    private Map updateMyChilds(StyleElementWrapperDTO elementWrapperDTO, Map elementChangeMap, Integer companyId, Integer styleSheetId) {
        List childs = elementWrapperDTO.getElementChild();
        for (Iterator iterator = childs.iterator(); iterator.hasNext();) {
            ElementChildWrapperDTO childWrapperDTO = (ElementChildWrapperDTO) iterator.next();
            String childName = childWrapperDTO.getChildName();

            if (elementChangeMap.containsKey(childName)) {
                StyleElementWrapperDTO childElementWrapperDTO = (StyleElementWrapperDTO) elementChangeMap.get(childName);
                childElementWrapperDTO = updateAttributesOfTheChild(elementWrapperDTO, childElementWrapperDTO, companyId, styleSheetId);

                //put the child updated (rewrite)
                elementChangeMap.put(childName, childElementWrapperDTO);
            }
        }

        return elementChangeMap;
    }

    /**
     * get an Map of StyleElementWrapperDTO only with the elements wath change
     *
     * @param sections Map of StyleSectionWrapperDTO
     * @return Map of StyleElementWrapperDTO
     */
    private Map getChangeElements(Map sections) {
        Map elementChangeMap = new HashMap();

        for (Iterator iterator = sections.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
            List elements = sectionWrapperDTO.getElements();

            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();
                elementChangeMap.put(elementWrapperDTO.getElementName(), elementWrapperDTO);
            }
        }
        return elementChangeMap;
    }

    /**
     * create or delete the attributes with overwrite
     *
     * @param elementWrapperDTO
     * @param styleSheet
     * @param companyId
     * @param ctx
     */
    private void overwriteAttributes(StyleElementWrapperDTO elementWrapperDTO, StyleSheet styleSheet, Integer companyId, SessionContext ctx) {
        Collection stylesCollection = styleSheet.getStyles();

        for (Iterator iterator = stylesCollection.iterator(); iterator.hasNext();) {
            Style style = (Style) iterator.next();
            Integer styleId = style.getStyleId();
            if (style.getName().equals(elementWrapperDTO.getElementClass())) {

                Collection attributeCollection = style.getStyleAttributes();
                List listAttributes = elementWrapperDTO.getAttributes();

                boolean existUserChanges = existUserAttributes(listAttributes, attributeCollection);
                for (Iterator iterator2 = listAttributes.iterator(); iterator2.hasNext();) {
                    StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator2.next();

                    if (attributeWrapperDTO.getOverwrite()) {
                        Collection attributeCollection2 = style.getStyleAttributes();
                        Object[] obj = attributeCollection2.toArray();
                        boolean flagCreate = true;
                        for (int i = 0; i < obj.length; i++) {
                            StyleAttribute styleAttribute = (StyleAttribute) obj[i];

                            if (styleAttribute.getName().equals(attributeWrapperDTO.getAttributeName())) { //verify if exist to not create
                                flagCreate = false;
                                if (!existUserChanges) {
                                    //remove attribute overwrite
                                    StyleAttributeCmd styleAttributeCmd = new StyleAttributeCmd();
                                    styleAttributeCmd.putParam("op", CRUDDirector.OP_DELETE);
                                    styleAttributeCmd.putParam("attributeId", styleAttribute.getAttributeId());
                                    styleAttributeCmd.executeInStateless(ctx);
                                }
                            }
                        }

                        if (flagCreate && existUserChanges) {
                            //set new value if is default value
                            if (attributeWrapperDTO.getAttributeNewValue().equals(UIManagerConstants.DEFAULT_KEY)) {
                                attributeWrapperDTO.setAttributeNewValue(attributeWrapperDTO.getAttributeXmlValue());
                            }

                            //create attribute overwrite
                            StyleAttributeCmd styleAttributeCmd = new StyleAttributeCmd();
                            styleAttributeCmd.putParam("op", CRUDDirector.OP_CREATE);
                            styleAttributeCmd.putParam("name", attributeWrapperDTO.getAttributeName());
                            styleAttributeCmd.putParam("value", composeAttributeValue(attributeWrapperDTO));
                            styleAttributeCmd.putParam("styleId", styleId);
                            styleAttributeCmd.putParam("companyId", companyId);
                            styleAttributeCmd.executeInStateless(ctx);
                        }
                    }
                }
            }
        }
    }

    /**
     * verif if the user have attributes that changed, without of the attributes with overwrite
     *
     * @param listAttributes
     * @param userAttributes
     * @return true or false
     */
    private boolean existUserAttributes(List listAttributes, Collection userAttributes) {

        for (Iterator iterator = listAttributes.iterator(); iterator.hasNext();) {
            StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator.next();

            for (Iterator iterator2 = userAttributes.iterator(); iterator2.hasNext();) {
                StyleAttribute styleAttribute = (StyleAttribute) iterator2.next();
                if (styleAttribute.getName().equals(attributeWrapperDTO.getAttributeName()) && !attributeWrapperDTO.getOverwrite()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * removed the duplicated class in the stylesheet of the user
     *
     * @param styleSheetId
     * @param styleClassName
     */
    private void removeDuplicatedStyleClass(Integer styleSheetId, String styleClassName) {

        StyleSheet styleSheet = (StyleSheet) EJBFactory.i.callFinder(new StyleSheetDTO(), "findByPrimaryKey", new Object[]{styleSheetId});
        Collection styles = styleSheet.getStyles();
        Object objStyles[] = styles.toArray();
        for (int i = 0; i < objStyles.length; i++) {
            Style style = (Style) objStyles[i];
            if (styleClassName.equals(style.getName())) {

                Collection attributes = style.getStyleAttributes();
                Object objAttributes[] = attributes.toArray();
                for (int j = 0; j < objAttributes.length; j++) {
                    StyleAttribute styleAttribute = (StyleAttribute) objAttributes[j];
                    EJBFactory.i.removeEJB(styleAttribute);
                }
                //remove style
                EJBFactory.i.removeEJB(style);
            }
        }
    }

}
