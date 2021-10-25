package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignContactDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.utils.CampaignConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 * cmd to create many campaing contacts to an activity from recipients of an campaign
 *
 * @author Miky
 * @version $Id: ActivityCampaignContactAddCmd.java 10068 2011-07-01 15:57:26Z fernando $
 */
public class ActivityCampaignContactAddCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(this.getClass());


    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ActivityCampaignContactAddCmd................" + paramDTO);

        Integer campaignId = new Integer(paramDTO.getAsInt("campaignId"));
        Integer activityId = new Integer((String) paramDTO.get("activityId"));

        Campaign campaign = (Campaign) ExtendedCRUDDirector.i.read(new CampaignDTO(campaignId), new ResultDTO(), false);
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(new CampaignActivityDTO(activityId), resultDTO, false);

        //this because be create from popup
        if (campaign != null && activity != null) {
            if ("createRecipients".equals(getOp())) {
                createCampaignContactFromRecipients(campaignId, activityId);
            } else if ("createActivities".equals(getOp())) {
                createCampaignContactFromActivity(campaignId, activityId);
            } else if ("createContact".equals(getOp())) {
                createCampaignContactFromContact(campaignId, activityId);
            } else if ("createAllRecipients".equals(getOp())) {
                createAllContactFromRecipients(campaignId, activityId);
            }
            resultDTO.put("successful", "true");
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void createCampaignContactFromRecipients(Integer campaignId, Integer activityId) {
        List values = null;
        if (paramDTO.get("selected") != null) {
            values = (List) paramDTO.get("selected");
            CampaignActivityDTO activityDTO = new CampaignActivityDTO(activityId);
            //ResultDTO actResultDTO = new ResultDTO();
            CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(activityDTO, resultDTO, false);
            if (activity != null) {
                for (Iterator iterator = values.iterator(); iterator.hasNext(); ) {
                    Integer campContactId = new Integer((String) iterator.next());
                    CampaignContactPK pk = new CampaignContactPK(campContactId, campaignId);
                    CampaignContactDTO campContactDTO = new CampaignContactDTO();
                    campContactDTO.setPrimKey(pk);
                    ResultDTO contResultDTO = new ResultDTO();

                    CampaignContact campContact = (CampaignContact) ExtendedCRUDDirector.i.read(campContactDTO, contResultDTO, false);
                    if (campContact != null) {
                        if (!existThisCampaignContact(activity.getActivityId(), campContact)) {
                            CampaignContactDTO dto = new CampaignContactDTO();
                            dto.put("activityId", activityId);
                            dto.put("active", new Boolean(true));
                            dto.put("addressId", campContact.getAddressId());
                            dto.put("campaignId", campContact.getCampaignId());
                            dto.put("companyId", campContact.getCompanyId());
                            dto.put("contactPersonId", campContact.getContactPersonId());

                            //create activity
                            ResultDTO campContactResultDTO = new ResultDTO();
                            ExtendedCRUDDirector.i.create(dto, campContactResultDTO, false);
                        } else {
                            //message already exist this camp contact
                            resultDTO.put("someExist", "true");
                        }
                    } else {
                        //message camp contact has be removed by other user
                        resultDTO.put("someDeleted", "true");
                    }
                }

                //update numberContact field in Activity
                activity.setNumberContact(activity.getCampaignContacts().size());
            }
        }
    }

    private void createCampaignContactFromActivity(Integer campaignId, Integer activityId) {
        Integer copyActivityId = new Integer((String) paramDTO.get("copyActivityId"));


        CampaignActivityDTO activityDTO = new CampaignActivityDTO(activityId);
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(activityDTO, resultDTO, false);
        if (activity != null) {

            CampaignActivityDTO copyActivityDTO = new CampaignActivityDTO(copyActivityId);
            ResultDTO actResultDTO = new ResultDTO();
            CampaignActivity copyActivity = (CampaignActivity) ExtendedCRUDDirector.i.read(copyActivityDTO, actResultDTO, false);

            if (copyActivity != null) {
                Collection copyCampContacts = copyActivity.getCampaignContacts();
                for (Iterator iterator = copyCampContacts.iterator(); iterator.hasNext(); ) {
                    CampaignContact campContact = (CampaignContact) iterator.next();

                    if (!existThisCampaignContact(activity.getActivityId(), campContact)) {
                        CampaignContactDTO dto = new CampaignContactDTO();
                        dto.put("activityId", activityId);
                        dto.put("active", new Boolean(true));
                        dto.put("addressId", campContact.getAddressId());
                        dto.put("campaignId", campContact.getCampaignId());
                        dto.put("companyId", campContact.getCompanyId());
                        dto.put("contactPersonId", campContact.getContactPersonId());

                        //create activity
                        ResultDTO campContactResultDTO = new ResultDTO();
                        ExtendedCRUDDirector.i.create(dto, campContactResultDTO, false);
                    } else {
                        //message already exist this camp contact
                        resultDTO.put("someExist", "true");
                    }
                }
            } else {
                //message copy activity has been deleted
                resultDTO.put("copyActivityDeleted", "true");
            }

            //update numberContact field in Activity
            activity.setNumberContact(activity.getCampaignContacts().size());
        }
    }

    private void createCampaignContactFromContact(Integer campaignId, Integer activityId) {

        List values = null;
        if (paramDTO.get("selected") != null) {
            values = (List) paramDTO.get("selected");
            CampaignActivityDTO activityDTO = new CampaignActivityDTO(activityId);
            CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(activityDTO, resultDTO, false);
            if (activity != null) {


                for (Iterator iterator = values.iterator(); iterator.hasNext(); ) {

                    String imp = (String) iterator.next();
                    StringTokenizer tokenizer = new StringTokenizer(imp, ",");

                    Integer addressId = new Integer(tokenizer.nextToken());
                    Integer contactPersonId = null;

                    if (tokenizer.hasMoreElements()) {
                        contactPersonId = new Integer(tokenizer.nextToken());
                    }

                    //verif if address has been deleted
                    AddressDTO addressDTO = new AddressDTO(addressId);
                    ResultDTO contResultDTO = new ResultDTO();
                    Address address = (Address) ExtendedCRUDDirector.i.read(addressDTO, contResultDTO, false);

                    if (address != null) {
                        if (!existsCampaignContact(activity.getActivityId(), addressId, contactPersonId)) {
                            CampaignContactDTO dto = new CampaignContactDTO();
                            dto.put("activityId", activityId);
                            dto.put("active", new Boolean(true));
                            dto.put("addressId", addressId);
                            dto.put("campaignId", campaignId);
                            dto.put("companyId", activity.getCompanyId());
                            dto.put("contactPersonId", contactPersonId);

                            //create activity
                            ResultDTO campContactResultDTO = new ResultDTO();
                            ExtendedCRUDDirector.i.create(dto, campContactResultDTO, false);
                        } else {
                            //message already exist this camp contact
                            resultDTO.put("someExist", "true");
                        }
                    } else {
                        //message contact has be removed by other user
                        resultDTO.put("someDeleted", "true");
                    }
                }

                //update numberContact field in Activity
                activity.setNumberContact(activity.getCampaignContacts().size());
            }
        }
    }

    private Collection<CampaignContact> getAllCampaignRecipients(Integer campaignId) {
        CampaignContactHome campaignContactHome =
                (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        try {
            //noinspection unchecked
            return campaignContactHome.findByCampaignIdAndActivityIdNULL(campaignId);
        } catch (FinderException e) {
            log.debug("-> Execute campaignContactHome.findByCampaignIdAndActivityIdNULL(" + campaignId + ") doesn't find anything ", e);
            return new ArrayList<CampaignContact>();
        }
    }

    private void createAllContactFromRecipients(Integer campaignId, Integer activityId) {
        CampaignActivityDTO activityDTO = new CampaignActivityDTO(activityId);
        CampaignActivity activity = (CampaignActivity) ExtendedCRUDDirector.i.read(activityDTO, resultDTO, false);

        if (null == activity) {
            return;
        }

        CampaignContactHome campaignContactHome =
                (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

        Collection<CampaignContact> allCampaignRecipients = getAllCampaignRecipients(campaignId);

        if (allCampaignRecipients.isEmpty()) {
            resultDTO.put("emptyRecipient", "true");
            return;
        }

        CampaignContactDTO dto;
        for (CampaignContact campContact : allCampaignRecipients) {
            if (existThisCampaignContact(activity.getActivityId(), campContact)) {
                resultDTO.put("someExist", "true");
                continue;
            }

            dto = new CampaignContactDTO();
            dto.put("activityId", activityId);
            dto.put("active", true);
            dto.put("addressId", campContact.getAddressId());
            dto.put("campaignId", campContact.getCampaignId());
            dto.put("companyId", campContact.getCompanyId());
            dto.put("contactPersonId", campContact.getContactPersonId());

            //create activity
            try {
                campaignContactHome.create(dto);
            } catch (CreateException e) {
                log.error("-> Execute CampaignContactHome.create(" + dto + ") FAIL", e);
            }
        }
        //update numberContact field in Activity
        activity.setNumberContact(activity.getCampaignContacts().size());
    }

    private boolean existThisCampaignContact(Integer activityId, CampaignContact campaignContact) {
        return existsCampaignContact(activityId, campaignContact.getAddressId(), campaignContact.getContactPersonId());
    }

    private boolean existsCampaignContact(Integer activityId, Integer addressId, Integer contactPersonId) {
        CampaignContactHome campaignContactHome =
                (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);

        if (null == contactPersonId) {
            try {
                campaignContactHome.findByActivityIdAddressIdContactPersonNULL(
                        activityId,
                        addressId
                );
                log.debug("-> CampaignContact activityId=" + activityId +
                        " addressId=" + addressId + " already exists");
                return true;
            } catch (FinderException e) {
                return false;
            }
        } else {
            try {
                campaignContactHome.findByActivityIdAddressIdContactPersonId(
                        activityId,
                        addressId,
                        contactPersonId
                );
                log.debug("-> CampaignContact activityId=" + activityId +
                        " addressId=" + addressId + " " +
                        " contactPersonId=" + contactPersonId + " already exists");
                return true;
            } catch (FinderException e) {
                return false;
            }
        }
    }
}
