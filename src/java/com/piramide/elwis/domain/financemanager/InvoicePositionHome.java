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

public interface InvoicePositionHome extends EJBLocalHome {
    InvoicePosition create(ComponentDTO dto) throws CreateException;

    InvoicePosition findByPrimaryKey(Integer key) throws FinderException;

    Collection findByInvoiceId(Integer invoiceId, Integer companyId) throws FinderException;

    Integer selectMaxPositionNumber(Integer processId, Integer companyId) throws FinderException;

    Collection findByContractId(Integer contractId, Integer companyId) throws FinderException;

    Collection findByContractIdPositive(Integer contractId, Integer companyId) throws FinderException;

    InvoicePosition findByPayStepId(Integer payStepId, Integer companyId) throws FinderException;

    Collection findByUnitPriceIsNull(Integer invoiceId, Integer companyId) throws FinderException;

    Collection findByUnitPriceGrossIsNull(Integer invoiceId, Integer companyId) throws FinderException;

    InvoicePosition findBySalePositionId(Integer salePositionId, Integer companyId) throws FinderException;

    Collection findByPaymentStepAndContract(Integer contractId,
                                            Integer paymentStepId,
                                            Integer companyId) throws FinderException;

    Collection findByDiscount() throws FinderException;
}
