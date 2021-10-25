package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.cmd.financemanager.SequenceRuleCmd;
import com.piramide.elwis.dto.financemanager.SequenceRuleDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.DataBaseValidator;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.utils.NumberSequenceRuleFormatUtil;
import com.piramide.elwis.web.utils.VoucherSequenceRuleFormatUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SequenceRuleForm extends DefaultForm {
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        if (!isSaveButtonPressed(request)) {
            return updateForm(request);
        }

        ActionErrors errors = super.validate(mapping, request);
        checkSequenceRuleDuplicated(errors, request);

        List<ActionError> validationErrors = validateSquenceRule(request);

        for (ActionError error : validationErrors) {
            errors.add("formarError", error);
        }

        if (errors.isEmpty() && "create".equals(getDto("op"))) {
            setDefaultLastNumber(request);
        }

        if (errors.isEmpty() && "update".equals(getDto("op")) && getDto("confirmChangeFormat") == null) {
            validateOldFormatNumberAvailable(errors, request);
        }
        return errors;
    }

    private void setDefaultLastNumber(HttpServletRequest request) {
        String type = (String) getDto("type");

        if (FinanceConstants.SequenceRuleType.VOUCHER.equal(type)) {
            return;
        }

        Map max = null;
        if (FinanceConstants.SequenceRuleType.CUSTOMER.equal(type)) {
            max = com.piramide.elwis.web.common.el.Functions.getMaxOfString(request,
                    "maxCustomerNumber",
                    "/contacts");
        }

        if (FinanceConstants.SequenceRuleType.ARTICLE.equal(type)) {
            max = com.piramide.elwis.web.common.el.Functions.getMaxOfString(request,
                    "maxArticleNumber",
                    "/support");
        }

        if (FinanceConstants.SequenceRuleType.SUPPORT_CASE.equal(type)) {
            max = com.piramide.elwis.web.common.el.Functions.getMaxOfString(request,
                    "maxSupportNumber",
                    "/support");
        }

        if (null != max && null != max.get("max")) {
            setDto("maximumNumber", max.get("max").toString());
        }
    }

    private List<ActionError> validateSquenceRule(HttpServletRequest request) {
        String type = (String) getDto("type");
        //type not selected
        if (GenericValidator.isBlankOrNull(type)) {
            return new ArrayList<ActionError>();
        }

        List<ActionError> errors = new ArrayList<ActionError>();

        //validate Voucher sequence rule
        if (FinanceConstants.SequenceRuleType.VOUCHER.equal(type)) {
            errors = validateVoucherSequenceRule(request);
        }

        //validate Contract Number sequence rule
        if (FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER.equal(type)) {
            errors = validateContractNumberSequenceRule(request);
        }

        //validate Customer sequence rule
        if (FinanceConstants.SequenceRuleType.CUSTOMER.equal(type)) {
            errors = validateNumberSequenceRule(request, FinanceConstants.SequenceRuleType.CUSTOMER);
        }

        //validate Article sequence rule
        if (FinanceConstants.SequenceRuleType.ARTICLE.equal(type)) {
            errors = validateNumberSequenceRule(request, FinanceConstants.SequenceRuleType.ARTICLE);
        }

        //Validate Support case sequence rule
        if (FinanceConstants.SequenceRuleType.SUPPORT_CASE.equal(type)) {
            errors = validateNumberSequenceRule(request, FinanceConstants.SequenceRuleType.SUPPORT_CASE);
        }

        return errors;
    }

    private List<ActionError> validateContractNumberSequenceRule(HttpServletRequest request) {
        List<ActionError> errors = validateVoucherSequenceRule(request);
        if (null != errors && !errors.isEmpty()) {
            return errors;
        }
        String op = (String) getDto("op");
        if ("create".equals(op)) {
            ActionError duplicateError = validateUniqueNumberSequenceRule(request,
                    FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER);
            if (null != duplicateError) {
                errors = new ArrayList<ActionError>();
                errors.add(duplicateError);
            }
        }

        return errors;
    }

    private List<ActionError> validateNumberSequenceRule(HttpServletRequest request,
                                                         FinanceConstants.SequenceRuleType type) {
        List<ActionError> errors = new ArrayList<ActionError>();

        String format = (String) getDto("format");
        //format is required field
        if (GenericValidator.isBlankOrNull(format)) {
            return errors;
        }

        NumberSequenceRuleFormatUtil util = new NumberSequenceRuleFormatUtil(format);
        errors.addAll(util.formatValidator(request));

        String op = (String) getDto("op");

        //check if exists Customer number format
        if ("create".equals(op) && errors.isEmpty()) {
            ActionError error = validateUniqueNumberSequenceRule(request, type);
            if (null != error) {
                errors.add(error);
            }
        }

        return errors;
    }

    private ActionError validateUniqueNumberSequenceRule(HttpServletRequest request,
                                                         FinanceConstants.SequenceRuleType type) {
        User user = RequestUtils.getUser(request);

        SequenceRuleCmd sequenceRuleCmd = new SequenceRuleCmd();
        sequenceRuleCmd.setOp("searchSequenceRuleByType");
        sequenceRuleCmd.putParam("type", type.getConstant());
        sequenceRuleCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        SequenceRuleDTO sequenceRuleDTO = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(sequenceRuleCmd, request);
            sequenceRuleDTO = (SequenceRuleDTO) resultDTO.get("searchSequenceRuleByType");
        } catch (AppLevelException e) {
            log.error("-> Execute " + SequenceRuleCmd.class.getName() + " FAIL", e);
        }

        if (null != sequenceRuleDTO) {
            if (FinanceConstants.SequenceRuleType.CUSTOMER.equals(type)) {
                return new ActionError("SequenceRule.customerNumberExists");
            }
            if (FinanceConstants.SequenceRuleType.ARTICLE.equals(type)) {
                return new ActionError("SequenceRule.articleNumberExists");
            }
            if (FinanceConstants.SequenceRuleType.SUPPORT_CASE.equals(type)) {
                return new ActionError("SequenceRule.supportCaseNumberExists");
            }
            if (FinanceConstants.SequenceRuleType.PRODUCT_CONTRACT_NUMBER.equals(type)) {
                return new ActionError("SequenceRule.contractNumberExists");
            }
        }


        return null;
    }

    private List<ActionError> validateVoucherSequenceRule(HttpServletRequest request) {
        String op = (String) getDto("op");
        List<ActionError> errors = new ArrayList<ActionError>();

        String resetType = (String) getDto("resetType");
        if (GenericValidator.isBlankOrNull(resetType)) {
            errors.add(new ActionError("errors.required",
                    JSPHelper.getMessage(request, "SequenceRule.resetType")));
        }

        String startNumber = (String) getDto("startNumber");
        if (GenericValidator.isBlankOrNull(startNumber)) {
            errors.add(new ActionError("errors.required",
                    JSPHelper.getMessage(request, "SequenceRule.startNumber")));
        }

        String lastNumber = (String) getDto("lastNumber");
        if ("update".equals(op) && GenericValidator.isBlankOrNull(lastNumber)) {
            errors.add(new ActionError("errors.required",
                    JSPHelper.getMessage(request, "SequenceRule.lastNumber")));
        }

        //return errors because it lacks required fields
        if (!errors.isEmpty()) {
            return errors;
        }

        String format = (String) getDto("format");
        //format is required field
        if (GenericValidator.isBlankOrNull(format)) {
            return errors;
        }

        //validate format field
        VoucherSequenceRuleFormatUtil util = new VoucherSequenceRuleFormatUtil(format, startNumber, resetType);
        errors.addAll(util.formatValidator(request));

        if ("update".equals(op) && errors.isEmpty()) {
            ActionError changeFormatOrResetType = validateResetTypeChange(request);
            String confirmatioMsg = (String) getDto("confirmatioMsg");
            if (null != changeFormatOrResetType && !"true".equals(confirmatioMsg)) {
                errors.add(changeFormatOrResetType);
                setDto("confirmatioMsg", "true");
            }
        }

        return errors;
    }

    private ActionError validateResetTypeChange(HttpServletRequest request) {
        String actualFormat = (String) getDto("format");
        String actualResetType = (String) getDto("resetType");
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        if (GenericValidator.isBlankOrNull(actualFormat) ||
                GenericValidator.isBlankOrNull(actualResetType)) {
            return null;
        }

        SequenceRuleCmd sequenceRuleCmd = new SequenceRuleCmd();
        sequenceRuleCmd.setOp("read");
        sequenceRuleCmd.putParam("numberId", getDto("numberId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(sequenceRuleCmd, request);
            //sequencerule was deleted
            if (resultDTO.isFailure()) {
                return null;
            }

            String dbFormat = (String) resultDTO.get("format");
            Integer dbResetType = (Integer) resultDTO.get("resetType");

            ActionErrors errors = new ActionErrors();
            Map columns = new HashMap();
            columns.put("sequenceruleid", getDto("numberId").toString());
            columns.put("companyid", companyId);
            errors = ForeignkeyValidator.i.validate("invoice", columns, errors, new ActionError("Admin.Company.new"));

            //exists invoces generated with old format and has change format or resetType
            if (errors.isEmpty() &&
                    (!dbFormat.equals(actualFormat) || !dbResetType.toString().equals(actualResetType))) {
                return new ActionError("SequenceRule.warn.hasGeneratedInvoices");
            }

        } catch (AppLevelException e) {
            log.error("-> Execute " + SequenceRuleCmd.class.getName() + " FAIL", e);
        }

        return null;
    }

    private void validateOldFormatNumberAvailable(ActionErrors errors, HttpServletRequest request) {
        String actualFormat = (String) getDto("format");
        Object numberId = getDto("numberId");

        SequenceRuleCmd sequenceRuleCmd = new SequenceRuleCmd();
        sequenceRuleCmd.setOp("read");
        sequenceRuleCmd.putParam("numberId", numberId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(sequenceRuleCmd, request);
            if (resultDTO.isFailure()) {
                //sequence rule was deleted
                return;
            }

            String dbFormat = (String) resultDTO.get("format");

            if (!dbFormat.equals(actualFormat.trim())) {
                //format as changed, then validate available free numbers of old format
                sequenceRuleCmd = new SequenceRuleCmd();
                sequenceRuleCmd.setOp("hasAvailableFreeNumber");
                sequenceRuleCmd.putParam("numberId", numberId);

                resultDTO = BusinessDelegate.i.execute(sequenceRuleCmd, request);
                Integer freeNumberSize = (Integer) resultDTO.get("availableFreeNumberSize");

                if (freeNumberSize > 0) {
                    request.setAttribute("numberAvailableToFormatMsg", "true");
                    request.setAttribute("jsLoad", "onload=\"hideSequenceRuleFormPanel();\"");

                    errors.add("freeNumberMessage", new ActionError("SequenceRule.warn.freeNumberAvailable", freeNumberSize));
                }
            }
        } catch (AppLevelException e) {
            log.error("-> Execute free number validation " + SequenceRuleCmd.class.getName() + " FAIL", e);
        }
    }

    private void checkSequenceRuleDuplicated(ActionErrors errors, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        boolean isUpdate = "update".equals(getDto("op"));
        String type = (String) getDto("type");
        String format = (String) getDto("format");

        String numberId = null;
        if (isUpdate) {
            numberId = (String) getDto("numberId");
        }

        Map conditions = new HashMap();
        conditions.put("type", type);
        conditions.put("companyid", companyId);

        if (FinanceConstants.SequenceRuleType.VOUCHER.equal(type)) {
            conditions.put("startnumber", getDto("startNumber"));
        }

        boolean isDuplicated = DataBaseValidator.i.isDuplicate("sequencerule",
                "format", format,
                "numberid", numberId,
                conditions, isUpdate);

        if (isDuplicated) {
            if (FinanceConstants.SequenceRuleType.VOUCHER.equal(type)) {
                errors.add("duplicated", new ActionError("SequenceRule.msg.duplicated", format, getDto("startNumber")));
            } else {
                errors.add("duplicated", new ActionError("msg.Duplicated", format));
            }
        }
    }

    private ActionErrors updateForm(HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        errors.add("emptyError", new ActionError("Admin.Company.new"));
        request.setAttribute("skipErrors", "true");
        return errors;
    }

    private boolean isSaveButtonPressed(HttpServletRequest request) {
        return (null != request.getParameter("save")
                || null != request.getParameter("SaveAndNew")
                || null != getDto("deleteAllNumbers")
                || null != getDto("updateNumberFormat"));
    }


}
