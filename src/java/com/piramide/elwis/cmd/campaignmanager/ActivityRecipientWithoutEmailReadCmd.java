package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.domain.campaignmanager.CampaignContact;
import com.piramide.elwis.domain.campaignmanager.CampaignContactHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Telecom;
import com.piramide.elwis.domain.contactmanager.TelecomHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * read all campaign activity recipients without email
 *
 * @author Miky
 * @version $Id: ActivityRecipientWithoutEmailReadCmd.java 10205 2012-05-02 20:01:09Z miguel $
 */
public class ActivityRecipientWithoutEmailReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing ActivityRecipientWithoutEmailReadCmd..... " + paramDTO);

        Integer telecomTypeId = new Integer(paramDTO.get("telecomTypeId").toString());
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

        if ("campaignRecipientsWithoutEmail".equals(getOp())) {
            Integer campaignId = new Integer(paramDTO.get("campaignId").toString());
            List<CampaignContact> recipients = null;
            try {
                recipients = new ArrayList<CampaignContact>(campaignContactHome.findByCampaignIdAndActivityIdNULL(campaignId));
            } catch (FinderException e) {
                log.debug("contact finder exception", e);
                recipients = new ArrayList<CampaignContact>();
            }
            processCampaignContacts(recipients, telecomTypeId);

        } else if ("isRecipientWithEmail".equals(getOp())) {
            isRecipientWithEmail(telecomTypeId);

        } else if ("isRecipientWithoutEmail".equals(getOp())) {
            isRecipientWithoutEmail(telecomTypeId);

        } else {

            Integer activityId = new Integer(paramDTO.get("activityId").toString());
            List<CampaignContact> recipients = null;
            try {
                recipients = new ArrayList<CampaignContact>(campaignContactHome.findEnabledCampaignContactsByActivity(activityId));
            } catch (FinderException e) {
                log.debug("contact finder exception", e);
                recipients = new ArrayList<CampaignContact>();
            }
            processCampaignContacts(recipients, telecomTypeId);
        }

    }

    private void processCampaignContacts(List<CampaignContact> recipients, Integer telecomTypeId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);

        List recipientWithoutEmailList = new ArrayList();
        for (CampaignContact campaignContact : recipients) {

            try {
                Address address = addressHome.findByPrimaryKey(campaignContact.getAddressId());
                Address contactPerson = null;
                boolean isPerson = ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType());

                if (campaignContact.getContactPersonId() != null) {
                    contactPerson = addressHome.findByPrimaryKey(campaignContact.getContactPersonId());
                }

                boolean hasEmail;
                //only if address is organization, process contact person
                if (isPerson) {
                    hasEmail = recipientHasEmail(address, null, telecomTypeId);
                } else {
                    hasEmail = recipientHasEmail(address, contactPerson, telecomTypeId);
                }

                if (!hasEmail) {
                    Map recipientNamesMap = new HashMap();

                    recipientNamesMap.put("contactName", address.getName());
                    if (contactPerson != null) {
                        recipientNamesMap.put("contactPersonName", contactPerson.getName());
                        if (isPerson) {
                            recipientNamesMap.put("addressWithoutMail", CampaignConstants.ADDRESS_WITHOUT_MAIL);
                        } else {
                            recipientNamesMap.put("addressWithoutMail", CampaignConstants.CONTACTPERSON_WITHOUT_MAIL);
                        }
                    }
                    recipientWithoutEmailList.add(recipientNamesMap);
                }
            } catch (FinderException e) {
                log.debug("fail in find address...", e);
                continue;
            }
        }

        resultDTO.put("withoutEmailList", recipientWithoutEmailList);
    }

    private void isRecipientWithEmail(Integer telecomTypeId) {
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Object contactPersonIdObj = paramDTO.get("contactPersonId");
        Integer contactPersonId = (contactPersonIdObj != null && !"".equals(contactPersonIdObj.toString())) ? new Integer(contactPersonIdObj.toString()) : null;
        resultDTO.put("isValidRecipient", recipientHasEmail(addressId, contactPersonId, telecomTypeId));
    }

    private void isRecipientWithoutEmail(Integer telecomTypeId) {
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Object contactPersonIdObj = paramDTO.get("contactPersonId");
        Integer contactPersonId = (contactPersonIdObj != null && !"".equals(contactPersonIdObj.toString())) ? new Integer(contactPersonIdObj.toString()) : null;
        resultDTO.put("isValidRecipient", !recipientHasEmail(addressId, contactPersonId, telecomTypeId));
    }

    private boolean recipientHasEmail(Integer addressId, Integer contactPersonId, Integer telecomTypeId) {
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        boolean hasEmail;

        try {
            Address address = addressHome.findByPrimaryKey(addressId);
            Address contactPerson = null;
            boolean isPerson = ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType());

            if (contactPersonId != null) {
                contactPerson = addressHome.findByPrimaryKey(contactPersonId);
            }

            //only if address is organization, process contact person
            if (isPerson) {
                hasEmail = recipientHasEmail(address, null, telecomTypeId);
            } else {
                hasEmail = recipientHasEmail(address, contactPerson, telecomTypeId);
            }

        } catch (FinderException e) {
            log.debug("fail in find address...", e);
            return false;
        }

        return hasEmail;
    }

    /**
     * verify if this recipient has email
     *
     * @param organization  person or organization
     * @param contactPerson contact person only to organization, if address is person this should be null
     * @param telecomTypeId telecom type id
     * @return true or false
     */
    private boolean recipientHasEmail(Address organization, Address contactPerson, Integer telecomTypeId) {
        TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
        Telecom telecom = null;
        if (contactPerson != null) {
            try {
                telecom = telecomHome.findContactPersonDefaultTelecomsByTypeId(organization.getAddressId(), contactPerson.getAddressId(), telecomTypeId);
            } catch (FinderException e) {
                try {
                    Collection telecoms = telecomHome.findAllContactPersonTelecomsByTypeId(organization.getAddressId(), contactPerson.getAddressId(), telecomTypeId);
                    if (!telecoms.isEmpty()) {
                        telecom = (Telecom) telecoms.iterator().next();
                    }
                } catch (FinderException e2) {
                    log.debug("can not find contact person telecom", e2);
                }
            }
        } else {
            try {
                telecom = telecomHome.findAddressDefaultTelecomsByTypeId(organization.getAddressId(), telecomTypeId);
            } catch (FinderException e) {
                try {
                    Collection telecoms = telecomHome.findAllAddressTelecomsByTypeId(organization.getAddressId(), telecomTypeId);
                    if (telecoms != null && !telecoms.isEmpty()) {
                        telecom = (Telecom) telecoms.iterator().next();
                    }
                } catch (FinderException e2) {
                    log.debug("can not find address telecom..." + e2);
                }
            }
        }

        if (telecom != null) {
            if (telecom.getData() != null) {
                return true;
            }
        }
        return false;
    }


    public boolean isStateful() {
        return false;
    }
}
