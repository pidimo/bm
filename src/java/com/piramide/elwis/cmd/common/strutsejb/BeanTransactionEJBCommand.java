package com.piramide.elwis.cmd.common.strutsejb;

import net.java.dev.strutsejb.EJBCommand;

/**
 * @author Fernando Javier Montaño Torrico
 * @version 1.0
 */
public class BeanTransactionEJBCommand extends EJBCommand {
    /**
     * 60 minutes in seconds is the default transaction timeout, by default the session bean will start the transaction
     * using this value for timeout. If some business logic needs to increase the timeout of the transaction it is
     * needed to write the logic to define the transaction timeout, this is something needed for long running services
     *
     * @return the transaction timeout
     */
    public int getTransactionTimeout() {
        return 3600;//60 minutes
    }

    /**
     * Defines if the command must be executed within a transaction or not, if is read-only, transaction is not started at all.
     *
     * @return by default the command is executed within a transaction, so false is returned as default.
     */
    public boolean isReadOnly() {
        return false;
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
