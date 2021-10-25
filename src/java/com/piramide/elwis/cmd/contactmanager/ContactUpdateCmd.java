package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.LightlySalesProcessCmd;
import com.piramide.elwis.cmd.salesmanager.SalesProcessUpdateProbabilityCmd;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.CommunicationTypes;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;


/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: ContactUpdateCmd.java 11368 2015-10-30 20:26:36Z miguel $
 */
public class ContactUpdateCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(ContactUpdateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        Integer contactId = Integer.valueOf(paramDTO.get("contactId").toString());

        ContactDTO contactDTO = new ContactDTO(paramDTO);
        Contact contact = (Contact) ExtendedCRUDDirector.i.update(contactDTO, resultDTO, false, true, false, "Fail");

        if ("Fail".equals(resultDTO.getForward())) { // object Not found
            log.debug("-> Read Contact contactId=" + contactId + " FAIL, now Redirect to " + resultDTO.getForward());
            return;
        }

        if (resultDTO.isFailure()) { // version control error
            ContactReadCmd read = new ContactReadCmd();
            read.putParam("contactId", contactId);
            read.executeInStateless(ctx);
            resultDTO.putAll(read.getResultDTO());
            return;
        }


        String type = (String) paramDTO.get("type");

        if (contact != null) {
            if (CommunicationTypes.DOCUMENT.equals(type)) {
                ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("freeText");
                if (null != wrapper) {
                    ContactFreeText contactFreeText = contact.getContactFreeText();
                    contactFreeText.setValue(wrapper.getFileData());
                }
            }
            if (CommunicationTypes.MEETING.equals(type) ||
                    CommunicationTypes.PHONE.equals(type) ||
                    CommunicationTypes.OTHER.equals(type)) {
                ContactFreeText contactFreeText = contact.getContactFreeText();
                String value = (String) paramDTO.get("freeText");
                contactFreeText.setValue(value.getBytes());
            }
            if (contact.getContactPersonId() != null && resultDTO.isFailure()) {
                Address address = contact.getContactPerson().getContactPerson();
                StringBuffer sb = new StringBuffer(address.getName1());
                if (address.getName2() != null) {
                    sb.append(", ").append(address.getName2());
                }
                resultDTO.put("contactPersonName", new String(sb));
            }
            if (contact.getProcessId() != null && resultDTO.isFailure()) {
                LightlySalesProcessCmd processCmd = new LightlySalesProcessCmd();
                processCmd.putParam("processId", contact.getProcessId());
                processCmd.executeInStateless(ctx);
                resultDTO.put("processName", processCmd.getResultDTO().get("processName"));
            }

            if (contact.getProcessId() != null) {
                SalesProcessUpdateProbabilityCmd updateProbabilityCmd = new SalesProcessUpdateProbabilityCmd();
                updateProbabilityCmd.putParam("addressId", contact.getAddressId());
                updateProbabilityCmd.putParam("processId", contact.getProcessId());
                updateProbabilityCmd.executeInStateless(ctx);
            }

            if ("true".equals(paramDTO.get("rebuild"))) {
                log.debug("IS REBUILD ... !!!!");
                contact.getContactFreeText().setValue(null);
            }
            if ("true".equals(paramDTO.get("generate"))) {
                log.debug("is generate...");
                resultDTO.put("contact", contact);
            }

            if ("true".equals(paramDTO.get("isWebDocumentGenerate"))) {
                contact.getContactFreeText().setValue(null);
            }

            //updated communication id
            resultDTO.put("contactId", contact.getContactId());

        } else {
            log.debug(" ... contact not found - contactUpdateCmd ...");
            if ("true".equals(paramDTO.get("isSupport"))) {
                resultDTO.setResultAsFailure();
                resultDTO.put("objNotFound", true);
            } else {
                resultDTO.setResultAsFailure();
                resultDTO.setForward("Fail");
            }
            return;
        }
    }

    public boolean isStateful() {
        return false;
    }

}
