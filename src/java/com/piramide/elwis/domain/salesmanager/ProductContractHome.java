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

public interface ProductContractHome extends EJBLocalHome {
    ProductContract create(ComponentDTO dto) throws CreateException;

    ProductContract findByPrimaryKey(Integer key) throws FinderException;

    Collection findBySalePositionId(Integer salePositionId, Integer companyId) throws FinderException;

    Collection findAll() throws FinderException;

    Collection findAllBetweenReminderTime(Long initialTime, Long endTime) throws FinderException;

    Collection findSingleContracts(Integer salePositionId, Integer companyId) throws FinderException;

    Collection findByAddress(Integer addressId) throws FinderException;

    Collection findBySentAddress(Integer sentAddressId) throws FinderException;

    Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;

    Collection findBySentContactPerson(Integer sentAddressId, Integer sentContactPersonId) throws FinderException;

    Collection findBySellerId(Integer sellerId) throws FinderException;

    Collection findByAdditionalAddressId(Integer additionalAddressId) throws FinderException;
}
