package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.ActionType;
import com.piramide.elwis.domain.salesmanager.ActionTypeHome;
import com.piramide.elwis.dto.salesmanager.ActionTypeDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class ActionTypeUtilCmd extends EJBCommand {
    @Override
    public void executeInStateless(SessionContext ctx) {
        if ("getActionType".equals(getOp())) {
            Integer actionTypeId = EJBCommandUtil.i.getValueAsInteger(this, "actionTypeId");
            getActionType(actionTypeId);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void getActionType(Integer actionTypeId) {
        ActionTypeDTO actionTypeDTO = null;

        ActionType actionType = findActionTypeByPrimaryKey(actionTypeId);
        if (null != actionType) {
            actionTypeDTO = new ActionTypeDTO();
            DTOFactory.i.copyToDTO(actionType, actionTypeDTO);
        }

        resultDTO.put("getActionType", actionTypeDTO);
    }

    private ActionType findActionTypeByPrimaryKey(Integer actionTypeId) {
        ActionTypeHome actionTypeHome =
                (ActionTypeHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONTYPE);
        try {
            return actionTypeHome.findByPrimaryKey(actionTypeId);
        } catch (FinderException e) {
            return null;
        }
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
