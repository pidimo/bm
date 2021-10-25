package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Ivan Alban
 * @version 4.2.1
 */
public interface ImportColumnHome extends EJBLocalHome {
    ImportColumn create(ComponentDTO dto) throws CreateException;

    ImportColumn findByPrimaryKey(Integer key) throws FinderException;

    Collection findByProfileId(Integer profileId, Integer companyId) throws FinderException;

    Collection findByProfileIdOrderByPosition(Integer profileId, Integer companyId) throws FinderException;
}
