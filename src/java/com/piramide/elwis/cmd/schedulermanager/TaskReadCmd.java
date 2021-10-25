package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.SchedulerUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * @author Mauren Carrasco
 */

public class TaskReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new TaskDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.put("referenced", Boolean.valueOf(true));
            }
        }

        Task task = (Task) ExtendedCRUDDirector.i.read(new TaskDTO(paramDTO), resultDTO, false);

        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        SalesProcessHome salesHome = (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        SalesProcess salesProcess = null;
        Address address = null;

        if (task != null && !resultDTO.isFailure()) {
            DateTimeZone zone = SchedulerUtil.i.getUserDateTimeZone(new Integer(paramDTO.get("recordUserId").toString()));

            Long startDate = task.getStartDateTime();
            DateTime startDateTime = new DateTime(startDate, zone);
            resultDTO.put("startDate", (DateUtils.dateToInteger(startDateTime)));
            resultDTO.put("startHour", new Integer(startDateTime.getHourOfDay()));
            resultDTO.put("startMin", new Integer(startDateTime.getMinuteOfHour()));
            resultDTO.put("createDateTime", task.getCreateDateTime());
            resultDTO.put("updateDateTime", task.getUpdateDateTime());

            if (((Integer) resultDTO.get("startMin")).intValue() < 10) {
                resultDTO.put("startMin", "0" + resultDTO.get("startMin").toString());
            }

            if (task.getExpireDate() != null) {
                Long expireDate = task.getExpireDateTime();
                DateTime expireDateTime = new DateTime(expireDate, zone);
                resultDTO.put("expireDate", (DateUtils.dateToInteger(expireDateTime)));
                resultDTO.put("expireHour", new Integer(expireDateTime.getHourOfDay()));
                resultDTO.put("expireMin", new Integer(expireDateTime.getMinuteOfHour()));
                resultDTO.put("date", new Boolean(true));
                if (((Integer) resultDTO.get("expireMin")).intValue() < 10) {
                    resultDTO.put("expireMin", "0" + resultDTO.get("expireMin").toString());
                }
            } else {
                resultDTO.put("date", new Boolean(false));
            }

            if (task.getSchedulerFreeText() != null) {
                resultDTO.put("descriptionText", new String(task.getSchedulerFreeText().getValue()));
                resultDTO.put("title", task.getTitle());
            }

            if (task.getAddressId() != null) {
                try {
                    address = addressHome.findByPrimaryKey(task.getAddressId());
                } catch (FinderException e) {
                    log.debug("address notFound ...");
                    e.printStackTrace();
                }
                if (address != null) {
                    StringBuffer addressName = new StringBuffer();
                    addressName.append(address.getName1());
                    if (address.getName2() != null && !"".equals(address.getName2())) {
                        if ("1".equals(address.getAddressType())) {
                            addressName.append(", ");
                        } else {
                            addressName.append(" ");
                        }
                        addressName.append(address.getName2());
                    }
                    resultDTO.put("addressId", address.getAddressId());
                    resultDTO.put("contact", addressName.toString());
                }
            }

            if (task.getProcessId() != null) {
                try {
                    salesProcess = salesHome.findByPrimaryKey(task.getProcessId());
                } catch (FinderException e) {
                    log.debug("salesProcess for this task notFound ... " + e);
                }
                if (salesProcess != null) {
                    resultDTO.put("processName", salesProcess.getProcessName());
                    resultDTO.put("processId", salesProcess.getProcessId());
                }
            }

            resultDTO.put("taskId", task.getTaskId());
            log.debug("userTask info .... ");
            ScheduledUserHome home = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
            UserTaskHome userTaskHome = (UserTaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_USERTASK);
            ScheduledUser scheduledUser = null;
            UserTask userTask = null;
            try {
                scheduledUser = (ScheduledUser) home.findByUserIdAndTaskId(new Integer(paramDTO.get("recordUserId").toString()), task.getTaskId());
                userTask = (UserTask) userTaskHome.findByPrimaryKey(scheduledUser.getScheduledUserId());
            } catch (FinderException e) {
                log.debug("schedulerUser or userTask notFound ...");
            }
            if (userTask != null) {
                resultDTO.put("statusId", userTask.getStatusId());
                resultDTO.put("oldAssignedStatus", userTask.getStatusId());  //to check if the status has been checked on update, for assigned
                resultDTO.put("participantVersion", userTask.getVersion());
                if (userTask.getSchedulerFreeText() != null) {
                    resultDTO.put("noteValue", new String(userTask.getSchedulerFreeText().getValue()));
                }
            }
            // in order to see if he is single for or I create for several and if it participates or no
            try {
                Collection schedulerUsers = home.findByTaskId(task.getTaskId());
                for (Iterator iterator = schedulerUsers.iterator(); iterator.hasNext();) {
                    ScheduledUser schedulerUser = (ScheduledUser) iterator.next();
                    //para habilitar (IU) el estado y descripcion del participante
                    if (schedulerUser.getUserId().equals(paramDTO.get("userId")) &&
                            !(schedulerUsers.size() == 1 && task.getUserId().equals(schedulerUser.getUserId()))) {
                        resultDTO.put("itParticipates", "true");
                        return;
                    }
                }

            } catch (FinderException e) {
                log.debug("not exist schedulerUser for this task ...");
                resultDTO.setResultAsFailure();
                return;
            }
        } else {
            log.debug(" task is Null or resultDTO isFailure ... ");
            resultDTO.setForward("Fail");
            resultDTO.setResultAsFailure();
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}

