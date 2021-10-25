/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface UidlTrackHome extends EJBLocalHome {
    UidlTrack create(ComponentDTO dto) throws CreateException;

    UidlTrack findByPrimaryKey(Integer key) throws FinderException;

    Collection selectGetUidls(Integer mailAccountId, Integer companyId) throws FinderException;

    UidlTrack findByUidl(String uidl, Integer mailAccountId, Integer companyId) throws FinderException;


    Collection findByMailAccountId(Integer mailAccountId, Integer companyId) throws FinderException;

    Collection findByMailAccountIdDeleteFromPOPServerAt(Integer mailAccountId, Long currentMillis) throws FinderException;
}
