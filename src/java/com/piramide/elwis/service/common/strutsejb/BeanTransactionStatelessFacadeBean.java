package com.piramide.elwis.service.common.strutsejb;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class BeanTransactionStatelessFacadeBean implements BeanTransactionStatelessFacade {

    private Log log = LogFactory.getLog(BeanTransactionStatelessFacade.class);

    @Resource
    private SessionContext sessionContext;

    @Resource
    private UserTransaction userTransaction;


    public ResultDTO execute(BeanTransactionEJBCommand ejbCmd) throws AppLevelException {

        try {
            final int timeout = ejbCmd.getTransactionTimeout();
            userTransaction.setTransactionTimeout(timeout);
            if (!ejbCmd.isReadOnly()) {
                log.debug("Initiating a transaction with timeout: " + timeout);
                userTransaction.begin();
            }
            ejbCmd.executeInStateless(sessionContext);
            if (!ejbCmd.isReadOnly()) {
                userTransaction.commit();
            }
            return ejbCmd.getResultDTO();
        } catch (Exception e) {
            try {
                if (!ejbCmd.isReadOnly()) {
                    userTransaction.rollback();
                }
            } catch (SystemException e1) {
                log.error("Unexpected exception when rolling back the transaction", e1);
            }
            throw new AppLevelException(e);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("execute: user=" + sessionContext.getCallerPrincipal());
            }
        }

    }
}
