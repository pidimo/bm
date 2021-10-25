package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.admin.UserGroupDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 19, 2005
 * Time: 12:06:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserGroupCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UserGroupCmd..." + paramDTO);
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, new UserGroupDTO().getClass());
        log.debug("====================================================================");
        log.debug("ResultDTO...: " + resultDTO);
        log.debug("ResultDTO.forward...: " + resultDTO.getForward());
        log.debug("====================================================================");
    }

    public boolean isStateful() {
        return false;
    }
}

