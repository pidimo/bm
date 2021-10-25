package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.webmailmanager.MailAccount;
import com.piramide.elwis.domain.webmailmanager.MailAccountHome;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Batch cmd to update mail account with old automatic reply message, this now is a freeText
 *
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public class UpdateMailAccountAutomaticReplyMessageBatchCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("UpdateMailAccountAutomaticReplyMessageBatchCmd execute..........................." + paramDTO);

        MailAccountHome mailAccountHome = (MailAccountHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAILACCOUNT);

        try {
            List emailAccounts = (List) mailAccountHome.findBatchWithReplyMessages();
            log.debug("Mail accounts to process: " + emailAccounts.size());

            for (int i = 0; i < emailAccounts.size(); i++) {
                MailAccount mailAccount = (MailAccount) emailAccounts.get(i);
                createFreeTextReplyMessages(mailAccount);
            }
        } catch (FinderException e) {
            log.debug("Error in execute finder findBatchWithReplyMessages....", e);
        }
    }

    private void createFreeTextReplyMessages(MailAccount mailAccount) {
        if (mailAccount != null && mailAccount.getAutomaticReplyMessage() != null) {

            //create text reply message
            FreeText freeText = createFreeText(mailAccount.getAutomaticReplyMessage(), mailAccount.getCompanyId());
            if (freeText != null) {
                mailAccount.setReplyMessageTextId(freeText.getFreeTextId());
            }

            //create html reply message
            FreeText freeTextHtml = createFreeText(mailAccount.getAutomaticReplyMessage(), mailAccount.getCompanyId());
            if (freeTextHtml != null) {
                mailAccount.setReplyMessageHtmlId(freeTextHtml.getFreeTextId());
            }
        }
    }

    private FreeText createFreeText(String message, Integer companyId) {
        FreeText freeText = null;

        if (message != null) {
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
            try {
                freeText = freeTextHome.create(message.getBytes(), companyId, FreeTextTypes.FREETEXT_MAILACCOUNT);
            } catch (CreateException e) {
                log.debug("Cannot create FreeText....", e);
            }
        }
        return freeText;
    }


    public boolean isStateful() {
        return false;
    }
}
