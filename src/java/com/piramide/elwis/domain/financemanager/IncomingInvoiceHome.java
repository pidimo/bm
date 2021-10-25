/**
 * Jatun S.R.L.
 * @author alvaro
 * @version $Id: ${NAME}.java 18-feb-2009 9:10:21
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface IncomingInvoiceHome extends EJBLocalHome {
    IncomingInvoice findByPrimaryKey(Integer key) throws FinderException;

    public IncomingInvoice create(ComponentDTO dto) throws CreateException;

    Collection findBySupplierId(Integer supplierId) throws FinderException;

}
