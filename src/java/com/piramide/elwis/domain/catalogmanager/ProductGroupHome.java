/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductGroupHome.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 16-ago-2004 16:54:38 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface ProductGroupHome extends EJBLocalHome {
    public ProductGroup create(ComponentDTO dto) throws CreateException;

    ProductGroup findByPrimaryKey(Integer key) throws FinderException;
}
