package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: UserMail.java 10480 2014-08-14 18:52:29Z miguel ${NAME}.java ,v 1.1 02-02-2005 04:08:33 PM miky Exp $
 */

public interface UserMail extends EJBLocalObject {
    Integer getUserMailId();

    void setUserMailId(Integer userMailId);

    Integer getUserMailDate();

    void setUserMailDate(Integer userMailDate);

    Integer getCompanyId();

    void setCompanyId(Integer conpanyId);

    Collection getSignatures();

    void setSignatures(Collection signatures);

    Collection getFolders();

    void setFolders(Collection folders);

    Collection getContactGroups();

    void setContactGroups(Collection contactGroups);

    Collection getMailGroupAddrs();

    void setMailGroupAddrs(Collection mailGroupAddrs);

    Integer getEditMode();

    void setEditMode(Integer editMode);

    Boolean getSaveSendItem();

    void setSaveSendItem(Boolean saveSendItems);

    Boolean getEmptyTrashLogout();

    void setEmptyTrashLogout(Boolean emptyTrashLogout);

    Boolean getReplyMode();

    void setReplyMode(Boolean replyMode);

    Collection getMailAccounts();

    void setMailAccounts(Collection mailAccount);

    String getEditorFont();

    void setEditorFont(String editorFont);

    String getEditorFontSize();

    void setEditorFontSize(String editorFontSize);

    Boolean getShowPopNotification();

    void setShowPopNotification(Boolean showPopNotifications);

    Boolean getBackgroundDownload();

    void setBackgroundDownload(Boolean backgroundDownload);
}
