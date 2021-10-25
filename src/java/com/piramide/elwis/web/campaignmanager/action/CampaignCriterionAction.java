package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignCriterionValueCmd;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionSimpleForm;
import com.piramide.elwis.web.common.util.MessagesUtil;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * User: yumi
 * Date: Nov 10, 2004
 * Time: 4:38:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class CampaignCriterionAction extends CampaignManagerAction {

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
        ArrayList currentList = new ArrayList();
        ActionErrors errors = new ActionErrors();
        String value = request.getParameter("dto(values)");
        boolean noContain = false;

        if (value == null || "-1".equals(value) || "".equals(value)) {
            errors.add("range", new ActionError("Campaign.emptyList"));
            saveErrors(request, errors);
            return mapping.findForward("fail");
        }

//values: field,tablename,typefield,fieldName,title,campaignCriterionValueId.
        DefaultForm criterioForm = (DefaultForm) form;
        ActionForward forward = mapping.findForward("Fail");
        StringTokenizer tokenizer = new StringTokenizer(value, ",");
        boolean criterionIsNull = true;
        field = tokenizer.nextToken();
        tablename = tokenizer.nextToken();
        typefield = tokenizer.nextToken();
        fieldname = tokenizer.nextToken();
        String title = tokenizer.nextToken();
        String campCriterionValueId = tokenizer.nextToken();
        CampaignCriterionValueCmd criterionValueCmd = new CampaignCriterionValueCmd();

        if ("0".equals(typefield)) {
            /*it verifies if it is a compound criterion
            * verifies if already exist one criterion with this table for open, if it does not exist it creates one new.
            single it must exist a type of criterion
            */
            ActionErrors aErrors = new ActionErrors();
            criterionValueCmd.putParam(criterioForm.getDtoMap());
            BusinessDelegate.i.execute(criterionValueCmd, request);
            aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, criterionValueCmd.getResultDTO());
            saveErrors(request, aErrors);

            if (criterionValueCmd.getResultDTO().isFailure()) {
                return forward;
            } else {
                criterionIsNull = ((Boolean) criterionValueCmd.getResultDTO().get("criterionIsNull")).booleanValue();
            }

            if (criterionIsNull) {
                if (!"categoryvalue".equals(tablename)) {
                    if ("priority".equals(tablename)) {
                        sql = "select " + fieldname + "," + field + " from " + tablename + " where companyid= " + request.getParameter("dto(companyId)") + " and type='CUSTOMER' order by " + fieldname;
                    } else if ("address".equals(tablename))//get the address complete name
                    {
                        sql = "select name1, name2, addresstype, addressid from address where companyid= " + request.getParameter("dto(companyId)") + " order by name1";
                    } else {
                        sql = "select " + fieldname + (fieldname.equals(field) ? "" : "," + field) + " from " + tablename + " where companyid= " + request.getParameter("dto(companyId)") + " order by " + fieldname;
                    }
                } else {
                    sql = "select " + fieldname + ", categoryvalueid from " + tablename + " where categoryid=" + field + " and companyid=" + request.getParameter("dto(companyId)") + " order by " + fieldname;
                    request.setAttribute("isCategory", new Integer(1));
                    request.setAttribute("campaignId", request.getParameter("dto(campaignId)"));
                    request.setAttribute("categoryId", field);
                    request.setAttribute("titlePage", title);
                    request.setAttribute("campCriterionValueId", campCriterionValueId);
                    field = "categoryvalueid";
                }
            } else {
                log.debug("... criteria type find .... call update criterion ...");
                String id = (String) criterionValueCmd.getResultDTO().get("campCriterionId");
                StringBuffer result = new StringBuffer();
                result.append("/Campaign/SelectionCriteria/Forward/Update.do?");
                result.append("module=campaign");
                result.append("&campaignCriterionId=" + id);
                result.append("&campaignId=" + request.getParameter("dto(campaignId)"));
                result.append("&manyCriterias=" + criterionValueCmd.getResultDTO().get("manyCriterias"));
                result.append("&ids=" + criterionValueCmd.getResultDTO().get("idList"));
                result.append("&index=1");

                ActionForward redirect = new ActionForward(result.toString());
                return redirect;
            }
            rs = (List) QueryUtil.i.executeQuery(sql);
        }

        if (!"0".equals(typefield)) {

            CampaignCriterionSimpleForm criterionForm = (CampaignCriterionSimpleForm) form;
            criterionForm.setDto("campCriterionValueId", campCriterionValueId);
            criterionForm.setDto("titlePage", title);
            criterionForm.setDto("typefield", typefield);
            criterionForm.setDto("tablename", tablename);
            criterionForm.setDto("field", field);
            //send the criterion simple page
            return mapping.findForward("campaignCriterionSimple");

        } else if ("0".equals(typefield) && !"address".equals(tablename)) {

            for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
                noContain = false;
                Map result = (Map) iterator.next();
                description = (String) result.get(fieldname);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append((String) result.get(field));
                if (!currentList.contains(description) && "department".equals(tablename)) {
                    currentList.add(description);
                    noContain = true;
                } else if (!"department".equals(tablename)) {
                    list.add(new LabelValueBean(description, stringBuffer.toString()));
                }

                if (noContain && "department".equals(tablename)) {
                    list.add(new LabelValueBean(description, stringBuffer.toString()));
                }

            }
            request.setAttribute("valueList", list);// here seting the listing of where it has  to select.
            request.setAttribute("campCriterionValueId", campCriterionValueId);
            request.setAttribute("titlePage", title);
            request.setAttribute("typefield", typefield);
            request.setAttribute("values", value);
            //if not selected none criterion does send  error message
            if ("true".equals(request.getParameter("fail"))) {
                errors.add("error", new ActionError("campaign.requiredCriteria"));
                saveErrors(request, errors);
            }  //send the criterion compound page
            return mapping.findForward("campaignCriterion");
        }
        // get address complete name
        else if ("0".equals(typefield) && "address".equals(tablename)) {

            StringBuffer addressName = new StringBuffer();
            for (Iterator iterator = rs.iterator(); iterator.hasNext();) {
                Map result = (Map) iterator.next();
                addressName = new StringBuffer();
                addressName.append((String) result.get("name1"));
                if (result.get("name2") != null && !result.get("name2").equals("") && "1".equals(result.get("addresstype"))) {
                    addressName.append(" ").append(result.get("name2"));
                } else if (result.get("name2") != null && !result.get("name2").equals("") && "0".equals(result.get("addresstype"))) {
                    addressName.append(" ").append(result.get("name2"));
                }

                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append((String) result.get(field));
                list.add(new LabelValueBean(addressName.toString(), stringBuffer.toString()));
            }
            request.setAttribute("valueList", list);
            request.setAttribute("campCriterionValueId", campCriterionValueId);
            request.setAttribute("titlePage", title);
            request.setAttribute("typefield", typefield);
            request.setAttribute("values", value);

            // if not selected none criterion does send error message
            if ("true".equals(request.getParameter("fail"))) {
                errors.add("error", new ActionError("campaign.requiredCriteria"));
                saveErrors(request, errors);
            }   //it send to the page of compound criterion
            return mapping.findForward("campaignCriterion");
        }

        if (isCancelled(request)) {
            log.debug("Is Cancel");
            return mapping.findForward("Cancel");
        }
        return mapping.findForward("Success");
    }
}