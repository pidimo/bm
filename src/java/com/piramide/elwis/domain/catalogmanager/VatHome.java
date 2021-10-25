/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: VatHome.java 8638 2008-11-05 22:40:53Z ivan ${NAME}.java, v 2.0 16-ago-2004 16:58:14 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface VatHome extends EJBLocalHome {
    public Vat create(ComponentDTO dto) throws CreateException;

    Vat findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    Vat findByLabel(String label, Integer companyId) throws FinderException;
}
