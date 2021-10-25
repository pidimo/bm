package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.catalogmanager.PriorityDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the priorities
 *
 * @author Ivan
 * @version $Id: PriorityCmd.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */


public class PriorityCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing PriorityCmd...");
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, PriorityDTO.class);
    }

    public boolean isStateful() {
        return false;
    }
}