package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.salesmanager.StatusDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the Status
 *
 * @author Fernando Montaño
 * @version $Id: StatusCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class StatusCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(StatusCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing StatusCmd...");
        super.checkDuplicate = false;
        super.setOp(this.getOp());
        super.execute(ctx, new StatusDTO(paramDTO));
    }

    public boolean isStateful() {
        return false;
    }
}
