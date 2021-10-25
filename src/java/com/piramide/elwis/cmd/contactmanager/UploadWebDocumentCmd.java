package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Contact;
import com.piramide.elwis.domain.contactmanager.ContactFreeText;
import com.piramide.elwis.domain.contactmanager.ContactHome;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Upload web document
 *
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class UploadWebDocumentCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing UploadWebDocumentCmd..........." + paramDTO);

        if ("saveContactWebDocument".equals(getOp())) {
            saveCommunicationWebDocument();
        }
    }

    private void saveCommunicationWebDocument() {
        String webGenerateUUID = paramDTO.get("webGenerateUUID").toString();
        ArrayByteWrapper arrayByteWrapper = (ArrayByteWrapper) paramDTO.get("documentWrapper");

        Contact contact = findContact(webGenerateUUID);

        if (contact != null) {
            ContactFreeText freeText = contact.getContactFreeText();
            freeText.setValue(arrayByteWrapper.getFileData());
            freeText.setVersion(freeText.getVersion() + 1);

            if (arrayByteWrapper.getFileName() != null && arrayByteWrapper.getFileName().length() > 0) {
                contact.setDocumentFileName(arrayByteWrapper.getFileName());
            }
        }

        if (contact == null) {
            resultDTO.setResultAsFailure();
            resultDTO.put("failCause", "Contact not foud with generation ID:" + webGenerateUUID);
        }
    }

    private Contact findContact(String webGenerateUUID) {
        ContactHome contactHome = (ContactHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACT);
        if (webGenerateUUID != null) {
            try {
                return contactHome.findByWebGenerateUUID(webGenerateUUID);
            } catch (FinderException e) {
                log.warn("Error upload web document, not found communication related with webGenerateUUID " + webGenerateUUID);
                log.debug("find error..", e);
            }
        }
        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
