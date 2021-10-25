package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.salesmanager.ActionPositionHome;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ActionPositionCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionPositionCmd.class);

    @Override
    public void executeInStateless(SessionContext sessionContext) {
        if ("getLastPositionNumber".equals(getOp())) {
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");

            getLastPositionNumber(processId, contactId, companyId);
        }
    }

    private void getLastPositionNumber(Integer processId, Integer contactId, Integer companyId) {
        ActionPositionHome actionPositionHome =
                (ActionPositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONPOSITION);
        Integer result = null;
        try {
            result = actionPositionHome.selectMaxPositionNumber(processId, contactId, companyId);
        } catch (FinderException e) {
            log.debug(" Cannot find the max number for the ActionPositions");
        }

        resultDTO.put("getLastPositionNumber", result);
    }

    public boolean isStateful() {
        return false;
    }
}
