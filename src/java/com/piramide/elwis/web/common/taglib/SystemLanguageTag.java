package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.StyleClassFunctions;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: SystemLanguageTag.java 11081 2015-09-28 13:59:40Z milver $
 */

public class SystemLanguageTag extends TagSupport {
    private Log log = LogFactory.getLog(this.getClass());
    String name;
    String labelStyleClass;
    String containStypeClass;
    String selectStyleClass;
    String labelWidth;
    String containWidth;
    String label;
    String languageId;
    String property;
    String operation;
    String visible;
    /*do if is mode bootstrap*/
    String mode;


    public SystemLanguageTag() {
        name = "org.apache.struts.taglib.html.BEAN";
    }

    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    public int doEndTag() throws JspException {
        doTag();
        return EVAL_PAGE;
    }

    public void doTag() {
        Object bean = null;

        String beanPropertyId = "";
        try {
            bean = RequestUtils.lookup(pageContext, name, null);
            String beanProperty[] = BeanUtils.getArrayProperty(bean, property);
            beanPropertyId = beanProperty[0];
        } catch (Exception e) {
            beanPropertyId = "";
        }

        //find the user
        User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);
        String result = "";


        //get IU languages that the user are configurated
        Map uiLanguages = getUILanguages(Integer.valueOf(user.getValue("companyId").toString()).intValue());

        //get all languages that the system are available
        List systemLanguages = JSPHelper.getLanguageList((HttpServletRequest) pageContext.getRequest());
        List sortedList = new ArrayList();

        List remainSystemLanguages = new ArrayList();

        //extract system languages that are not assigned to any language
        for (int i = 0; i < systemLanguages.size(); i++) {
            LabelValueBean labelValueBean = (LabelValueBean) systemLanguages.get(i);
            String value = labelValueBean.getValue();

            if (!uiLanguages.containsValue(value)) {
                remainSystemLanguages.add(labelValueBean);
            }
        }


        //if any of uiLanguages are equals to actual language, then value and laben can be added to remainSystemLanguages list
        if (uiLanguages.containsKey(languageId)) {
            for (int i = 0; i < systemLanguages.size(); i++) {
                LabelValueBean labelValueBean = (LabelValueBean) systemLanguages.get(i);
                String value = labelValueBean.getValue();
                for (Iterator iterator = uiLanguages.entrySet().iterator(); iterator.hasNext();) {
                    Map.Entry entry = (Map.Entry) iterator.next();

                    if (entry.getValue().equals(value) && entry.getKey().equals(languageId)) {
                        remainSystemLanguages.add(labelValueBean);
                    }
                }
            }
        }

        //only if exists languages the tag render the HTML code
        if (!remainSystemLanguages.isEmpty()) {
            sortedList = SortUtils.orderByProperty((ArrayList) remainSystemLanguages, "label");
            if(Constants.UIMode.BOOTSTRAP.getConstant().equals(mode)){
                result = htmlContentBootstrap(sortedList, beanPropertyId);
            }else {
                result = htmlContent(sortedList, beanPropertyId);
            }
        }


/*
        if(sortedList.size()>0){
            result +="<tr>\n" +
                    "  <TD class=\"label\" width=\"25%\" nowrap>"+JSPHelper.getMessage((HttpServletRequest) pageContext.getRequest(), "Common.default") +"</TD>\n" +
                    "  <TD class=\"contain\" width=\"75%\" >\n" +
                    "      <input name=\"dto(isDefault)\" tabindex=\"5\" checked=\"checked\" class=\"radio\" type=\"checkbox\">"+
                    "  </TD>\n" +
                    "</tr>";
        }
*/

//        result += "\n <input type=\"hidden\" name=\"dto(size)\" value=\""+sortedList.size()+"\">";
        log.debug("------------------------------------");
        log.debug("HTML code writing by TAG ... \n" + result);
        log.debug("------------------------------------");

        JspWriter out = pageContext.getOut();
        try {
            out.write(result);
        } catch (IOException e) {
            log.error("Cannot write tag into jsp page...");
        }
    }
    private String htmlContentBootstrap(List systemLanguages, String beanPropertyId){
        boolean visible="true".equals(getVisible())? true : false;
        return  "<div class=\""+StyleClassFunctions.getFormGroupClasses()+"\">" +
                    "<label class=\""+StyleClassFunctions.getFormLabelClasses()+"\">\n" +
                    "\n" +label+
                    "</label>" +
                    "<div class=\""+StyleClassFunctions.getFormContainClasses(visible)+"\">"+
                        getSelectHTMLTag(systemLanguages,beanPropertyId)+"" +
                    " <span class=\"glyphicon form-control-feedback iconValidation\" ></span>" +
                    "</div>" +
                "</div>";
    }
    private String htmlContent(List systemLanguages, String beanPropertyId) {
        String result = "\n<tr>\n" +
                "<td class=\"" + labelStyleClass + "\">" + label + "</td>\n" +
                "<td class=\"" + containStypeClass + "\">" + getSelectHTMLTag(systemLanguages, beanPropertyId) + "</td>\n" +
                "</tr>\n";
        return result;
    }

    private String getSelectHTMLTag(List systemLanguages, String beanPropertyId) {
        String result = "";
        String cacheLabel = "";
        String cacheValue = "";

        result += "\n<select name=\"" + property + "\" class=\"" + selectStyleClass + "\">\n";
        result += "<option value=\"\"></option>\n";
        for (int i = 0; i < systemLanguages.size(); i++) {
            LabelValueBean labelValueBean = (LabelValueBean) systemLanguages.get(i);

            if (labelValueBean.getValue().equals(beanPropertyId)) {
                result += "<option value=\"" + labelValueBean.getValue() + "\" selected=\"selected\">" + labelValueBean.getLabel() + "</option>\n";
                cacheLabel = labelValueBean.getLabel();
                cacheValue = labelValueBean.getValue();
            } else {
                result += "<option value=\"" + labelValueBean.getValue() + "\">" + labelValueBean.getLabel() + "</option>\n";
            }
        }
        result += "</select>\n";
        if ("true".equals(visible)) {
            log.debug("------------------------------------");
            log.debug("ONLY visible mode");
            log.debug("------------------------------------");
            result = cacheLabel;
        }

        return result;
    }

    private Map getUILanguages(int companyId) {
        Map keyUILanguages = new HashMap();
        String sql = "select languageid as id, languagename as name, iso as iso\n" +
                "from language \n" +
                "where language.companyid=" + companyId + " and language.iso is not null;";
        List result = QueryUtil.i.executeQuery(sql);
        for (int i = 0; i < result.size(); i++) {
            Map map = (Map) result.get(i);
            keyUILanguages.put(map.get("id"), map.get("iso"));
        }
        return keyUILanguages;
    }

    public String getLabelStyleClass() {
        return labelStyleClass;
    }

    public void setLabelStyleClass(String labelStyleClass) {
        this.labelStyleClass = labelStyleClass;
    }

    public String getContainStypeClass() {
        return containStypeClass;
    }

    public void setContainStypeClass(String containStypeClass) {
        this.containStypeClass = containStypeClass;
    }

    public String getSelectStyleClass() {
        return selectStyleClass;
    }

    public void setSelectStyleClass(String selectStyleClass) {
        this.selectStyleClass = selectStyleClass;
    }

    public String getLabelWidth() {
        return labelWidth;
    }

    public void setLabelWidth(String labelWidth) {
        this.labelWidth = labelWidth;
    }

    public String getContainWidth() {
        return containWidth;
    }

    public void setContainWidth(String containWidth) {
        this.containWidth = containWidth;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getOperation() {
        return operation;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
