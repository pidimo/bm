/**
 * @author: ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface InvoiceReminderHome extends EJBLocalHome {
    InvoiceReminder create(ComponentDTO dto) throws CreateException;

    InvoiceReminder findByPrimaryKey(Integer key) throws FinderException;

    Collection findByInvoiceId(Integer invoiceId, Integer companyId) throws FinderException;

    Collection findByReminderLevel(Integer invoiceId, Integer companyId, Integer level) throws FinderException;
}
