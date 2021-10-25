/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

public interface UidlTrack extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUidlTrackId();

    void setUidlTrackId(Integer uidlTrackId);

    String getUidl();

    void setUidl(String uidl);

    Integer getMailAccountId();

    void setMailAccountId(Integer mailAccountId);

    Long getDeleteFromPopAtTime();

    void setDeleteFromPopAtTime(Long deleteFromPopAtTime);
}
