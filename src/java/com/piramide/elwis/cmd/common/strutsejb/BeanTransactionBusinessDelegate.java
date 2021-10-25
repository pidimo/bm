package com.piramide.elwis.cmd.common.strutsejb;

import com.piramide.elwis.service.common.strutsejb.BeanTransactionStatelessFacade;
import com.piramide.elwis.utils.configuration.EARJndiProperty;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.ServiceLocator;
import net.java.dev.strutsejb.SysLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.naming.NamingException;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
public class BeanTransactionBusinessDelegate {

    protected final Log log = LogFactory.getLog(getClass());

    private static final String JNDI_BEAN_TRANSACTION_STATELESS_FACADE = EARJndiProperty.getEarName() + "BeanTransactionStatelessFacadeBean/local";

    /**
     * Singleton instance.
     */
    public static final BeanTransactionBusinessDelegate i = new BeanTransactionBusinessDelegate();

    private BeanTransactionBusinessDelegate() {
    }


    /**
     * Executes an EJBCommand.
     *
     * @param cmd EJBCommand to be executed
     * @return DTO
     * @throws net.java.dev.strutsejb.AppLevelException
     *          an uncaught exception
     */
    public ResultDTO execute(BeanTransactionEJBCommand cmd) throws AppLevelException {

        //logging
        if (log.isDebugEnabled()) {
            log.debug("execute: executing: " + cmd);
        }

        //execute EJBCommand
        final ResultDTO resultDTO;
        resultDTO = executeInStateless(cmd);

        //logging
        if (log.isDebugEnabled()) {
            log.debug("execute: resultDTO: " + resultDTO);
        }
        return resultDTO;
    }

    private ResultDTO executeInStateless(BeanTransactionEJBCommand cmd)
            throws AppLevelException {
        try {
            final BeanTransactionStatelessFacade service =
                    (BeanTransactionStatelessFacade) ServiceLocator.i.lookup(
                            JNDI_BEAN_TRANSACTION_STATELESS_FACADE);

            return service.execute(cmd);

        } catch (NamingException e) {
            throw new SysLevelException(e);
        }
    }


}

