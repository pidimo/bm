package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.dto.schedulermanager.AppointmentTypeDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 * This class executes the operations(create, read, update, delete) for a AppointmentType
 *
 * @author Alvaro
 * @version $Id: AppointmentTypeCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AppointmentTypeCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AppointmentTypeCmd..." + this.getOp());
        super.setOp(this.getOp());
        super.executeInStateless(ctx, paramDTO, AppointmentTypeDTO.class);
    }

    public boolean isStateful() {
        return false;
    }
}