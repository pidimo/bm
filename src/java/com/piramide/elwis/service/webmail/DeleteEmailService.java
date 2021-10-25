/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.EJBLocalObject;
import java.util.List;

public interface DeleteEmailService extends EJBLocalObject {
    public void deleteSelectedEmails(List identifiers, Integer userMailId);

    public void emptyTrashFolder(Integer userMailId, Integer companyId);

    public void deleteEmailsByfolder(Integer folderId, Integer userMailId);
}
