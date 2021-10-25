package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public interface ImportProfileHome extends EJBLocalHome {
    ImportProfile create(ComponentDTO dto) throws CreateException;

    ImportProfile findByPrimaryKey(Integer key) throws FinderException;

    ImportProfile findByLabel(String label, Integer userId, Integer companyId) throws FinderException;

    ImportProfile findByImportStartTime(Long importStartTime) throws FinderException;
}
