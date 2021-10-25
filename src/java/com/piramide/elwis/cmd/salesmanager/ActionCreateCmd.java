package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GenerateDocumentCmd;
import com.piramide.elwis.cmd.contactmanager.ContactCreateCmd;
import com.piramide.elwis.cmd.utils.SchedulerUtil;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.domain.salesmanager.ActionHome;
import com.piramide.elwis.dto.salesmanager.ActionDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTimeZone;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Create sales process action logic
 *
 * @author Fernando Montaño
 * @version $Id: ActionCreateCmd.java 10043 2011-02-03 19:26:47Z ivan $
 */
public class ActionCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionCreateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing Action Create Command...");

        paramDTO.put("isAction", new Boolean(true)); //creating saying is related to an action
        paramDTO.put("inOut", "0"); //All action are outgoing type

        DateTimeZone zone = SchedulerUtil.i.getUserDateTimeZone(new Integer(paramDTO.get("userId").toString()));
        Long createDateTime = EJBCommandUtil.i.getValueAsLong(this, "createDateTime");
        if (null == createDateTime) {
            createDateTime = DateUtils.createDate((new Date()).getTime(), zone.getID()).getMillis();
        }

        paramDTO.put("createDateTime", createDateTime);

        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");

        ContactCreateCmd contactCreateCmd = new ContactCreateCmd();
        contactCreateCmd.putParam(paramDTO);
        contactCreateCmd.executeInStateless(ctx);
        List<Integer> salesProcessCommunicationIdentifiers =
                (List<Integer>) contactCreateCmd.getResultDTO().get("salesProcessCommunicationIdentifiers");

        if (null == salesProcessCommunicationIdentifiers) {
            //String nextNumber = getActionNextNumber(processId, companyId);

            paramDTO.put("contactId", contactCreateCmd.getResultDTO().get("contactId"));
            //paramDTO.put("number", nextNumber);

            ExtendedCRUDDirector.i.create(new ActionDTO(paramDTO), resultDTO, false);

            if ("true".equals(paramDTO.get("generate"))) {
                log.debug("Is GENERATE!!!");
                GenerateDocumentCmd cmd = new GenerateDocumentCmd();
                cmd.putParam("version", contactCreateCmd.getResultDTO().get("version"));
                cmd.putParam(paramDTO);
                cmd.setOp("update");
                log.debug("PARAMDTO-GENERATE:" + cmd.getParamDTO());
                cmd.executeInStateless(ctx);
                ResultDTO contactResultDTO = cmd.getResultDTO();
                resultDTO.putAll(contactResultDTO);
                if (contactResultDTO.hasResultMessage()) { //reading the error messages
                    if (contactResultDTO.isFailure()) {
                        for (Iterator iterator = contactResultDTO.getResultMessages(); iterator.hasNext();) {
                            ResultMessage message = (ResultMessage) iterator.next();
                            resultDTO.addResultMessage(message);
                        }
                        resultDTO.setResultAsFailure();
                    }
                }
            }
            return;
        }

        if ("FailSendMail".equals(contactCreateCmd.getResultDTO().getForward())) {
            resultDTO.putAll(paramDTO);
            resultDTO.setForward("FailSendMail");
            resultDTO.addResultMessage("error.Webmail.unavailable.service");
            return;
        }

        boolean hasCreatedAction = false;
        for (Integer contactId : salesProcessCommunicationIdentifiers) {
            //String nextNumber = getActionNextNumber(processId, companyId);

            Contact contact = getContact(contactId);
            contact.setIsAction(true);

            ActionDTO actionDTO = new ActionDTO();
            actionDTO.putAll(paramDTO);
            actionDTO.put("contactId", contactId);
            //actionDTO.put("number", nextNumber);

            ExtendedCRUDDirector.i.create(actionDTO, resultDTO, false);
            hasCreatedAction = true;
            log.debug("-> Create SalesProcess Action for " + contactId + " OK");
        }
        if (!hasCreatedAction) {
            resultDTO.setForward("Fail");
        }

        resultDTO.put("hasCreatedAction", hasCreatedAction);
    }


    private String getActionNextNumber(Integer processId, Integer companyId) {
        ActionHome actionHome = (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);

        String nextNumber = "1";
        try {
            Integer number = actionHome.selectMaxActionNumber(companyId, processId);
            if (number != null) {
                nextNumber = Integer.toString(number + 1);
            }

        } catch (FinderException e) {
            //nothing to do
        } catch (NumberFormatException e) {
            //nothing to do, it seems the number is not a numeric value.
        }

        return nextNumber;
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
