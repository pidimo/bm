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

public interface SaleHome extends EJBLocalHome {
    public Sale create(ComponentDTO dto) throws CreateException;

    Sale findByPrimaryKey(Integer key) throws FinderException;

    Collection findByProsessId(Integer processId, Integer companyId) throws FinderException;

    Collection findByContactId(Integer contactId, Integer processId, Integer companyId) throws FinderException;

    Collection findBySentAddress(Integer sentAddressId) throws FinderException;

    Collection findBySentContactPerson(Integer sentAddressId, Integer sentContactPersonId) throws FinderException;

    Collection findByCustomer(Integer customerId) throws FinderException;

    Collection findByContactPerson(Integer customerId, Integer contactPersonId) throws FinderException;

    Collection findBySellerId(Integer sellerId) throws FinderException;

    Collection findByAdditionalAddressId(Integer additionalAddressId) throws FinderException;
}
