package com.piramide.elwis.web.campaignmanager.action;

import com.piramide.elwis.cmd.campaignmanager.CampaignCriterionCmd;
import com.piramide.elwis.domain.campaignmanager.CampaignCriterionValue;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeText;
import com.piramide.elwis.domain.campaignmanager.CampaignFreeTextHome;
import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import com.piramide.elwis.web.campaignmanager.form.CampaignCriterionForm;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.struts.action.*;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * User: yumi
 * Date: Nov 11, 2004
 * Time: 1:41:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class CampaignCriterionReadAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("*******  campaignCriterionReadAction  .........  execute  ....");

        if (request.getParameter("dto(cancel)") != null) {
            return mapping.findForward("Cancel");
        }
        CampaignCriterionForm criterioForm = (CampaignCriterionForm) form;
        CampaignFreeText freeText = null;
        StringBuffer sql = new StringBuffer();
        List res = new ArrayList();
        String ids;
        User user = RequestUtils.getUser(request);
        String campaignId = request.getParameter("campaignId");
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        CampaignCriterionCmd cmd = new CampaignCriterionCmd();
        CampaignFreeTextHome freeTextHome = (CampaignFreeTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_FREETEXT);
        CampaignCriterionValue criterionValue = null;
        ActionErrors aErrors = new ActionErrors();
        cmd.putParam(criterioForm.getDtoMap());

        if (criterioForm.getDto("campaignCriterionId") != null) {
            cmd.putParam("campaignCriterionId", criterioForm.getDto("campaignCriterionId"));
        } else {
            cmd.putParam("campaignCriterionId", request.getParameter("campaignCriterionId"));
        }
        cmd.putParam("op", CampaignConstants.EMPTY);
        cmd.putParam("companyId", user.getValue("companyId"));
        cmd.putParam("campaignId", campaignId);

        BusinessDelegate.i.execute(cmd, request);
        aErrors = MessagesUtil.i.convertToActionErrors(mapping, request, cmd.getResultDTO());
        saveErrors(request, aErrors);

        if (cmd.getResultDTO().isFailure()) {
            ActionErrors errors = new ActionErrors();
            errors.add("error", new ActionError("campCriteriaMsg.NotFound"));
            saveErrors(request, errors);
            request.setAttribute("campaignId", request.getParameter("campaignId"));
            return mapping.findForward("Fail");

        } else {
            StringBuffer title = new StringBuffer();
            String campCriterionValueId = null;
            String criteriaOperator = cmd.getResultDTO().get("operator").toString();
            criterioForm.getDtoMap().putAll(cmd.getResultDTO());
            freeText = freeTextHome.findByPrimaryKey(new Integer(cmd.getResultDTO().get("valueId").toString()));
            ids = new String(freeText.getValue());//criteria keys value to read.
            String id;
            String fieldName;
            String tableName;
            String field;
            Category category = null;

            if (cmd.getResultDTO().get("categoryId") == null) {
                if (CampaignConstants.CUSTOMER_CATEGORY.equals(cmd.getResultDTO().get("tableId").toString())) {
                    title.append(JSPHelper.getMessage(locale, "Category.customer")).append(" : ");
                } else if (CampaignConstants.CONTACTPERSON_CATEGORY.equals(cmd.getResultDTO().get("tableId").toString())) {
                    title.append(JSPHelper.getMessage(locale, "Category.contactPerson")).append(" : ");
                } else if (CampaignConstants.PRODUCT_CATEGORY.equals(cmd.getResultDTO().get("tableId").toString())) {
                    title.append(JSPHelper.getMessage(locale, "Category.product")).append(" : ");
                } else if (CampaignConstants.ADDRESS_CATEGORY.equals(cmd.getResultDTO().get("tableId").toString())) {
                    title.append(JSPHelper.getMessage(locale, "Category.address")).append(" : ");
                }
                campCriterionValueId = cmd.getResultDTO().get("campCriterionValueId").toString();
                title.append(JSPHelper.getMessage(locale, cmd.getResultDTO().get("description").toString()));
                criterioForm.setDto("categoryId", "-100");
            } else if ((cmd.getResultDTO().get("categoryId") != null) && (campCriterionValueId == null || !CampaignConstants.EMPTY.equals(campCriterionValueId))) {
                CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CATEGORY);
                try {
                    category = categoryHome.findByPrimaryKey(new Integer(cmd.getResultDTO().get("categoryId").toString()));
                } catch (FinderException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }

                String tableLabel = JSPHelper.getCategoryTable(category.getTable(), request);
                if (tableLabel != null) {
                    title.append(tableLabel).append(" : ");
                }

                title.append(category.getCategoryName());
                sql.append("select categoryid, categoryname from category where companyid=" + user.getValue("companyId")).append(" order by  categoryname");
                res = QueryUtil.i.executeQuery(sql.toString());
                fieldName = "categoryvaluename";
                tableName = "categoryvalue";
                field = "categoryvalueid";
                criterioForm.setDto("campCriterionValueId", "-100");
            } else {
                fieldName = criterionValue.getFieldName();
                tableName = criterionValue.getTableName();
                field = criterionValue.getField();
            }

            criterioForm.setDto("table", title.toString());
            if (criterioForm.getDto("value") != null) {
                criterioForm.setDto("date_value", DateUtils.parseDate(DateUtils.integerToDate(new Integer(criterioForm.getDto("value").toString())),
                        JSPHelper.getMessage(locale, "datePattern")));
            }
            if (criterioForm.getDto("value1") != null) {
                criterioForm.setDto("dateValue_1", DateUtils.parseDate(DateUtils.integerToDate((Integer) criterioForm.getDto("value1")),
                        JSPHelper.getMessage(locale, "datePattern")));
            }
            if (criterioForm.getDto("value2") != null) {
                criterioForm.setDto("dateValue_2", DateUtils.parseDate(DateUtils.integerToDate((Integer) criterioForm.getDto("value2")),
                        JSPHelper.getMessage(locale, "datePattern")));
            }

            request.setAttribute("version", cmd.getResultDTO().get("version"));
            request.setAttribute("dto(withReferences)", request.getParameter(("withReferences")));

            if ((new Integer(6).equals(cmd.getResultDTO().get("fieldType")) || new Integer(5).equals(cmd.getResultDTO().get("fieldType"))
                    || (!CampaignConstants.CEROVALUE.equals(cmd.getResultDTO().get("fieldname")) && "-100".equals(criterioForm.getDto("categoryId"))))) {
                String catId = null, critValue = null;
                String ope = request.getParameter("ope");
                String operator = cmd.getResultDTO().get("operator").toString();

                if (cmd.getResultDTO().get("categoryId") == null) {
                    catId = "-100";
                } else {
                    catId = cmd.getResultDTO().get("categoryId").toString();
                }
                if (cmd.getResultDTO().get("campCriterionValueId") == null) {
                    critValue = "-100";
                } else {
                    critValue = cmd.getResultDTO().get("campCriterionValueId").toString();
                }
                if (!"4".equals(cmd.getResultDTO().get("IU_Type")) && !"11".equals(cmd.getResultDTO().get("IU_Type"))
                        && !"12".equals(cmd.getResultDTO().get("IU_Type"))
                        && !"13".equals(cmd.getResultDTO().get("IU_Type"))
                        && !"14".equals(cmd.getResultDTO().get("IU_Type"))) {
                    request.setAttribute("jsLoad", "onLoad=\"showIU_updateValues(" +
                            catId + "," + critValue + "," + cmd.getResultDTO().get("campaignCriterionId") + ",'" + ope + "','" + operator + "')\"");
                }
            }

            if (CampaignConstants.CriteriaComparator.RELATION_EXISTS.equal(criteriaOperator)) {
                request.setAttribute("readOnlyValue", Functions.composeRelationExistsOperatorValue(campCriterionValueId, request));
            }

            return mapping.findForward("Success");
        }
    }
}