package com.piramide.elwis.service.common.strutsejb;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.Local;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
@Local
public interface BeanTransactionStatelessFacade {

    /**
     * Executes a business logic. All Throwables thrown in EJB tier will
     * be wrapped in Exception, passed to Web tier and unwrapped.
     *
     * @param ejbCmd BeanTransactionEJBCommand that holds the business logic
     * @return DTO holds resultDTO of the business logic
     * @throws Exception that wraps SysLevelException thrown in EJB tier.
     */
    public ResultDTO execute(BeanTransactionEJBCommand ejbCmd) throws AppLevelException;
}
