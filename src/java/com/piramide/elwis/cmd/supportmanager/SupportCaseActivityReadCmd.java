package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.ContactReadCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.supportmanager.SupportCaseActivity;
import com.piramide.elwis.domain.supportmanager.SupportCaseActivityHome;
import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseActivityDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseActivityReadCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(SupportCaseActivityReadCmd.class);
    SessionContext ctx;

    public Integer getOpenCaseActivityId(Integer caseId) {
        Integer activityId = null;
        SupportCaseActivityHome caseActivityHome = (SupportCaseActivityHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE_ACTIVITY);
        try {
            SupportCaseActivity activity = caseActivityHome.findByTypeOpen(caseId, SupportConstants.OPENTYPE_OPEN);
            activityId = activity.getActivityId();
        } catch (FinderException e) {
            log.debug("Not found open activity for:" + caseId);
        }
        return activityId;
    }

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        log.debug("Executing  ... SupportCaseActivityReadCmd     ...");
        this.ctx = ctx;

        Integer activityId = new Integer(paramDTO.get("activityId").toString());
        SupportCaseActivity caseActivity = (SupportCaseActivity) ExtendedCRUDDirector.i.read(new SupportCaseActivityDTO(activityId), resultDTO, false);
        if (caseActivity != null) {
            resultDTO.put("isOpen", caseActivity.getIsOpen());
            resultDTO.put("closeDate", caseActivity.getCloseDate());
            resultDTO.put("stateId", caseActivity.getStateId());
            resultDTO.put("workLevelId", caseActivity.getWorkLevelId());
            resultDTO.put("od", caseActivity.getOpenDate());
            resultDTO.put("userAssigned", caseActivity.getToUserId());
            SupportCaseActivity parentActivity = caseActivity.getParentActivity();

            if (parentActivity != null) {
                resultDTO.put("type", null);
                resultDTO.put("parentDescription", new String(caseActivity.getParentActivity().getDescriptionText().getFreeTextValue()));
            } else {
                resultDTO.put("parentDescription", new String(caseActivity.getSupportCase().getDescriptionText().getFreeTextValue()));
            }
            resultDTO.put("activityDescription", new String(caseActivity.getDescriptionText().getFreeTextValue()));

            readUserName(caseActivity.getFromUserId(), "fromUserName");
            readCommunication(caseActivity.getActivityId(), caseActivity.getCaseId());
        }
    }

    private void readUserName(Integer userId, String field) {
        User user = (User) EJBFactory.i.findEJB(new UserDTO(userId));
        Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(user.getAddressId()));
        resultDTO.put(field, address.getName());
    }

    public void readCommunication(Integer activityId, Integer caseId) throws AppLevelException {
        SupportContactCmd supportContactCmd = new SupportContactCmd();
        List supportContacts = supportContactCmd.readByActivity(caseId, activityId);
        if (null == supportContacts) {
            return;
        }

        if (supportContacts.isEmpty()) {
            return;
        }

        SupportContact supportContact = (SupportContact) supportContacts.get(0);

        paramDTO.remove("generate");
        if (supportContact != null) {
            ContactReadCmd contactCmd = new ContactReadCmd();
            contactCmd.putParam(paramDTO);
            contactCmd.putParam("contactId", supportContact.getContactId().toString());
            contactCmd.executeInStateless(ctx);

            ResultDTO contactResultDTO = contactCmd.getResultDTO();
            log.debug("Communication RESILT:" + contactResultDTO);
            contactResultDTO.put("commVersion", contactResultDTO.remove("version"));
            Address address = (Address) EJBFactory.i.findEJB(new AddressDTO((Integer) contactResultDTO.get("addressId")));
            resultDTO.put("contact", address.getName());
            resultDTO.putAll(contactResultDTO);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
