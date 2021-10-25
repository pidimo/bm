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
public interface RecordDuplicateHome extends EJBLocalHome {
    public RecordDuplicate create(ComponentDTO dto) throws CreateException;

    RecordDuplicate findByPrimaryKey(com.piramide.elwis.domain.contactmanager.RecordDuplicatePK key) throws FinderException;

    public Collection findByImportRecordId(Integer importRecordId) throws FinderException;

    public Collection findByAddressId(Integer addressId) throws FinderException;
}
