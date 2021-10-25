/**
 * @author : ivan
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface InvoicePaymentHome extends EJBLocalHome {
    InvoicePayment create(ComponentDTO dto) throws CreateException;

    InvoicePayment findByPrimaryKey(Integer key) throws FinderException;

    Collection findByInvoiceId(Integer invoiceId, Integer companyId) throws FinderException;

    Collection findByCreditNoteId(Integer invoiceId, Integer companyId) throws FinderException;
}
