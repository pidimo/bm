package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface DuplicateAddressHome extends EJBLocalHome {
    public DuplicateAddress create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.DuplicateAddress findByPrimaryKey(com.piramide.elwis.domain.contactmanager.DuplicateAddressPK key) throws FinderException;

    public Collection findByDuplicateGroupId(Integer duplicateGroupId) throws FinderException;

    public Collection findByDuplicateGroupIdOrderPositionIndex(Integer duplicateGroupId) throws FinderException;

    public Collection findByAddressId(Integer addressId) throws FinderException;
}
