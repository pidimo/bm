package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Get communication document info, set freetextid in resultDTO if fretext value is not null
 * @author Miky
 * @version $Id: ContactDocumentInfoCmd.java 2009-09-18 06:36:58 PM $
 */
public class ContactDocumentInfoCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ContactDocumentInfoCmd..........." + paramDTO);
        Integer contactId = Integer.valueOf(paramDTO.get("contactId").toString());

        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        boolean contactHasDocumentValue = false;
        try {
            Contact contact = contactHome.findByPrimaryKey(contactId);
            ContactFreeText contactFreeText = contact.getContactFreeText();
            if (contactFreeText != null && contactFreeText.getValue() != null) {
                resultDTO.put("contactFreeTextId", contactFreeText.getFreeTextId());
                contactHasDocumentValue = true;
            }

        } catch (FinderException e) {
            log.debug("Not found communication:" + contactId);
        }

        resultDTO.put("contactHasDocument", contactHasDocumentValue);
    }

    public boolean isStateful() {
        return false;
    }

}
