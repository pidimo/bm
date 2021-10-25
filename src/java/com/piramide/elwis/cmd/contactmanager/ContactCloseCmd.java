package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.HashMap;


/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: ContactCloseCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ContactCloseCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog(ContactCloseCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Close Command");
        try {
            ContactDTO contactDTO = new ContactDTO(new Integer(paramDTO.getAsInt("contactId")));
            Contact contact = (Contact) CRUDDirector.i.update(new HashMap(), contactDTO, resultDTO);
            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
            } else {
                contact.setStatus("0".equals(contact.getStatus()) ? "1" : "0");
            }

        } catch (NumberFormatException e) {
            resultDTO.addResultMessage("Common.invalid.id");
        }
    }

    public boolean isStateful() {
        return false;
    }

}
