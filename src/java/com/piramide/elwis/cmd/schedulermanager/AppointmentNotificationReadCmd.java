package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.catalogmanager.Priority;
import com.piramide.elwis.domain.catalogmanager.PriorityHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.schedulermanager.AppointmentType;
import com.piramide.elwis.domain.schedulermanager.AppointmentTypeHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Read appointment data to send notification email to participants
 *
 * @author Miky
 * @version $Id: AppointmentNotificationReadCmd.java 02-jul-2008 13:44:41 $
 */
public class AppointmentNotificationReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing AppointmentNotificationReadCmd................" + paramDTO);
        Integer appointmentId = new Integer(paramDTO.get("appointmentId").toString());

        AppointmentReadCmd appointmentReadCmd = new AppointmentReadCmd();
        appointmentReadCmd.putParam("appointmentId", appointmentId);
        appointmentReadCmd.putParam("viewerUserId", paramDTO.get("viewerUserId"));
        appointmentReadCmd.putParam("userSessionId", paramDTO.get("userSessionId"));
        appointmentReadCmd.executeInStateless(ctx);

        ResultDTO appResultDTO = appointmentReadCmd.getResultDTO();
        resultDTO.putAll(appResultDTO);
        if (!appResultDTO.isFailure()) {
            //read apointment type name
            AppointmentTypeHome typeHome = (AppointmentTypeHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENTTYPE);
            if (appResultDTO.get("appointmentTypeId") != null) {
                try {
                    AppointmentType appointmentType = typeHome.findByPrimaryKey((Integer) appResultDTO.get("appointmentTypeId"));
                    resultDTO.put("typeName", appointmentType.getName());
                } catch (FinderException e) {
                    log.debug("Can't find EJB " + e);
                }
            }

            //read priority name
            PriorityHome priorityHome = (PriorityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRIORITY);
            if (appResultDTO.get("priorityId") != null) {
                try {
                    Priority priority = priorityHome.findByPrimaryKey((Integer) appResultDTO.get("priorityId"));
                    resultDTO.put("priorityName", priority.getPriorityName());
                } catch (FinderException e) {
                    log.debug("Can't find EJB " + e);
                }
            }

            //read user name
            UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
            AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
            try {
                User user = userHome.findByPrimaryKey((Integer) appResultDTO.get("userId"));
                Address userAddress = addressHome.findByPrimaryKey(user.getAddressId());
                resultDTO.put("userName", userAddress.getName());
            } catch (FinderException e) {
                log.debug("Can't find EJB " + e);
            }

            //read createdBy user
            try {
                User createdByUser = userHome.findByPrimaryKey((Integer) appResultDTO.get("createdById"));
                Address createdByAddress = addressHome.findByPrimaryKey(createdByUser.getAddressId());
                resultDTO.put("createdByName", createdByAddress.getName());
            } catch (FinderException e) {
                log.debug("Can't find EJB " + e);
            }

            //read contact person name
            if (appResultDTO.get("contactPersonId") != null) {
                try {
                    Address contactPersonAddress = addressHome.findByPrimaryKey((Integer) appResultDTO.get("contactPersonId"));
                    resultDTO.put("contactPersonName", contactPersonAddress.getName());
                } catch (FinderException e) {
                    log.debug("Can't find EJB " + e);
                }
            }
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}