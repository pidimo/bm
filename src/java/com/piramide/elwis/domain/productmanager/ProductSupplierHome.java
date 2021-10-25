/**
 * AlfaCentauro Team
 * @author Titus
 * @version ${NAME}.java, v 2.0 Aug 26, 2004 11:04:33 AM  
 */
package com.piramide.elwis.domain.productmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProductSupplierHome extends EJBLocalHome {

    ProductSupplier findByPrimaryKey(ProductSupplierPK key) throws FinderException;

    ProductSupplier create(ComponentDTO dto) throws CreateException;

    Collection findBySupplier(Integer supplierId) throws FinderException;

    Collection findByContactPerson(Integer supplierId, Integer contactPersonId) throws FinderException;
}
