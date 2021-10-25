/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.productmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProductTextHome extends EJBLocalHome {
    ProductText create(ComponentDTO componentDTO) throws CreateException;

    ProductText findByPrimaryKey(ProductTextPK key) throws FinderException;

    Collection findProductTextByProductId(Integer productId, Integer companyId) throws FinderException;

    ProductText findDefaultProductText(Integer productId, Integer companyId) throws FinderException;

    ProductText findProductTextByLanguageId(Integer productId, Integer languageId, Integer companyId) throws FinderException;
}
