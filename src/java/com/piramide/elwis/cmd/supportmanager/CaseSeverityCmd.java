package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.supportmanager.SupportCaseSeverityDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 10:15:01 AM
 * To change this template use File | Settings | File Templates.
 */
public class CaseSeverityCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing CaseSeverityCmd     ...");
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, SupportCaseSeverityDTO.class);
    }

    public boolean isStateful() {
        return false;
    }
}