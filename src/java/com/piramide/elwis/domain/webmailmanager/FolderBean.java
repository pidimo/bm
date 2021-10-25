/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: FolderBean.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 02-feb-2005 15:17:35 Ivan Exp $
 */
package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

public abstract class FolderBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setFolderId(PKGenerator.i.nextKey(WebMailConstants.TABLE_FOLDER));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public FolderBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getFolderId();

    public abstract void setFolderId(Integer myField);

    public abstract String getFolderName();

    public abstract void setFolderName(String folderName);

    public abstract Integer getFolderDate();

    public abstract void setFolderDate(Integer folderDate);

    public abstract Integer getUserMailId();

    public abstract void setUserMailId(Integer userMailId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Collection getFilters();

    public abstract void setFilters(Collection filters);

    public abstract Collection getMails();

    public abstract void setMails(Collection mails);

    public abstract Integer getFolderType();

    public abstract void setFolderType(Integer folderType);

    public abstract Integer getColumnToShow();

    public abstract void setColumnToShow(Integer columnToShow);

    public abstract Boolean getIsOpen();

    public abstract void setIsOpen(Boolean isOpen);

    public abstract Folder getParentFolder();

    public abstract void setParentFolder(Folder parentFolder);

    public abstract Integer getParentFolderId();

    public abstract void setParentFolderId(Integer parentFolderId);

    public abstract Collection ejbSelectGetFolderIdentifiers(Integer userMailId, Integer companyId) throws FinderException;

    public Collection ejbHomeSelectGetFolderIdentifiers(Integer userMailId, Integer companyId) throws FinderException {
        return ejbSelectGetFolderIdentifiers(userMailId, companyId);
    }
}
