package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.strutsejb.BeanTransactionEJBCommand;
import com.piramide.elwis.domain.financemanager.InvoicePayment;
import com.piramide.elwis.domain.financemanager.InvoicePaymentHome;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.financemanager.InvoicePositionHome;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * Cmd to manage custom csv export
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class InvoiceCSVExportCmd extends BeanTransactionEJBCommand {
    private Log log = LogFactory.getLog(InvoiceCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if ("markPositionsAsExported".equals(getOp())) {
            List positionIdList = (List) paramDTO.get("invoicePositionExportedIds");
            setPositionsAsExported(positionIdList);
        }

        if ("markPaymentAsExported".equals(getOp())) {
            List paymentIdList = (List) paramDTO.get("invoicePaymentExportedIds");
            setPaymentsAsExported(paymentIdList);
        }
    }

    private void setPositionsAsExported(List<Integer> positionIdList) {
        InvoicePositionHome invoicePositionHome = (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);
        for (Integer positionId : positionIdList) {
            try {
                InvoicePosition invoicePosition = invoicePositionHome.findByPrimaryKey(positionId);
                invoicePosition.setExported(true);
            } catch (FinderException e) {
                log.debug("Error in update position as exported..." + positionId, e);
            }
        }
    }

    private void setPaymentsAsExported(List<Integer> paymentIdList) {
        InvoicePaymentHome invoicePaymentHome = (InvoicePaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPAYMENT);
        for (Integer paymentId : paymentIdList) {
            try {
                InvoicePayment invoicePayment = invoicePaymentHome.findByPrimaryKey(paymentId);
                invoicePayment.setExported(true);
            } catch (FinderException e) {
                log.debug("Error in update payment as exported..." + paymentId, e);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
