package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.ContactDeleteCmd;
import com.piramide.elwis.cmd.supportmanager.SupportCaseCommunicationCmd;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.supportmanager.SupportContact;
import com.piramide.elwis.domain.supportmanager.SupportContactHome;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.dto.webmailmanager.MailContactDTO;
import com.piramide.elwis.service.webmail.SaveEmailService;
import com.piramide.elwis.service.webmail.SaveEmailServiceHome;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id CommunicationManagerCmd ${time}
 */
public class CommunicationManagerCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String op = getOp();

        if ("checkFrom".equals(op)) {
            Integer mailId = (Integer) paramDTO.get("mailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Boolean saveSendItems = Boolean.valueOf(true);
            if (null == paramDTO.get("saveSendItems")) {
                saveSendItems = Boolean.valueOf(false);
            }
            checkFrom(mailId, companyId, saveSendItems);
        }
        if (ExtendedCRUDDirector.OP_CREATE.equals(op)) {
            Mail email = (Mail) paramDTO.get("email");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            List recipientMapList = (List) paramDTO.get("recipientMapList");
            List addressSelectedUI = (List) paramDTO.get("addressSelectedUI");
            Integer inOut = Integer.valueOf("0");
            if (null != paramDTO.get("inOut") && "1".equals(paramDTO.get("inOut").toString())) {
                inOut = Integer.valueOf("1");
            }
            createWebCommunications(email, userMailId, recipientMapList, inOut, addressSelectedUI, ctx);
        }
        if ("createInCommunication".equals(op)) {
            Mail email = (Mail) paramDTO.get("email");
            Integer userMailId = (Integer) paramDTO.get("userMailId");
            List recipientMapList = (List) paramDTO.get("recipientMapList");
            Integer inOut = Integer.valueOf("0");
            createInCommunications(email, userMailId, recipientMapList, ctx);
        }
        if ("createManualAddressContact".equals(op)) {
            Mail email = (Mail) paramDTO.get("email");
            Integer employeeId = (Integer) paramDTO.get("employeeId");
            Integer addressId = (Integer) paramDTO.get("addressId");
            String emailAddress = (String) paramDTO.get("emailAddress");
            Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
            User user = WebmailAccountUtil.i.getElwisUser(userMailId);

            Integer inOut = Integer.valueOf("0");
            if (null != paramDTO.get("inOut") && "1".equals(paramDTO.get("inOut").toString())) {
                inOut = Integer.valueOf("1");
            }
            createContactForPersonal(addressId, email, employeeId, emailAddress, inOut, user.getTimeZone(), ctx);
        }
        if ("createManualChiefContact".equals(op)) {
            Mail email = (Mail) paramDTO.get("email");
            Integer employeeId = (Integer) paramDTO.get("employeeId");
            Integer contactPersonId = (Integer) paramDTO.get("contactPersonId");
            Integer addressId = (Integer) paramDTO.get("addressId");
            String emailAddress = (String) paramDTO.get("emailAddress");
            Integer userMailId = EJBCommandUtil.i.getValueAsInteger(this, "userMailId");
            User user = WebmailAccountUtil.i.getElwisUser(userMailId);

            Integer inOut = Integer.valueOf("0");
            if (null != paramDTO.get("inOut") && "1".equals(paramDTO.get("inOut").toString())) {
                inOut = Integer.valueOf("1");
            }
            createContactForChiefs(contactPersonId, email, addressId, employeeId, emailAddress, inOut, user.getTimeZone(), ctx);
        }
        if ("deleteWebCommunications".equals(op)) {
            Integer mailId = (Integer) paramDTO.get("mailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            deleteWebCommunications(mailId, companyId, ctx);
        }
        if ("setRelation".equals(op)) {
            Integer contactId = (Integer) paramDTO.get("contactId");
            Integer mailId = (Integer) paramDTO.get("mailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            String email = (String) paramDTO.get("email");
            createRelation(mailId, contactId, companyId, email);

        }
        if ("getMailContact".equals(op)) {
            Integer mailId = (Integer) paramDTO.get("mailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getMailContactRelations(mailId, companyId);
        }
        if ("readWebmailCommunications".equals(op)) {
            String email = (String) paramDTO.get("email");
            Integer emailId = (Integer) paramDTO.get("mailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            readWebmailCommunications(email, emailId, companyId, ctx);
        }
        if ("checkRecipientSent".equals(op)) {
            Integer mailId = (Integer) paramDTO.get("mailId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Boolean saveSendItems = Boolean.valueOf(true);
            if (null == paramDTO.get("saveSendItem")) {
                saveSendItems = Boolean.valueOf(false);
            }
            checkRecipientSent(mailId, companyId, saveSendItems);
        }
        if ("checkUserMail".equals(op)) {
            Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
            Integer userMailId = null;
            UserMailHome home = (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);

            try {
                UserMail userMail = home.findByPrimaryKey(userId);
                userMailId = userMail.getUserMailId();
            } catch (FinderException fex) {
                log.debug("The UserMail of the user: " + userId + " DONT EXISTS");
                resultDTO.setResultAsFailure();
            }
            resultDTO.put("userMailId", userMailId);
        }
        if ("readActualEmailCommunications".equals(op)) {
            Integer mailId = Integer.valueOf(paramDTO.get("mailId").toString());
            Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
            readActualEmailCommunications(mailId, companyId);
        }

        if ("deleteEmailContact".equals(op)) {
            Integer contactId = Integer.valueOf(paramDTO.get("contactId").toString());
            Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
            deleteEmailContact(contactId, companyId);
        }

        if ("getRecipientsList".equals(op)) {
            Integer mailId = EJBCommandUtil.i.getValueAsInteger(this, "mailId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getRecipientsList(mailId, companyId);
        }

    }


    /**
     * This method eliminates <code>MailContact</code> object and its associated mail,
     * as long as this is not being used in other objects <code>MailContact</code>
     * and the state of email is a hidden.
     *
     * @param contactId Contact identifier
     * @param companyId Company identifier
     */
    private void deleteEmailContact(Integer contactId, Integer companyId) {
        MailContactHome mailContactHome =
                (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);

        MailContact emailCommunication = null;

        try {
            emailCommunication = mailContactHome.findByContactId(contactId, companyId);
        } catch (FinderException e) {
            log.debug("-> Cannot search email communication 'MailContact' object for contactId=" + contactId);
        }

        if (null != emailCommunication) {
            Collection otherEmailCommunications = new ArrayList();
            try {
                otherEmailCommunications = mailContactHome.findByMailId(emailCommunication.getMailId(), companyId);
            } catch (FinderException e) {
                log.debug("-> Cannot search communications for " + emailCommunication.getMailId());
            }

            if (otherEmailCommunications.size() == 1) {
                Mail associatedEmail = emailCommunication.getMail();
                Integer mailId = associatedEmail.getMailId();
                //delete the relationship with email
                try {
                    emailCommunication.remove();
                    log.debug("-> Now delete EmailContact OK");
                } catch (RemoveException e) {
                    log.debug("-> Cannot remove Communication Relation", e);
                }
                //delete email only if this hidden
                if (associatedEmail.getHidden()) {
                    try {
                        SaveEmailService saveEmailService = getSaveEmailService();
                        saveEmailService.deleteEmail(associatedEmail);
                        log.debug("-> Now Delete Hidden emailId= " + mailId + "  OK");
                    } catch (RemoveException e) {
                        log.debug("-> Cannot deleted Associated Email", e);
                    }
                }
            } else {
                try {
                    emailCommunication.remove();
                    log.debug("-> Now delete EmailContact OK");
                } catch (RemoveException e) {
                    log.debug("-> Cannot remove Communication Relation", e);
                }
            }
        }
    }

    private SaveEmailService getSaveEmailService() {
        SaveEmailServiceHome saveEmailServiceHome =
                (SaveEmailServiceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_SAVE_EMAIL_SERVICE);
        try {
            return saveEmailServiceHome.create();
        } catch (CreateException e) {
            log.error("Create SaveEmailService FAIL ", e);
            return null;
        }
    }

    private void checkFrom(Integer mailId, Integer companyId, Boolean saveSendItems) {
        List recipientSent = new ArrayList();
        MailHome home = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        Mail mail = null;

        //find email
        try {
            mail = home.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            log.debug("Cannot find email...");
        }
        if (null != mail) {

            //get all relations assitiated for mail
            //mailContacts = [{email=xxxx@xxx.xxx, contactId=YYY, telecomsNumber=zzz}, {...}, ...]
            List mailContacts = getMailContactRelations(mailId, mail.getCompanyId());

            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

            //search recipient in relations with mailcontacts
            Map mailContactMap = contain(mailContacts, mail.getMailFrom(), "email");

            //if recipient exists in relations, the recipient have a contact
            if (null != mailContactMap) {
                mailContactMap.put("dirEmail", mail.getMailFrom());
                mailContactMap.put("isContact", "true");
                mailContactMap.put("addressName", mail.getMailPersonalFrom());
                mailContactMap.put("haveCommunication", Boolean.valueOf(true));
            } else {

                mailContactMap = new HashMap();
                try {
                    Collection telecoms = telecomHome.findTelecomsWithTelecomNumber(mail.getMailFrom(),
                            mail.getCompanyId());
                    if (!telecoms.isEmpty()) {
                        mailContactMap.put("isContact", "true");
                        mailContactMap.put("mailId", mail.getMailId());
                        mailContactMap.put("haveCommunication", Boolean.valueOf(false));
                    } else {
                        mailContactMap.put("isCommunication", null);
                        mailContactMap.put("isContact", "false");
                    }
                } catch (FinderException e) {
                    mailContactMap.put("isCommunication", null);
                    mailContactMap.put("isContact", "false");
                }
                mailContactMap.put("dirEmail", mail.getMailFrom());
                mailContactMap.put("addressName", mail.getMailPersonalFrom());
            }

            recipientSent.add(mailContactMap);

            resultDTO.put("subject", mail.getMailSubject());
            //DateTime dateTime = new DateTime(mail.getSentDate().longValue());
            //DateUtils.createDateTime(mail.getMailDate(), mail.getMailHour(), mail.getTimezone());
            resultDTO.put("dateTime", mail.getSentDate());
        }
        resultDTO.put("mailListRecived", recipientSent);
        resultDTO.put("saveSendItem", saveSendItems);
    }

    private void getRecipientsList(Integer mailId, Integer companyId) {
        List recipientSent = new ArrayList();

        List dbRecipients = getMailRecipients(mailId, companyId);
        for (int i = 0; i < dbRecipients.size(); i++) {
            MailRecipient recipient = (MailRecipient) dbRecipients.get(i);

            Map mailContactMap = new HashMap();
            mailContactMap.put("dirEmail", recipient.getEmail());
            mailContactMap.put("addressName", recipient.getPersonalName());

            List telecoms = findTelecomsWithTelecomNumber(recipient.getEmail(), companyId);
            if (telecoms.isEmpty()) {
                mailContactMap.put("isCommunication", null);
                mailContactMap.put("isContact", "false");
                recipientSent.add(mailContactMap);
                continue;
            }

            mailContactMap.put("isContact", "true");
            mailContactMap.put("mailId", mailId);
            mailContactMap.put("haveCommunication", false);
            recipientSent.add(mailContactMap);
        }

        resultDTO.put("getRecipientsList", recipientSent);
    }

    /**
     * @param mailId
     * @param companyId
     * @param saveSendItems
     */
    private void checkRecipientSent(Integer mailId, Integer companyId, Boolean saveSendItems) {
        log.debug("Executing checkRecipientSent method with \n" +
                "mailId = " + mailId + "\n" +
                "companyId = " + companyId + "\n" +
                "saveSendItems = " + saveSendItems);
        List recipientSent = new ArrayList();
        MailHome home = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        Mail mail = null;
        //find the mail
        try {
            mail = home.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            log.debug("Cannot find email...");
        }

        if (null != mail) {
            //get all recipients assiciated to mail
            Collection recipients = mail.getMailRecipients();

            //get all relations assitiated for mail
            //mailContacts = [{email=xxxx@xxx.xxx, contactId=YYY, telecomsNumber=zzz}, {...}, ...]
            List mailContacts = getMailContactRelations(mailId, mail.getCompanyId());

            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

            for (Iterator iterator = recipients.iterator(); iterator.hasNext();) {
                MailRecipient mailRecipient = (MailRecipient) iterator.next();

                //search recipient in relations with mailcontacts
                Map mailContactMap = contain(mailContacts, mailRecipient.getEmail(), "email");

                //if recipient exists in relations, the recipient have a contact
                if (null != mailContactMap) {
                    mailContactMap.put("dirEmail", mailRecipient.getEmail());
                    mailContactMap.put("isContact", "true");
                    mailContactMap.put("addressName", mailRecipient.getPersonalName());
                    mailContactMap.put("haveCommunication", Boolean.valueOf(true));
                } else {

                    mailContactMap = new HashMap();
                    try {
                        Collection telecoms = telecomHome.findTelecomsWithTelecomNumber(mailRecipient.getEmail(),
                                mailRecipient.getCompanyId());
                        if (!telecoms.isEmpty()) {


                            mailContactMap.put("isContact", "true");
                            mailContactMap.put("mailId", mail.getMailId());
                            mailContactMap.put("haveCommunication", Boolean.valueOf(false));

                            List contactsByMail = getContactsForMail(mailId, companyId);
                            for (Iterator telecomsIterator = telecoms.iterator(); telecomsIterator.hasNext();) {
                                Telecom telecom = (Telecom) telecomsIterator.next();
                                Map contactMap = searchContactPersonIdAndAddressIdInContacs(contactsByMail,
                                        telecom.getContactPersonId(), telecom.getAddressId());
                                if (null != contactMap) {
                                    mailContactMap.put("haveCommunication", Boolean.valueOf(true));
                                    break;
                                }
                            }

                        } else {
                            mailContactMap.put("isCommunication", null);
                            mailContactMap.put("isContact", "false");
                        }
                    } catch (FinderException e) {
                        mailContactMap.put("isCommunication", null);
                        mailContactMap.put("isContact", "false");
                    }
                    mailContactMap.put("dirEmail", mailRecipient.getEmail());
                    mailContactMap.put("addressName", mailRecipient.getPersonalName());
                }

                recipientSent.add(mailContactMap);

            }

            resultDTO.put("subject", mail.getMailSubject());
            //DateTime dateTime = new DateTime(mail.getSentDate().longValue());
            resultDTO.put("dateTime", mail.getSentDate());
        }
        resultDTO.put("mailListDispatched", recipientSent);
        resultDTO.put("saveSendItem", saveSendItems);
    }

    private Map contain(List mapList, Object value, String key) {
        Map result = null;
        for (int i = 0; i < mapList.size(); i++) {
            Map map = (Map) mapList.get(i);

            if (value.equals(map.get(key))) {
                result = map;
            }
        }
        if (null != result) {
            mapList.remove(result);
        }
        return result;
    }

    private void readWebmailCommunications(String email, Integer mailId, Integer companyId, SessionContext ctx) {
        log.debug("readWebmailCommunications('" + email + "', '" + mailId + "', '" + companyId + "', javax.ejb.SessionContext)");

        ReadTelecomCmd cmd = new ReadTelecomCmd();
        cmd.setOp("readAllTelecoms");
        cmd.putParam("email", email);
        cmd.putParam("companyId", companyId);
        cmd.executeInStateless(ctx);

        //read all telecoms
        Collection telecoms = (Collection) cmd.getResultDTO().get("telecoms");

        //read all communications for this email
        List contacs_II = getContactsForMail(mailId, companyId);

        List contacs = getContactsForMail(email, mailId, companyId);

        List mailCommunicationList = new ArrayList();


        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);

        if (!telecoms.isEmpty()) {
            for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                Map telecomMap = (Map) iterator.next();

                //search in all communications if owner of telelecom has have communication created
                Map contactMap = searchContactPersonIdAndAddressIdInContacs(contacs,
                        ((Integer) telecomMap.get("contactPersonId")),
                        ((Integer) telecomMap.get("addressId")));


                Map mailCommunicationMap = new HashMap();

                //if telecom have a contactperson find the chief and the contactperson
                if (null != telecomMap.get("contactPersonId")) {
                    //find the chief of contactperson
                    try {
                        Address chief = addressHome.findByPrimaryKey((Integer) telecomMap.get("addressId"));
                        Map chiefMap = new HashMap();
                        chiefMap.put("name1", chief.getName1());
                        chiefMap.put("name2", chief.getName2());
                        chiefMap.put("name3", chief.getName3());
                        chiefMap.put("addressType", chief.getAddressType());
                        chiefMap.put("contactPersonId", chief.getAddressId());

                        mailCommunicationMap.put("chief", chiefMap);
                    } catch (FinderException e) {
                    }

                    //find the contactperson personal information
                    try {
                        Address address = addressHome.findByPrimaryKey((Integer) telecomMap.get("contactPersonId"));
                        Map addressMap = new HashMap();
                        addressMap.put("name1", address.getName1());
                        addressMap.put("name2", address.getName2());
                        addressMap.put("name3", address.getName3());
                        addressMap.put("addressType", address.getAddressType());
                        addressMap.put("addressId", address.getAddressId());

                        mailCommunicationMap.put("address", addressMap);
                    } catch (FinderException e) {
                    }
                } else
                //find address personal information
                {
                    try {
                        Address address = addressHome.findByPrimaryKey((Integer) telecomMap.get("addressId"));
                        Map addressMap = new HashMap();
                        addressMap.put("name1", address.getName1());
                        addressMap.put("name2", address.getName2());
                        addressMap.put("name3", address.getName3());
                        addressMap.put("addressType", address.getAddressType());
                        addressMap.put("addressId", address.getAddressId());

                        mailCommunicationMap.put("address", addressMap);
                    } catch (FinderException e) {
                    }
                }

                mailCommunicationMap.put("email", telecomMap.get("data"));

                //if the owner of telecom have a communication
                if (null != contactMap) {
                    mailCommunicationMap.put("contactId", contactMap.get("contactId"));
                } else {
                    resultDTO.put("haveAlmostOne", Boolean.valueOf(true));
                }

                mailCommunicationList.add(mailCommunicationMap);
            }
        }

        //if relations with other contacts exist that do not have the electronic mail
        if (!contacs.isEmpty()) {
            for (int i = 0; i < contacs.size(); i++) {
                Map contactMap = (Map) contacs.get(i);

                Map mailCommunicationMap = new HashMap();

                //if contact have a contactperson find the chief and the contactperson
                if (null != contactMap.get("contactPersonId")) {
                    //find the chief of contactperson
                    try {
                        Address chief = addressHome.findByPrimaryKey((Integer) contactMap.get("addressId"));
                        Map chiefMap = new HashMap();
                        chiefMap.put("name1", chief.getName1());
                        chiefMap.put("name2", chief.getName2());
                        chiefMap.put("name3", chief.getName3());
                        chiefMap.put("addressType", chief.getAddressType());
                        chiefMap.put("contactPersonId", chief.getAddressId());

                        mailCommunicationMap.put("chief", chiefMap);
                    } catch (FinderException e) {
                    }

                    //find the contactperson personal information
                    try {
                        Address address = addressHome.findByPrimaryKey((Integer) contactMap.get("contactPersonId"));
                        Map addressMap = new HashMap();
                        addressMap.put("name1", address.getName1());
                        addressMap.put("name2", address.getName2());
                        addressMap.put("name3", address.getName3());
                        addressMap.put("addressType", address.getAddressType());
                        addressMap.put("addressId", address.getAddressId());

                        mailCommunicationMap.put("address", addressMap);
                    } catch (FinderException e) {
                    }
                } else
                //find address personal information
                {
                    try {
                        Address address = addressHome.findByPrimaryKey((Integer) contactMap.get("addressId"));
                        Map addressMap = new HashMap();
                        addressMap.put("name1", address.getName1());
                        addressMap.put("name2", address.getName2());
                        addressMap.put("name3", address.getName3());
                        addressMap.put("addressType", address.getAddressType());
                        addressMap.put("addressId", address.getAddressId());

                        mailCommunicationMap.put("address", addressMap);
                    } catch (FinderException e) {
                    }
                }

                mailCommunicationMap.put("email", contactMap.get("data"));

                //if the contact have a communication
                if (null != contactMap) {
                    mailCommunicationMap.put("contactId", contactMap.get("contactId"));
                } else {
                    resultDTO.put("haveAlmostOne", Boolean.valueOf(true));
                }

                mailCommunicationList.add(mailCommunicationMap);

            }
        }

        resultDTO.put("webmailCommunications", mailCommunicationList);
        resultDTO.put("size", Integer.valueOf(String.valueOf(mailCommunicationList.size())));
        resultDTO.put("mailId", mailId);
        resultDTO.put("email", email);
    }

    /**
     * it looks for  addressId and contactPersonId in the list of contactPersons
     *
     * @param contactLispMap         List of contactPerson
     * @param telecomContactPersonId ContactpersonId
     * @param telecomAddressId       AddressId
     * @return Map where addressId and contactPersonId are equals to telecomContactPersonId and telecomAddressId
     */
    private Map searchContactPersonIdAndAddressIdInContacs(List contactLispMap, Integer telecomContactPersonId,
                                                           Integer telecomAddressId) {
        Map result = null;
        for (int i = 0; i < contactLispMap.size(); i++) {
            Map contactMap = (Map) contactLispMap.get(i);
            Integer addressId = (Integer) contactMap.get("addressId");
            Integer contactPersonId = (Integer) contactMap.get("contactPersonId");

            if (null != telecomContactPersonId) {
                if (telecomContactPersonId.equals(contactPersonId) && telecomAddressId.equals(addressId)) {
                    result = contactMap;
                    contactLispMap.remove(contactMap);
                    break;
                }
            } else {
                if (telecomAddressId.equals(addressId) && null == contactPersonId) {
                    result = contactMap;
                    contactLispMap.remove(contactMap);
                    break;
                }
            }
        }
        return result;
    }


    private List<Map> getContactsForMail(String email, Integer mailId, Integer companyId) {
        log.debug("getContactsForMail('" + email + "', '" + mailId + "', '" + companyId + "')");
        List partialResult = getContactsForMail(mailId, companyId);

        List<Map> result = new ArrayList<Map>();
        for (Object obj : partialResult) {
            Map map = (Map) obj;
            if (email.equals((String) map.get("data"))) {
                result.add(map);
            }
        }

        return result;
    }

    /**
     * it looks for the contacts by  email making use of the relation MailContac
     *
     * @param mailId    id of the mail
     * @param companyId id of the company
     * @return List that contains map objects [{addressId=123, contactPersonId=456, contactId=678}, {addressId=123, ...}, ...]
     */
    private List getContactsForMail(Integer mailId, Integer companyId) {
        MailContactHome mailContactHome = (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);

        //search all mailcontacts by email
        Collection mailContacts = new ArrayList();
        try {
            mailContacts = mailContactHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find mailContact");
        }

        List contacts = new ArrayList();
        if (!mailContacts.isEmpty()) {

            //search all contacts using mailContact relation
            for (Iterator iterator = mailContacts.iterator(); iterator.hasNext();) {
                Map contactMap = new HashMap();
                MailContact mailContact = (MailContact) iterator.next();
                try {
                    Contact contact = contactHome.findByPrimaryKey(mailContact.getContactId());
                    contact.getContactId();
                    contactMap.put("contactId", contact.getContactId());
                    contactMap.put("addressId", contact.getAddressId());
                    contactMap.put("contactPersonId", contact.getContactPersonId());
                    contactMap.put("data", mailContact.getEmail());
                    contacts.add(contactMap);
                } catch (FinderException e) {
                    log.debug("Cannot find contact for mailContact");
                }
            }
        }
        return contacts;
    }

    private void deleteWebCommunications(Integer mailId, Integer companyId, SessionContext ctx) {
        log.debug("Deleting a communication....... mailId: " + mailId + ", companyId:" + companyId);
        ArrayList mailContactCollection = (ArrayList) EJBFactory.i.callFinder(new MailContactDTO(),
                "findByMailId",
                new Object[]{mailId, companyId});
        if (mailContactCollection.size() > 0) {
            ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
            SupportContactHome supportContactHome = (SupportContactHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CONTACT);
            for (int i = 0; i < mailContactCollection.size(); i++) {
                MailContact mailContact = (MailContact) mailContactCollection.get(i);
                try {
                    Contact contact = contactHome.findByPrimaryKey(mailContact.getContactId());
                    SupportContact supportContact = null;
                    try {
                        supportContact = supportContactHome.findByContactId(contact.getContactId(), contact.getCompanyId());
                    }
                    catch (FinderException fex1) {
                        log.debug("The contact: " + contact.getContactId() + " don't have a support contact");
                    }

                    if (supportContact != null && supportContact.getActivityId() == null) {
                        SupportCaseCommunicationCmd supportCaseCommunicationCmd = new SupportCaseCommunicationCmd();
                        supportCaseCommunicationCmd.putParam("op", "delete");
                        supportCaseCommunicationCmd.putParam("caseId", supportContact.getCaseId().toString());
                        supportCaseCommunicationCmd.putParam("contactId", supportContact.getContactId().toString());
                        supportCaseCommunicationCmd.putParam("type", CommunicationTypes.EMAIL);
                        supportCaseCommunicationCmd.executeInStateless(ctx);
                    } else {
                        ContactDeleteCmd contactDeleteCmd = new ContactDeleteCmd();
                        contactDeleteCmd.putParam("contactId", contact.getContactId());
                        contactDeleteCmd.putParam("companyId", contact.getCompanyId());
                        contactDeleteCmd.putParam("type", CommunicationTypes.EMAIL);
                        contactDeleteCmd.executeInStateless(ctx);
                    }
                } catch (FinderException e) {
                    log.debug("Cannot find contact with contactId = " +
                            mailContact.getContactId() + "\n" + e);
                    resultDTO.setResultAsFailure();
                } catch (Exception e) {
                    log.debug("Cannot remove mailContact or contact..." + e.getMessage());
                    e.printStackTrace();
                    resultDTO.setResultAsFailure();
                }
            }
        }
    }

    private void createInCommunications(Mail email, Integer userMailId, List recipientMapList, SessionContext ctx) {

        User user = WebmailAccountUtil.i.getElwisUser(userMailId);

        String userTimeZone = user.getTimeZone();
        Employee employee = getEmployee(user.getAddressId());
        if (null == employee) {
            log.info("-> CommunicationManagerCmd can't relate the incoming email with communication, because the user userId=" +
                    userMailId + " isn't employee");
            return;
        }

        //the identifier of the employee is obtained
        Integer employeeId = employee.getEmployeeId();

        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        Collection telecoms = null;
        try {
            telecoms = telecomHome.findTelecomsWithTelecomNumber(email.getMailFrom(), email.getCompanyId());
        } catch (FinderException e) {

        }
        if (null != telecoms && telecoms.size() == 1) {
            Telecom telecom = (Telecom) telecoms.toArray()[0];

            if (null != telecom.getContactPersonId()) {
                createContactForChiefs(telecom.getAddressId(),
                        email,
                        telecom.getContactPersonId(),
                        employeeId,
                        email.getMailFrom(),
                        Integer.valueOf("1"),
                        userTimeZone,
                        ctx);
            } else {
                createContactForPersonal(telecom.getAddressId(),
                        email,
                        employeeId,
                        email.getMailFrom(),
                        Integer.valueOf("1"),
                        userTimeZone,
                        ctx);
            }

        }
    }

    private void createWebCommunications(Mail email,
                                         Integer userMailId,
                                         List recipientMapList,
                                         Integer inOut,
                                         List addressSelectedUI,
                                         SessionContext ctx) {

        //get all recipients to message was sent
        Collection recipients = email.getMailRecipients();

        AddressHome home = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        User user = WebmailAccountUtil.i.getElwisUser(userMailId);

        String userTimeZone = user.getTimeZone();
        Employee employee = getEmployee(user.getAddressId());
        if (null == employee) {
            log.debug("-> CommunicationManagerCmd can't relate the outgoing email with communication, because the user userId=" +
                    userMailId + " isn't employee");
            return;
        }

        //the identifier of the employee is obtained
        Integer employeeId = employee.getEmployeeId();

        List<Integer> contactIds = new ArrayList<Integer>();

        for (Iterator iterator = recipients.iterator(); iterator.hasNext();) {

            MailRecipient recipient = (MailRecipient) iterator.next();
            if (WebMailConstants.TO_TYPE_BCC.equals(recipient.getType().toString())) {
                continue;
            }

            //all address are requested for who the email is a contactPerson
            ReadTelecomCmd readTelecomCmd = new ReadTelecomCmd();
            readTelecomCmd.putParam("email", recipient.getEmail());
            readTelecomCmd.putParam("companyId", recipient.getCompanyId());
            readTelecomCmd.putParam("op", "readChiefList");
            readTelecomCmd.executeInStateless(ctx);
            List chiefIdsList = (List) readTelecomCmd.getResultDTO().get("myChiefList");
            List personalIdsList = (List) readTelecomCmd.getResultDTO().get("myPersonalList");

            Map map = processRecipientMapList(recipient.getEmail(), recipientMapList);

            //all address are requested for who the email is a contactPerson and are selected
            List chiefIdsSelected = (List) map.get("contactPersonOf");

            //The communication was created for address
            List personalSelectedList = (List) map.get("addressId");

            //the valid contactsPersons are extracted
            Map selectedCommunications = excludeSelectedIdsToallIds(chiefIdsList,
                    chiefIdsSelected,
                    personalIdsList,
                    personalSelectedList, addressSelectedUI);

            List chiefCommunications = (List) selectedCommunications.get("chiefCommunications");
            List personalCommunications = (List) selectedCommunications.get("personalCommunications");

            //the communications for the selected contactPerson were created
            if (!chiefCommunications.isEmpty()) {
                log.debug("Create new chief communication for " + chiefCommunications);

                for (int i = 0; i < chiefCommunications.size(); i++) {
                    Map selected = (Map) chiefCommunications.get(i);

                    Integer id = (Integer) selected.get("addressId");
                    Integer contactPersonId = (Integer) selected.get("contactPersonId");
                    String dirEmail = (String) selected.get("email");
                    Integer contactId = createContactForChiefs(id, email, contactPersonId, employeeId, dirEmail, inOut, userTimeZone, ctx);
                    if (null != contactId) {
                        contactIds.add(contactId);
                    }
                }
            }
            //the communications for the selected personalAddress  were created
            if (!personalCommunications.isEmpty()) {
                log.debug("Create new personal communication for " + personalCommunications);
                for (int i = 0; i < personalCommunications.size(); i++) {
                    try {
                        Map selected = (Map) personalCommunications.get(i);

                        Integer id = (Integer) selected.get("addressId");
                        String dirEmail = (String) selected.get("email");

                        home.findByPrimaryKey(id);
                        Integer contactId = createContactForPersonal(id, email, employeeId, dirEmail, inOut, userTimeZone, ctx);
                        if (null != contactId) {
                            contactIds.add(contactId);
                        }
                    } catch (FinderException e) {
                        log.debug("Address was deleted by othe user and cannot create communication...");
                    }
                }
            }

            //default case if no have chiefCommunications and no have personalCommunications and email have one occurrence

            if (chiefCommunications.isEmpty() &&
                    personalCommunications.isEmpty() &&
                    (personalIdsList.size() + chiefIdsList.size()) == 1) {

                if (personalIdsList.size() == 1) {

                    try {
                        Map selected = (Map) personalIdsList.get(0);
                        Integer id = (Integer) selected.get("addressId");
                        String dirEmail = (String) selected.get("email");

                        home.findByPrimaryKey(id);
                        Integer contactId = createContactForPersonal(id, email, employeeId, dirEmail, inOut, userTimeZone, ctx);
                        if (null != contactId) {
                            contactIds.add(contactId);
                        }
                    } catch (FinderException e) {
                        log.debug("Address was deleted by othe user and cannot create communication...");
                    }
                } else {
                    Map selected = (Map) chiefIdsList.get(0);
                    Integer id = (Integer) selected.get("addressId");
                    Integer contactPersonId = (Integer) selected.get("contactPersonId");
                    String dirEmail = (String) selected.get("email");

                    Integer contactId = createContactForChiefs(id, email, contactPersonId, employeeId, dirEmail, inOut, userTimeZone, ctx);
                    if (null != contactId) {
                        contactIds.add(contactId);
                    }
                }
            }
        }

        //list that contain contact identifiers
        resultDTO.put("communicationIdentifiers", contactIds);
    }

    private void createRelation(Integer mailId, Integer contactId, Integer companyId, String email) {
        MailContactDTO dto = new MailContactDTO();
        dto.put("contactId", contactId);
        dto.put("mailId", mailId);
        dto.put("companyId", companyId);
        dto.put("email", email);
        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }

    /**
     * reads all MailContacts associated a mail
     *
     * @param mailId    primary key of mail
     * @param companyId primary key of company
     * @return List object that contains Maps Objects :[{email=xxxx@xxx.xxx, contactId=YYY, telecomsNumber=zzz}, {...}, ...]
     */
    private List getMailContactRelations(Integer mailId, Integer companyId) {
        MailContactHome home = (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);

        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        List emailCommunications = new ArrayList();

        Collection mailContacts = new ArrayList();

        //find all MailContacts by Mail
        try {
            mailContacts = home.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find mailcontact...");
        }

        //if exists relations
        if (!mailContacts.isEmpty())

        {
            for (Iterator iterator = mailContacts.iterator(); iterator.hasNext();) {
                MailContact mailContact = (MailContact) iterator.next();

                //construct the map {email=xxx@xx.com, ...}
                Map dto = new HashMap();
                dto.put("contactId", mailContact.getContactId());
                dto.put("email", mailContact.getEmail());

                //count ocurrences of email in telecoms
                try {
                    Collection telecoms = telecomHome.findTelecomsWithTelecomNumber(mailContact.getEmail(),
                            mailContact.getCompanyId());
                    dto.put("telecomsNumber", Integer.valueOf(String.valueOf(telecoms.size())));
                } catch (FinderException e) {
                }
                emailCommunications.add(dto);
            }
        }


        resultDTO.put("mailContacts", emailCommunications);
        return emailCommunications;
    }

    private Map processRecipientMapList(String email, List recipienMapList) {
        Map myResult = new HashMap();

        List contactPersonOf = new ArrayList();
        List addressList = new ArrayList();
        Map actual = new HashMap();
        if (null != recipienMapList) {
            for (int i = 0; i < recipienMapList.size(); i++) {
                Map recipient = (Map) recipienMapList.get(i);
                if (recipient.get("email").toString().equals(email)) {
                    if (null != recipient.get("contactPersonOf")) {
                        contactPersonOf = (List) recipient.get("contactPersonOf");
                    }
                    if (null != recipient.get("addressId")) {
                        addressList = (List) recipient.get("addressId");
                    }
                    actual = recipient;
                    break;
                }
            }
            if (recipienMapList.contains(actual)) {
                recipienMapList.remove(actual);
            }
        }

        myResult.put("contactPersonOf", contactPersonOf);
        myResult.put("addressId", addressList);

        return myResult;
    }

    private Map excludeSelectedIdsToallIds(List allIds,
                                           List selectedIds,
                                           List personalList,
                                           List personalSelectedList,
                                           List addressSelectedUI) {
        Map result = new HashMap(2);
        List chiefCommunicationList = new ArrayList();
        for (int i = 0; i < selectedIds.size(); i++) {
            Integer id = (Integer) selectedIds.get(i);
            Map selected = containsContacPerson(allIds, id, (Integer) personalSelectedList.get(0));
            if (null != selected) {
                chiefCommunicationList.add(selected);
            }
        }

        List personalComunicationList = new ArrayList();
        for (int i = 0; i < personalSelectedList.size(); i++) {
            Integer id = (Integer) personalSelectedList.get(i);
            if (addressSelectedUI.contains(id.toString()) || selectedIds.isEmpty()) {
                Map selected = contains(personalList, id, "addressId");
                if (null != selected) {
                    personalComunicationList.add(selected);
                }
            }
        }

        result.put("personalCommunications", personalComunicationList);
        result.put("chiefCommunications", chiefCommunicationList);

        return result;
    }


    private Map containsContacPerson(List mapList, Integer addressId, Integer contactPersonId) {
        Map result = null;
        for (int i = 0; i < mapList.size(); i++) {
            Map map = (Map) mapList.get(i);
            String mapAddressId = map.get("addressId").toString();
            String mapContactPersonId = map.get("contactPersonId").toString();
            if (mapAddressId.equals(addressId.toString()) &&
                    mapContactPersonId.equals(contactPersonId.toString())) {
                result = map;
                break;
            }
        }
        return result;
    }

    private Map contains(List mapList, Integer id, String key) {
        Map result = null;
        for (int i = 0; i < mapList.size(); i++) {
            Map map = (Map) mapList.get(i);
            if (map.get(key).toString().equals(id.toString())) {
                result = map;
                break;
            }
        }
        return result;
    }

    private Integer createContactForChiefs(Integer id,
                                           Mail email,
                                           Integer contactPersonId,
                                           Integer employeeId,
                                           String emailString,
                                           Integer inOut,
                                           String userTimeZone,
                                           SessionContext ctx) {
        Integer contactId = null;
        List contactListMap = getContactsForMail(email.getMailId(), email.getCompanyId());

        Map contactMap = searchContactPersonIdAndAddressIdInContacs(contactListMap, contactPersonId, id);
        if (null == contactMap) {

            ContactDTO dto = new ContactDTO();
            dto.put("addressId", id);
            dto.put("contactPersonId", contactPersonId);
            DateTime dateTime = DateUtils.createDate(email.getSentDate(), userTimeZone);
            dto.put("dateStart", DateUtils.dateToInteger(dateTime));
            dto.put("companyId", email.getCompanyId());
            dto.put("employeeId", employeeId);
            dto.put("isAction", Boolean.valueOf(false));
            dto.put("note", email.getMailSubject());
            dto.put("inOut", inOut);
            dto.put("type", CommunicationTypes.EMAIL);
            log.debug("create contact for : " + dto);
            Contact contact = (Contact) ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            if (null != contact && !resultDTO.isFailure()) {
                MailContactDTO mailContactDTO = new MailContactDTO();
                mailContactDTO.put("companyId", email.getCompanyId());
                mailContactDTO.put("contactId", contact.getContactId());
                mailContactDTO.put("mailId", email.getMailId());
                mailContactDTO.put("email", emailString);

                MailContactHome mailContactHome =
                        (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
                try {
                    mailContactHome.create(mailContactDTO);
                } catch (CreateException e) {
                    log.error("->Cannot Create Mailcontact ", e);
                    ctx.setRollbackOnly();
                }

                contactId = contact.getContactId();
            }
        }

        return contactId;

    }

    private void readActualEmailCommunications(Integer mailId, Integer companyId) {
        MailContactHome home = (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        Collection emailCommunications = new ArrayList();
        try {
            emailCommunications = home.findByMailId(mailId, companyId);
        } catch (FinderException e) {
        }

        List<Map> actualEmailCommunications = new ArrayList<Map>();
        for (Iterator iterator = emailCommunications.iterator(); iterator.hasNext();) {
            Map map = new HashMap();

            MailContact emailCommunication = (MailContact) iterator.next();
            Integer contactId = emailCommunication.getContactId();
            try {
                Contact contact = contactHome.findByPrimaryKey(contactId);

                if (null != contact.getContactPersonId()) {
                    map.put("addressId", contact.getContactPersonId());
                    map.put("contactPersonId", contact.getAddressId());
                    map.put("email", emailCommunication.getEmail());
                } else {
                    map.put("addressId", contact.getAddressId());
                    map.put("contactPersonId", contact.getContactPersonId());
                    map.put("email", emailCommunication.getEmail());
                }
                actualEmailCommunications.add(map);
            } catch (FinderException e) {
            }
        }

        resultDTO.put("actualEmailCommunications", actualEmailCommunications);
    }

    private Integer createContactForPersonal(Integer id,
                                             Mail email,
                                             Integer employeeId,
                                             String emailAddress,
                                             Integer inOut,
                                             String userTimeZone,
                                             SessionContext ctx) {

        Integer contactId = null;
        List contactListMap = getContactsForMail(email.getMailId(), email.getCompanyId());

        Map contactMap = searchContactPersonIdAndAddressIdInContacs(contactListMap, null, id);
        if (null == contactMap) {
            log.debug("Create contact for address ID = " + id);
            ContactDTO dto = new ContactDTO();
            dto.put("addressId", id);
            DateTime dateTime = DateUtils.createDate(email.getSentDate(), userTimeZone);
            dto.put("dateStart", DateUtils.dateToInteger(dateTime));
            dto.put("companyId", email.getCompanyId());
            dto.put("employeeId", employeeId);
            dto.put("isAction", Boolean.valueOf(false));
            dto.put("note", email.getMailSubject());
            dto.put("inOut", inOut);
            dto.put("type", CommunicationTypes.EMAIL);

            Contact contact = (Contact) ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            if (null != contact && !resultDTO.isFailure()) {
                MailContactDTO mailContactDTO = new MailContactDTO();
                mailContactDTO.put("companyId", email.getCompanyId());
                mailContactDTO.put("contactId", contact.getContactId());
                mailContactDTO.put("mailId", email.getMailId());
                mailContactDTO.put("email", emailAddress);

                MailContactHome mailContactHome =
                        (MailContactHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILCONTACT);
                try {
                    mailContactHome.create(mailContactDTO);
                } catch (CreateException e) {
                    log.error("->Cannot Create Mailcontact ", e);
                    ctx.setRollbackOnly();
                }

                contactId = contact.getContactId();
            }
        }

        return contactId;
    }

    private List getMailRecipients(Integer mailId, Integer companyId) {
        MailRecipientHome mailRecipientHome =
                (MailRecipientHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILRECIPIENT);

        try {
            return (List) mailRecipientHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    private List findTelecomsWithTelecomNumber(String email, Integer companyId) {
        TelecomHome telecomHome =
                (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        try {
            return (List) telecomHome.findTelecomsWithTelecomNumber(email, companyId);
        } catch (FinderException e) {
            return new ArrayList();
        }
    }

    private Employee getEmployee(Integer addressId) {
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        try {
            return employeeHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.debug("-> The address addressId=" + addressId + "is not associated to any Employee");
        }
        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
