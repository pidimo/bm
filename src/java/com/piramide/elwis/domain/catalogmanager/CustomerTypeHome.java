package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of CustomerType Entity Bean
 *
 * @author Ivan
 * @version $Id: CustomerTypeHome.java 10209 2012-05-07 19:07:49Z miguel ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CustomerTypeHome extends EJBLocalHome {
    public CustomerType create(ComponentDTO dto) throws CreateException;

    CustomerType findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findByCustomerTypeName(String name, Integer companyId) throws FinderException;
}
