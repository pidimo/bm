package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public interface AdditionalAddressHome extends EJBLocalHome {
    AdditionalAddress create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.AdditionalAddress findByPrimaryKey(Integer key) throws FinderException;

    Collection findByDefault(Integer addressId, Integer companyId) throws FinderException;

    Collection findByAdditionalAddressType(Integer addressId, Integer additionalAddressType) throws FinderException;

    Collection findByAddress(Integer addressId) throws FinderException;
}
