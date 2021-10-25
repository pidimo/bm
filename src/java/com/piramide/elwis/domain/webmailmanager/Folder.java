/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: Folder.java 9384 2009-06-19 01:50:50Z alvaro ${NAME}.java, v 2.0 02-feb-2005 15:17:35 Ivan Exp $
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

public interface Folder extends EJBLocalObject {
    Integer getFolderId();

    void setFolderId(Integer myField);

    String getFolderName();

    void setFolderName(String folderName);

    Integer getFolderDate();

    void setFolderDate(Integer folderDate);

    Integer getUserMailId();

    void setUserMailId(Integer userMailId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Collection getFilters();

    void setFilters(Collection filters);

    Collection getMails();

    void setMails(Collection mails);

    Integer getFolderType();

    void setFolderType(Integer folderType);

    Integer getColumnToShow();

    void setColumnToShow(Integer columnToShow);

    Boolean getIsOpen();

    void setIsOpen(Boolean isOpen);

    Folder getParentFolder();

    void setParentFolder(Folder parentFolder);

    Integer getParentFolderId();

    void setParentFolderId(Integer parentFolderId);

}
