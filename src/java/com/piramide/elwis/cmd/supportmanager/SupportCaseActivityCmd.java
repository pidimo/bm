package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GenerateDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.ContactCreateCmd;
import com.piramide.elwis.cmd.contactmanager.ContactDeleteCmd;
import com.piramide.elwis.cmd.contactmanager.ContactUpdateCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.catalogmanager.Priority;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.supportmanager.*;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.catalogmanager.PriorityDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.supportmanager.StateDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseActivityDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseActivityCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(SupportCaseActivityCmd.class);
    SessionContext ctx;

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        log.debug("->Execute " + SupportCaseActivityCmd.class.getName() + " OK");
        this.ctx = ctx;
        update(paramDTO);
    }

    public void update(ParamDTO paramDTO) throws AppLevelException {
        SupportCaseActivityDTO dto = new SupportCaseActivityDTO();
        dto.put("workLevelId", paramDTO.get("workLevelId"));
        dto.put("activityId", paramDTO.get("activityId"));
        dto.put("version", paramDTO.get("version"));
        log.debug("Update Activity:  " + dto);

        SupportCaseActivity activity = (SupportCaseActivity) EJBFactory.i.findEJB(dto);
        if (activity.getIsOpen().intValue() != SupportConstants.OPENTYPE_OPEN && !"s".equals(paramDTO.get("nosave"))) {
            resultDTO.setForward("CaseMainSearch");
            if (activity.getIsOpen().intValue() == SupportConstants.OPENTYPE_CLOSE) {
                resultDTO.addResultMessage("SupportCase.msg.assignedAnotherUser", readUserName(activity.getToUserId()));
                return;
            } else if (activity.getIsOpen().intValue() == SupportConstants.OPENTYPE_CASECLOSE) {
                resultDTO.addResultMessage("SupportCase.msg.closedCase", readUserName(activity.getToUserId()));
                dto.remove("workLevelId");
                paramDTO.remove("stateId");
            }
        }

        if ("s".equals(paramDTO.get("nosave"))) {
            paramDTO.put("commOp", "update");
            paramDTO.put("view", "true");
            paramDTO.put("op", "update");
            doCRUD_Communication(activity, paramDTO);
            return;
        }

        activity = (SupportCaseActivity) ExtendedCRUDDirector.i.update(dto, resultDTO, false, true, false, null);
        if (activity != null && !resultDTO.isFailure()) {
            if (paramDTO.containsKey("stateId")) {
                State state = (State) EJBFactory.i.findEJB(new StateDTO(paramDTO.getAsInt("stateId")));
                activity.setState(state);
            }
            String description = (String) paramDTO.get("activityDescription");
            activity.getDescriptionText().setFreeTextValue(description.getBytes());

            if (paramDTO.containsKey("commOp") && AdminConstants.INTERNAL_USER.equals(paramDTO.get("userType"))) {
                doCRUD_Communication(activity, paramDTO);
                if (resultDTO.isFailure()) {
                    ctx.setRollbackOnly();
                    return;
                }
            }
            SupportCaseActivity currentActivity = newOpenOrCloseActivity(activity, paramDTO.containsKey("new_activity"));
            resultDTO.put("activityId", currentActivity.getActivityId());
        }
    }

    private void doCRUD_Communication(SupportCaseActivity activity, ParamDTO paramDTO) throws AppLevelException {
        String commOp = paramDTO.getAsString("commOp");
        log.debug("Communication Operation  dto(commOp)=" + commOp);

        SupportContactCmd supportContactCmd = new SupportContactCmd();
        List supportContacts = supportContactCmd.readByActivity(activity.getCaseId(), activity.getActivityId());

        if (supportContacts == null) {
            createCommunication(activity, paramDTO);
            return;
        }

        if ("delete".equals(commOp)) {
            deleteCommunication(supportContacts, paramDTO);
            resultDTO.put("DELETE_COMM", "true");
            resultDTO.put("activityId", activity.getActivityId());
        } else {
            updateCommunication(supportContacts, paramDTO);
        }
    }

    private void deleteSupportContact(SupportContact supportContact) throws AppLevelException {
        try {
            supportContact.remove();
        } catch (RemoveException e) {
            throw new AppLevelException(e.getMessage());
        }
    }


    private void deleteCommunication(List supportContacts, ParamDTO paramDTO) throws AppLevelException {
        log.debug("Delete communication");
        for (Object object : supportContacts) {
            SupportContact supportContact = (SupportContact) object;
            String contactId = supportContact.getContactId().toString();
            Integer companyId = supportContact.getCompanyId();
            deleteSupportContact(supportContact);

            ContactDeleteCmd contactDeleteCmd = new ContactDeleteCmd();
            contactDeleteCmd.putParam("contactId", contactId);
            contactDeleteCmd.putParam("companyId", companyId);
            contactDeleteCmd.putParam("type", paramDTO.get("type"));
            contactDeleteCmd.executeInStateless(ctx);
        }
    }

    private void createCommunication(SupportCaseActivity activity, ParamDTO paramDTO) throws AppLevelException {
        log.debug("createCommunication:" + paramDTO);
        paramDTO.remove("contactId");

        EJBCommand cmd = getEJBCommandCreate(paramDTO);
        cmd.putParam("isAction", false);
        cmd.putParam(paramDTO);
        cmd.setOp("create");
        cmd.executeInStateless(ctx);

        resultDTO.putAll(cmd.getResultDTO());
        if (cmd.getResultDTO().isFailure() || "FailSendMail".equals(cmd.getResultDTO().getForward())) {
            addErrorMessages(cmd);
            resultDTO.setResultAsFailure();
            resultDTO.putAll(paramDTO);
            return;
        }

        List<Integer> communicationIdentifiers =
                (List<Integer>) cmd.getResultDTO().get("communicationIdentifiers");
        log.debug("->communicationIdentifiers : " + communicationIdentifiers);

        SupportContactCmd supportContactCmd = new SupportContactCmd();
        if (null == communicationIdentifiers) {
            supportContactCmd.create(activity.getActivityId(),
                    activity.getCaseId(),
                    activity.getCompanyId(),
                    (Integer) cmd.getResultDTO().get("contactId"));
            return;
        }

        for (Integer contactId : communicationIdentifiers) {
            supportContactCmd.create(activity.getActivityId(), activity.getCaseId(), activity.getCompanyId(), contactId);
        }

        if (communicationIdentifiers.isEmpty()) {
            resultDTO.addResultMessage("Communication.msg.NoActivityCreatedForSupportCase");
            resultDTO.setForward("FailCreateActivities");
        }
    }

    private void updateCommunication(List supportContacts, ParamDTO paramDTO) throws AppLevelException {
        if (supportContacts == null) {
            return;
        }

        //only email can be return many supportContacts
        if (supportContacts.size() != 1) {
            return;
        }

        SupportContact supportContact = (SupportContact) supportContacts.get(0);

        EJBCommand command = getEJBCommandUpdate(paramDTO);

        paramDTO.put("contactId", supportContact.getContactId().toString());
        paramDTO.put("version", paramDTO.get("commVersion"));

        command.putParam(paramDTO);
        command.executeInStateless(ctx);

        if (command.getResultDTO().isFailure()) {
            addErrorMessages(command);
            resultDTO.setResultAsFailure();
        } else {
            resultDTO.putAll(command.getResultDTO());
        }
    }

    private EJBCommand getEJBCommandCreate(ParamDTO paramDTO) {
        if ("true".equals(paramDTO.get("generate"))
                || "true".equals(paramDTO.get("build"))) {
            return new GenerateDocumentCmd();
        }

        return new ContactCreateCmd();
    }

    private EJBCommand getEJBCommandUpdate(ParamDTO paramDTO) {
        if ("true".equals(paramDTO.get("generate"))
                || "true".equals(paramDTO.get("build"))) {
            return new GenerateDocumentCmd();
        }

        return new ContactUpdateCmd();
    }

    private void addErrorMessages(EJBCommand ejbCommand) {
        if (ejbCommand.getResultDTO().hasResultMessage()) {
            for (Iterator iterator = ejbCommand.getResultDTO().getResultMessages(); iterator.hasNext();) {
                ResultMessage message = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(message);
            }
        }
    }

    public SupportCaseActivity newOpenOrCloseActivity(SupportCaseActivity activity, boolean newActivity) throws AppLevelException {
        log.debug("newOpenOrCloseActivity: " + activity + " - isNew:" + newActivity);
        boolean closeCase = activity.getState().getStageType().intValue() == SupportConstants.CLOSE_STATE;
        SupportCaseActivity childActivity = activity;
        if (!closeCase && newActivity) {
            setCloseState(activity, SupportConstants.OPENTYPE_CLOSE);
            log.debug("......... New Activity  ... !!");
            childActivity = createChild(activity, "");
            updateSupportCase(childActivity, false);
        } else {
            updateSupportCase(activity, closeCase);
        }

        return childActivity;
    }

    public SupportCaseActivity createChild(SupportCaseActivity parentCaseActivity, String description) throws AppLevelException {
        log.debug("Create child activity:  " + paramDTO);

        AutoAssignateUserCmd cmd = new AutoAssignateUserCmd();
        Integer toUser = null;
        if (AdminConstants.EXTERNAL_USER.equals(paramDTO.getAsString("userType"))) {
            toUser = parentCaseActivity.getFromUserId();
        } else {
            toUser = cmd.getPossibleUserForAssignate(paramDTO);
        }

        Integer fromUserId = parentCaseActivity.getToUserId();
        Integer stateId = parentCaseActivity.getStateId();

        if (paramDTO.containsKey("newStateId")) {
            stateId = (Integer) paramDTO.get("newStateId");
        }

        Integer workLevelId = parentCaseActivity.getWorkLevelId();
        if (paramDTO.containsKey("newWorkLevelId")) {
            workLevelId = (Integer) paramDTO.get("newWorkLevelId");
        }

        SupportCaseActivity activity = create(parentCaseActivity.getCaseId(), toUser, fromUserId, stateId,
                workLevelId, parentCaseActivity.getCompanyId(), description);
        Integer openDate = parentCaseActivity.getCloseDate();
        activity.setOpenDate(openDate);
        activity.setParentActivityId(parentCaseActivity.getActivityId());

        if (paramDTO.containsKey("USER_SESSIONID") && !toUser.equals(paramDTO.get("USER_SESSIONID"))) {
            extractNotificationInformation(activity, toUser, (Integer) paramDTO.get("USER_SESSIONID"));
        }

        resultDTO.put("res_toUser", toUser);
        return activity;
    }

    public void extractNotificationInformation(SupportCaseActivity activity, Integer toUserId, Integer userSessionId) {
        log.debug(" ... extractNotificationInformation.....");
        Map templateParameters = new HashMap();
        Map mailParameters = new HashMap();
        User TOuser = (User) EJBFactory.i.findEJB(new UserDTO(toUserId));
        //log.debug("ToUser:"+TOuser.getUserId()+" - "+TOuser.getUserLogin()+" ->"+TOuser.getSupportCaseNotificationEmails());
        if (!TOuser.getSupportCaseNotificationEmails().isEmpty()) {
            User FROMuser = (User) EJBFactory.i.findEJB(new UserDTO(userSessionId));
            String mailFrom = null;
            if (!FROMuser.getSupportCaseNotificationEmails().isEmpty()) {
                mailFrom = (String) FROMuser.getSupportCaseNotificationEmails().get(0);//get the first one as sender
            }
            SupportCase supportCase = activity.getSupportCase();

            templateParameters.put("number", supportCase.getNumber());
            templateParameters.put("title", supportCase.getCaseTitle());
            templateParameters.put("openBy", readUserName(supportCase.getOpenByUserId()));
            templateParameters.put("from", readAddressName(FROMuser));
            templateParameters.put("to", readAddressName(TOuser));
            templateParameters.put("state", activity.getState().getStateName());
            templateParameters.put("priority", (supportCase.getPriorityId() != null) ? getPriorityName(supportCase.getPriorityId()) : "");
            templateParameters.put("assignedAt", activity.getOpenDate());
            if (activity.getParentActivityId() != null) {
                SupportCaseActivity parentCaseActivity = activity.getParentActivity();
                templateParameters.put("activityDetail", new String(parentCaseActivity.getDescriptionText().getFreeTextValue()));
            } else {
                templateParameters.put("activityDetail", "");
            }

            templateParameters.put("detail", new String(supportCase.getDescriptionText().getFreeTextValue()));

            mailParameters.put("emailFrom", mailFrom);
            mailParameters.put("emailTo", TOuser.getNotificationSupportCaseEmail());
            mailParameters.put("fromPersonal", templateParameters.get("from"));
            //mailParameters.put("subject", activity.getSupportCase().getCaseTitle());
            resultDTO.put("TEMPLATE_PAMAMETERS", templateParameters);
            resultDTO.put("MAIL_PAMAMETERS", mailParameters);
        }
    }


    public SupportCaseActivity create(SupportCase supportCase) throws AppLevelException {
        log.debug(" .. create function execute ..." + supportCase.getCaseId());
        SupportCaseActivity activity = create(supportCase.getCaseId(), supportCase.getToUserId(), supportCase.getFromUserId(), supportCase.getStateId(),
                supportCase.getWorkLevelId(), supportCase.getCompanyId(), "");
        State state = (State) EJBFactory.i.findEJB(new StateDTO(supportCase.getStateId()));
        activity.setState(state);
        activity.setSupportCase(supportCase);
        activity.setOpenDate(supportCase.getOpenDate());
        return activity;

    }

    private SupportCaseActivity create(Integer caseId, Integer toUserId, Integer fromUserId, Integer stateId, Integer workLevelId,
                                       Integer companyId, String description) throws AppLevelException {
        log.debug(" .. create function execute ...");
        SupportCaseActivityDTO dto = new SupportCaseActivityDTO();
        dto.put("caseId", caseId);
        dto.put("companyId", companyId);
        dto.put("fromUserId", fromUserId);
        dto.put("stateId", stateId);
        dto.put("toUserId", toUserId);
        dto.put("workLevelId", workLevelId);
        dto.put("freeTextId", createDescription(description, companyId));
        log.debug("Create Activity :" + dto);
        SupportCaseActivity activity = (SupportCaseActivity) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
        activity.setIsOpen(new Integer(SupportConstants.OPENTYPE_OPEN));//0
        return activity;
    }

    private void updateSupportCase(SupportCaseActivity activity, boolean isCloseCase) {
        log.debug("  updateSupportCase function :    " + activity + "    - IsClose:     " + isCloseCase);
        SupportCase supportCase = activity.getSupportCase();
        supportCase.setWorkLevelId(activity.getWorkLevelId());
        supportCase.setStateId(activity.getStateId());
        supportCase.setToUserId(activity.getToUserId());
        supportCase.setFromUserId(activity.getFromUserId());

        if (isCloseCase) {
            setCloseState(activity, SupportConstants.OPENTYPE_CASECLOSE);
            supportCase.setCloseDate(activity.getCloseDate());
            resultDTO.put("IS_CLOSED", "true");
        }
    }

    private void setCloseState(SupportCaseActivity activity, int openType) {
        log.debug("Set Close state the activity ......setCloseState  function ");
        activity.setCloseDate(DateUtils.dateToInteger(new Date()));
        activity.setIsOpen(new Integer(openType));
    }

    public void changeAsignedUser(SupportCaseActivity activity, Integer toUserId) throws AppLevelException {
        log.debug(" ... changeAsignedUser ..execute function ...");
        paramDTO.put("toUserId", toUserId);
        SupportCaseActivity newActivity = newOpenOrCloseActivity(activity, true);
        resultDTO.put("activityId", newActivity.getActivityId());
    }

    private Integer createDescription(String description, Integer companyId) throws AppLevelException {

        SupportFreeTextHome freeTextHome = (SupportFreeTextHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_FREETEXT);
        try {
            SupportFreeText freeText = freeTextHome.create(description.getBytes(), companyId, new Integer(FreeTextTypes.FREETEXT_SUPPORT));
            log.debug("Created description....");
            return freeText.getFreeTextId();
        } catch (CreateException e) {
            throw new AppLevelException(e.getMessage());
        }
    }

    public String readUserName(Integer userId) {
        User user = (User) EJBFactory.i.findEJB(new UserDTO(userId));
        return readAddressName(user);
    }

    private String readAddressName(User user) {
        Address address = (Address) EJBFactory.i.findEJB(new AddressDTO(user.getAddressId()));
        return address.getName();
    }

    private String getPriorityName(Integer priorityId) {
        Priority priority = (Priority) EJBFactory.i.findEJB(new PriorityDTO(priorityId));
        return priority.getPriorityName();
    }

    public void setCtx(SessionContext ctx) {
        this.ctx = ctx;
    }

    public boolean isStateful() {
        return false;
    }
}