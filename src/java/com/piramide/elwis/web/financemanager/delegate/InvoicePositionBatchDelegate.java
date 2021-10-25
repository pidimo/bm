package com.piramide.elwis.web.financemanager.delegate;

import com.piramide.elwis.domain.financemanager.InvoicePositionBatchService;
import com.piramide.elwis.domain.financemanager.InvoicePositionBatchServiceHome;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class InvoicePositionBatchDelegate {
    private Log log = LogFactory.getLog(this.getClass());

    public static InvoicePositionBatchDelegate i = new InvoicePositionBatchDelegate();

    private InvoicePositionBatchDelegate() {

    }

    public void updateDiscountValue() {
        InvoicePositionBatchService service = getService();
        log.debug("------------------------- START UPDATE DISCOUNT VALUES ON INVOICE POSITIONS --------------------------");
        service.updateDiscount();
        log.debug("------------------------------------------------------------------------------------------------------");
    }

    private InvoicePositionBatchService getService() {
        InvoicePositionBatchServiceHome home =
                (InvoicePositionBatchServiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION_BATCH_SERVICE);
        try {
            return home.create();
        } catch (CreateException e) {
            log.error("Unexpexted error was happen ", e);
        }

        return null;
    }
}
