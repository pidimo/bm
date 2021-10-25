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

public interface InvoiceTextHome extends EJBLocalHome {

    InvoiceText create(ComponentDTO dto) throws CreateException;

    InvoiceText findByPrimaryKey(InvoiceTextPK key) throws FinderException;

    InvoiceText findDefaultInvoiceText(Integer templateId, Integer companyId) throws FinderException;

    Collection findInvoiceTextsByTemplateId(Integer templateId, Integer companyId) throws FinderException;
    
    InvoiceText findByDocumentFreeTextId(Integer freeTextId) throws FinderException;
}
