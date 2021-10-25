package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: yumi
 * Date: Jan 8, 2005
 * Time: 8:13:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class CampaignDynamicCriteriaAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        String field = "";
        String tablename = "";
        String typefield = "";
        String fieldname = "";
        List rs = null;
        String sql = "";
        String description;
        ArrayList list = new ArrayList();
        String value = request.getAttribute("values").toString();

        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        field = tokenizer.nextToken();
        tablename = tokenizer.nextToken();
        typefield = tokenizer.nextToken();
        fieldname = tokenizer.nextToken();
        String title = tokenizer.nextToken();
        String campCriterionValueId = tokenizer.nextToken();

        if (!"categoryvalue".equals(tablename)) {
            sql = "select " + fieldname + (fieldname.equals(field) ? "" : ", " + field) + " from " + tablename + " where companyid= " + request.getAttribute("companyId") + " order by " + fieldname;
        } else {
            sql = "select " + fieldname + ", categoryvalueid from " + tablename + " where categoryid=" + field + " and companyid=" + request.getAttribute("companyId") + " order by " + fieldname;
            request.setAttribute("isCategory", new Integer(1));
            request.setAttribute("campaignId", request.getParameter("dto(campaignId)"));
            request.setAttribute("categoryId", field);
            request.setAttribute("titlePage", title);
            request.setAttribute("campCriterionValueId", campCriterionValueId);
            field = "categoryvalueid";
        }

        rs = (List) QueryUtil.i.executeQuery(sql);

        for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
            Map result = (Map) iterator.next();
            description = (String) result.get(fieldname);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append((String) result.get(field));
            list.add(new LabelValueBean(description, stringBuffer.toString()));
        }
        request.setAttribute("valueList", list);
        request.setAttribute("campCriterionValueId", campCriterionValueId);
        request.setAttribute("titlePage", title);
        request.setAttribute("typefield", typefield);

        return mapping.findForward("Success");
    }
}

