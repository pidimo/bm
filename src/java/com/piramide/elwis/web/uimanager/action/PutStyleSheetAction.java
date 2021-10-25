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
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Alfacentauro Team
 * this class update the style sheet of an user or company in the web clients
 *
 * @author miky
 * @version $Id: PutStyleSheetAction.java 12260 2016-01-27 01:55:35Z miguel $
 */
public class PutStyleSheetAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing PutStyleSheetAction...............................................");

        //get of the application context the object xmlStyleSheet structure
        Map xmlStyleSheetMap = new HashMap();
        xmlStyleSheetMap = Functions.getXmlStyleSheetEstructure(request, null, true);

        //verify if is company style
        String companyStyle = null;
        if (request.getParameter("companyChangeCount") != null) {
            companyStyle = UIManagerConstants.COMPANY_STYLE_KEY;
        }

        //read style sheet of the user
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        Integer userId = (Integer) user.getValue("userId");
        Integer companyId = (Integer) user.getValue("companyId");
        StyleSheetCmd styleSheetCmd = new StyleSheetCmd();
        styleSheetCmd.putParam("op", UIManagerConstants.OP_READ);
        styleSheetCmd.putParam("userId", userId);
        styleSheetCmd.putParam("companyId", companyId);
        styleSheetCmd.putParam("styleSheetType", Functions.getStyleSheetTypeByUIMode(request));
        styleSheetCmd.putParam("companyStyle", companyStyle);
        ResultDTO resultDTO = BusinessDelegate.i.execute(styleSheetCmd, request);

        Map mapUserStyleSheet = (Map) resultDTO.get("styleSheet");
        List userStyles = new ArrayList();

        StringBuffer styleStr = new StringBuffer();

        if (!mapUserStyleSheet.isEmpty()) {
            String contextPath = request.getContextPath();

            userStyles = putInOrderTheElementClass((List) mapUserStyleSheet.get("styleData"), xmlStyleSheetMap);

            for (Iterator iterator = userStyles.iterator(); iterator.hasNext();) {
                Map mapStyleClass = (Map) iterator.next();
                String styleClassName = mapStyleClass.get("name").toString();

                if (!styleClassName.startsWith(UIManagerConstants.WITHOUT_CLASSNAME_KEY)) {

                    styleStr.append(styleClassName);
                    styleStr.append("{\n");

                    List userAttributes = putInOrderTheAttributes((List) mapStyleClass.get("attributeData"), styleClassName, xmlStyleSheetMap);
                    for (Iterator iterator2 = userAttributes.iterator(); iterator2.hasNext();) {
                        Map mapAttribute = (Map) iterator2.next();

                        styleStr.append(composeCssAttribute(mapAttribute, styleClassName, xmlStyleSheetMap, contextPath));
                    }
                    styleStr.append("}\n\n");
                }
            }
        }

        //set in page
        response.setHeader("Cache-Control", "max-age=3600");
        response.setContentType("text/css");

        //log.debug("string styles......................................................."+styleStr.toString());
        ServletOutputStream os = response.getOutputStream();
        os.write(styleStr.toString().getBytes());
        os.flush();

        return null;
    }

    private String composeCssAttribute(Map mapAttribute, String styleClassName, Map xmlStyleSheetMap, String contextPath) {
        String attributeCss = "";

        if (mapAttribute != null && !mapAttribute.isEmpty()) {
            String attrName = mapAttribute.get("name").toString();
            String value = mapAttribute.get("value").toString();

            if (Functions.attributeIsOfTypeGradient(attrName, xmlStyleSheetMap, styleClassName)) {
                attributeCss = writeGradientAttribute(attrName, value);

            } else if (Functions.attributeIsOfTypeBoxShadow(attrName, xmlStyleSheetMap, styleClassName)) {
                attributeCss = writeBoxShadowAttribute(attrName, value);

            } else {
                attributeCss = writeAttribute(attrName, value, contextPath);
            }
        }

        return attributeCss;
    }

    private String writeAttribute(String attributeName, String value, String contextPath) {
        StringBuilder builder = new StringBuilder();

        if (value.indexOf(UIManagerConstants.SEPARATOR_KEY) != -1) {
            value = value.replaceAll(UIManagerConstants.SEPARATOR_KEY, " ");
        }
        if (value.indexOf(UIManagerConstants.CONTEXT_PATH) != -1) {
            value = value.replaceFirst(UIManagerConstants.CONTEXT_PATH, contextPath);
        }

        builder.append("\t");
        builder.append(attributeName);
        builder.append(": ");
        builder.append(value);
        builder.append(";\n");

        return builder.toString();
    }

    private String writeGradientAttribute(String attributeName, String value) {
        StringBuilder builder = new StringBuilder();

        String[] colorValues = value.split(Pattern.quote(UIManagerConstants.VALUE_SEPARATOR_KEY));

        if (colorValues.length == 2) {
            builder.append("\t");
            builder.append(attributeName);
            builder.append(": ");
            builder.append("-webkit-linear-gradient(top, ").append(colorValues[0]).append(", ").append(colorValues[1]).append(")");
            builder.append(";\n");

            builder.append("\t");
            builder.append(attributeName);
            builder.append(": ");
            builder.append("linear-gradient(to bottom, ").append(colorValues[0]).append(", ").append(colorValues[1]).append(")");
            builder.append(";\n");
        } else {
            builder.append(writeAttribute(attributeName, value, null));
        }

        return builder.toString();
    }

    private String writeBoxShadowAttribute(String attributeName, String value) {
        StringBuilder builder = new StringBuilder();

        builder.append("\t");
        builder.append("-webkit-box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px ").append(value);
        builder.append(";\n");

        builder.append("\t");
        builder.append("box-shadow: inset 0 1px 1px rgba(0, 0, 0, .075), 0 0 8px ").append(value);
        builder.append(";\n");

        return builder.toString();
    }

    /**
     * put in order the attributes of the user
     *
     * @param userAttributes      list of attributes of the user
     * @param elementClass        name of the style class
     * @param styleSheetStructure structure of the elwis_style.xml file
     * @return list in order
     */
    private List putInOrderTheAttributes(List userAttributes, String elementClass, Map styleSheetStructure) {
        List listInOrder = new ArrayList();

        for (Iterator iterator = styleSheetStructure.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
            List elements = sectionWrapperDTO.getElements();
            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                if (elementClass.equals(elementWrapperDTO.getElementClass())) {
                    List attributes = elementWrapperDTO.getAttributes();
                    for (Iterator iterator3 = attributes.iterator(); iterator3.hasNext();) {
                        StyleAttributeWrapperDTO attributeWrapperDTO = (StyleAttributeWrapperDTO) iterator3.next();

                        //put in order
                        for (Iterator iterator4 = userAttributes.iterator(); iterator4.hasNext();) {
                            Map mapUserAttribute = (Map) iterator4.next();
                            if (attributeWrapperDTO.getAttributeName().equals(mapUserAttribute.get("name").toString())) {
                                listInOrder.add(mapUserAttribute);
                            }
                        }
                    }
                }
            }
        }
        return listInOrder;
    }

    private List putInOrderTheElementClass(List userStylesMapList, Map styleSheetStructure) {
        List listInOrder = new ArrayList();

        for (Iterator iterator = styleSheetStructure.values().iterator(); iterator.hasNext();) {
            StyleSectionWrapperDTO sectionWrapperDTO = (StyleSectionWrapperDTO) iterator.next();
            List elements = sectionWrapperDTO.getElements();
            for (Iterator iterator2 = elements.iterator(); iterator2.hasNext();) {
                StyleElementWrapperDTO elementWrapperDTO = (StyleElementWrapperDTO) iterator2.next();

                //put in order
                for (Iterator iterator4 = userStylesMapList.iterator(); iterator4.hasNext();) {
                    Map userStyleClassMap = (Map) iterator4.next();
                    if (elementWrapperDTO.getElementClass().equals(userStyleClassMap.get("name").toString())) {
                        listInOrder.add(userStyleClassMap);
                        iterator4.remove();
                    }
                }
            }
        }

        //add reamin not ordered
        listInOrder.addAll(userStylesMapList);

        return listInOrder;
    }

}
