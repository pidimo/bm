package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.0
 */
public interface AddressRelationTypeHome extends EJBLocalHome {
    AddressRelationType create(ComponentDTO dto) throws CreateException;

    AddressRelationType findByPrimaryKey(Integer key) throws FinderException;
}
