package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.domain.contactmanager.EmployeeHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.domain.webmailmanager.Mail;
import com.piramide.elwis.domain.webmailmanager.MailHome;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.WebmailAccountUtil;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author : ivan
 * @version : $Id MailCommunicationCmd ${time}
 */
public class MailCommunicationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        if ("create".equals(this.getOp())) {
            Integer mailId = Integer.valueOf(paramDTO.get("mailId").toString());
            Integer size = Integer.valueOf(paramDTO.get("size").toString());
            Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
            Integer inOut = null;
            if (null != paramDTO.get("inOut")) {
                try {
                    inOut = Integer.valueOf(paramDTO.get("inOut").toString());
                } catch (NumberFormatException e) {
                }
            }

            create(mailId, size, companyId, inOut, ctx);

            String email = (String) paramDTO.get("email");
            //read(email, mailId, companyId, ctx);

        } else {
            String email = (String) paramDTO.get("email");
            Integer mailId = Integer.valueOf(paramDTO.get("mailId").toString());
            Integer companyId = (Integer) paramDTO.get("companyId");
            read(email, mailId, companyId, ctx);
        }
    }

    /**
     * read the communications that have been made from webmail module
     *
     * @param email     email with which the communications of registered
     * @param mailId    identifier of the mail that has associate the communication
     * @param companyId company identifier in which I am made the communication
     * @param ctx
     */
    private void read(String email, Integer mailId, Integer companyId, SessionContext ctx) {

        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("readWebmailCommunications");
        cmd.putParam("email", email);
        cmd.putParam("mailId", mailId);
        cmd.putParam("companyId", companyId);
        cmd.executeInStateless(ctx);
        resultDTO.put("webmailCommunications", cmd.getResultDTO().get("webmailCommunications"));
        resultDTO.put("size", cmd.getResultDTO().get("size"));
        resultDTO.put("mailId", cmd.getResultDTO().get("mailId"));
        resultDTO.put("email", cmd.getResultDTO().get("email"));
        resultDTO.put("inOut", paramDTO.get("inOut"));
        resultDTO.put("haveErrors", Boolean.valueOf(false));
        resultDTO.put("haveAlmostOne", cmd.getResultDTO().get("haveAlmostOne"));
        resultDTO.put("read", paramDTO.get("read"));
        log.debug("resultDTO = " + resultDTO);
    }

    private void create(Integer mailId, Integer size, Integer companyId, Integer inOut, SessionContext ctx) {

        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        String emailAddress = (String) paramDTO.get("email");

        //find all telecoms by email Address
        Collection telecoms = null;
        try {
            telecoms = telecomHome.findTelecomsWithTelecomNumber(emailAddress, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find telecoms...");
        }

        //find employeeId
        Integer userMailId = Integer.valueOf(paramDTO.get("userMailId").toString());
        User user = WebmailAccountUtil.i.getElwisUser(userMailId);
        Employee employee = getEmployee(user.getAddressId());
        if (null == employee) {
            log.info("MailCommunicationCmd can't create manual relation with communication, because the mail user userMailId =" +
                    userMailId + " isn't employee");
            return;
        }

        Integer employeeId = employee.getEmployeeId();
        // find the mail
        MailHome home = (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        Mail mail = null;
        try {
            mail = home.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            log.debug("Cannot find mail with mailId ..: " + mailId);
        }

        boolean hasCreateRelation = false;
        if (null != mail) {
            List<Map> structure = (List<Map>) paramDTO.get("otherContacts");

            createCommunicationForAddedContacts(structure, inOut, employeeId, mail, userMailId, ctx);
            hasCreateRelation = true;
        }

        //if have email, telecoms and exists telecoms
        if (null != mail && null != telecoms && !telecoms.isEmpty()) {
            hasCreateRelation = true;

            for (int i = 1; i <= size.intValue(); i++) {
                //we verified that check box was selected
                String checkBox = (String) paramDTO.get("checkBox_" + i);

                if (null != checkBox) {
                    Map map = parse(checkBox);
                    if (!map.isEmpty() && !Boolean.valueOf((String) map.get("hidden"))) {

                        Integer addressId = (Integer) map.get("addressId");
                        Integer contactPersonId = (Integer) map.get("contactPersonId");
                        Integer contactId = (Integer) map.get("contactId");

                        //if no have contactId and exists one telecom with addressId and contactpersonId
                        if (null == contactId && searchInTelecoms(addressId, contactPersonId, telecoms)) {

                            CommunicationManagerCmd cmd = new CommunicationManagerCmd();
                            if (null == contactPersonId) {
                                cmd.setOp("createManualAddressContact");
                                cmd.putParam("email", mail);
                                cmd.putParam("employeeId", employeeId);
                                cmd.putParam("addressId", addressId);
                                cmd.putParam("emailAddress", emailAddress);
                                cmd.putParam("userMailId", userMailId);
                                cmd.putParam("inOut", inOut);
                                cmd.executeInStateless(ctx);

                            } else {
                                cmd.setOp("createManualChiefContact");
                                cmd.putParam("email", mail);
                                cmd.putParam("employeeId", employeeId);
                                cmd.putParam("addressId", addressId);
                                cmd.putParam("emailAddress", emailAddress);
                                cmd.putParam("contactPersonId", contactPersonId);
                                cmd.putParam("userMailId", userMailId);
                                cmd.putParam("inOut", inOut);
                                cmd.executeInStateless(ctx);
                            }
                            resultDTO.put("test", Boolean.valueOf(true));
                        }
                        //if no exists one telecom with addressId and contactpersonId
                        if (!searchInTelecoms(addressId, contactPersonId, telecoms)) {
                            resultDTO.addResultMessage("Webmail.error.emailNotFound", paramDTO.get("name_" + i), emailAddress);
                            resultDTO.put("haveErrors", Boolean.valueOf(true));
                        }
                    }
                }
            }
        }

        if (!hasCreateRelation) {
            //no have telecoms for email
            if (null != paramDTO.get("name_1") && !"".equals(paramDTO.get("name_1").toString().trim())) {
                resultDTO.addResultMessage("Webmail.error.emailNotFound", paramDTO.get("name_1"), emailAddress);
                resultDTO.put("haveErrors", Boolean.valueOf(true));
            }
        }
    }

    private void createCommunicationForAddedContacts(List<Map> structure, Integer inOut, Integer employeeId, Mail email, Integer userMailId, SessionContext ctx) {
        log.debug("createCommunicationForAddedContacts('" + structure + "', '" + inOut + "', '" + employeeId + "', 'com.piramide...Mail', javax.ejb.SessionContext)");
        String emailAddress = (String) paramDTO.get("email");

        for (Map map : structure) {
            Integer addressId = (Integer) map.get("addressId");
            Integer contactPersonId = (Integer) map.get("contactPersonId");
            Integer telecomTypeId = (Integer) map.get("telecomTypeId");

            if (null != telecomTypeId) {
                createTelecom(addressId, contactPersonId, telecomTypeId, emailAddress);
            }

            CommunicationManagerCmd cmd = new CommunicationManagerCmd();
            if (null == contactPersonId) {
                cmd.setOp("createManualAddressContact");
                cmd.putParam("email", email);
                cmd.putParam("employeeId", employeeId);
                cmd.putParam("addressId", addressId);
                cmd.putParam("emailAddress", emailAddress);
                cmd.putParam("userMailId", userMailId);
                cmd.putParam("inOut", inOut);
                cmd.executeInStateless(ctx);
            } else {
                cmd.setOp("createManualChiefContact");
                cmd.putParam("email", email);
                cmd.putParam("employeeId", employeeId);
                cmd.putParam("addressId", addressId);
                cmd.putParam("emailAddress", emailAddress);
                cmd.putParam("contactPersonId", contactPersonId);
                cmd.putParam("userMailId", userMailId);
                cmd.putParam("inOut", inOut);
                cmd.executeInStateless(ctx);
            }
        }
    }

    public void createTelecom(Integer addressId, Integer contactPersonId, Integer telecomTypeId, String data) {
        log.debug("createTelecom('" + addressId + "', '" + contactPersonId + "', '" + telecomTypeId + "', '" + data + "')");
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        TelecomHome home = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);

        boolean isContactperson = (null != contactPersonId);
        boolean isAddress = (null == contactPersonId);
        boolean isDefault = true;


        if (isContactperson) {
            try {
                Telecom telecom = home.findContactPersonDefaultTelecomsByTypeId(contactPersonId, addressId, telecomTypeId);
                if (null != telecom) {
                    isDefault = false;
                }
            } catch (FinderException e) {
            }
            try {
                home.create(contactPersonId, addressId, data, null, isDefault, telecomTypeId, companyId);
            } catch (CreateException ce) {
                log.error("Cannot create telecom for : addressId = "
                        + addressId + " and contactPersonId  = "
                        + contactPersonId + "...", ce);
            }
        }
        if (isAddress) {
            try {
                Telecom telecom = home.findAddressDefaultTelecomsByTypeId(addressId, telecomTypeId);
                if (null != telecom) {
                    isDefault = false;
                }
            } catch (FinderException e) {
            }
            try {
                home.create(addressId, contactPersonId, data, null, isDefault, telecomTypeId, companyId);
            } catch (CreateException ce) {
                log.error("Cannot create telecom for : addressId = "
                        + addressId + "...", ce);
            }
        }
    }


    /**
     * Parse String with format cad = "addressId=xxxx_contactPersonId=yyyy_contactId=zzzz"
     *
     * @param cad string to be parsed
     * @return Map with format {addressId=xxxx, contactId=yyyy, contactPersonId=zzzz}
     */
    private Map parse(String cad) {
        log.debug("Executing parse method with \n" +
                "cad = " + cad);
        StringTokenizer tokenizer = new StringTokenizer(cad, "_");
        List elements = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            elements.add(tokenizer.nextElement());
        }

        log.debug("Elements  ... " + elements);

        Map map = new HashMap();
        for (int i = 0; i < elements.size(); i++) {
            String element = (String) elements.get(i);
            StringTokenizer newTokenizer = new StringTokenizer(element, "=");

            String key = newTokenizer.nextElement().toString();
            Object obj = null;
            try {
                obj = newTokenizer.nextElement();
            } catch (NoSuchElementException e) {

            }
            Integer value = null;

            boolean isInteger = true;
            if (null != obj && !"".equals(obj.toString())) {
                try {
                    value = Integer.valueOf(obj.toString());
                } catch (NumberFormatException nfe) {
                    isInteger = false;
                }
            }
            if (isInteger) {
                map.put(key, value);
            } else {
                map.put(key, obj);
            }
        }
        log.debug("Map...: " + map);
        return map;
    }

    private boolean searchInTelecoms(Integer addressId, Integer contactPersonId, Collection telecoms) {
        log.debug("Executing searchInTelecoms method with \n" +
                "addressId       = " + addressId + "\n" +
                "contactPersonId = " + contactPersonId + "\n" +
                "telecoms        = " + telecoms);
        boolean result = false;
        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
            Telecom telecom = (Telecom) iterator.next();
            if (null != contactPersonId) {
                if (addressId.equals(telecom.getContactPersonId()) && contactPersonId.equals(telecom.getAddressId())) {
                    result = true;
                }
            } else {
                if (addressId.equals(telecom.getAddressId())) {
                    result = true;
                }
            }
        }
        return result;
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
