package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.dto.webmailmanager.MailDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author : ivan
 * @version : $Id MailRecipientCmd ${time}
 */
public class MailRecipientCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO : " + paramDTO);
        Integer mailId = Integer.valueOf(paramDTO.get("mailId").toString());
        Integer companId = Integer.valueOf(paramDTO.get("companyId").toString());

        MailDTO dto = new MailDTO();
        dto.put("mailId", mailId);
        ExtendedCRUDDirector.i.read(dto, resultDTO, false);

        checkRecipientSent(mailId, companId, ctx);
        checkFromIn(mailId, companId, ctx);
    }

    private void checkRecipientSent(Integer mailId, Integer companyId, SessionContext ctx) {
        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("checkRecipientSent");
        cmd.putParam("mailId", mailId);
        cmd.putParam("companyId", companyId);
        cmd.putParam("saveSendItems", Boolean.valueOf(true));
        cmd.executeInStateless(ctx);
        resultDTO.put("mailListDispatched", cmd.getResultDTO().get("mailListDispatched"));
        resultDTO.put("saveSendItem", cmd.getResultDTO().get("saveSendItem"));
        resultDTO.put("mailId", mailId);
        resultDTO.put("subject", cmd.getResultDTO().get("subject"));
        resultDTO.put("dateTime", cmd.getResultDTO().get("dateTime"));
    }

    private void checkFromIn(Integer mailId, Integer companyId, SessionContext ctx) {
        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("checkFrom");
        cmd.putParam("mailId", mailId);
        cmd.putParam("companyId", companyId);
        cmd.putParam("saveSendItems", Boolean.valueOf(true));
        cmd.executeInStateless(ctx);
        resultDTO.put("mailListRecived", cmd.getResultDTO().get("mailListRecived"));
        resultDTO.put("saveSendItem", cmd.getResultDTO().get("saveSendItem"));
        resultDTO.put("mailId", mailId);
        resultDTO.put("subject", cmd.getResultDTO().get("subject"));
        resultDTO.put("dateTime", cmd.getResultDTO().get("dateTime"));
    }

    public boolean isStateful() {
        return false;
    }
}
