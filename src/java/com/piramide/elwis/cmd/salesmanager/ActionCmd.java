package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ActionCmd extends EJBCommand {
    @Override
    public void executeInStateless(SessionContext context) {
        if ("isActionNumberUnique".equals(getOp())) {
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            Integer actionTypeId = EJBCommandUtil.i.getValueAsInteger(this, "actionTypeId");
            String number = (String) paramDTO.get("number");
            isActionNumberUnique(contactId, processId, companyId, number, actionTypeId);
        }
        if ("changeNetGross".equals(getOp())) {
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            Integer pageNetGross = EJBCommandUtil.i.getValueAsInteger(this, "pageNetGross");
            changeNetGross(contactId, processId, pageNetGross);
        }
        if ("containValidActionPositions".equals(getOp())) {
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            Integer pageNetGross = EJBCommandUtil.i.getValueAsInteger(this, "pageNetGross");
            containValidActionPositions(contactId, processId, pageNetGross);
        }
        if ("checkNetGrossChange".equals(getOp())) {
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            checkNetGrossChange(contactId, processId, companyId);
        }

    }

    private void checkNetGrossChange(Integer contactId, Integer processId, Integer companyId) {
        Action action = getAction(contactId, processId);
        boolean isActionRelatedWithSale = false;
        if (null == action) {
            resultDTO.put("isActionRelatedWithSale", isActionRelatedWithSale);
            return;
        }

        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);

        try {
            Collection sales = saleHome.findByContactId(contactId, processId, companyId);
            isActionRelatedWithSale = !sales.isEmpty();
        } catch (FinderException e) {
            //
        }
        resultDTO.put("actualNetGross", action.getNetGross());
        resultDTO.put("isActionRelatedWithSale", isActionRelatedWithSale);
    }


    /**
     * This method check if the sale positions associated to sales process action with 'contactId' and 'processId' as key
     * contain valid prices according to 'pageNetGross' selected from ui.
     * <p/>
     * If can't find the action, the method put in ResultDTO and returns true value.
     * <p/>
     * After put the result in ResultDTO object the key used is 'containValidActionPositions'
     *
     * @param contactId    contact identifier
     * @param processId    sales process identifier
     * @param pageNetGross netGross selected from ui
     * @return true if all action positions contains the prices acording to 'pageNetGross' selected or the action asociated
     *         to contacId and processId was deleted.
     */
    private boolean containValidActionPositions(Integer contactId, Integer processId, Integer pageNetGross) {
        boolean containValidActionPositions = true;
        Action action = getAction(contactId, processId);
        if (null == action) {
            resultDTO.put("containValidActionPositions", containValidActionPositions);
            return containValidActionPositions;
        }

        List actionPositions = new ArrayList(action.getActionPositions());

        for (int i = 0; i < actionPositions.size(); i++) {
            ActionPosition actionPosition = (ActionPosition) actionPositions.get(i);
            if (FinanceConstants.NetGrossFLag.NET.equal(pageNetGross) && null == actionPosition.getPrice()) {
                containValidActionPositions = false;
                break;
            }

            if (FinanceConstants.NetGrossFLag.GROSS.equal(pageNetGross) && null == actionPosition.getUnitPriceGross()) {
                containValidActionPositions = false;
                break;
            }
        }

        resultDTO.put("containValidActionPositions", containValidActionPositions);

        return containValidActionPositions;
    }

    private void changeNetGross(Integer contactId, Integer processId, Integer pageNetGross) {

        resultDTO.put("changeNetGross", 0);
        Action action = getAction(contactId, processId);
        if (null == action) {
            return;
        }

        if (action.getNetGross().equals(pageNetGross)) {
            return;
        }

        Collection actionPositions = action.getActionPositions();
        if (null == actionPositions || actionPositions.isEmpty()) {
            resultDTO.put("changeNetGross", 1);
            return;
        }

        for (Object object : actionPositions) {
            ActionPosition actionPosition = (ActionPosition) object;
            if (FinanceConstants.NetGrossFLag.NET.equal(pageNetGross) && null == actionPosition.getPrice()) {
                resultDTO.put("changeNetGross", -1);
                break;
            }
            if (FinanceConstants.NetGrossFLag.GROSS.equal(pageNetGross) && null == actionPosition.getUnitPriceGross()) {
                resultDTO.put("changeNetGross", -1);
                break;
            }
        }
    }

    private void isActionNumberUnique(Integer contactId,
                                      Integer processId,
                                      Integer companyId,
                                      String number,
                                      Integer actionTypeId) {
        ActionHome actionHome =
                (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);
        ActionType actionType = getActionType(actionTypeId);

        String actionTypeName = "";
        if (null != actionType) {
            actionTypeName = actionType.getActionTypeName();
        }

        resultDTO.put("actionTypeName", actionTypeName);
        try {
            List elements = (List) actionHome.findByNumber(companyId, actionTypeId, number);
            if (elements.size() > 1) {
                resultDTO.put("isActionNumberUnique", false);
                return;
            }

            if (elements.size() == 1) {
                Action action = (Action) elements.get(0);

                if (action.getContactId().equals(contactId) && action.getProcessId().equals(processId)) {
                    resultDTO.put("isActionNumberUnique", true);
                } else {
                    resultDTO.put("isActionNumberUnique", false);
                }

                return;
            }

            if (elements.size() == 0) {
                resultDTO.put("isActionNumberUnique", true);
            }
        } catch (FinderException e) {
            resultDTO.put("isActionNumberUnique", true);
        }
    }


    private Action getAction(Integer contactId, Integer processId) {
        ActionHome actionHome =
                (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);

        ActionPK actionPK = new ActionPK();
        actionPK.contactId = contactId;
        actionPK.processId = processId;

        Action action = null;
        try {
            action = actionHome.findByPrimaryKey(actionPK);
        } catch (FinderException e) {
            //
        }
        return action;
    }

    private ActionType getActionType(Integer actionTypeId) {
        ActionTypeHome actionTypeHome =
                (ActionTypeHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONTYPE);
        try {
            return actionTypeHome.findByPrimaryKey(actionTypeId);
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
