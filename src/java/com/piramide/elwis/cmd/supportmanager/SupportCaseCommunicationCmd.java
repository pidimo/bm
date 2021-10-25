package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.GenerateDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.ContactCreateCmd;
import com.piramide.elwis.cmd.contactmanager.ContactDeleteCmd;
import com.piramide.elwis.cmd.contactmanager.ContactReadCmd;
import com.piramide.elwis.cmd.contactmanager.ContactUpdateCmd;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.supportmanager.SupportCaseActivity;
import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.dto.supportmanager.SupportCaseActivityDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Iterator;
import java.util.List;

/**
 * User: yumi
 * Date: Aug 12, 2005
 * Time: 4:40:01 PM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseCommunicationCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(SupportCaseCommunicationCmd.class);
    SessionContext ctx;

    public void executeInStateless(SessionContext ctx_) throws AppLevelException {
        ctx = ctx_;
        if ("create".equals(getOp())) {
            createCommunication(paramDTO);
        } else if ("update".equals(getOp())) {
            updateCommunication(paramDTO);
        } else if ("delete".equals(getOp())) {
            deleteCommunication(paramDTO);
        } else {
            readCommunication(paramDTO);
        }
    }

    private void deleteSupportContact(SupportContact supportContact) throws AppLevelException {
        try {
            if (supportContact != null) {
                supportContact.remove();
            }
        } catch (RemoveException e) {
            throw new AppLevelException(e.getMessage());
        }
    }

    private EJBCommand getEJBCommand() {
        if ("true".equals(paramDTO.get("generate")) || "true".equals(paramDTO.get("build"))) {
            return new GenerateDocumentCmd();
        }

        return new ContactCreateCmd();
    }

    private void createCommunication(ParamDTO paramDTO) throws AppLevelException {
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        Integer caseId = EJBCommandUtil.i.getValueAsInteger(this, "caseId");

        paramDTO.remove("contactId");

        EJBCommand cmd = getEJBCommand();
        cmd.putParam(paramDTO);
        cmd.putParam("companyId", companyId.toString());
        cmd.putParam("isAction", false);
        if (null != paramDTO.get("addressId")) {
            cmd.putParam(Constants.USER_ADDRESSID, paramDTO.get("addressId").toString());
        }
        cmd.executeInStateless(ctx);

        resultDTO.putAll(cmd.getResultDTO());

        if (cmd.getResultDTO().hasResultMessage() &&
                (cmd.getResultDTO().isFailure() || "FailSendMail".equals(cmd.getResultDTO().getForward()))) {
            for (Iterator iterator = cmd.getResultDTO().getResultMessages(); iterator.hasNext();) {
                ResultMessage message = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(message);
            }
            resultDTO.setResultAsFailure();
            return;
        }

        List<Integer> communicationIdentifiers =
                (List<Integer>) cmd.getResultDTO().get("communicationIdentifiers");

        log.debug("->communicationIdentifiers : " + communicationIdentifiers);

        SupportContactCmd contactCmd = new SupportContactCmd();
        if (null == communicationIdentifiers) {
            contactCmd.create(caseId, companyId, (Integer) cmd.getResultDTO().get("contactId"));
            return;
        }

        for (Integer contactId : communicationIdentifiers) {
            contactCmd.create(caseId, companyId, contactId);
        }
    }

    private void updateCommunication(ParamDTO paramDTO) throws AppLevelException {
        EJBCommand cmd = null;
        Contact comm = null;

        if ("true".equals(paramDTO.get("generate")) || "true".equals(paramDTO.get("build"))) {
            log.debug("Is GENERATE!!!");
            cmd = new GenerateDocumentCmd();
        } else {
            cmd = new ContactUpdateCmd();
        }
        cmd.putParam(paramDTO);
        cmd.putParam("companyId", paramDTO.get("companyId").toString());
        cmd.executeInStateless(ctx);
        ContactDTO commDTO = new ContactDTO(paramDTO);

        log.debug("updateCommunication:     " + paramDTO);

        if (cmd instanceof GenerateDocumentCmd) {
            if (cmd.getResultDTO().isFailure()) {
                Iterator it = cmd.getResultDTO().getResultMessages();
                while (it.hasNext()) {
                    ResultMessage resultMessage = (ResultMessage) it.next();
                    resultDTO.addResultMessage(resultMessage);
                }
                resultDTO.setForward("Fail");
                return;
            }

            if ("Fail".equals(cmd.getResultDTO().getForward()) || null != cmd.getResultDTO().get("objNotFound")) {
                resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
                resultDTO.putAll(cmd.getResultDTO());
                resultDTO.setForward("Fail");
                return;
            }
        }

        if (cmd instanceof ContactUpdateCmd) {
            if (cmd.getResultDTO().isFailure()) {
                resultDTO.addResultMessage("Common.error.concurrency");
                paramDTO.setOp("read");
                readCommunication(paramDTO);
                resultDTO.setForward("Redirect");
            }

            if ("Fail".equals(cmd.getResultDTO().getForward()) || null != cmd.getResultDTO().get("objNotFound")) {
                resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
                resultDTO.setResultAsFailure();
                resultDTO.setForward("Fail");
                return;
            }
        }

        log.debug("After execute Dyna CMD .... ");
        ResultDTO contactResultDTO = cmd.getResultDTO();
        resultDTO.putAll(contactResultDTO);
    }


    public void deleteSupportContact(Integer caseId, Integer contactId) throws AppLevelException {
        SupportContactCmd supportContactCmd = new SupportContactCmd();
        SupportContact supportContact = supportContactCmd.readByContact(caseId, contactId);
        deleteSupportContact(supportContact);
    }

    private void deleteCommunication(ParamDTO paramDTO) throws AppLevelException {
        Integer caseId = new Integer(paramDTO.getAsString("caseId"));
        Integer contactId = new Integer(paramDTO.getAsString("contactId"));
        SupportContactCmd supportContactCmd = new SupportContactCmd();
        SupportContact supportContact = supportContactCmd.readByContact(caseId, contactId);

        if (supportContact != null) {
            Integer companyId = supportContact.getCompanyId();
            ContactDeleteCmd contactDeleteCmd = new ContactDeleteCmd();
            paramDTO.remove("caseId");
            contactDeleteCmd.putParam(paramDTO);
            contactDeleteCmd.putParam("contactId", contactId.toString());
            contactDeleteCmd.putParam("companyId", companyId);
            deleteSupportContact(supportContact);
            contactDeleteCmd.executeInStateless(ctx);
            if (contactDeleteCmd.getResultDTO().isFailure()) {
                Iterator iterator = contactDeleteCmd.getResultDTO().getResultMessages();
                while (iterator.hasNext()) {
                    resultDTO.addResultMessage((ResultMessage) iterator.next());
                }
                resultDTO.setResultAsFailure();
                return;
            }
        } else {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
            resultDTO.setForward("Fail");
            return;
        }
    }

    public void readCommunication(ParamDTO paramDTO) throws AppLevelException {
        Integer caseId = new Integer(paramDTO.get("caseId").toString());
        Integer contactId = new Integer(paramDTO.get("contactId").toString());
        SupportContactCmd supportContactCmd = new SupportContactCmd();
        SupportContact supportContact = supportContactCmd.readByContact(caseId, contactId);
        log.debug("Read Communication ... !!" + supportContact);
        if (supportContact != null) {
            if (supportContact.getActivityId() != null) {
                resultDTO.put("activityId", supportContact.getActivityId());
                SupportCaseActivity activity = (SupportCaseActivity) EJBFactory.i.findEJB(new SupportCaseActivityDTO(supportContact.getActivityId()));
                if (activity.getIsOpen().intValue() != SupportConstants.OPENTYPE_OPEN) {
                    if ("true".equals(paramDTO.get("withReferences"))) {
                        resultDTO.addResultMessage("SupportCaseActivity.msg.referencedCommunication", paramDTO.get("note"));
                        resultDTO.setResultAsFailure();
                        return;
                    } else {
                        resultDTO.put("READ_ONLY", "true");
                    }
                }
            }

            ContactReadCmd contactCmd = new ContactReadCmd();
            paramDTO.remove("caseId");
            paramDTO.remove("activityId");
            contactCmd.putParam(paramDTO);
            contactCmd.executeInStateless(ctx);
            ResultDTO contactResultDTO = contactCmd.getResultDTO();
            Address address = (Address) EJBFactory.i.findEJB(new AddressDTO((Integer) contactResultDTO.get("addressId")));
            resultDTO.put("contact", address.getName());
            resultDTO.putAll(contactResultDTO);
        } else {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }
}