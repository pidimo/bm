package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.SalesProcessUpdateProbabilityCmd;
import com.piramide.elwis.cmd.webmailmanager.ComposeMailCmd;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.domain.contactmanager.ContactFreeTextHome;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.salesmanager.SalesProcess;
import com.piramide.elwis.domain.salesmanager.SalesProcessHome;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;


/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: ContactCreateCmd.java 11332 2015-10-27 23:44:46Z miguel $
 */

public class ContactCreateCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(ContactCreateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if (!paramDTO.containsKey("isAction")) //when is created from communication it belongs no to an action.
        {
            paramDTO.put("isAction", Boolean.FALSE);
        }

        //create email communication
        String communicationType = (String) paramDTO.get("type");
        if (null != communicationType &&
                CommunicationTypes.EMAIL.equals(communicationType)) {
            createEmailCommunication(ctx);
            return;
        }

        //create another types of communications
        ContactDTO contactDTO = new ContactDTO(paramDTO);
        Contact contact = (Contact) ExtendedCRUDDirector.i.create(contactDTO, resultDTO, false);
        if (resultDTO.isFailure()) {
            log.debug("Cant' create Communication:" + paramDTO.get("note"));
            ctx.setRollbackOnly();
            return;
        }


        ContactFreeTextHome freeTextHome =
                (ContactFreeTextHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTFREETEXT);

        try {
            byte[] freeTextContent = null;

            if (paramDTO.containsKey("freeText")) {
                if (CommunicationTypes.DOCUMENT.equals(contact.getType())) {
                    ArrayByteWrapper wrapper = (ArrayByteWrapper) paramDTO.get("freeText");
                    freeTextContent = wrapper.getFileData();
                } else {
                    freeTextContent = paramDTO.getAsString("freeText").getBytes();
                }
            }

            ContactFreeText freeText = freeTextHome.create(freeTextContent, new Integer(paramDTO.get("companyId").toString()),
                    new Integer(FreeTextTypes.FREETEXT_CONTACT));

            contact.setContactFreeText(freeText);

            if (contact.getProcessId() != null) {
                SalesProcessUpdateProbabilityCmd updateProbabilityCmd = new SalesProcessUpdateProbabilityCmd();
                updateProbabilityCmd.putParam("addressId", contact.getAddressId());
                updateProbabilityCmd.putParam("processId", contact.getProcessId());
                updateProbabilityCmd.executeInStateless(ctx);
            }

            log.debug("Create Communication");
            if ("true".equals(paramDTO.get("generate"))) {
                resultDTO.put("contact", contact);
            }

        } catch (CreateException e) {
            log.debug("Cant'n create Freetext" + e);
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("document.error.create", paramDTO.get("note"));
            ctx.setRollbackOnly();
            return;
        }

        //created communication id
        resultDTO.put("contactId", contact.getContactId());
    }

    private void createEmailCommunication(SessionContext context) {
        ComposeMailCmd composeMailCmd = new ComposeMailCmd();
        composeMailCmd.putParam(paramDTO);
        composeMailCmd.executeInStateless(context);

        if ("Fail".equals(composeMailCmd.getResultDTO().getForward())) {
            resultDTO.putAll(paramDTO);
            resultDTO.setForward("FailSendMail");
            resultDTO.addResultMessage("error.Webmail.unavailable.service");
            return;
        }

        List<Integer> communicationIdentifiers =
                (List<Integer>) composeMailCmd.getResultDTO().get("communicationIdentifiers");
        resultDTO.put("communicationIdentifiers", communicationIdentifiers);

        Integer salesProcessId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
        Integer probability = EJBCommandUtil.i.getValueAsInteger(this, "probability");
        if (null != salesProcessId) {
            List<Integer> salesProcessCommunicationIdentifiers =
                    createSalesProcessRelationShip(salesProcessId, probability, communicationIdentifiers, context);
            resultDTO.put("salesProcessCommunicationIdentifiers", salesProcessCommunicationIdentifiers);
        }

    }

    private List<Integer> createSalesProcessRelationShip(Integer salesProcessId,
                                                         Integer probability,
                                                         List<Integer> communicationIdentifiers, SessionContext context) {
        if (null == communicationIdentifiers) {
            return new ArrayList<Integer>();
        }

        SalesProcess salesProcess = getSalesProces(salesProcessId);
        if (null == salesProcess) {
            return new ArrayList<Integer>();
        }

        List<Integer> salesProcessCommunicationIdentifiers = new ArrayList<Integer>();
        boolean hasCreatedSalesProcessContacts = false;
        for (Integer contactId : communicationIdentifiers) {
            Contact contact = getContact(contactId);
            if (null == contact) {
                continue;
            }

            if (!salesProcess.getAddressId().equals(contact.getAddressId())) {
                continue;
            }

            contact.setProcessId(salesProcess.getProcessId());
            contact.setProbability(probability);

            SalesProcessUpdateProbabilityCmd updateProbabilityCmd = new SalesProcessUpdateProbabilityCmd();
            updateProbabilityCmd.putParam("addressId", contact.getAddressId());
            updateProbabilityCmd.putParam("processId", contact.getProcessId());
            updateProbabilityCmd.executeInStateless(context);

            hasCreatedSalesProcessContacts = true;
            salesProcessCommunicationIdentifiers.add(contactId);
            log.debug("-> Create SalesProcess Relation for " + contactId + " OK");
        }
        resultDTO.put("hasCreatedSalesProcessContacts", hasCreatedSalesProcessContacts);

        return salesProcessCommunicationIdentifiers;
    }

    private SalesProcess getSalesProces(Integer salesProcessId) {
        SalesProcessHome salesProcessHome =
                (SalesProcessHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALESPROCESS);
        try {
            return salesProcessHome.findByPrimaryKey(salesProcessId);
        } catch (FinderException e) {
            return null;
        }
    }

    private Contact getContact(Integer contactId) {
        ContactHome contactHome =
                (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            return contactHome.findByPrimaryKey(contactId);
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }


}
