package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.ContactCreateCmd;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityContactDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * class to create communications in document massive generate from campaign activity
 *
 * @author Miky
 * @version $Id: DocumentCommunicationMassiveCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class DocumentCommunicationMassiveCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing DocumentCommunicationMassiveCmd................" + paramDTO);

        if ("create".equals(getOp())) {
            create(ctx);
        }
    }

    private void create(SessionContext ctx) {
        int campaignId = new Integer(paramDTO.get("campaignId").toString());
        int companyId = new Integer(paramDTO.get("companyId").toString());

        ContactCreateCmd cmd = new ContactCreateCmd();
        cmd.putParam("freeText", paramDTO.get("freeText"));
        cmd.putParam("addressId", paramDTO.get("addressId"));
        cmd.putParam("dateStart", paramDTO.get("dateStart"));
        cmd.putParam("companyId", paramDTO.get("companyId"));
        cmd.putParam("contactPersonId", paramDTO.get("contactPersonId"));
        cmd.putParam("employeeId", paramDTO.get("employeeId"));
        cmd.putParam("note", paramDTO.get("note")); //subject
        cmd.putParam("status", paramDTO.get("status"));
        cmd.putParam("type", paramDTO.get("type"));
        cmd.putParam("inOut", paramDTO.get("inOut"));
        cmd.putParam("isAction", new Boolean(false));

        //cmd.putParam("dateFinish", paramDTO.get("dateFinish"));
        //cmd.putParam("freeTextId", paramDTO.get("freeTextId"));
        //cmd.putParam("processId", paramDTO.get("processId"));
        //cmd.putParam("templateId", paramDTO.get("templateId"));
        //cmd.putParam("contactNumber", paramDTO.get("contactNumber"));
        //cmd.putParam("probability", paramDTO.get("probability"));
        cmd.executeInStateless(ctx);

        ResultDTO contactResultDTO = cmd.getResultDTO();
        resultDTO.putAll(contactResultDTO);
        if (contactResultDTO.hasResultMessage()) { //reading the error messages
            if (contactResultDTO.isFailure() || contactResultDTO.getForward().toString().equals("FailSendMail")) {
                for (Iterator iterator = contactResultDTO.getResultMessages(); iterator.hasNext();) {
                    ResultMessage message = (ResultMessage) iterator.next();
                    resultDTO.addResultMessage(message);
                }
                resultDTO.setResultAsFailure();
                return;
            }
        }

        int contactId = (Integer) cmd.getResultDTO().get("contactId");

        CampaignActivityContactDTO dto = new CampaignActivityContactDTO();
        dto.put("campaignId", campaignId);
        dto.put("contactId", contactId);
        dto.put("companyId", companyId);
        dto.put("activityId", paramDTO.get("activityId"));
        dto.put("isMassive", new Boolean(true));

        ExtendedCRUDDirector.i.create(dto, resultDTO, false);
    }
}
