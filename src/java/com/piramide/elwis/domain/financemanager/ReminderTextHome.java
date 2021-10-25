/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ReminderTextHome extends EJBLocalHome {
    ReminderText create(ComponentDTO dto) throws CreateException;

    ReminderText findByPrimaryKey(ReminderTextPK key) throws FinderException;

    ReminderText findDefaultReminderText(Integer reminderLevelId, Integer companyId) throws FinderException;

    Collection findReminderTextByReminderLevelId(Integer reminderLevelId, Integer companyId) throws FinderException;
    
    ReminderText findByDocumentFreeTextId(Integer freeTextId) throws FinderException;

}
