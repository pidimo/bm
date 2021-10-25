/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: ProductPictureHome.java 10630 2015-07-15 21:50:31Z miguel ${NAME}.java, v 2.0 23-ago-2004 9:32:35 Ivan Exp $
 */
package com.piramide.elwis.domain.productmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProductPictureHome extends EJBLocalHome {
    public ProductPicture create(ComponentDTO dto) throws CreateException;

    ProductPicture findByPrimaryKey(ProductPicturePK key) throws FinderException;

    Collection findByProductId(Integer productId) throws FinderException;
}
