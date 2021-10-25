package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.campaignmanager.CommunicationFromCampaignGenerationCmd;
import com.piramide.elwis.cmd.campaignmanager.ReadCampaignGenerationEmailCmd;
import com.piramide.elwis.cmd.salesmanager.LightlySalesProcessCmd;
import com.piramide.elwis.cmd.webmailmanager.ReadEmailCmd;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.supportmanager.SupportCase;
import com.piramide.elwis.domain.supportmanager.SupportCaseActivity;
import com.piramide.elwis.domain.supportmanager.SupportCaseActivityHome;
import com.piramide.elwis.domain.supportmanager.SupportCaseHome;
import com.piramide.elwis.domain.webmailmanager.MailContact;
import com.piramide.elwis.domain.webmailmanager.MailContactHome;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.CommunicationTypes;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.SupportConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.EmailSourceUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;


/**
 * Communication reading logic
 *
 * @author Tayes
 * @version $Id: ContactReadCmd.java 9979 2010-08-23 21:15:47Z ivan $
 */
public class ContactReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        try {
            if (paramDTO.get("withReferences") != null) {
                ContactDTO dto = new ContactDTO(paramDTO);
                IntegrityReferentialChecker.i.check(dto, resultDTO);
                if (resultDTO.isFailure()) {
                    return;
                }
            }

            Contact myContact = (Contact) CRUDDirector.i.read(new ContactDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.put("actionForward", "/Communication/List.do");
                return;
            }

            if (paramDTO.containsKey("generate")) {
                resultDTO.clear();
                resultDTO.put("contact", myContact);
                return;
            } else {
                readSupportCase();
                if (myContact.getContactPersonId() != null) {
                    Address address = myContact.getContactPerson().getContactPerson();
                    StringBuffer sb = new StringBuffer(address.getName1());
                    if (address.getName2() != null) {
                        sb.append(", ").append(address.getName2());
                    }
                    resultDTO.put("contactPersonName", new String(sb));
                }
                if (myContact.getProcessId() != null) {
                    LightlySalesProcessCmd processCmd = new LightlySalesProcessCmd();
                    processCmd.putParam("processId", myContact.getProcessId());
                    processCmd.executeInStateless(ctx);
                    resultDTO.put("processName", processCmd.getResultDTO().get("processName"));

                }
                if (CommunicationTypes.MEETING.equals(myContact.getType()) ||
                        CommunicationTypes.PHONE.equals(myContact.getType()) ||
                        CommunicationTypes.OTHER.equals(myContact.getType())) {
                    String freeTextValue = "";
                    if (null != myContact.getContactFreeText()) {
                        freeTextValue = new String(myContact.getContactFreeText().getValue());
                    }
                    resultDTO.put("freeText", freeTextValue);
                } else if (CommunicationTypes.EMAIL.equals(myContact.getType())) {//Read a mail
                    if (isCampaignGenerationCommunication(myContact.getContactId(), ctx)) {
                        readCampaignGenerationMail(myContact.getContactId(), myContact.getCompanyId(), paramDTO.get("note"), ctx);
                    } else {
                        readMail(myContact.getContactId(), myContact.getCompanyId(), myContact.getAddressId(),
                                myContact.getContactPersonId(), paramDTO.get("note"), myContact.getInOut(), ctx);
                    }
                }
            }

            if ("ok".equals(paramDTO.getAsString("return"))) {
                if (resultDTO.get("version").toString().equals(paramDTO.get("version"))) {
                    resultDTO.putAll(paramDTO);
                } else {
                    resultDTO.addResultMessage("Document.update.other");
                }
            }
        } catch (NumberFormatException e) {
            resultDTO.addResultMessage("Common.invalid.id");
            resultDTO.setResultAsFailure();
        }
    }


    public boolean isStateful() {
        return false;
    }

    private void readSupportCase() {
        if (paramDTO.containsKey("caseId")) {
            SupportCaseHome supportCaseHome = (SupportCaseHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE);
            try {
                SupportCase supportCase = supportCaseHome.findByPrimaryKey(new Integer(paramDTO.get("caseId").toString()));
                if (paramDTO.containsKey("activityId")) {
                    SupportCaseActivityHome activityHome = (SupportCaseActivityHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE_ACTIVITY);
                    SupportCaseActivity activity = activityHome.findByPrimaryKey(new Integer(paramDTO.get("activityId").toString()));
                    if (activity.getIsOpen().intValue() != SupportConstants.OPENTYPE_OPEN) {
                        resultDTO.put("READ_ONLY", "true");
                    }
                }
                resultDTO.put("supportCaseName", supportCase.getCaseTitle());
                resultDTO.put("caseId", supportCase.getCaseId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void readMail(Integer contactId, Integer companyId, Integer addressId, Integer contactPersonId,
                          Object subject, Integer in_Out, SessionContext ctx) {
        MailContact mailContact = getEmailContact(contactId, companyId);
        if (null == mailContact) {
            resultDTO.addResultMessage("msg.NotFound", subject);
            resultDTO.setResultAsFailure();
            return;
        }
        Integer mailId = mailContact.getMailId();
        Integer userMailId = mailContact.getMail().getFolder().getUserMailId();
        EmailSourceUtil.i.buildAndCompleteMail(mailId, userMailId);
        Boolean changeToReadState = false;
        if (null != paramDTO.get("elwisUserId")) {
            Integer elwisUserId = (Integer) paramDTO.get("elwisUserId");
            if (userMailId.equals(elwisUserId)) {
                changeToReadState = true;
            }
        }
        ReadEmailCmd readEmailCmd = new ReadEmailCmd();
        readEmailCmd.putParam("mailId", mailId);
        readEmailCmd.putParam("userMailId", userMailId);
        readEmailCmd.putParam("changeToReadState", changeToReadState);
        readEmailCmd.putParam("readerUserMailId", paramDTO.get("elwisUserId"));
        readEmailCmd.executeInStateless(ctx);
        resultDTO.putAll(readEmailCmd.getResultDTO());
    }

    private MailContact getEmailContact(Integer contactId, Integer companyId) {
        MailContactHome mailContactHome =
                (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
        try {
            return mailContactHome.findByContactId(contactId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private boolean isCampaignGenerationCommunication(Integer contactId, SessionContext ctx) {
        CommunicationFromCampaignGenerationCmd campGenCommunicationCmd = new CommunicationFromCampaignGenerationCmd();
        campGenCommunicationCmd.putParam("contactId", contactId);
        campGenCommunicationCmd.executeInStateless(ctx);
        return campGenCommunicationCmd.isCampaignGenerationCommunication();
    }

    private void readCampaignGenerationMail(Integer contactId, Integer companyId, Object subject, SessionContext ctx) {
        MailContact mailContact = getEmailContact(contactId, companyId);
        if (null == mailContact) {
            resultDTO.addResultMessage("msg.NotFound", subject);
            resultDTO.setResultAsFailure();
            return;
        }

        ReadCampaignGenerationEmailCmd readCampaignGenerationEmailCmd = new ReadCampaignGenerationEmailCmd();
        readCampaignGenerationEmailCmd.putParam("mailId", mailContact.getMailId());
        readCampaignGenerationEmailCmd.putParam("userMailId", mailContact.getMail().getFolder().getUserMailId());
        readCampaignGenerationEmailCmd.putParam("contactId", contactId);
        readCampaignGenerationEmailCmd.executeInStateless(ctx);

        resultDTO.putAll(readCampaignGenerationEmailCmd.getResultDTO());
        resultDTO.put("to", mailContact.getEmail());
    }
}
