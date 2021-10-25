package com.piramide.elwis.web.salesmanager.form;

import com.piramide.elwis.cmd.salesmanager.ActionCalculateValueCmd;
import com.piramide.elwis.cmd.salesmanager.ActionCmd;
import com.piramide.elwis.cmd.salesmanager.ActionTypeNumberGeneratorCmd;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.dto.salesmanager.ActionTypeDTO;
import com.piramide.elwis.dto.salesmanager.SalesProcessDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.form.MainCommunicationForm;
import com.piramide.elwis.web.salesmanager.el.Functions;
import com.piramide.elwis.web.salesmanager.util.ActionFormUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * Action Form handler.
 *
 * @author Fernando Montaño
 * @version $Id: ActionForm.java 10010 2010-11-30 19:46:17Z ivan $
 */
public class ActionForm extends MainCommunicationForm {
    private Log log = LogFactory.getLog(ActionForm.class);

    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {
        String changeActionType = (String) getDto("changeActionType");
        if ("true".equals(changeActionType)) {
            updateActionTypeRule(request);
            updateProbability(request);
            ActionErrors errors = new ActionErrors();
            request.setAttribute("skipErrors", "true");
            errors.add("returnToForm", new ActionError("Address.error.cityNotFound"));
            return errors;
        }

        if (isCalculateButtonPressed(request)) {
            updateValue(request);
        }

        ActionErrors errors = super.validate(mapping, request);
        ActionError duplicateNumberError = validateActionTypeNumber(request);
        if (null != duplicateNumberError) {
            errors.add("duplicateNumberError", duplicateNumberError);
        }

        ActionError changeNetGrossError = validateNetGrossChange(request);
        if (null != changeNetGrossError) {
            errors.add("changeNetGrossError", changeNetGrossError);
        }
        if (errors.isEmpty()) {
            ActionError netGrossChangeMsg = checkNetGrossChange(request);
            if (null != netGrossChangeMsg) {
                errors.add("netGrossChangeMsg", netGrossChangeMsg);
            }
        }
        return errors;
    }

    private void updateProbability(HttpServletRequest request) {
        String actionTypeId = (String) getDto("actionTypeId");

        if (null != actionTypeId && !"".equals(actionTypeId.trim())) {
            ActionTypeDTO actionTypeDTO = ActionFormUtil.i.getActionType(actionTypeId, request);
            if (null != actionTypeDTO) {
                setDto("probability", actionTypeDTO.get("probability"));
            }
        }
    }

    private void updateActionTypeRule(HttpServletRequest request) {
        String op = (String) getDto("op");

        if ("update".equals(op)) {
            return;
        }

        String actionTypeId = (String) getDto("actionTypeId");

        if (GenericValidator.isBlankOrNull(actionTypeId)) {
            setDto("number", "");
            return;
        }

        String processId = (String) getDto("processId");

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        String customerNumber = getCustomerNumber(request);

        ActionTypeNumberGeneratorCmd actionTypeNumberGeneratorCmd = new ActionTypeNumberGeneratorCmd();
        actionTypeNumberGeneratorCmd.putParam("companyId", companyId);
        actionTypeNumberGeneratorCmd.putParam("actionTypeId", actionTypeId);
        actionTypeNumberGeneratorCmd.putParam("customerNumber", customerNumber);
        actionTypeNumberGeneratorCmd.putParam("processId", processId);

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionTypeNumberGeneratorCmd, request);
            String actionTypenumber = (String) resultDTO.get("generateActionTypeNumber");
            if (null != actionTypenumber) {
                setDto("number", actionTypenumber);
            } else {
                setDto("number", "");
            }

        } catch (AppLevelException e) {
            log.error(ActionForm.class.getName() + " can't execute " + ActionTypeNumberGeneratorCmd.class.getName(), e);
        }
    }

    private String getCustomerNumber(HttpServletRequest request) {
        String processId = (String) getDto("processId");
        if (GenericValidator.isBlankOrNull(processId)) {
            return null;
        }

        SalesProcessDTO salesProcessDTO =
                com.piramide.elwis.web.salesmanager.el.Functions.getSalesProcessDTO(processId, request);

        if (null == salesProcessDTO) {
            return null;
        }

        Integer addressId = (Integer) salesProcessDTO.get("addressId");

        CustomerDTO customerDTO = com.piramide.elwis.web.contactmanager.el.Functions.getCustomer(addressId, request);
        if (null == customerDTO) {
            return null;
        }

        return (String) customerDTO.get("number");
    }

    private ActionError validateActionTypeNumber(HttpServletRequest request) {
        String number = (String) getDto("number");
        if (GenericValidator.isBlankOrNull(number)) {
            return null;
        }

        String actionTypeId = (String) getDto("actionTypeId");
        if (GenericValidator.isBlankOrNull(actionTypeId)) {
            return null;
        }

        User user = RequestUtils.getUser(request);

        ActionCmd actionCmd = new ActionCmd();
        actionCmd.setOp("isActionNumberUnique");
        actionCmd.putParam("companyId", user.getValue(Constants.COMPANYID));
        actionCmd.putParam("number", number);
        actionCmd.putParam("actionTypeId", actionTypeId);

        String op = (String) getDto("op");
        if ("update".equals(op)) {
            String contactId = (String) getDto("contactId");
            String processId = (String) getDto("processId");
            actionCmd.putParam("contactId", contactId);
            actionCmd.putParam("processId", processId);
        }

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionCmd, request);
            Boolean isActionNumberUnique = (Boolean) resultDTO.get("isActionNumberUnique");
            String actionTypeName = (String) resultDTO.get("actionTypeName");
            if (!isActionNumberUnique && !"".equals(actionTypeName)) {
                return new ActionError("Action.number.Duplicated", number, actionTypeName);
            }

        } catch (AppLevelException e) {
            log.error(ActionForm.class.getName() + " can't execute " + ActionCmd.class.getName(), e);
        }

        return null;
    }


    private ActionError checkNetGrossChange(HttpServletRequest request) {
        String showNetGrossWarningMessage = (String) getDto("showNetGrossWarningMessage");
        if (!GenericValidator.isBlankOrNull(showNetGrossWarningMessage) &&
                "true".equals(showNetGrossWarningMessage)) {
            return null;
        }

        String op = (String) getDto("op");
        if (!"update".equals(op)) {
            return null;
        }

        String pageNetGross = (String) getDto("netGross");
        if (GenericValidator.isBlankOrNull(pageNetGross)) {
            return null;
        }

        User user = RequestUtils.getUser(request);

        ActionCmd actionCmd = new ActionCmd();
        actionCmd.setOp("checkNetGrossChange");
        String contactId = (String) getDto("contactId");
        String processId = (String) getDto("processId");
        actionCmd.putParam("contactId", contactId);
        actionCmd.putParam("processId", processId);
        actionCmd.putParam("companyId", user.getValue(Constants.COMPANYID));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionCmd, request);
            Boolean isActionRelatedWithSale = (Boolean) resultDTO.get("isActionRelatedWithSale");
            if (null != isActionRelatedWithSale && isActionRelatedWithSale) {
                Integer actualNetGross = (Integer) resultDTO.get("actualNetGross");
                if (actualNetGross.toString().equals(pageNetGross)) {
                    return null;
                }

                setDto("showNetGrossWarningMessage", true);
                return new ActionError("Action.actionRelatedWithSale.msg",
                        JSPHelper.getMessage(request, "SalesProcessAction.netGross"));
            }

        } catch (AppLevelException e) {
            log.error(ActionForm.class.getName() + " can't execute " + ActionCmd.class.getName(), e);
        }

        return null;
    }

    private ActionError validateNetGrossChange(HttpServletRequest request) {
        String op = (String) getDto("op");
        if (!"update".equals(op)) {
            return null;
        }

        String pageNetGross = (String) getDto("netGross");
        if (GenericValidator.isBlankOrNull(pageNetGross)) {
            return null;
        }

        String contactId = (String) getDto("contactId");
        String processId = (String) getDto("processId");

        Boolean containValidActionPositions = Functions.containValidActionPositions(contactId,
                processId,
                pageNetGross,
                request);

        if (!containValidActionPositions) {
            return new ActionError("Action.netGrossChange.invalidActionPosition.error",
                    JSPHelper.getMessage(request, "SalesProcessAction.netGross"));
        }

        return null;
    }

    private void updateValue(HttpServletRequest request) {
        String contactId = (String) getDto("contactId");
        String processId = (String) getDto("processId");

        ActionCalculateValueCmd actionCalculateValueCmd = new ActionCalculateValueCmd();
        actionCalculateValueCmd.putParam("contactId", contactId);
        actionCalculateValueCmd.putParam("processId", processId);
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(actionCalculateValueCmd, request);
            if (null == resultDTO.get("actionValue")) {
                return;
            }

            BigDecimal value = (BigDecimal) resultDTO.get("actionValue");
            setDto("value", value);
        } catch (AppLevelException e) {
            log.error(ActionForm.class.getName() + " can't execute " + ActionCalculateValueCmd.class.getName(), e);
        }
    }

    private boolean isCalculateButtonPressed(HttpServletRequest request) {
        return null != request.getParameter("calculate");
    }
}
