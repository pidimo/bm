package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AttachBean.java 12561 2016-07-05 21:34:16Z miguel ${NAME}, 02-02-2005 04:27:32 PM alvaro Exp $
 */

public abstract class AttachBean implements EntityBean {
    EntityContext entityContext;

    public AttachBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        this.setAttachId(PKGenerator.i.nextKey(WebMailConstants.TABLE_ATTACH));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) {
    }

    public Integer ejbCreate(Integer companyId, Integer mailId, String attachName, byte[] attachFile) throws CreateException {
        this.setAttachFile(attachFile);
        this.setAttachName(attachName);
        this.setCompanyId(companyId);
        this.setMailId(mailId);
        this.setAttachId(PKGenerator.i.nextKey(WebMailConstants.TABLE_ATTACH));
        this.setSize(attachFile.length);
        return null;
    }

    public void ejbPostCreate(Integer companyId, Integer mailId, String attachName, byte[] attachFile) throws CreateException {
    }

    public Integer ejbCreate(Integer companyId, Integer mailId, String attachName, String emlAttachUUID, Integer size) throws CreateException {
        this.setAttachName(attachName);
        this.setCompanyId(companyId);
        this.setMailId(mailId);
        this.setAttachId(PKGenerator.i.nextKey(WebMailConstants.TABLE_ATTACH));
        this.setEmlAttachUUID(emlAttachUUID);
        this.setSize(size);
        return null;
    }

    public void ejbPostCreate(Integer companyId, Integer mailId, String attachName, String emlAttachUUID, Integer size) throws CreateException {
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

    public abstract Integer getAttachId();

    public abstract void setAttachId(Integer attachId);

    public abstract String getAttachName();

    public abstract void setAttachName(String attachName);

    public abstract byte[] getAttachFile();

    public abstract void setAttachFile(byte[] attachFile);

    public abstract Integer getMailId();

    public abstract void setMailId(Integer attachMailId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer attachCompanyId);

    public abstract Boolean getVisible();

    public abstract void setVisible(Boolean visible);

    public abstract Integer getSize();

    public abstract void setSize(Integer size);

    public abstract String getEmlAttachUUID();

    public abstract void setEmlAttachUUID(String emlAttachUUID);

    public abstract Integer ejbSelectSumSizeByMailId(Integer mailId) throws FinderException;

    public Integer ejbHomeSelectSumSizeByMailId(Integer mailId) throws FinderException {
        return ejbSelectSumSizeByMailId(mailId);
    }

}
