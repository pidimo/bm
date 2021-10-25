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
public interface DuplicateGroupHome extends EJBLocalHome {
    DuplicateGroup create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.DuplicateGroup findByPrimaryKey(Integer key) throws FinderException;

    Collection findByDedupliContactId(Integer dedupliContactId) throws FinderException;
}
