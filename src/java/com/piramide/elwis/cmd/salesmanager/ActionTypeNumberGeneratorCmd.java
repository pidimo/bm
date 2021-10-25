package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.salesmanager.ActionTypeSequence;
import com.piramide.elwis.domain.salesmanager.ActionTypeSequenceHome;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ActionTypeNumberGeneratorCmd extends EJBCommand {
    @Override
    public void executeInStateless(SessionContext context) {
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        Integer actionTypeId = EJBCommandUtil.i.getValueAsInteger(this, "actionTypeId");
        Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
        String customerNumber = (String) paramDTO.get("customerNumber");

        generateActionTypeNumber(processId, actionTypeId, companyId, customerNumber);
    }

    private void generateActionTypeNumber(Integer processId, Integer actionTypeId, Integer companyId, String customerNumber) {
        Integer sequenceRuleId = getSequenceRuleId(actionTypeId, companyId);

        //The ActionType doesn't relate with sequence rule
        if (null == sequenceRuleId) {
            resultDTO.put("generateActionTypeNumber", null);
            return;
        }

        String number = InvoiceUtil.i.getActionTypeNumber(sequenceRuleId, customerNumber);

        resultDTO.put("generateActionTypeNumber", number);

    }

    private Integer getSequenceRuleId(Integer actionTypeId, Integer companyId) {
        ActionTypeSequenceHome actionTypeSequenceHome =
                (ActionTypeSequenceHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONTYPESEQUENCE);
        try {
            ActionTypeSequence actionTypeSequence = actionTypeSequenceHome.findByActionTypeId(actionTypeId, companyId);
            return actionTypeSequence.getNumberId();
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
