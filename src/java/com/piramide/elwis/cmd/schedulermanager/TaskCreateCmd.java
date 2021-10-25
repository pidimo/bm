package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.SchedulerUtil;
import com.piramide.elwis.domain.schedulermanager.ScheduledUser;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeText;
import com.piramide.elwis.domain.schedulermanager.SchedulerFreeTextHome;
import com.piramide.elwis.domain.schedulermanager.Task;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import com.piramide.elwis.dto.schedulermanager.TaskDTO;
import com.piramide.elwis.dto.schedulermanager.UserTaskDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import java.util.Date;

/**
 * @author Mauren Carrasco
 */

public class TaskCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        TaskDTO taskDTO = new TaskDTO(paramDTO);

        DateTimeZone zone = SchedulerUtil.i.getUserDateTimeZone(new Integer(paramDTO.get("userId").toString()));

        Long createDateTime = EJBCommandUtil.i.getValueAsLong(this, "createDateTime");
        if (null == createDateTime) {
            createDateTime = DateUtils.createDate((new Date()).getTime(), zone.getID()).getMillis();
        }

        taskDTO.put("createDateTime", createDateTime);

        int startYearMonthDay[] = DateUtils.getYearMonthDay(new Integer(paramDTO.get("startDate").toString()));
        DateTime startDateTime = new DateTime(startYearMonthDay[0],
                startYearMonthDay[1],
                startYearMonthDay[2],
                Integer.parseInt(paramDTO.get("startHour").toString()),
                Integer.parseInt(paramDTO.get("startMin").toString()),
                0,
                0,
                zone);

        StringBuffer startTime = new StringBuffer();
        startTime.append(paramDTO.getAsString("startHour")).append(":").append(paramDTO.getAsString("startMin"));
        taskDTO.put("startTime", startTime.toString());
        taskDTO.put("startDateTime", new Long(startDateTime.getMillis()));

        //if expirationDate
        if (SchedulerConstants.TRUE_VALUE.equals(paramDTO.get("date"))) {
            StringBuffer expireTime = new StringBuffer();
            expireTime.append(paramDTO.getAsString("expireHour")).append(":").append(paramDTO.getAsString("expireMin"));
            taskDTO.put("expireTime", expireTime.toString());

            int yearMonthDay[] = DateUtils.getYearMonthDay(new Integer(paramDTO.get("expireDate").toString()));
            DateTime resultDateTime = new DateTime(yearMonthDay[0],
                    yearMonthDay[1],
                    yearMonthDay[2],
                    Integer.parseInt(paramDTO.get("expireHour").toString()),
                    Integer.parseInt(paramDTO.get("expireMin").toString()),
                    0,
                    0,
                    zone);
            taskDTO.put("expireDateTime", new Long(resultDateTime.getMillis()));
        } else {
            taskDTO.put("expireTime", null);
            taskDTO.put("expireDate", null);
            taskDTO.put("expireDateTime", null);
        }

        taskDTO.put("notification", new Boolean("on".equals(paramDTO.getAsString("notification"))));

        Task task = (Task) ExtendedCRUDDirector.i.create(taskDTO, resultDTO, false);

        if (task != null && !resultDTO.isFailure()) {//for owner task
            ScheduledUserDTO dto = new ScheduledUserDTO();
            dto.put("taskId", task.getTaskId());
            dto.put("userId", paramDTO.get("userTaskId"));
            dto.put("userGroupId", null);
            dto.put("companyId", task.getCompanyId());
            ScheduledUser scheduledUser = (ScheduledUser) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            if (scheduledUser != null) {
                UserTaskDTO userTaskDTO = new UserTaskDTO();
                userTaskDTO.put("scheduledUserId", scheduledUser.getScheduledUserId());
                userTaskDTO.put("statusId", task.getStatus());
                userTaskDTO.put("companyId", task.getCompanyId());
                ExtendedCRUDDirector.i.create(userTaskDTO, resultDTO, false);
            }

            if (!SchedulerConstants.EMPTY_VALUE.equals(paramDTO.get("descriptionText"))) {
                SchedulerFreeTextHome freeTextHome = (SchedulerFreeTextHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULER_FREETEXT);
                try {
                    SchedulerFreeText freeText = freeTextHome.create(paramDTO.getAsString("descriptionText").getBytes(), task.getCompanyId(),
                            new Integer(FreeTextTypes.FREETEXT_SCHEDULER));
                    task.setFreeTextId(freeText.getFreeTextId());
                } catch (CreateException e) {
                    log.error("Error creating the description", e);
                }
            } else {
                task.setFreeTextId(null);
            }
            resultDTO.put("taskId", task.getTaskId());
        } else {
            resultDTO.setResultAsFailure();
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}

