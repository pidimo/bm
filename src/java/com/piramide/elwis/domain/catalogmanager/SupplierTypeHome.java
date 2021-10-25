package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of SupplierType Entity Bean
 *
 * @author Ivan
 * @version $Id: SupplierTypeHome.java 8020 2008-02-22 18:54:26Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface SupplierTypeHome extends EJBLocalHome {
    SupplierType create(ComponentDTO dto) throws CreateException;

    SupplierType findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
