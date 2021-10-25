package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.admin.UserOfGroup;
import com.piramide.elwis.domain.admin.UserOfGroupHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.schedulermanager.*;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import com.piramide.elwis.dto.schedulermanager.UserTaskDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * AlfaCentauro Team
 * This class is an adaptation of ParticipantCreateCmd
 *
 * @author Alvaro
 * @version $Id: ParticipantTaskCreateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ParticipantTaskCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("......  ParticipantTaskCreateCMD ...  execute ......" + paramDTO);

        ScheduledUserDTO dto = new ScheduledUserDTO();
        UserTaskDTO userTaskDTO = new UserTaskDTO();
        ScheduledUser scheduledUser = null;
        UserTask userTask = null;
        Task task = null;
        User user = null;
        Address address = null;
        StringBuffer addressName = null;
        boolean failGroup = false;
        boolean failUser = false;
        List values = null;
        values = (List) paramDTO.get("aditionals");
        String imp = null;
        ScheduledUserHome scheduledUserHome = (ScheduledUserHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_SCHEDULEDUSER);
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        TaskHome taskHome = (TaskHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_TASK);

        if (paramDTO.get("cancel") != null) {
            resultDTO.setForward("Cancel");
            return;
        }
        try {
            task = taskHome.findByPrimaryKey(new Integer(paramDTO.get("taskId").toString()));
        } catch (FinderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        if ("group".equals(paramDTO.get("type"))) {
            UserOfGroup userOfGroup = null;
            Collection users = null;
            addressName = new StringBuffer();
            UserOfGroupHome userOfGroupHome = (UserOfGroupHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USEROFGROUP);
            try {
                users = userOfGroupHome.findByUserGroupId(new Integer(paramDTO.get("userGroupId").toString()));
            } catch (FinderException e) {
            }
            for (Iterator iterator = users.iterator(); iterator.hasNext();) {
                userOfGroup = (UserOfGroup) iterator.next();
                if (userOfGroup != null) {
                    try {
                        scheduledUser = null;
                        scheduledUser = scheduledUserHome.findByUserIdAndTaskId(userOfGroup.getUserId(), new Integer(paramDTO.get("taskId").toString()));
                    } catch (FinderException e) {
                    }
                    if (scheduledUser == null) {
                        dto.put("taskId", paramDTO.get("taskId"));
                        dto.put("userId", userOfGroup.getUserId());
                        dto.put("userGroupId", userOfGroup.getUserGroupId());
                        dto.put("statusId", task.getStatus());
                        dto.put("companyId", task.getCompanyId());
                        scheduledUser = (ScheduledUser) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
                        if (scheduledUser != null) {
                            userTaskDTO.put("scheduledUserId", scheduledUser.getScheduledUserId());
                            userTaskDTO.put("companyId", task.getCompanyId());
                            userTask = (UserTask) ExtendedCRUDDirector.i.create(userTaskDTO, resultDTO, false);
                            userTask.setStatusId(task.getStatus());
                        }
                    } else {
                        try {
                            user = userHome.findByPrimaryKey(scheduledUser.getUserId());
                            address = addressHome.findByPrimaryKey(user.getAddressId());
                            addressName.append(address.getName1());
                            if (address.getName() != null && !"".equals(address.getName2().trim())) {
                                addressName.append(",").append(address.getName2());
                            }
                            addressName.append("     ");
                        } catch (FinderException e) {
                        }
                        failGroup = true;
                    }
                } else {
                    resultDTO.addResultMessage("UserGroup.notFound");
                }
            }

        } else if ("user".equals(paramDTO.get("type"))) {
            addressName = new StringBuffer();
            for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                user = null;
                imp = null;
                dto = new ScheduledUserDTO();
                scheduledUser = null;
                userTask = null;
                userTaskDTO = new UserTaskDTO();
                imp = (String) iterator.next();

                try {
                    user = userHome.findByPrimaryKey(new Integer(imp));

                } catch (FinderException e) {
                }
                if (user != null) {
                    try {
                        scheduledUser = scheduledUserHome.findByUserIdAndTaskId(user.getUserId(), new Integer(paramDTO.get("taskId").toString()));
                    } catch (FinderException e) {
                    }
                    if (scheduledUser == null) {
                        dto.put("taskId", paramDTO.get("taskId"));
                        dto.put("userId", user.getUserId());
                        dto.put("userGroupId", null);
                        dto.put("statusId", task.getStatus());
                        dto.put("companyId", task.getCompanyId());
                        scheduledUser = (ScheduledUser) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
                        if (scheduledUser != null) {
                            userTaskDTO.put("scheduledUserId", scheduledUser.getScheduledUserId());
                            userTaskDTO.put("companyId", task.getCompanyId());
                            userTask = (UserTask) ExtendedCRUDDirector.i.create(userTaskDTO, resultDTO, false);
                            userTask.setStatusId(task.getStatus());
                        }
                    } else {

                        try {
                            user = userHome.findByPrimaryKey(scheduledUser.getUserId());
                            address = addressHome.findByPrimaryKey(user.getAddressId());
                            addressName.append(address.getName1());
                            if (address.getName2() != null && !"".equals(address.getName2().trim())) {
                                addressName.append(", ").append(address.getName2());
                            }
                        } catch (FinderException e) {
                        }
                        resultDTO.isFailure();
                        failUser = true;
                    }
                } else {
                    resultDTO.addResultMessage("User.notFound");
                }
            }
        }
        if (failGroup) {
            resultDTO.addResultMessage("Task.SchedulerNoEmpty", addressName.toString());
        }
        if (failUser) {
            resultDTO.addResultMessage("Task.UserNoEmpty", addressName.toString());
        }
        resultDTO.put("failGroup", new Boolean(failGroup));
        resultDTO.put("failUser", new Boolean(failUser));
        resultDTO.put("taskId", paramDTO.get("taskId"));
    }

    public boolean isStateful() {
        return false;
    }
}


