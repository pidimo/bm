/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface InvoiceVatHome extends EJBLocalHome {
    InvoiceVat create(ComponentDTO dto) throws CreateException;

    InvoiceVat findByPrimaryKey(InvoiceVatPK key) throws FinderException;

    Collection findByInvoiceId(Integer invoiceId, Integer companyId) throws FinderException;
}
