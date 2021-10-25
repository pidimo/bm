package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.dto.schedulermanager.AppointmentDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 7, 2005
 * Time: 9:44:05 AM
 * To change this template use File | Settings | File Templates.
 */

public class LightlySchedulerCmd extends EJBCommand {

    private Log log = LogFactory.getLog(LightlySchedulerCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read appointment command ... ");
        log.debug("appoinmentId to read = " + paramDTO);

        //Read the appointment
        AppointmentDTO appoinmentDTO = new AppointmentDTO(paramDTO);
        Appointment appointment;

        try {
            appointment = (Appointment) EJBFactory.i.findEJB(appoinmentDTO);
            resultDTO.put("title", appointment.getTitle());
            resultDTO.put("isPrivate", appointment.getIsPrivate());
        } catch (EJBFactoryException ex) {
            appoinmentDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
