package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.el.Functions;
import com.piramide.elwis.web.common.el.StyleClassFunctions;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * This class is responsible of to build the selects with values (simples or multiples)
 * User: yumi
 * Date: 21-oct-2006
 * Time: 15:29:11
 */

public class CampaignCriterionValueAction extends ForwardAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        PrintWriter printerWriter = response.getWriter();
        response.setContentType("text/html");
        response.setHeader("Cache-Control", "max-age=0");
        response.setCharacterEncoding(Constants.CHARSET_ENCODING);
        response.setHeader("Pragma", "no-cache");
        printerWriter.write(getSelectObject(request, response, user));
        return mapping.findForward("Success");
    }

    public String getSelectObject(HttpServletRequest request, HttpServletResponse response, User user) {
        StringBuffer select = new StringBuffer();
        StringBuffer sql = new StringBuffer();
        String fieldType;
        String fieldName;
        String field;
        String tableName;
        String companyId = user.getValue("companyId").toString();
        String categoryId = request.getParameter("categoryId").trim();
        String selectType = request.getParameter("selectType");
        String campCategoryValueId = request.getParameter("campCriterionValueId").trim();
        //for read
        String ope = request.getParameter("ope"); // read or readDelete
        String operator = request.getParameter("operator"); // read or readDelete
        ArrayList arrayList = new ArrayList();
        boolean read = false;
        if ("read".equals(ope) || "readDelete".equals(ope)) {
            read = true;
            String ids = "";
            sql.append("select freetextvalue as freetextvalue from freetext left join campcriterion on \n" +
                    "freetext.freetextid = campcriterion.valueid \n" +
                    "where campcriterion.criterionid=").append(request.getParameter("criterionId"));
            List result = new ArrayList();
            result = QueryUtil.i.executeQuery(sql.toString());
            Iterator it = result.iterator();
            if (it.hasNext()) {
                Map item = (Map) it.next();
                ids = (String) item.get("freetextvalue");
            }
            StringTokenizer st = new StringTokenizer(ids, ",");// sacar los valores de comparacion
            while (st.hasMoreTokens()) {
                arrayList.add(st.nextToken());
            }
        }
        /* category criteria type */
        if (!"-100".equals(categoryId) && !"null".equals(categoryId)) {
            sql = new StringBuffer();
            sql.append("select categoryvaluename as categoryvaluename, categoryvalueid as categoryvalueid from categoryvalue where categoryid=")
                    .append(categoryId).append(" and companyid=").append(companyId).append(" order by categoryvaluename");
            List rss = QueryUtil.i.executeQuery(sql.toString());
            select.append("<select name=\"multipleSelectValue\"");
            if (CampaignConstants.COMPARATOR_IN.equals(selectType) || CampaignConstants.COMPARATOR_IN.equals(operator) ||
                    CampaignConstants.COMPARATOR_NOTIN.equals(selectType) || CampaignConstants.COMPARATOR_NOTIN.equals(operator)) {
                if ("readDelete".equals(ope)) {
                    select.append(" disabled=\"true\" ");
                }
                select.append(" multiple=\"true\"").append(" class=\"").append(getMultipleSelectStyleClass(request)).append("\">\n");
            } else if (CampaignConstants.EQUAL.equals(selectType) || CampaignConstants.EQUAL.equals(operator) ||
                    CampaignConstants.COMPARATOR_DISTINCT.equals(selectType) || CampaignConstants.COMPARATOR_DISTINCT.equals(operator)) {
                if ("readDelete".equals(ope)) {
                    select.append("disabled=\"true\" ");
                }
                select.append(" class=\"").append(getSelectStyleClass(request)).append("\">\n");
                select.append("<option value=\"\"></option>\n");
            }
            for (Iterator iterator = rss.iterator(); iterator.hasNext();) {
                Map result = (Map) iterator.next();
                if (read && (arrayList.contains(result.get("categoryvalueid")))) {
                    select.append("<option selected =\"true\" value=\"").append(result.get("categoryvalueid")).append("\">");
                } else {
                    select.append("<option value=\"").append(result.get("categoryvalueid")).append("\">");
                }
                select.append(result.get("categoryvaluename")).append("</option>\n");
            }

            select.append("</select>\n");

        } else if (!("-100").equals(campCategoryValueId)) {
            /* custom criteria type  */
            sql = new StringBuffer();
            sql.append("select tablename as tablename, fieldtype as fieldtype, fieldname as fieldname, field as field, descriptionkey as descriptionkey, campcriterionvalueid as campcriterionvalueid")
                    .append(" from campcriterionvalue where campcriterionvalueid=").append(campCategoryValueId).append(";");
            List rss = QueryUtil.i.executeQuery(sql.toString());
            sql = new StringBuffer();
            Map result_ = null;
            for (Iterator iterator = rss.iterator(); iterator.hasNext();) {
                result_ = (Map) iterator.next();
            }
            fieldType = result_.get("fieldtype").toString();
            field = result_.get("field").toString();
            fieldName = result_.get("fieldname").toString();
            tableName = result_.get("tablename").toString();
            boolean isAddress = false;
            List result = null;

            if ("priority".equals(tableName)) {
                sql.append("select priorityname as fieldname, priorityid as field from ").append(tableName).append(" where companyid=").append(companyId).append(" and type='CUSTOMER' order by fieldname;");
            } else if ("4".equals(fieldType)) {
                sql.append("select distinct ").append(fieldName).append(" as fieldname from ").append(tableName).append(" where companyid=").append(companyId).append(" order by fieldname;");
            } else if ("function".equals(result_.get("fieldname"))) {
                sql.append("select distinct function as fieldname, function as field from contactperson where companyid=").append(companyId)
                        .append(" and function is not null and function <> '' order by fieldname;");
            } else if ("employeeid".equals(result_.get("field"))) {
                sql.append("select employeeid from employee where companyid=" + companyId);
                List result_address = QueryUtil.i.executeQuery(sql.toString());
                isAddress = true;
                StringBuffer addressid = new StringBuffer();
                sql = new StringBuffer();
                for (Iterator iterator = result_address.iterator(); iterator.hasNext();) {
                    Map res = (Map) iterator.next();
                    addressid.append("addressid=").append(res.get("employeeid"));
                    if (iterator.hasNext()) {
                        addressid.append(" or ");
                    }
                }
                sql.append("select addresstype as addresstype, addressid as field, name1 as name1, name2 as name2, name3 as name3 from address ")
                        .append("where ").append(addressid.toString()).append(" and companyid=" + companyId).append(" order by name1");
                isAddress = true;
            } else if ("recorduserid".equals(result_.get("field"))) {
                sql.append("SELECT  user.userid AS field,  address.name1 AS name1,  address.name2 AS name2, address.name3 AS name3,")
                        .append("address.addresstype AS addresstype FROM elwisuser AS user ")
                        .append("JOIN address AS address ON user.addressid=address.addressid ")
                        .append("WHERE user.active=1 and user.companyid =" + companyId)
                        .append(" order by name1");
                isAddress = true;
            } else if ("partnerid".equals(result_.get("field"))) {
                sql.append("select addresstype as addresstype, addressid as field, name1 as name1, name2 as name2, name3 as name3 from address where companyid=" + companyId).
                        append(" order by name1");
                isAddress = true;
            } else {
                sql.append("select ").append(fieldName).append(" as fieldname, ").append(field)
                        .append(" as field from ").append(tableName).append(" where companyid=").append(companyId).append(" order by fieldname;");
            }
            result = QueryUtil.i.executeQuery(sql.toString());
            select.append("<select name=\"multipleSelectValue\"");

            if (CampaignConstants.COMPARATOR_IN.equals(selectType) || CampaignConstants.COMPARATOR_IN.equals(operator) ||
                    CampaignConstants.COMPARATOR_NOTIN.equals(selectType) || CampaignConstants.COMPARATOR_NOTIN.equals(operator)) {
                if ("readDelete".equals(ope)) {
                    select.append(" disabled=\"true\" ");
                }
                select.append(" multiple=\"true\"").append(" class=\"").append(getMultipleSelectStyleClass(request)).append("\">\n");
            } else if (CampaignConstants.EQUAL.equals(selectType) || CampaignConstants.EQUAL.equals(operator) ||
                    CampaignConstants.COMPARATOR_DISTINCT.equals(selectType) || CampaignConstants.COMPARATOR_DISTINCT.equals(operator)) {
                if ("readDelete".equals(ope)) {
                    select.append(" disabled=\"true\"");
                }
                select.append(" class=\"").append(getSelectStyleClass(request)).append("\">\n");
                select.append("<option value=\"\"></option>\n");
            }
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                Map res = (Map) iterator.next();
                boolean isEmpty = false;
                if ("4".equals(fieldType)) {
                    if (res.get("fieldname") != null) {
                        if (!CampaignConstants.EMPTY.equals(res.get("fieldname").toString().trim())) {
                            if (read && (arrayList.contains(res.get("fieldname")))) {
                                select.append("<option selected =\"true\" value=\"").append(res.get("fieldname")).append("\">");
                            } else {
                                select.append("<option value=\"").append(res.get("fieldname")).append("\">");
                            }
                        } else {
                            isEmpty = true;
                        }
                    }
                } else {
                    if (read && (arrayList.contains(res.get("field")))) {
                        select.append("<option selected =\"true\" value=\"").append(res.get("field")).append("\">");
                    } else {
                        select.append("<option value=\"").append(res.get("field")).append("\">");
                    }
                }
                if (isAddress) {
                    select.append(getNameFormat(res.get("name1"), res.get("name2"), res.get("addresstype").toString())).append("</option>\n");
                } else if (!isEmpty) {
                    select.append(res.get("fieldname")).append("</option>\n");
                }
            }
            select.append("</select>\n");
        }
        return select.toString();
    }

    /* This method formats address name according to the type (person or organization) */
    private String getNameFormat(Object name1, Object name2, String addressType) {
        StringBuffer name = new StringBuffer();

        if ("0".equals(addressType)) {//organization
            name.append(name1);
            if (name2 != null && !CampaignConstants.EMPTY.equals(name2)) {
                name.append(" ").append(name2);
            }
        } else if ("1".equals(addressType)) { //person
            name.append(name1);
            if (name2 != null && !CampaignConstants.EMPTY.equals(name2)) {
                name.append(", ").append(name2);
            }
        }
        return name.toString();
    }

    private String getSelectStyleClass(HttpServletRequest request) {
        String styleClass = "select";
        if (Functions.isBootstrapUIMode(request)) {
            styleClass = StyleClassFunctions.getFormSelectClasses();
        }
        return styleClass;
    }

    private String getMultipleSelectStyleClass(HttpServletRequest request) {
        String styleClass = "multipleSelect";
        if (Functions.isBootstrapUIMode(request)) {
            styleClass = StyleClassFunctions.getFormSelectClasses();
        }
        return styleClass;
    }
}
