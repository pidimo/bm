package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of  CategoryValue Entity Bean
 *
 * @author Ivan
 * @version $Id: CategoryValueHome.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CategoryValueHome extends EJBLocalHome {
    public CategoryValue create(ComponentDTO dto) throws CreateException;

    CategoryValue findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCategoryId(Integer key) throws FinderException;

    Collection findByCategoryValueName(Integer categoryId, String categoryName, Integer companyId) throws FinderException;

    Collection findAll() throws FinderException;
}
