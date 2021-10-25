package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.webmailmanager.AddressGroup;
import com.piramide.elwis.domain.webmailmanager.MailGroupAddr;
import com.piramide.elwis.domain.webmailmanager.UserMail;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.webmailmanager.AddressGroupDTO;
import com.piramide.elwis.dto.webmailmanager.MailGroupAddrDTO;
import com.piramide.elwis.dto.webmailmanager.UserMailDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * AlfaCentauro Team
 * <p/>
 * This class implements the operations of read, create, update and delete over the AddressGroupBean
 *
 * @author Alvaro
 * @version $Id: AddressGroupCmd.java 12562 2016-07-20 23:43:31Z miguel $
 */
public class AddressGroupCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext context) {
        String op = null;
        try {
            op = paramDTO.get("op").toString();
            resultDTO.put("op", op);
        } catch (NullPointerException n) {
            op = "read";
        }
        //String userMailId=paramDTO.get("userMailId").toString();
        //String companyId=paramDTO.get("companyId").toString();
        if (op.equals("create")) {
            Object mails = paramDTO.get("selectedMails");
            Object mailGroupAddrId = paramDTO.get("mailGroupAddrId");
            Integer companyId = (Integer) paramDTO.get("companyId");
            Collection c = (Collection) paramDTO.get("selectedMails");
            createAddressGroups(c, mailGroupAddrId, companyId);

            resultDTO.put("mailGroupAddrId", paramDTO.get("mailGroupAddrId"));
            resultDTO.put("name", paramDTO.get("name"));

        } else if (op.equals("delete")) {
            String addressGroupId = paramDTO.get("addressGroupId").toString();
            deleteAddressGroup(addressGroupId);
        } else if (op.equals("update")) {
            String addressGroupId = paramDTO.get("addressGroupId").toString();
            String telecomId = paramDTO.get("addressGroupEmailId").toString();
            updateAddressGroup(addressGroupId, telecomId);
        } else if (op.equals("getAddressGroups")) {
            String mailGroupAddrId = paramDTO.get("mailGroupAddrId").toString();
            getAddressGroups(mailGroupAddrId);
        } else if (op.equals("concurrenceValidation")) {
            resultDTO.put("errors", verifyConcurrence((ArrayList) paramDTO.get("addresses")));
        } else if (op.equals("verifyTelecom")) {
            String telecomId = paramDTO.get("addressGroupEmailId").toString();
            verifyTelecomId(telecomId);
        } else if (op.equals("getListGroups")) {
            getContactGroupList(context);
        } else {
            readAddressGroup(paramDTO.get("addressGroupId").toString());
        }

    }

    public void createAddressGroups(Collection data, Object mailGroupAddrId, Integer companyId) {
        if (mailGroupAddrId != null) {
            AddressGroupDTO addressGroupDTO = new AddressGroupDTO();
            Iterator i = data.iterator();
            while (i.hasNext()) {
                DTO dto = (DTO) i.next();
                addressGroupDTO = new AddressGroupDTO();
                addressGroupDTO.put("mailGroupAddrId", new Integer(mailGroupAddrId.toString()));
                if (dto.get("telecomId").toString().equals("ALL")) {
                    addressGroupDTO.put("sendToAll", new Integer(WebMailConstants.SEND_TO_ALL_TELECOMS));//true
                } else {
                    addressGroupDTO.put("telecomId", new Integer(dto.get("telecomId").toString()));
                    addressGroupDTO.put("sendToAll", new Integer(WebMailConstants.SEND_TO_A_TELECOM));//false
                }
                if (dto.get("contactPersonAddressId") != null && !dto.get("contactPersonAddressId").equals("null")) {
                    addressGroupDTO.put("contactPersonId", new Integer(dto.get("addressId").toString()));
                    addressGroupDTO.put("addressId", new Integer(dto.get("contactPersonAddressId").toString()));
                } else {
                    addressGroupDTO.put("addressId", new Integer(dto.get("addressId").toString()));
                }

                addressGroupDTO.put("companyId", companyId);

                ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, addressGroupDTO, new ResultDTO());
            }
        }
    }

    public void deleteAddressGroup(String addressGroupId) {
        AddressGroupDTO addressGroupDTO = new AddressGroupDTO();
        addressGroupDTO.put("addressGroupId", new Integer(addressGroupId));
        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, addressGroupDTO, new ResultDTO());
    }

    public void updateAddressGroup(String addressGroupId, String telecomId) {
        AddressGroupDTO addressGroupDTO = new AddressGroupDTO();
        addressGroupDTO.put("addressGroupId", new Integer(addressGroupId));
        AddressGroup addressGroup = (AddressGroup) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, addressGroupDTO, new ResultDTO());
        if (telecomId.equals("")) {
            addressGroup.setTelecomId(null);
            addressGroup.setSendToAll(new Integer(WebMailConstants.SEND_TO_ALL_TELECOMS));
        } else {
            addressGroup.setSendToAll(new Integer(WebMailConstants.SEND_TO_A_TELECOM));
            addressGroup.setTelecomId(new Integer(telecomId));
        }
    }

    public void getAddressGroups(String mailGroupAddrId) {
        MailGroupAddrDTO mailGroupAddrDTO = new MailGroupAddrDTO();
        mailGroupAddrDTO.put("mailGroupAddrId", new Integer(mailGroupAddrId));
        MailGroupAddr mailGroupAddr = (MailGroupAddr) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailGroupAddrDTO, new ResultDTO());
        Collection addressGroups = mailGroupAddr.getAddressGroups();
        Collection addressGroupDTOS = new ArrayList();
        Iterator i = addressGroups.iterator();
        while (i.hasNext()) {
            AddressGroupDTO dto = new AddressGroupDTO();
            AddressGroup addressGroup = (AddressGroup) i.next();
            dto.put("addressGroupId", addressGroup.getAddressGroupId());
            dto.put("addressId", addressGroup.getAddressId());
            dto.put("contactPersonId", addressGroup.getContactPersonId());
            dto.put("mailGroupAddrId", addressGroup.getMailGroupAddrId());
            dto.put("telecomId", addressGroup.getTelecomId());
            dto.put("sendToAll", addressGroup.getSendToAll());
            addressGroupDTOS.add(dto);
        }
        resultDTO.put("addressGroups", addressGroupDTOS);
    }

    public ArrayList verifyConcurrence(ArrayList addresses) {
        ArrayList wasDeleted = new ArrayList();
        boolean failure = false;
        Object addressId, contactPersonAddressId, telecomId;
        for (int i = 0; i < addresses.size(); i++) {
            AddressGroupDTO addressGroupDTO_i = (AddressGroupDTO) addresses.get(i);
            contactPersonAddressId = addressGroupDTO_i.get("contactPersonAddressId");
            addressId = addressGroupDTO_i.get("addressId");
            telecomId = addressGroupDTO_i.get("telecomId");
            if (contactPersonAddressId != null && !contactPersonAddressId.toString().equals("null")) {
                ContactPersonPK contactPersonPK = new ContactPersonPK(new Integer(contactPersonAddressId.toString()), new Integer(addressId.toString()));
                ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
                try {
                    ContactPerson contactPerson = contactPersonHome.findByPrimaryKey(contactPersonPK);
                } catch (FinderException fe) {
                    DTO dto = new DTO();
                    dto.put("ERRORTYPE", "CONTACTPERSON");
                    dto.put("ID", addressId);
                    wasDeleted.add(dto);
                    failure = true;
                }
            } else {
                AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
                try {
                    Address address = addressHome.findByPrimaryKey(new Integer(addressId.toString()));
                } catch (FinderException fe) {
                    DTO dto = new DTO();
                    dto.put("ERRORTYPE", "ADDRESS");
                    dto.put("ID", addressId);
                    wasDeleted.add(dto);
                    failure = true;
                }
            }
            if (!failure) {
                TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
                if (!telecomId.toString().equals("ALL")) {
                    try {
                        Telecom telecom = telecomHome.findByPrimaryKey(new Integer(telecomId.toString()));
                    } catch (FinderException fe) {
                        DTO dto = new DTO();
                        dto.put("ERRORTYPE", "TELECOMID");
                        dto.put("ID", addressId);
                        wasDeleted.add(dto);
                    }
                } else {
                    boolean fail = false;
                    Collection telecoms_contactPerson = null;
                    if (contactPersonAddressId != null && !contactPersonAddressId.toString().equals("null")) {
                        try {
                            telecoms_contactPerson = telecomHome.findContactPersonTelecomsByTelecomTypeType(new Integer(contactPersonAddressId.toString()), new Integer(addressId.toString()), ContactConstants.TELECOMTYPE_EMAIL);
                            fail = false;
                        }
                        catch (FinderException fe) {
                            fail = true;
                        }
                        if (fail || telecoms_contactPerson == null || (telecoms_contactPerson != null && telecoms_contactPerson.size() == 0)) {
                            DTO dto = new DTO();
                            dto.put("ERRORTYPE", "TELECOMALL");
                            dto.put("ID", addressId);
                            wasDeleted.add(dto);
                        }

                    } else {
                        Collection telecoms_address = null;
                        try {
                            telecoms_address = telecomHome.findAddressTelecomsByTelecomTypeType(new Integer(addressId.toString()), ContactConstants.TELECOMTYPE_EMAIL);
                            fail = false;
                        }
                        catch (FinderException fe) {
                            fail = true;
                        }
                        if (fail || telecoms_address == null || (telecoms_address != null && telecoms_address.size() == 0)) {
                            DTO dto = new DTO();
                            dto.put("ERRORTYPE", "TELECOMALL");
                            dto.put("ID", addressId);
                            wasDeleted.add(dto);
                        }
                    }
                }
            }
        }
        return (wasDeleted);
    }

    public void verifyTelecomId(String telecomId) {
        if (!telecomId.equals("")) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            try {
                Telecom telecom = telecomHome.findByPrimaryKey(new Integer(telecomId.toString()));
                resultDTO.put("ERRORTELECOM", "FALSE");
            } catch (FinderException fe) {
                resultDTO.put("ERRORTELECOM", "TRUE");
            }
        }
    }

    /**
     * this method obtain the list of groups with your dir e-mails
     * to add in compose and set in DTO (make for miky)
     *
     * @param ctx is the Session context
     */
    public void getContactGroupList(SessionContext ctx) {
        log.debug("Executing method getContactGroupList in AddressGroupCmd....... ");

        List listContactGroup = new ArrayList();
        Integer userMailId = new Integer(paramDTO.get("userMailId").toString());
        UserMailDTO userMailDto = new UserMailDTO();
        userMailDto.put("userMailId", userMailId);

        UserMail userMail = (UserMail) EJBFactory.i.findEJB(userMailDto);
        Collection mailGroupsAddrs = userMail.getMailGroupAddrs();
        for (Iterator iterator = mailGroupsAddrs.iterator(); iterator.hasNext();) {

            MailGroupAddr mailGroupAddr = (MailGroupAddr) iterator.next();
            Collection addressGroups = mailGroupAddr.getAddressGroups();
            String listEmails = "";
            List<Map> contacts = new ArrayList<Map>();
            for (Iterator iterator2 = addressGroups.iterator(); iterator2.hasNext();) {

                AddressGroup addressGroup = (AddressGroup) iterator2.next();
                TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
                TelecomTypeHome telecomTypeHome = (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

                try {
                    if (addressGroup.getContactPersonId() != null) {

                        if (addressGroup.getSendToAll().intValue() == 1) {

                            Collection telecoms = telecomHome.findContactPersonTelecomsByTelecomTypeType(addressGroup.getAddressId(), addressGroup.getContactPersonId(), com.piramide.elwis.utils.TelecomType.EMAIL_TYPE);
                            for (Iterator iterator3 = telecoms.iterator(); iterator3.hasNext();) {
                                Telecom telecom = (Telecom) iterator3.next();
                                TelecomType telecomType = telecomTypeHome.findByPrimaryKey(telecom.getTelecomTypeId());
                                if (telecomType.getType().equals(com.piramide.elwis.utils.TelecomType.EMAIL_TYPE)) {
                                    String addressName = getAddressName(addressGroup.getContactPersonId(), ctx);
                                    Map<String, String> contact = new HashMap<String, String>();
                                    contact.put("text", "\"" + addressName + "\"" + " <" + telecom.getData() + ">");
                                    contact.put("email", telecom.getData());
                                    contact.put("addressName", addressName);
                                    contacts.add(contact);

                                    listEmails = listEmails + "\"" + addressName + "\" <" + telecom.getData() + "><,>";
                                }
                            }
                        } else {
                            Telecom telecom = telecomHome.findByPrimaryKey(addressGroup.getTelecomId());
                            TelecomType telecomType = telecomTypeHome.findByPrimaryKey(telecom.getTelecomTypeId());
                            if (telecomType.getType().equals(com.piramide.elwis.utils.TelecomType.EMAIL_TYPE)) {
                                String addressName = getAddressName(addressGroup.getContactPersonId(), ctx);
                                Map<String, String> contact = new HashMap<String, String>();
                                contact.put("text", "\"" + addressName + "\"" + " <" + telecom.getData() + ">");
                                contact.put("email", telecom.getData());
                                contact.put("addressName", addressName);
                                contacts.add(contact);

                                listEmails = listEmails + "\"" + addressName + "\" <" + telecom.getData() + "><,>";
                            }

                        }
                    } else {

                        if (addressGroup.getSendToAll().intValue() == 1) {

                            Collection telecoms = telecomHome.findAddressTelecomsByTelecomTypeType(addressGroup.getAddressId(), com.piramide.elwis.utils.TelecomType.EMAIL_TYPE);
                            for (Iterator iterator3 = telecoms.iterator(); iterator3.hasNext();) {
                                Telecom telecom = (Telecom) iterator3.next();
                                TelecomType telecomType = telecomTypeHome.findByPrimaryKey(telecom.getTelecomTypeId());
                                if (telecomType.getType().equals(com.piramide.elwis.utils.TelecomType.EMAIL_TYPE)) {
                                    String addressName = getAddressName(addressGroup.getAddressId(), ctx);
                                    Map<String, String> contact = new HashMap<String, String>();
                                    contact.put("text", "\"" + addressName + "\"" + " <" + telecom.getData() + ">");
                                    contact.put("email", telecom.getData());
                                    contact.put("addressName", addressName);
                                    contacts.add(contact);

                                    listEmails = listEmails + "\"" + addressName + "\" <" + telecom.getData() + "><,>";
                                }

                            }
                        } else {
                            Telecom telecom = telecomHome.findByPrimaryKey(addressGroup.getTelecomId());
                            TelecomType telecomType = telecomTypeHome.findByPrimaryKey(telecom.getTelecomTypeId());
                            if (telecomType.getType().equals(com.piramide.elwis.utils.TelecomType.EMAIL_TYPE)) {
                                String addressName = getAddressName(addressGroup.getAddressId(), ctx);
                                Map<String, String> contact = new HashMap<String, String>();
                                contact.put("text", "\"" + addressName + "\"" + " <" + telecom.getData() + ">");
                                contact.put("email", telecom.getData());
                                contact.put("addressName", addressName);
                                contacts.add(contact);

                                listEmails = listEmails + "\"" + addressName + "\" <" + telecom.getData() + "><,>";
                            }

                        }
                    }
                } catch (FinderException fe) {
                    log.error("Error in Finder....." + fe);
                }
            }

            int contMails = 0;
            if (listEmails.length() > 0) {
                listEmails = listEmails.substring(0, listEmails.length() - 3);  // delete the "<,>" at the end of the cad (3 characters)
                contMails = listEmails.split("<,>").length;
            }

            Map groupsMap = new HashMap();
            groupsMap.put("groupId", mailGroupAddr.getMailGroupAddrId());
            groupsMap.put("groupName", mailGroupAddr.getName());
            groupsMap.put("listEmails", listEmails);
            groupsMap.put("numEmails", String.valueOf(contMails));
            groupsMap.put("contacts", contacts);

            listContactGroup.add(groupsMap);
        }
        resultDTO.put("listGroups", listContactGroup);

    }

    /**
     * get address name in a format specify to show list of emails in webmail
     *
     * @param addressId
     * @param ctx       is the Session context
     * @return String, the name
     */
    private String getAddressName(Integer addressId, SessionContext ctx) {

        String name = "";
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);

        addressCmd.executeInStateless(ctx);
        ResultDTO resultDTOAddress = addressCmd.getResultDTO();
        name = resultDTOAddress.get("addressName").toString();

        name = name.replaceAll("\"", "\'");
        return name.trim();
    }

    private void readAddressGroup(String addressGroupId) {
        AddressGroupDTO addressGroupDTO = new AddressGroupDTO();
        addressGroupDTO.put("addressGroupId", new Integer(addressGroupId));
        AddressGroup addressGroup = (AddressGroup) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, addressGroupDTO, resultDTO);

        MailGroupAddrDTO mailGroupAddrDTO = new MailGroupAddrDTO();
        mailGroupAddrDTO.put("mailGroupAddrId", addressGroup.getMailGroupAddrId());
        MailGroupAddr mailGroupAddr = (MailGroupAddr) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, mailGroupAddrDTO, resultDTO);

        Object sendToAll = addressGroup.getSendToAll();
        if (sendToAll != null && sendToAll.toString().equals(WebMailConstants.SEND_TO_A_TELECOM)) {
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            Telecom telecom = null;
            try {
                if (addressGroup.getTelecomId() != null) {
                    telecom = telecomHome.findByPrimaryKey(addressGroup.getTelecomId());
                }
            } catch (FinderException fex) {
                log.debug("Telecom not found.......");
            }
            if (telecom != null) {
                resultDTO.put("addressGroupEmail", telecom.getData());
                resultDTO.put("addressGroupEmailId", telecom.getTelecomId());
            }
        } else {
            resultDTO.put("addressGroupEmailId", "");
        }
        Address address = null;
        AddressDTO addressDTO = new AddressDTO();
        if (addressGroup.getContactPersonId() != null) {
            addressDTO.put("addressId", addressGroup.getContactPersonId());
        } else {
            addressDTO.put("addressId", addressGroup.getAddressId());
        }
        address = (Address) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ, addressDTO, new ResultDTO());
        String addressName = "";
        if (address != null) {
            if (address.getAddressType().toString().equals(ContactConstants.ADDRESSTYPE_PERSON)) {
                addressName = address.getName1();
                String name2 = address.getName2();
                addressName += (name2 != null) ? (", " + name2) : "";
            } else if (address.getAddressType().toString().equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {
                addressName = address.getName1();
                String name2 = address.getName2();
                String name3 = address.getName3();
                addressName += (name2 != null) ? (" " + name2 + (name3 != null ? " " + name3 : "")) : "";
            }
            resultDTO.put("addressGroupName", addressName);
        }
    }
}
