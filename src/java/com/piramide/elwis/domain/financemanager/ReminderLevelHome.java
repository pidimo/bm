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

public interface ReminderLevelHome extends EJBLocalHome {
    ReminderLevel create(ComponentDTO dto) throws CreateException;

    ReminderLevel findByPrimaryKey(Integer key) throws FinderException;

    ReminderLevel findNext(Integer nextLevel, Integer companyId) throws FinderException;

    Collection findByCompanyId(Integer companyId) throws FinderException;

    ReminderLevel findByLevel(Integer level, Integer companyId) throws FinderException;

    public Integer selectMaxLevel(Integer companyId) throws FinderException;
}
