package com.piramide.elwis.domain.common;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: AttachmentBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 05-jun-2008 17:10:03 Miky Exp $
 */

public abstract class AttachmentBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setAttachmentId(PKGenerator.i.nextKey(Constants.TABLE_ATTACHMENT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public AttachmentBean() {
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

    public abstract Integer getAttachmentId();

    public abstract void setAttachmentId(Integer attachmentId);

    public abstract Integer getAttachmentType();

    public abstract void setAttachmentType(Integer attachmentType);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getContentType();

    public abstract void setContentType(String contentType);

    public abstract String getFileName();

    public abstract void setFileName(String fileName);

    public abstract Integer getFileSize();

    public abstract void setFileSize(Integer fileSize);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract FileFreeText getFileFreeText();

    public abstract void setFileFreeText(FileFreeText fileFreeText);

    public void setFileFreeText(EJBLocalObject fileFreeText) {
        setFileFreeText((FileFreeText) fileFreeText);
    }
}
