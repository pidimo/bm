package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface DynamicSearchFieldHome extends EJBLocalHome {
    DynamicSearchField create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.admin.DynamicSearchField findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByDynamicSearch(Integer dynamicSearchId) throws FinderException;
}
