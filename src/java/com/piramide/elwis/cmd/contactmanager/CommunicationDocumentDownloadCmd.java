package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.domain.contactmanager.ContactFreeTextHome;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.dto.contactmanager.ContactDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class CommunicationDocumentDownloadCmd extends EJBCommand {
    @Override
    public void executeInStateless(SessionContext ctx) {
        if ("getDocument".equals(getOp())) {
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            getDocument(contactId);
        }
    }

    @SuppressWarnings(value = "unchecked")
    private void getDocument(Integer contactId) {
        Contact contact = getCommunication(contactId);
        ContactDTO contactDTO = null;
        if (null != contact) {
            contactDTO = new ContactDTO();
            DTOFactory.i.copyToDTO(contact, contactDTO);

            ContactFreeText content = getContent(contact.getFreeTextId());
            if (null != content) {
                byte[] data = content.getValue();
                InputStream inputStream = new ByteArrayInputStream(data);
                contactDTO.put("inputStream", inputStream);
                contactDTO.put("inputStreamSize", data.length);
            }
        }

        resultDTO.put("getDocument", contactDTO);
    }

    private ContactFreeText getContent(Integer freeTextId) {
        if (null != freeTextId) {
            ContactFreeTextHome contactFreeTextHome =
                    (ContactFreeTextHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTFREETEXT);
            try {
                return contactFreeTextHome.findByPrimaryKey(freeTextId);
            } catch (FinderException e) {
                return null;
            }
        }

        return null;
    }


    private Contact getCommunication(Integer contactId) {
        ContactHome contactHome =
                (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        try {
            return contactHome.findByPrimaryKey(contactId);
        } catch (FinderException e) {
            return null;
        }
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
