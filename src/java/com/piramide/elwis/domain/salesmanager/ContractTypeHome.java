/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ContractTypeHome extends EJBLocalHome {
    ContractType create(ComponentDTO dto) throws CreateException;

    ContractType findByPrimaryKey(Integer key) throws FinderException;

    Collection findByName(String name, Integer companyId) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
