/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CategoryGroupHome extends EJBLocalHome {
    public CategoryGroup create(ComponentDTO dto) throws CreateException;

    CategoryGroup findByPrimaryKey(Integer key) throws FinderException;

    public Collection findGroupsByTab(Integer categoryTabId, Integer companyId) throws FinderException;

    public Collection findGroupsWithoutTabs(String table, Integer companyId) throws FinderException;
}
