package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * cmd to read only basic appointment data
 *
 * @author Miky
 * @version $Id: LightlyAppointmentReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class LightlyAppointmentReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing LightlyAppointmentReadCmd................" + paramDTO);
        Integer appointmentId = new Integer(paramDTO.get("appointmentId").toString());
        Appointment appointment = (Appointment) ExtendedCRUDDirector.i.read(new AppointmentDTO(appointmentId), resultDTO, false);
    }

    public boolean isStateful() {
        return false;
    }
}