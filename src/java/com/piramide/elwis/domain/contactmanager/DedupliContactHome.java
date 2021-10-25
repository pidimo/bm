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
public interface DedupliContactHome extends EJBLocalHome {
    DedupliContact create(ComponentDTO dto) throws CreateException;

    DedupliContact findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCompanyId(Integer companyId) throws FinderException;
}
