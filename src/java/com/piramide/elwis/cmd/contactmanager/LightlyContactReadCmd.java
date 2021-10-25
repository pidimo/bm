package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class LightlyContactReadCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing LightlyContactReadCmd................" + paramDTO);
        Integer contactId = new Integer(paramDTO.get("contactId").toString());
        Contact contact = (Contact) ExtendedCRUDDirector.i.read(new ContactDTO(contactId), resultDTO, false);
    }

    public boolean isStateful() {
        return false;
    }
}