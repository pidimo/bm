/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface AccountHome extends EJBLocalHome {
    Account create(ComponentDTO dto) throws CreateException;

    Account findByPrimaryKey(Integer key) throws FinderException;
}
