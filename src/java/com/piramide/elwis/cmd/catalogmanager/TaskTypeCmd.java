package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.schedulermanager.TaskTypeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jun 29, 2005
 * Time: 2:04:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaskTypeCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing TaskTypeCmd..." + this.getOp());
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, TaskTypeDTO.class);
    }

    public boolean isStateful() {
        return false;
    }
}
