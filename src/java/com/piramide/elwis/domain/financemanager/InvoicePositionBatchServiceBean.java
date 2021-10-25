/**
 * @author Ivan Alban
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.domain.salesmanager.ProductContractHome;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.*;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import java.math.BigDecimal;
import java.util.List;

public class InvoicePositionBatchServiceBean implements SessionBean {
    private Log log = LogFactory.getLog(this.getClass());

    private SessionContext ctx;

    public InvoicePositionBatchServiceBean() {
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void updateDiscount() {
        UserTransaction userTransaction = ctx.getUserTransaction();

        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        List invoicePositions;
        try {
            invoicePositions = (List) invoicePositionHome.findByDiscount();
        } catch (FinderException e) {
            log.debug("Cannot find invoice positions");
            return;
        }

        try {
            userTransaction.begin();
            for (int i = 0; i < invoicePositions.size(); i++) {
                InvoicePosition invoicePosition = (InvoicePosition) invoicePositions.get(i);

                BigDecimal totalReference = null;

                if (useNetOnInvoice(invoicePosition.getInvoice())) {
                    totalReference = invoicePosition.getTotalPrice();
                }

                if (useGrossOnInvoice(invoicePosition.getInvoice())) {
                    totalReference = invoicePosition.getTotalPriceGross();
                }

                if (null == totalReference) {
                    log.debug("Cannot update the discounted value for invoicePosition: " + invoicePosition.getPositionId() + " because the total is : " + totalReference);
                } else {
                    BigDecimal discountedValue = BigDecimalUtils.calculateDiscountedValue(totalReference,
                            invoicePosition.getDiscount());

                    log.debug("Update invoicePosition: " + invoicePosition.getPositionId() + " the discounted value is: " + discountedValue + " discount: " + invoicePosition.getDiscount() + " total: " + totalReference);
                    invoicePosition.setDiscountValue(discountedValue);
                }

            }
            userTransaction.commit();
        } catch (Exception e) {
            log.debug(".... ", e);
            try {
                userTransaction.rollback();
            } catch (SystemException e1) {
                log.error("Unexpexted error was happen ", e1);
            }
        }
    }

    private ProductContract getProductContract(Integer contractId) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            return productContractHome.findByPrimaryKey(contractId);
        } catch (FinderException e) {
            return null;
        }
    }

    private boolean useNetOnContract(ProductContract productContract) {
        return FinanceConstants.NetGrossFLag.NET.equal(productContract.getNetGross());
    }

    private boolean useGrossOnContract(ProductContract productContract) {
        return FinanceConstants.NetGrossFLag.GROSS.equal(productContract.getNetGross());
    }

    private boolean useNetOnInvoice(Invoice invoice) {
        return FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross());
    }

    private boolean useGrossOnInvoice(Invoice invoice) {
        return FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross());
    }
}
