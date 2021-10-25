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
public interface RecordColumnHome extends EJBLocalHome {

    public RecordColumn create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.RecordColumn findByPrimaryKey(com.piramide.elwis.domain.contactmanager.RecordColumnPK key) throws FinderException;

    Collection findByImportRecord(Integer importRecordId) throws FinderException;
}
