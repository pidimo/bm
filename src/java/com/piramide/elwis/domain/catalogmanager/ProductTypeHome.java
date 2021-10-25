/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductTypeHome.java 11758 2015-12-09 00:16:33Z miguel ${NAME}.java, v 2.0 16-ago-2004 16:51:27 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProductTypeHome extends EJBLocalHome {
    public ProductType create(ComponentDTO dto) throws CreateException;

    ProductType findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByProductTypeType(Integer ProductTypeType, Integer companyId) throws FinderException;
}
