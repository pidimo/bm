package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.supportmanager.SupportCaseWorkLevelDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 3:33:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkLevelCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SupportCaseWorkLevelCmd     ...");
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, SupportCaseWorkLevelDTO.class);
    }

    public boolean isStateful() {
        return false;
    }
}
