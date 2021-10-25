package com.piramide.elwis.web.uimanager.action;

import com.piramide.elwis.cmd.uimanager.StyleSheetCmd;
import com.piramide.elwis.dto.uimanager.StyleAttributeWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleElementWrapperDTO;
import com.piramide.elwis.dto.uimanager.StyleSectionWrapperDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.UIManagerConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.uimanager.el.Functions;
import com.piramide.elwis.web.uimanager.form.StyleSheetForm;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.*;

/**
 * Alfacentauro Team
 * used to forward to form the style sheet of an user or company
 *
 * @author miky
 * @version $Id: StyleSheetForwardAction.java 12260 2016-01-27 01:55:35Z miguel $
 */
public class StyleSheetForwardAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing StyleSheetForwardAction................");

        ActionForward forward;
        forward = null;

        readConstants(request, form);

        forward = new ActionForward(mapping.getParameter());

        return forward;
    }

    public void readConstants(HttpServletRequest request, ActionForm form) throws Exception {
        log.debug("Executing method readConstants........................." + request.getParameter("paramSection"));

        String paramSection = request.getParameter("paramSection");

        StyleSheetForm styleSheetForm = (StyleSheetForm) form;

        //if is company style
        String companyStyle = null;
        if (request.getParameter("companyStyleKey") != null) {
            companyStyle = request.getParameter("companyStyleKey");
            request.setAttribute("companyStyleKey", companyStyle);

        } else if (request.getAttribute("companyStyleKey") != null) {
            if (request.getAttribute("companyStyleKey").equals(UIManagerConstants.COMPANY_STYLE_KEY)) {
                companyStyle = request.getAttribute("companyStyleKey").toString();
                request.setAttribute("companyStyleKey", companyStyle);
            }
        }

        //read style sheet of the user
        Map mapUserStyleSheet = readStyleSheet(request, companyStyle);

        List userStyles = new ArrayList();
        if (!mapUserStyleSheet.isEmpty()) {
            userStyles = (List) mapUserStyleSheet.get("styleData");
            styleSheetForm.setDto("styleSheetId", mapUserStyleSheet.get("styleSheetId"));
            styleSheetForm.setDto("styleSheetType", mapUserStyleSheet.get("styleSheetType"));
            styleSheetForm.setDto("version", mapUserStyleSheet.get("version"));
        } else {
            styleSheetForm.setDto("version", new Integer(0));
            if (styleSheetForm.getDto("styleSheetId") != null) {
                styleSheetForm.setDto("styleSheetId", null);
            }
        }

        //get structure with default values
        Map sectionsViewMap = Functions.getXmlStyleSheetEstructure(request, paramSection, false);
        if (!UIManagerConstants.COMPANY_STYLE_KEY.equals(request.getAttribute("companyStyleKey"))) {
            //set company default values
            Map mapCompanyStyleSheet = readStyleSheet(request, UIManagerConstants.COMPANY_STYLE_KEY);
            List companyStyles = new ArrayList();
            if (!mapCompanyStyleSheet.isEmpty()) {
                companyStyles = (List) mapCompanyStyleSheet.get("styleData");
                request.setAttribute("flagChangeCompanyStyle", "true");
            }
            sectionsViewMap = setDefaultValuesOfTheCompany(sectionsViewMap, companyStyles);
        }

        Map sectionMap = getUIManagerStructure(sectionsViewMap, userStyles);

        styleSheetForm.setDto("sectionMap", sectionMap);
    }

    /**
     * execute StyleSheetCmd.java with op=read
     *
     * @param request
     * @param companyStyle flag to read the style sheet oof the company
     * @return Map with style sheet data of the user or company
     * @throws Exception
     */
    private Map readStyleSheet(HttpServletRequest request, String companyStyle) throws Exception {

        Map styleSheetMap = new HashMap();
        //read style sheet
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        StyleSheetCmd styleSheetCmd = new StyleSheetCmd();
        styleSheetCmd.putParam("op", UIManagerConstants.OP_READ);
        styleSheetCmd.putParam("userId", user.getValue("userId"));
        styleSheetCmd.putParam("companyId", user.getValue("companyId"));
        styleSheetCmd.putParam("styleSheetType", Functions.getStyleSheetTypeByUIMode(request));
        styleSheetCmd.putParam("companyStyle", companyStyle);
        ResultDTO resultDTO = BusinessDelegate.i.execute(styleSheetCmd, request);

        styleSheetMap = (Map) resultDTO.get("styleSheet");

        return styleSheetMap;
    }

    /**
     * Generates the DataStructure for generate the view of update UIManager
     *
     * @param sectionsViewMap the sections wath will change
     * @return the DataStructure
     */
    private Map getUIManagerStructure(Map sectionsViewMap, List userStyleClass) {
        log.debug("Executing method getUIManagerStructure.........................");

        //to section wrapper
        int keySection = 1;
        Map sectionMap = new HashMap();

        Map userStyleMap = new HashMap();
        Map userAttributeMap;
        Integer userAttributeId;
        String userAttributeValue;
        Integer userStyleId;

        for (Iterator iterator = sectionsViewMap.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            List elements = sectionWrapperDTO.getElements();
            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                //user styles
                userStyleMap = getStyleClassMapOfUser(userStyleClass, elementWrapperDTO.getElementClass());

                if (!userStyleMap.isEmpty()) {
                    List attributes = elementWrapperDTO.getAttributes();
                    for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext();) {
                        StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();

                        //set user data
                        userAttributeMap = getAttributeMapOfUser((List) userStyleMap.get("attributeData"), attributeWrapperDTO.getAttributeName());
                        if (!userAttributeMap.isEmpty()) {
                            userAttributeId = (Integer) userAttributeMap.get("attributeId");
                            userAttributeValue = discomposeAttributeValue((String) userAttributeMap.get("value"), attributeWrapperDTO);
                            attributeWrapperDTO.setAttributeId(userAttributeId);
                            attributeWrapperDTO.setAttributeValue(userAttributeValue);
                            attributeWrapperDTO.setAttributeNewValue(userAttributeValue);

                            if (Functions.attributeIsOfTypeColor(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getIsUrl())) {
                                if (!attributeWrapperDTO.getAttributeXmlValue().equals(userAttributeValue)) {
                                    attributeWrapperDTO.setIsDefault(false);
                                }
                            }
                            if (attributeWrapperDTO.getIsUrl()) {
                                if (!attributeWrapperDTO.getAttributeXmlValue().equals(userAttributeValue)) {
                                    attributeWrapperDTO.setIsDefault(false);
                                }

                                if (isHexadecimalColor(userAttributeValue)) {
                                    attributeWrapperDTO.setBackgroundKey(UIManagerConstants.IS_COLOR_KEY);
                                } else {
                                    attributeWrapperDTO.setBackgroundKey(UIManagerConstants.IS_MOSAIC_KEY);
                                }
                            }
                        }
                    }
                    userStyleId = (Integer) userStyleMap.get("styleId");
                    elementWrapperDTO.setStyleId(userStyleId);

                    //remove this styleclass of the user
                    if (!userStyleMap.isEmpty()) {
                        userStyleClass = removeThisStyleClass(userStyleClass, elementWrapperDTO.getElementClass());
                    }
                }
            }
            //put in sectionMap
            sectionMap.put(String.valueOf(keySection), sectionWrapperDTO);
            keySection++;

        }

        return (sectionMap);
    }

    /**
     * get Map with the data of an Style class of this user if have
     *
     * @param listStyleClass list style class of the user
     * @param styleClass     style class of the file xml
     * @return Map with the data of an style class
     */
    private Map getStyleClassMapOfUser(List listStyleClass, String styleClass) {
        //log.debug("Executing method getAttributesOfThisStyle............."+listStyleClass);
        Map map = new HashMap();
        for (Iterator iterator = listStyleClass.iterator(); iterator.hasNext();) {
            Map mapStyleClass = (Map) iterator.next();
            if (styleClass.equals(mapStyleClass.get("name").toString())) {
                map = mapStyleClass;
                return map;
            }
        }
        return map;
    }

    /**
     * get Map with the data of an attribute of this user if exist
     *
     * @param listAttributes list of attributes of the user
     * @param attributeName  attribute of the file xml
     * @return Map with attribute values if exist
     */
    private Map getAttributeMapOfUser(List listAttributes, String attributeName) {
        Map map = new HashMap();
        for (Iterator iterator = listAttributes.iterator(); iterator.hasNext();) {
            Map mapAttribute = (Map) iterator.next();
            if (attributeName.equals(mapAttribute.get("name").toString())) {
                map = mapAttribute;
                return map;
            }
        }
        return map;
    }

    /**
     * remove of this list an object wath contain this style class
     *
     * @param listStyleClass
     * @param styleClas
     * @return list
     */
    private List removeThisStyleClass(List listStyleClass, String styleClas) {

        for (Iterator iterator = listStyleClass.iterator(); iterator.hasNext();) {
            Map mapStyleClass = (Map) iterator.next();
            if (styleClas.equals(mapStyleClass.get("name").toString())) {
                iterator.remove();
                return listStyleClass;
            }
        }
        return listStyleClass;
    }

    /**
     * discompose an attribute composed
     *
     * @param value is the attribute value
     * @param attributeWrapperDTO xml attribute wrapper
     * @return String , attribute discomposed
     */
    private String discomposeAttributeValue(String value, StyleAttributeWrapperDTO attributeWrapperDTO) {

        String newValue = value;

        if (Functions.attributeIsOfTypeBoxShadow(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getAttributeType())) {
            return newValue;
        }

        if (Functions.attributeIsOfTypeGradient(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getAttributeType())) {
            return newValue;
        }

        value = value.trim();
        if (value.indexOf(",") != -1) {
            newValue = value.substring(0, value.indexOf(",")).trim();
        } else if (value.indexOf(UIManagerConstants.SEPARATOR_KEY) != -1) {
            newValue = value.substring(0, value.indexOf(UIManagerConstants.SEPARATOR_KEY)).trim();
        } else {
            newValue = value;
        }

        if (attributeWrapperDTO.getIsUrl() && newValue.indexOf("(") != -1 && newValue.endsWith(")")) {
            int beginStr = newValue.indexOf("(");
            int endStr = newValue.lastIndexOf(")");
            newValue = newValue.substring(beginStr + 1, endStr).trim();
            newValue = newValue.replaceFirst(UIManagerConstants.CONTEXT_PATH, ""); //quit context path
        }
        return newValue;
    }

    /**
     * verif if is hexadecimal format color
     *
     * @param value
     * @return true or false
     */
    private boolean isHexadecimalColor(String value) {
        value = value.trim();
        if (value.startsWith("#")) {
            try {
                String hexValue = value.substring(value.indexOf("#") + 1, value.length());  // color format #FFFFFF
                BigInteger bi = new BigInteger(hexValue, 16);
                return true;
            } catch (NumberFormatException ne) {
                return false;
            }
        }
        return false;
    }

    /**
     * set new default values if has changes in the style sheet of the company
     *
     * @param sectionsViewMap   section view
     * @param companyStyleClass List style class of the company
     * @return Map of section view with new dafault values
     */
    private Map setDefaultValuesOfTheCompany(Map sectionsViewMap, List companyStyleClass) {
        log.debug("Executing method setDefaultValuesOfTheCompany.........................");

        Map companyStyleMap = new HashMap();
        Map companyAttributeMap;
        String companyAttributeValue;

        for (Iterator iterator = sectionsViewMap.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();

            List elements = sectionWrapperDTO.getElements();
            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                //company styles
                companyStyleMap = getStyleClassMapOfUser(companyStyleClass, elementWrapperDTO.getElementClass());

                if (!companyStyleMap.isEmpty()) {
                    List attributes = elementWrapperDTO.getAttributes();
                    for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext();) {
                        StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();

                        //set company data (new default values)
                        companyAttributeMap = getAttributeMapOfUser((List) companyStyleMap.get("attributeData"), attributeWrapperDTO.getAttributeName());
                        if (!companyAttributeMap.isEmpty()) {
                            companyAttributeValue = discomposeAttributeValue((String) companyAttributeMap.get("value"), attributeWrapperDTO);
                            attributeWrapperDTO.setAttributeXmlValue(companyAttributeValue);
                            attributeWrapperDTO.setAttributeValue(companyAttributeValue);

                            if (Functions.attributeIsOfTypeColor(attributeWrapperDTO.getAttributeName(), attributeWrapperDTO.getIsUrl())) {
                                attributeWrapperDTO.setIsDefault(true);
                                attributeWrapperDTO.setAttributeNewValue(companyAttributeValue);
                            } else {
                                if (attributeWrapperDTO.getIsUrl()) {
                                    attributeWrapperDTO.setIsDefault(true);
                                    attributeWrapperDTO.setAttributeNewValue(companyAttributeValue);

                                    if (isHexadecimalColor(companyAttributeValue)) {
                                        attributeWrapperDTO.setBackgroundKey(UIManagerConstants.IS_COLOR_KEY);
                                    } else {
                                        attributeWrapperDTO.setBackgroundKey(UIManagerConstants.IS_MOSAIC_KEY);
                                    }
                                } else {
                                    attributeWrapperDTO.setIsDefault(false);
                                    attributeWrapperDTO.setAttributeNewValue(UIManagerConstants.DEFAULT_KEY);
                                }
                            }
                        }
                    }

                    //remove this styleclass of the company
                    if (!companyStyleMap.isEmpty()) {
                        companyStyleClass = removeThisStyleClass(companyStyleClass, elementWrapperDTO.getElementClass());
                    }
                }
            }
        }

        return (sectionsViewMap);
    }

}
