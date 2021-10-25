package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.el.StyleClassFunctions;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class is responsible of to build the comparation selects
 * according to the type of the field.
 *
 * @autor Mauren
 * Date: 23-oct-2006
 * Time: 13:47:28
 */

public class FieldTypeForwardAction extends ForwardAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        PrintWriter printerWriter = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "max-age=0");
        response.setCharacterEncoding(Constants.CHARSET_ENCODING);
        printerWriter.write(getSelectObject(request, response, user));
        return mapping.findForward("Success");
    }

    public String getSelectObject(HttpServletRequest request, HttpServletResponse response, User user) {

        StringBuffer title = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        String companyId = user.getValue("companyId").toString();
        String fieldType = "0";
        String table = null;
        String fieldView = null;
        StringBuffer titleCriteria = new StringBuffer();
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        String fieldname = null;
        String operator = request.getParameter("operator");
        boolean deleted = false;
        if (!"-100".equals(request.getParameter("categoryId").trim())) {
            sql.append("select categorytype as categorytype, categoryname as categoryname, tableid as tableid from category where categoryid=")
                    .append(request.getParameter("categoryId")).append(" and companyid=").append(companyId).append(";");
            List rss = QueryUtil.i.executeQuery(sql.toString());

            if (rss.size() > 0) {
                for (Iterator iterator = rss.iterator(); iterator.hasNext();) {
                    Map result = (Map) iterator.next();
                    table = result.get("tableid").toString();
                    fieldView = result.get("categoryname").toString();
                    fieldType = result.get("categorytype").toString();
                }
            } else {
                deleted = true;
            }
        } else if (!("-100").equals(request.getParameter("campCriterionValueId").trim())) {
            sql = new StringBuffer();
            sql.append("select fieldtype as fieldtype, fieldname as fieldname, descriptionkey as descriptionkey, tableid as tableid from campcriterionvalue where campcriterionvalueid=").append(request.getParameter("campCriterionValueId")).append(";");
            List rss = QueryUtil.i.executeQuery(sql.toString());
            Map result_ = null;

            for (Iterator iterator = rss.iterator(); iterator.hasNext();) {
                result_ = (Map) iterator.next();
                fieldType = result_.get("fieldtype").toString();
                table = result_.get("tableid").toString();
                fieldname = result_.get("fieldname").toString();
                fieldView = JSPHelper.getMessage(locale, result_.get("descriptionkey").toString());
            }
        }

        if ("1".equals(table)) {
            titleCriteria.append(JSPHelper.getMessage(locale, "Category.customer")).append(" : ");
        } else if ("2".equals(table)) {
            titleCriteria.append(JSPHelper.getMessage(locale, "Campaign.contactPerson")).append(" : ");
        } else if ("3".equals(table)) {
            titleCriteria.append(JSPHelper.getMessage(locale, "Campaign.product")).append(" : ");
        } else if ("4".equals(table)) {
            titleCriteria.append(JSPHelper.getMessage(locale, "Contact")).append(" : ");
        } else if (ContactConstants.ADDRESS_CONTACTPERSON_CATEGORY.equals(table)) {
            titleCriteria.append(JSPHelper.getMessage(locale, "Category.addressContactPerson")).append(" : ");
        }

        title.append(titleCriteria.append(fieldView).toString());
        title.append("||");
        title.append("<select name=\"dto(operator)\" onchange=\" javascript:showIU_values(")
                .append(fieldType).append(", ").append(request.getParameter("categoryId").trim()).append(",")
                .append(request.getParameter("campCriterionValueId").trim()).append(",'" + fieldname + "',this)\" ")
                .append(" class=\"").append(getSelectStyleClass(request)).append("\"")
                .append(" id=\"operator\">\n")
                .append("<option value=\"\"></option>\n");

        if ("code".equals(fieldname)) {
            title.append("<option value=\"EQUAL\" ").append("EQUAL".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.equal")).append("</option>\n");
        } else if ("inuse".equals(fieldname)) {
            title.append("<option value=\"EQUAL\" ").append("EQUAL".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.equal")).append("</option>\n");
        } else if (("4".equals(fieldType) && !"-100".equals(request.getParameter("categoryId").trim())) ||
                ("4".equals(fieldType) && !"-100".equals(request.getParameter("campCriterionValueId").trim()) && "0".equals(fieldname))
                || "18".equals(request.getParameter("campCriterionValueId"))
                || "13".equals(request.getParameter("campCriterionValueId"))
                || "26".equals(request.getParameter("campCriterionValueId"))
                || "22".equals(request.getParameter("campCriterionValueId"))) {
            title.append("<option value=\"CONTAIN\" ").append("CONTAIN".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.contain")).append("</option>\n")
                    .append("<option value=\"DISTINCT\" ").append("DISTINCT".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.notEqual")).append("</option>\n")
                    .append("<option value=\"EQUAL\" ").append("EQUAL".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.equal")).append("</option>\n")
                    .append("<option value=\"" + CampaignConstants.CriteriaComparator.NOT_CONTAIN.getConstant() + "\" ").append(CampaignConstants.CriteriaComparator.NOT_CONTAIN.getConstant().equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, CampaignConstants.CriteriaComparator.NOT_CONTAIN.getResource())).append("</option>\n");

        } else if ("5".equals(fieldType) || "6".equals(fieldType) || ("4".equals(fieldType) && !"-100".equals(request.getParameter("campCriterionValueId").trim()) && !"0".equals(fieldname))) {
            title.append("<option value=\"EQUAL\" ").append("EQUAL".equals(operator) ? "selected =\"true\"" : "").append(" >").append(JSPHelper.getMessage(locale, "Report.filter.op.equal")).append("</option>\n")
                    .append("<option value=\"IN\" ").append("IN".equals(operator) ? "selected =\"true\"" : "").append(" >").append(JSPHelper.getMessage(locale, "Report.filter.op.oneOf")).append("</option>\n")
                    .append("<option value=\"DISTINCT\" ").append("DISTINCT".equals(operator) ? "selected =\"true\"" : "").append(" >").append(JSPHelper.getMessage(locale, "Report.filter.op.notEqual")).append("</option>\n")
                    .append("<option value=\"NOTIN\" ").append("NOTIN".equals(operator) ? "selected =\"true\"" : "").append(" >").append(JSPHelper.getMessage(locale, "Report.filter.op.notOneOf")).append("</option>\n");
        } else if ("1".equals(fieldType) || "2".equals(fieldType) || "3".equals(fieldType)) {
            title.append("<option value=\"DISTINCT\" ").append("DISTINCT".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.notEqual")).append("</option>\n")
                    .append("<option value=\"EQUAL\" ").append("EQUAL".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.equal")).append("</option>\n")
                    .append("<option value=\"GREATER\" ").append("GREATER".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.greaterThan")).append("</option>\n")
                    .append("<option value=\"BETWEEN\" ").append("BETWEEN".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.between")).append("</option>\n")
                    .append("<option value=\"LESS\" ").append("LESS".equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, "Report.filter.op.lessThan")).append("</option>\n");
        } else if (CampaignConstants.FIELD_RELATION_EXISTS.equals(fieldType) && !"-100".equals(request.getParameter("campCriterionValueId").trim())) {
            String relationExistsOp = CampaignConstants.CriteriaComparator.RELATION_EXISTS.getConstant();
            title.append("<option value=\"" + relationExistsOp + "\" ").append(relationExistsOp.equals(operator) ? "selected =\"true\"" : "").append(">").append(JSPHelper.getMessage(locale, CampaignConstants.CriteriaComparator.RELATION_EXISTS.getResource())).append("</option>\n");
        }

        title.append("</select>\n");
        title.append("<input type=\"hidden\" name=\"dto(categoryId)\"  value=\"")
                .append(request.getParameter("categoryId").trim()).append("\" id=\"categoryId\">\n");
        title.append("<input type=\"hidden\" name=\"dto(campCriterionValueId)\"  value=\"")
                .append(request.getParameter("campCriterionValueId").trim()).append("\" id=\"campCriterionValueId\">\n");
        title.append("<input type=\"hidden\" name=\"dto(fieldType)\"  value=\"").append(fieldType).append("\" id=\"fieldType\">\n");

        if (!deleted) {
            return title.toString();
        } else {
            return "";
        }
    }

    private String getSelectStyleClass(HttpServletRequest request) {
        String styleClass = "select";
        if (Functions.isBootstrapUIMode(request)) {
            styleClass = StyleClassFunctions.getFormSelectClasses();
        }
        return styleClass;
    }

}
