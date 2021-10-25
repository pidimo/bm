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
public interface AddressRelationHome extends EJBLocalHome {
    AddressRelation create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.AddressRelation findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByAddressId(Integer addressId) throws FinderException;

    public Collection findByRelatedAddressId(Integer relatedAddressId) throws FinderException;
}
