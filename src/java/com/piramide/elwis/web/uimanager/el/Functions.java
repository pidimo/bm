package com.piramide.elwis.web.uimanager.el;

import com.piramide.elwis.dto.uimanager.ElementChildWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleAttributeWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleElementWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleSectionWrapperDTO;
import com.piramide.elwis.utils.UIManagerConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.uimanager.xmlprocessor.XMLProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.*;

/**
 * Alfacentauro Team
 * <p/>
 * UIManager jstl functions
 *
 * @author miky
 * @version $Id: Functions.java 12327 2016-01-29 19:45:06Z miguel $
 */
public class Functions {

    private static Log log = LogFactory.getLog(com.piramide.elwis.web.uimanager.el.Functions.class);

    /**
     * get list of constants to an style attribute
     *
     * @param attributeType the attribute type of the style attribute
     * @param sRequest      ServletRequest
     * @return List, with the constants
     */
    public static List getAttributeValuesForType(String attributeType, ServletRequest sRequest) {
        ArrayList listConstants = new ArrayList();
        HttpServletRequest request = (HttpServletRequest) sRequest;

        //get of the application context
        ArrayList listAtributeTypes = new ArrayList();
        Object obj = request.getSession().getServletContext().getAttribute(UIManagerConstants.XMLSTYLETYPES_KEY);
        if (obj != null) {
            listAtributeTypes = (ArrayList) obj;
        }

        for (Iterator iterator = listAtributeTypes.iterator(); iterator.hasNext();) {
            Map typeMap = (Map) iterator.next();

            if (typeMap.get("attributeName").equals(attributeType)) {
                if (typeMap.get("itemList") != null && ((List) typeMap.get("itemList")).size() > 0) {

                    for (Iterator iterator2 = ((List) typeMap.get("itemList")).iterator(); iterator2.hasNext();) {
                        Map itemMap = (Map) iterator2.next();
                        if (itemMap.get("itemName") != null && itemMap.get("itemValue") != null) {
                            //set label
                            String label = itemMap.get("itemName").toString();
                            if (!GenericValidator.isBlankOrNull((String) itemMap.get("itemResource"))) {
                                label = JSPHelper.getMessage(request, itemMap.get("itemResource").toString());
                            }

                            if (itemMap.get("itemUrl") != null) {
                                String urlImgPath = "/" + itemMap.get("itemUrl").toString();
                                listConstants.add(new LabelValueBean(label, urlImgPath));
                            } else {
                                listConstants.add(new LabelValueBean(label, itemMap.get("itemValue").toString()));
                            }
                        }
                    }
                }
            }
        }

        return listConstants;
    }

    /**
     * verif if the attribute is of type color
     *
     * @param attributeName
     * @param isUrl         if this attribute have url
     * @return true or false
     */
    public static boolean attributeIsOfTypeColor(String attributeName, boolean isUrl) {
        boolean res = false;
        if (attributeName.equals(UIManagerConstants.ATTRIBUTE_COLOR) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BACKGROUND_COLOR) ||
                (attributeName.equals(UIManagerConstants.ATTRIBUTE_BACKGROUND) && !isUrl) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BORDER_COLOR) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BORDER) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BORDER_LEFT) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BORDER_RIGHT) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BORDER_TOP) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BORDER_BOTTOM) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BACKGROUND_IMAGE) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_BOX_SHADOW) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_WEBKIT_BOX_SHADOW)) {
            res = true;
        }
        return res;
    }

    /**
     * verif if the values of the attribute be show in a select
     *
     * @param attributeName
     * @return true or false
     */
    public static boolean attributeShowInSelect(String attributeName) {
        boolean res = false;
        if (attributeName.equals(UIManagerConstants.ATTRIBUTE_FONT_SIZE) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_FONT_FAMILY) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_HEIGHT) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_WIDTH) ||
                attributeName.equals(UIManagerConstants.ATTRIBUTE_FONT_WEIGHT)) {
            res = true;
        }
        return res;
    }

    public static boolean attributeIsOfTypeGradient(String attributeName, String attributeType) {
        boolean res = false;
        if (UIManagerConstants.ATTRIBUTE_BACKGROUND_IMAGE.equals(attributeName) && UIManagerConstants.StyleAttributeType.GRADIENT.equal(attributeType)) {
            res = true;
        }
        return res;
    }

    public static boolean attributeIsOfTypeGradient(String attributeName, Map xmlStyleSheetMap, String styleClassName) {
        boolean res = false;
        if (UIManagerConstants.ATTRIBUTE_BACKGROUND_IMAGE.equals(attributeName) && xmlStyleSheetMap != null) {

            StyleAttributeWrapperDTO gradientAttributeWrapperDTO = findStyleAttributeWrapperDTO(xmlStyleSheetMap, styleClassName, attributeName);
            if (gradientAttributeWrapperDTO != null) {
                res = attributeIsOfTypeGradient(attributeName, gradientAttributeWrapperDTO.getAttributeType());
            }
        }
        return res;
    }

    public static boolean attributeIsOfTypeBoxShadow(String attributeName, String attributeType) {
        boolean res = false;
        if (UIManagerConstants.StyleAttributeType.BOX_SHADOW.equal(attributeType) &&
                ( UIManagerConstants.ATTRIBUTE_BOX_SHADOW.equals(attributeName) ||
                UIManagerConstants.ATTRIBUTE_WEBKIT_BOX_SHADOW.equals(attributeName) )) {

            res = true;
        }
        return res;
    }

    public static boolean attributeIsOfTypeBoxShadow(String attributeName, Map xmlStyleSheetMap, String styleClassName) {
        boolean res = false;
        if ((UIManagerConstants.ATTRIBUTE_BOX_SHADOW.equals(attributeName) || UIManagerConstants.ATTRIBUTE_WEBKIT_BOX_SHADOW.equals(attributeName))
                && xmlStyleSheetMap != null) {

            StyleAttributeWrapperDTO attributeWrapperDTO = findStyleAttributeWrapperDTO(xmlStyleSheetMap, styleClassName, attributeName);
            if (attributeWrapperDTO != null) {
                res = attributeIsOfTypeBoxShadow(attributeName, attributeWrapperDTO.getAttributeType());
            }
        }
        return res;
    }

    private static StyleAttributeWrapperDTO findStyleAttributeWrapperDTO(Map xmlStyleSheetMap, String styleClassName, String attributeName) {

        for (Iterator iterator = xmlStyleSheetMap.values().iterator(); iterator.hasNext(); ) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            StyleAttributeWrapperDTO attributeWrapperDTO = findStyleAttributeWrapperDTOInSection(sectionWrapperDTO, styleClassName, attributeName);
            if (attributeWrapperDTO != null) {
                return attributeWrapperDTO;
            }
        }

        return null;
    }

    private static StyleAttributeWrapperDTO findStyleAttributeWrapperDTOInSection(StyleSectionWrapperDTO sectionWrapperDTO, String styleClassName, String attributeName) {
        StyleAttributeWrapperDTO attributeWrapperDTO = null;

        List elements = sectionWrapperDTO.getElements();
        for (Iterator iterator2 = elements.iterator(); iterator2.hasNext(); ) {
            StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

            if (styleClassName.equals(elementWrapperDTO.getElementClass())) {

                attributeWrapperDTO = findStyleAttributeWrapperDTOInElement(elementWrapperDTO, attributeName);
                if (attributeWrapperDTO != null) {
                    break;
                }
            }
        }

        return attributeWrapperDTO;
    }

    private static StyleElementWrapperDTO findStyleElementWrapperDTOByName(StyleSectionWrapperDTO sectionWrapperDTO, String elementName) {

        for (Iterator iterator2 = sectionWrapperDTO.getElements().iterator(); iterator2.hasNext(); ) {
            StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();
            if (elementWrapperDTO.getElementName().equals(elementName)) {
                return elementWrapperDTO;
            }
        }
        return null;
    }

    private static StyleAttributeWrapperDTO findStyleAttributeWrapperDTOInElement(StyleElementWrapperDTO elementWrapperDTO, String attributeName) {
        List attributes = elementWrapperDTO.getAttributes();
        for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext(); ) {
            StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();

            if (attributeWrapperDTO.getAttributeName().equals(attributeName)) {
                return attributeWrapperDTO;
            }
        }
        return null;
    }

    /**
     * get the sections of the xml style sheet file
     *
     * @param sRequest ServletRequest
     * @return List, with the sections
     */
    public static List getSectionListOfXmlFile(ServletRequest sRequest) {
        log.debug("Executing method getSectionListOfXmlFile................");

        HttpServletRequest request = (HttpServletRequest) sRequest;
        List sectionList = new ArrayList();

        //get of the application context the object xmlStyleSheet
        Map sessionXmlStyleSheetMap = getSessionXmlStyleSheetMap(request);

        for (Iterator iterator = sessionXmlStyleSheetMap.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            if (sectionWrapperDTO.getSectionConfigurable().equals("true")) {
                sectionList.add(new LabelValueBean(sectionWrapperDTO.getSectionResource(), sectionWrapperDTO.getSectionName()));
            }
        }
        return sectionList;
    }

    /**
     * read the xml style sheet file and return an Map of StyleSectionWrapperDTO
     *
     * @param inputStream  an stream of the xml file
     * @param resourceFile is the resource path of this file
     * @return Map of StyleSectionWrapperDTO
     */
    public static Map readXmlStyleSheetFile(InputStream inputStream, String resourceFile) {
        log.debug("Executing method readXmlStyleSheetFile.........................");

        //read file
        /*String urlPath = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/uimanager/xmlstyles/elwis_style.xml";
        Map xmlStyleSheetMap = XMLProcessor.getStyleData(urlPath, XMLProcessor.URLTYPE, false,null);*/
        Map xmlStyleSheetMap = XMLProcessor.getStyleData(resourceFile, XMLProcessor.RELATIVEURLTYPE, false, inputStream);

        ArrayList data = (ArrayList) xmlStyleSheetMap.get("styleData");
        Map childElementsMap = (Map) xmlStyleSheetMap.get("sonsMap");

        //to section wrapper
        Map sectionMap = new LinkedHashMap();
        int keySection = 1;
        String sName;
        String sResource;
        String sConfigurable;

        //element
        String elementClass = null;
        String eName;
        String eResource;
        String eConfigurable;
        String eExtends;

        //attribute
        String aName;
        String aResource;
        String aConfigurable;
        String aXmlValue;
        String aNewValue;
        String aType;
        boolean aIsDefault;
        boolean aIsUrl;
        String aArguments;
        boolean aIsInherit;
        boolean aOverwrite;

        Iterator i = data.iterator();
        while (i.hasNext()) {
            HashMap hm_i = (HashMap) i.next();
            String type = hm_i.get("type").toString();

            if (type.equals("section_open")) {
                ///////////log.debug("section configurable....................................................." + hm_i.get("name"));
                sName = (hm_i.get("name") != null ? hm_i.get("name").toString() : "");
                sResource = (hm_i.get("resource") != null ? hm_i.get("resource").toString() : "");
                sConfigurable = ((hm_i.get("configurable") != null && hm_i.get("configurable").equals("true")) ? hm_i.get("configurable").toString() : "");

                boolean sectionView = false;
                StyleSectionWrapperDTO.initMapSectionWrapperDTO(sectionMap, String.valueOf(keySection), sName, sResource, sConfigurable, sectionView);
            }

            if (type.equals("element")) {

                /////////log.debug("------element configurable........................." + hm_i.get("name"));
                StyleElementWrapperDTO elementWrapperDTO = null;
                eName = hm_i.get("name").toString();
                eResource = (hm_i.get("resource") != null ? hm_i.get("resource").toString() : "");
                eConfigurable = ((hm_i.get("configurable") != null && hm_i.get("configurable").equals("true")) ? hm_i.get("configurable").toString() : "");
                elementClass = (!GenericValidator.isBlankOrNull((String) hm_i.get("class")) ? hm_i.get("class").toString() : UIManagerConstants.WITHOUT_CLASSNAME_KEY + "_" + eName);
                eExtends = (hm_i.get("extends") != null ? hm_i.get("extends").toString() : null);

                elementWrapperDTO = new StyleElementWrapperDTO(eName, eResource, eConfigurable, elementClass, eExtends);

                //attribute structure
                int attrPosition = 1;
                for (Iterator iterator = ((ArrayList) hm_i.get("data")).iterator(); iterator.hasNext();) {
                    Map aMap = (Map) iterator.next(); //attribute map

                    aName = (aMap.get("name") != null ? aMap.get("name").toString() : "");
                    aResource = (aMap.get("resource") != null ? aMap.get("resource").toString() : "");
                    aConfigurable = ((aMap.get("configurable") != null && aMap.get("configurable").equals("true")) ? aMap.get("configurable").toString() : "");
                    aXmlValue = (aMap.get("value") != null ? aMap.get("value").toString() : "");
                    aType = (aMap.get("type") != null ? aMap.get("type").toString() : "");
                    aArguments = (aMap.get("arguments") != null ? aMap.get("arguments").toString() : "");
                    aIsUrl = !GenericValidator.isBlankOrNull((String) aMap.get("url"));
                    aIsInherit = !GenericValidator.isBlankOrNull((String) aMap.get("isInherit"));//not is of the xml file, can find in element.java
                    aOverwrite = ((aMap.get("overwrite") != null && aMap.get("overwrite").equals("true")) ? true : false);

                    // set default values
                    if (Functions.attributeIsOfTypeColor(aName, aIsUrl)) {
                        aIsDefault = true;
                        aNewValue = aXmlValue;
                    } else {
                        if (aIsUrl) {
                            aIsDefault = true;
                            aXmlValue = "/" + aMap.get("url").toString();  //add "/" to show in the jsp page with the context path 
                            aNewValue = aXmlValue;
                        } else {
                            aIsDefault = false;
                            aNewValue = UIManagerConstants.DEFAULT_KEY;
                        }
                    }

                    StyleAttributeWrapperDTO attributeWrapperDTO = new StyleAttributeWrapperDTO(aName, aResource, aConfigurable, aXmlValue, aXmlValue, aNewValue, aType, aIsUrl, aIsDefault, aArguments, aIsInherit, attrPosition, aOverwrite);
                    attrPosition++;

                    elementWrapperDTO.addAttributeDTO(attributeWrapperDTO); //add attributes
                }
                //set child of this element
                if (childElementsMap.containsKey(elementWrapperDTO.getElementName())) {
                    ArrayList childs = (ArrayList) childElementsMap.get(elementWrapperDTO.getElementName());
                    for (Iterator iterator = childs.iterator(); iterator.hasNext();) {
                        String childName = (String) iterator.next();
                        ElementChildWrapperDTO childWrapperDTO = new ElementChildWrapperDTO(childName);
                        elementWrapperDTO.addElementChildDTO(childWrapperDTO);
                    }
                }

                StyleSectionWrapperDTO.addToMapElementDTO(sectionMap, String.valueOf(keySection), elementWrapperDTO);
            }

            if (type.equals("section_close")) {
                keySection++;
            }
        }

        return (sectionMap);
    }

    /**
     * read the attributeTypes.xml
     *
     * @param inputStream  an stream of the xml file
     * @param resourceFile is the resource path of this file
     * @return List with all types to styles
     */
    public static List readXmlStyleTypes(InputStream inputStream, String resourceFile) {
        //read file
        ArrayList listAtributeTypes = XMLProcessor.getAttributeTypes(resourceFile, XMLProcessor.RELATIVEURLTYPE, false, inputStream);
        return listAtributeTypes;
    }

    /**
     * get the new structure of StyleSectionWrapperDTO from xml style sheet structure
     *
     * @param request
     * @param paramSection param sent from jsp page
     * @return Map with sections wrapper DTO structure
     */
    public static Map getXmlStyleSheetEstructure(HttpServletRequest request, String paramSection, boolean getAllStructure) {
        log.debug("Excecuting method getXmlStyleSheetEstructure..................");

        //get of the application context the object xmlStyleSheet
        Map sessionXmlStyleSheetMap = getSessionXmlStyleSheetMap(request);

        //to section wrapper
        Map sectionStructureMap = new LinkedHashMap();
        int keySection = 1;
        String sName;
        String sResource;
        String sConfigurable;
        boolean sIsSectionView;

        //element
        String eName;
        String eClass = null;
        String eResource;
        String eConfigurable;

        //attribute
        String aName;
        String aResource;
        String aConfigurable;
        String aXmlValue;
        String aValue;
        String aNewValue;
        String aType;
        boolean aIsDefault;
        boolean aIsUrl;
        String aArguments;
        boolean aIsInherit;
        int aPosition;
        boolean aOverwrite;

        for (Iterator iterator = sessionXmlStyleSheetMap.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            if (sectionWrapperDTO.getSectionName().equals(paramSection) || getAllStructure) {
                sName = sectionWrapperDTO.getSectionName();
                sResource = sectionWrapperDTO.getSectionResource();
                sConfigurable = sectionWrapperDTO.getSectionConfigurable();

                if (sectionWrapperDTO.getSectionName().equals(paramSection)) {
                    sIsSectionView = true;
                } else {
                    sIsSectionView = false;
                }

                //create new structure of StyleSectionWrapperDTO
                StyleSectionWrapperDTO.initMapSectionWrapperDTO(sectionStructureMap, String.valueOf(keySection), sName, sResource, sConfigurable, sIsSectionView);

                List elements = sectionWrapperDTO.getElements();
                for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                    StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                    if (elementWrapperDTO.getElementConfigurable().equals("true") || getAllStructure) {

                        eName = elementWrapperDTO.getElementName();
                        eClass = elementWrapperDTO.getElementClass();
                        eResource = elementWrapperDTO.getElementResource();
                        eConfigurable = elementWrapperDTO.getElementConfigurable();

                        StyleElementWrapperDTO tempElementDTO = null;
                        tempElementDTO = new StyleElementWrapperDTO(eName, eResource, eConfigurable, eClass, elementWrapperDTO.getElementExtends());

                        List attributes = elementWrapperDTO.getAttributes();
                        for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext();) {
                            StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();

                            aName = attributeWrapperDTO.getAttributeName();
                            aResource = attributeWrapperDTO.getAttributeResource();
                            aConfigurable = attributeWrapperDTO.getAttributeConfigurable();
                            aXmlValue = attributeWrapperDTO.getAttributeXmlValue();
                            aValue = attributeWrapperDTO.getAttributeValue();
                            aNewValue = attributeWrapperDTO.getAttributeNewValue();
                            aType = attributeWrapperDTO.getAttributeType();
                            aIsDefault = attributeWrapperDTO.getIsDefault();
                            aIsUrl = attributeWrapperDTO.getIsUrl();
                            aArguments = attributeWrapperDTO.getAttributeArguments();
                            aIsInherit = attributeWrapperDTO.getIsInherit();
                            aPosition = attributeWrapperDTO.getAttributePosition();
                            aOverwrite = attributeWrapperDTO.getOverwrite();

                            //create new StyleAttributeWrapperDTO
                            StyleAttributeWrapperDTO tempAttributeDTO = new StyleAttributeWrapperDTO(aName, aResource, aConfigurable, aXmlValue, aValue, aNewValue, aType, aIsUrl, aIsDefault, aArguments, aIsInherit, aPosition, aOverwrite);
                            tempElementDTO.addAttributeDTO(tempAttributeDTO);//add attributes
                        }

                        //childs of this element
                        List childs = elementWrapperDTO.getElementChild();
                        for (Iterator iterator4 = childs.iterator(); iterator4.hasNext();) {
                            ElementChildWrapperDTO childWrapperDTO = (ElementChildWrapperDTO) iterator4.next();
                            tempElementDTO.addElementChildDTO(childWrapperDTO);
                        }

                        //add element the new StyleSectionWrapperDTO
                        StyleSectionWrapperDTO.addToMapElementDTO(sectionStructureMap, String.valueOf(keySection), tempElementDTO);
                    }
                }
                keySection++;
            }
        }

        return sectionStructureMap;
    }

    public static Map getSessionXmlStyleSheetMap(HttpServletRequest request) {
        //get of the application context the object xmlStyleSheet
        Map sessionXmlStyleSheetMap = new HashMap();

        Object obj;
        if (com.piramide.elwis.web.common.el.Functions.isBootstrapUIMode(request)) {
            obj = request.getSession().getServletContext().getAttribute(UIManagerConstants.XMLSTYLESHEET_BOOTSTRAP_KEY);
        } else {
            obj = request.getSession().getServletContext().getAttribute(UIManagerConstants.XMLSTYLESHEET_KEY);
        }

        if (obj != null) {
            sessionXmlStyleSheetMap = (Map) obj;
        }
        return sessionXmlStyleSheetMap;
    }

    public static Integer getStyleSheetTypeByUIMode(HttpServletRequest request) {
        Integer styleSheetType = UIManagerConstants.StyleSheetType.NORMAL.getConstant();

        if (com.piramide.elwis.web.common.el.Functions.isBootstrapUIMode(request)) {
            styleSheetType = UIManagerConstants.StyleSheetType.BOOTSTRAP.getConstant();
        }
        return styleSheetType;
    }


}
