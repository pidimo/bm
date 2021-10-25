package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.salesmanager.PriorityDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the sales process priorities
 *
 * @author Fernando Montaño
 * @version $Id: PriorityCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */


public class PriorityCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing PriorityCmd...");
        super.checkDuplicate = false;
        super.setOp(this.getOp());
        super.execute(ctx, new PriorityDTO(paramDTO));

    }

    public boolean isStateful() {
        return false;
    }
}