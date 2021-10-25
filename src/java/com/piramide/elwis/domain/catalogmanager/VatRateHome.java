/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: VatRateHome.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 18-ago-2004 10:31:12 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface VatRateHome extends EJBLocalHome {
    public VatRate create(ComponentDTO dto) throws CreateException;

    VatRate findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByVatId(Integer vatId) throws FinderException;
}
