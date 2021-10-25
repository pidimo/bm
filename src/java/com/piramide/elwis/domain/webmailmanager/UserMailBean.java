package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: UserMailBean.java 10480 2014-08-14 18:52:29Z miguel ${NAME}.java ,v 1.1 02-02-2005 04:08:33 PM miky Exp $
 */

public abstract class UserMailBean implements EntityBean {
    EntityContext entityContext;

    public UserMailBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

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

    public abstract Integer getUserMailId();

    public abstract void setUserMailId(Integer userMailId);

    public abstract Integer getUserMailDate();

    public abstract void setUserMailDate(Integer userMailDate);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer conpanyId);

    public abstract Collection getSignatures();

    public abstract void setSignatures(Collection signatures);

    public abstract Collection getFolders();

    public abstract void setFolders(Collection folders);

    public abstract Collection getContactGroups();

    public abstract void setContactGroups(Collection contactGroups);

    public abstract Collection getMailGroupAddrs();

    public abstract void setMailGroupAddrs(Collection mailGroupAddrs);

    public abstract Integer getEditMode();

    public abstract void setEditMode(Integer editMode);

    public abstract Boolean getSaveSendItem();

    public abstract void setSaveSendItem(Boolean saveSendItems);

    public abstract Boolean getEmptyTrashLogout();

    public abstract void setEmptyTrashLogout(Boolean emptyTrashLogout);

    public abstract Boolean getReplyMode();

    public abstract void setReplyMode(Boolean replyMode);

    public abstract Collection getMailAccounts();

    public abstract void setMailAccounts(Collection mailAccount);

    public abstract String getEditorFont();

    public abstract void setEditorFont(String editorFont);

    public abstract String getEditorFontSize();

    public abstract void setEditorFontSize(String editorFontSize);

    public abstract Boolean getShowPopNotification();

    public abstract void setShowPopNotification(Boolean showPopNotifications);

    public abstract Boolean getBackgroundDownload();

    public abstract void setBackgroundDownload(Boolean backgroundDownload);

    public abstract Collection ejbSelectBackgroundUserMailIds() throws FinderException;

    public Collection ejbHomeSelectBackgroundUserMailIds() throws FinderException {
        return ejbSelectBackgroundUserMailIds();
    }
}
