package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Category Entity Bean
 *
 * @author Ivan
 * @version $Id: CategoryHome.java 12573 2016-08-10 17:02:39Z miguel ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CategoryHome extends EJBLocalHome {
    public Category create(ComponentDTO dto) throws CreateException;

    Category findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByTableId(String key, Integer companyId) throws FinderException;

    public Collection findByTableIdOrSecondTableId(String tableId, String secondTableId, Integer companyId) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findCategoriesByGroup(Integer categoryGroupId, Integer companyId) throws FinderException;

    public Collection findChildrenCategories(Integer categoryId, Integer companyId) throws FinderException;

    public Collection selectCategoriesWithOutGroup(String tableId, String secondTableId, Integer companyId) throws FinderException;

    public Collection findSingleCategories(String tableId, Integer companyId) throws FinderException;

    public Collection findContactCategories(Integer companyId) throws FinderException;

    public Collection findContactPersonCategories(Integer companyId) throws FinderException;

    public Category findByFieldIdentifier(String fieldIdentifier, Integer companyId) throws FinderException;
}


