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

public interface SalePositionHome extends EJBLocalHome {
    public SalePosition create(ComponentDTO dto) throws CreateException;

    SalePosition findByPrimaryKey(Integer key) throws FinderException;

    Collection findBySaleId(Integer saleId, Integer companyId) throws FinderException;

    Integer selectMaxDeliveryDate(Integer saleId) throws FinderException;

    Collection findByCustomer(Integer customerId) throws FinderException;

    Collection findByContactPerson(Integer customerId, Integer contactPersonId) throws FinderException;

    Collection findByProductAndContactPersonId(Integer productId, Integer contactPersonId) throws FinderException;

    Integer selectCountByProductId(Integer productId) throws FinderException;

}
