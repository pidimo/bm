package com.piramide.elwis.web.contactmanager.delegate;

import com.piramide.elwis.service.contact.ContactMergeService;
import com.piramide.elwis.service.contact.ContactMergeServiceHome;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.deduplication.ContactMergeWrapper;
import com.piramide.elwis.utils.deduplication.exception.DeleteAddressException;
import com.piramide.elwis.utils.deduplication.exception.MergeAddressException;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class ContactMergeDelegate {
    private Log log = LogFactory.getLog(this.getClass());

    public static ContactMergeDelegate i = new ContactMergeDelegate();

    private ContactMergeDelegate() {
    }

    private ContactMergeService getService() {
        ContactMergeServiceHome home = (ContactMergeServiceHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTMERGESERVICE);
        try {
            return home.create();
        } catch (CreateException e) {
            log.debug("Create ContactMergeService Fail ", e);
        }
        return null;
    }

    public boolean mergeDuplicateContact(ContactMergeWrapper contactMergeWrapper) throws MergeAddressException {
        ContactMergeService service = getService();
        return service.mergeDuplicateContact(contactMergeWrapper);
    }

    public void deleteDuplicateAddressProcess(Integer duplicateGroupId, Integer addressId) throws DeleteAddressException {
        ContactMergeService service = getService();
        service.deleteDuplicateAddressProcess(duplicateGroupId, addressId);
    }

    public void removeDuplicateGroup(Integer duplicateGroupId) throws MergeAddressException {
        ContactMergeService service = getService();
        service.removeDuplicateGroup(duplicateGroupId);
    }

    public void keepDuplicateAddressProcess(Integer duplicateGroupId, Integer addressId) throws MergeAddressException {
        ContactMergeService service = getService();
        service.keepDuplicateAddressProcess(duplicateGroupId, addressId);
    }

    public void keepAllDuplicateAddressProcess(Integer duplicateGroupId) throws MergeAddressException {
        ContactMergeService service = getService();
        service.keepAllDuplicateAddressProcess(duplicateGroupId);
    }
}
