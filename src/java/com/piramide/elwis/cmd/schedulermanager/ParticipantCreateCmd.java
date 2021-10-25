package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.admin.UserOfGroup;
import com.piramide.elwis.domain.admin.UserOfGroupHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.schedulermanager.Appointment;
import com.piramide.elwis.domain.schedulermanager.AppointmentHome;
import com.piramide.elwis.domain.schedulermanager.ScheduledUser;
import com.piramide.elwis.domain.schedulermanager.ScheduledUserHome;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.schedulermanager.ScheduledUserDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 28, 2005
 * Time: 6:10:20 PM
 * To change this template use File | Settings | File Templates.
 */

public class ParticipantCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());


    public void executeInStateless(SessionContext ctx) {
        log.debug("......  ParticipantCreateCMD ....  APPOINTMENT CMD..  execute ......" + paramDTO);

        List<Map> userEmailMapList = new ArrayList<Map>();
        ScheduledUserDTO dto = new ScheduledUserDTO();
        ScheduledUser scheduledUser = null;
        Appointment app = null;

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
        AppointmentHome appHome = (AppointmentHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_APPOINTMENT);

        if (paramDTO.get("cancel") != null) {
            resultDTO.setForward("Cancel");
            return;
        }
        try {
            app = appHome.findByPrimaryKey(new Integer(paramDTO.get("appointmentId").toString()));
        } catch (FinderException e) {
            log.debug("... appointment not found ...");
        }

        if ("group".equals(paramDTO.get("type"))) {
            log.debug(" ........type    group  option ................");
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
                        scheduledUser = scheduledUserHome.findByUserIdAndAppId(userOfGroup.getUserId(), new Integer(paramDTO.get("appointmentId").toString()));
                    } catch (FinderException e) {
                    }
                    if (scheduledUser == null) {
                        dto.put("appointmentId", paramDTO.get("appointmentId"));
                        dto.put("userId", userOfGroup.getUserId());
                        dto.put("userGroupId", userOfGroup.getUserGroupId());
                        dto.put("companyId", userOfGroup.getCompanyId());
                        scheduledUser = (ScheduledUser) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
                        if (scheduledUser != null) {
                            userEmailMapList = addUserSendEmailParameters(userEmailMapList, userOfGroup.getUserId());
                        }
                    } else {
                        try {
                            user = userHome.findByPrimaryKey(scheduledUser.getUserId());
                            address = addressHome.findByPrimaryKey(user.getAddressId());
                            addressName.append(address.getName());
                            addressName.append("    ");
                        } catch (FinderException e) {
                        }
                        failGroup = true;
                    }
                } else {
                    resultDTO.addResultMessage("UserGroup.notFound");
                }
            }

        } else if ("user".equals(paramDTO.get("type"))) {
            log.debug("........ type User option ............");
            addressName = new StringBuffer();
            if (values != null) {
                for (Iterator iterator = values.iterator(); iterator.hasNext();) {
                    user = null;
                    imp = null;
                    dto = new ScheduledUserDTO();
                    scheduledUser = null;
                    imp = (String) iterator.next();
                    try {
                        user = userHome.findByPrimaryKey(new Integer(imp));
                    } catch (FinderException e) {
                        log.debug("... user notFound ...");
                    }
                    if (user != null) {
                        try {
                            scheduledUser = scheduledUserHome.findByUserIdAndTaskId(user.getUserId(), new Integer(paramDTO.get("appointmentId").toString()));
                        } catch (FinderException e) {
                            log.debug(".... ScheduledUser not Found .....");
                        }
                        if (scheduledUser == null) {
                            dto.put("appointmentId", paramDTO.get("appointmentId"));
                            dto.put("userId", user.getUserId());
                            dto.put("userGroupId", null);
                            dto.put("companyId", user.getCompanyId());

                            scheduledUser = (ScheduledUser) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
                            if (scheduledUser != null) {
                                userEmailMapList = addUserSendEmailParameters(userEmailMapList, user.getUserId());
                            }
                        } else {
                            try {
                                user = userHome.findByPrimaryKey(scheduledUser.getUserId());
                                address = addressHome.findByPrimaryKey(user.getAddressId());
                                addressName.append(address.getName1());
                                if (address.getName2() != null && !"".equals(address.getName2().trim())) {
                                    addressName.append(",").append(address.getName2());
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
        }
        if (failGroup) {
            resultDTO.addResultMessage("Task.SchedulerNoEmpty", addressName.toString());
        }
        if (failUser) {
            resultDTO.addResultMessage("Task.UserNoEmpty", addressName.toString());
        }
        resultDTO.put("failGroup", new Boolean(failGroup));
        resultDTO.put("failUser", new Boolean(failUser));
        resultDTO.put("appointmentId", paramDTO.get("appointmentId"));

        //user notification parameters
        resultDTO.put("userNotificationList", userEmailMapList);
    }

    /**
     * Add user notification email parameters in list
     *
     * @param userMapList
     * @param toUserId
     * @return List
     */
    private List addUserSendEmailParameters(List<Map> userMapList, Integer toUserId) {
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User toUser = userHome.findByPrimaryKey(toUserId);
            if (!toUser.getAppointmentNotificationEmails().isEmpty()) {

                Map userEmailMap = new HashMap();

                if (userMapList.isEmpty()) {
                    User fromUser = userHome.findByPrimaryKey(new Integer(paramDTO.get("userSessionId").toString()));
                    String mailFrom = null;
                    if (!fromUser.getAppointmentNotificationEmails().isEmpty()) {
                        mailFrom = (String) fromUser.getAppointmentNotificationEmails().get(0);//get the first one as sender
                    }
                    //read from personal
                    String fromPersonal = null;
                    Address fromAddress = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(fromUser.getAddressId()), new ResultDTO(), false);
                    if (fromAddress != null) {
                        fromPersonal = fromAddress.getName();
                    }

                    userEmailMap.put("userId", toUserId);
                    userEmailMap.put("emailFrom", mailFrom);
                    userEmailMap.put("fromPersonal", fromPersonal);
                    userEmailMap.put("emailTo", toUser.getNotificationAppointmentEmail());
                    userMapList.add(userEmailMap);

                } else {
                    userEmailMap.putAll(userMapList.get(0));
                    userEmailMap.put("userId", toUserId);
                    userEmailMap.put("emailTo", toUser.getNotificationAppointmentEmail());
                    userMapList.add(userEmailMap);
                }
            }
        } catch (FinderException e) {
            log.debug("Can't find user...");
        }
        return userMapList;
    }

    public boolean isStateful() {
        return false;
    }
}

