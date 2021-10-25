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

public interface CategoryTabHome extends EJBLocalHome {
    public CategoryTab create(ComponentDTO dto) throws CreateException;

    public CategoryTab findByPrimaryKey(Integer key) throws FinderException;

    public Collection findTabsByTable(String table, Integer companyId) throws FinderException;
}
