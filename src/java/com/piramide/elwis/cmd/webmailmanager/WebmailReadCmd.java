package com.piramide.elwis.cmd.webmailmanager;

import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: WebmailReadCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class WebmailReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Execute WebmailReadCmd with  ..." + paramDTO);

        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        EmailUserCmd emailUserCmd = new EmailUserCmd();
        emailUserCmd.setOp("hasWebmailModuleConfiguration");
        emailUserCmd.putParam("userId", userId);
        emailUserCmd.executeInStateless(ctx);
        resultDTO.putAll(emailUserCmd.getResultDTO());
    }

    public boolean isStateful() {
        return false;
    }
}
