package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.campaignmanager.CampaignActivityContact;
import com.piramide.elwis.domain.campaignmanager.CampaignGenText;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 * Read generation related template text
 *
 * @author Miky
 * @version $Id: GenerationCommunicationTextReadCmd.java 9761 2009-09-29 19:43:01Z miguel $
 */
public class GenerationCommunicationTextReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing GenerationCommunicationTextReadCmd................" + paramDTO);

        Integer contactId = new Integer(paramDTO.get("contactId").toString());

        Contact contact = (Contact) ExtendedCRUDDirector.i.read(new ContactDTO(paramDTO), resultDTO, false);

        if (contact != null && !resultDTO.isFailure()) {
            //verify if is communication created from campaign generation
            CommunicationFromCampaignGenerationCmd campGenCommunicationCmd = new CommunicationFromCampaignGenerationCmd();
            campGenCommunicationCmd.putParam("contactId", contact.getContactId());
            campGenCommunicationCmd.executeInStateless(ctx);

            if (campGenCommunicationCmd.isCampaignGenerationCommunication()) {
                CampaignActivityContact activityContact = campGenCommunicationCmd.getCampaignActivityContact();
                Integer languageId = getDefaultLanguageId(contact.getAddressId(), contact.getContactPersonId());

                CampaignGenText campaignGenText = null;
                if (languageId != null) {
                    campaignGenText = getCampaignGenText(new ArrayList(activityContact.getCampaignGeneration().getCampaignGenTexts()), languageId);
                }
                if (campaignGenText == null) {
                    campaignGenText = getDefaultCampaignGenText(new ArrayList(activityContact.getCampaignGeneration().getCampaignGenTexts()));
                }

                resultDTO.put("fid", campaignGenText.getFreeTextId());
                resultDTO.put("freeText", new ArrayByteWrapper(campaignGenText.getGenerationText().getValue()));
                resultDTO.put("communicationSubject", contact.getNote());

            } else {
                resultDTO.setForward("Fail");
            }
        } else {
            resultDTO.setForward("Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }

    private Integer getDefaultLanguageId(Integer addressId, Integer contactPersonId) {
        Integer defaultLangId = null;

        Address contactPerson = null;
        Address address = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(addressId), new ResultDTO(), false);
        if (contactPersonId != null) {
            contactPerson = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(contactPersonId), new ResultDTO(), false);
        }

        if (contactPerson != null && contactPerson.getLanguageId() != null) {
            defaultLangId = contactPerson.getLanguageId();
        } else if (address != null && address.getLanguageId() != null) {
            defaultLangId = address.getLanguageId();
        }

        return defaultLangId;
    }

    private CampaignGenText getCampaignGenText(List<CampaignGenText> campaignGenTextList, Integer languageId) {
        for (CampaignGenText campaignGenText : campaignGenTextList) {
            if (campaignGenText.getLanguageId().equals(languageId)) {
                return campaignGenText;
            }
        }
        return null;
    }

    private CampaignGenText getDefaultCampaignGenText(List<CampaignGenText> campaignGenTextList) {
        for (CampaignGenText campaignGenText : campaignGenTextList) {
            if (campaignGenText.getIsDefault()) {
                return campaignGenText;
            }
        }
        return campaignGenTextList.get(0);
    }

}
