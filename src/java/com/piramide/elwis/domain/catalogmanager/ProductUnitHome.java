/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductUnitHome.java 12663 2017-03-31 22:30:43Z miguel ${NAME}.java, v 2.0 16-ago-2004 17:10:23 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProductUnitHome extends EJBLocalHome {
    public ProductUnit create(ComponentDTO dto) throws CreateException;

    ProductUnit findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
