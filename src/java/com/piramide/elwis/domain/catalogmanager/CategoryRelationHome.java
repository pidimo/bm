/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CategoryRelationHome extends EJBLocalHome {
    CategoryRelation create(ComponentDTO dto) throws CreateException;

    CategoryRelation create(Integer categoryId,
                            Integer categoryValueId,
                            Integer companyId) throws CreateException;

    CategoryRelation findByPrimaryKey(CategoryRelationPK key) throws FinderException;

    Collection findByCategoryValue(Integer categoryValueId, Integer companyId) throws FinderException;
}
