package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.catalogmanager.Vat;
import com.piramide.elwis.domain.catalogmanager.VatHome;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoiceVatDTO;
import com.piramide.elwis.utils.BigDecimalUtils;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceVatCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceVatCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if ("update".equals(getOp())) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Invoice invoice = getInvoice(invoiceId);
            update(invoice, companyId);
        }
        if ("getInvoiceVats".equals(getOp())) {
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            Integer companyId = (Integer) paramDTO.get("companyId");

            getInvoiceVats(invoiceId, companyId);
        }
    }

    private List<InvoiceVatDTO> getInvoiceVats(Integer invoiceId, Integer companyId) {
        List<InvoiceVatDTO> result = new ArrayList<InvoiceVatDTO>();
        InvoiceVatHome invoiceVatHome = getInvoiceVatHome();
        try {
            Collection invoiceVats = invoiceVatHome.findByInvoiceId(invoiceId, companyId);
            for (Object object : invoiceVats) {
                InvoiceVat invoiceVat = (InvoiceVat) object;
                InvoiceVatDTO dto = new InvoiceVatDTO();
                DTOFactory.i.copyToDTO(invoiceVat, dto);
                String vatLabel = getVatLabel(invoiceVat.getVatId());
                dto.put("label", vatLabel);

                BigDecimal percent = invoiceVat.getVatRate().divide(new BigDecimal(100));

                BigDecimal value = BigDecimalUtils.multiply(invoiceVat.getAmount(), percent);

                dto.put("value", value);

                result.add(dto);
            }
        } catch (FinderException e) {
            log.debug("-> Read InvoiceVats invoiceId=" + invoiceId + " FAIL");
        }
        resultDTO.put("getInvoiceVats", result);
        return result;
    }

    private String getVatLabel(Integer vatId) {
        VatHome vatHome =
                (VatHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_VAT);
        try {
            Vat vat = vatHome.findByPrimaryKey(vatId);
            return vat.getVatLabel();
        } catch (FinderException e) {
            log.error("-> Read vat vatId=" + vatId + " FAIL");
        }
        return "";
    }

    private void update(Invoice invoice, Integer companyId) {
        InvoiceVatHome invoiceVatHome = getInvoiceVatHome();
        try {
            Collection invoiceVats = invoiceVatHome.findByInvoiceId(invoice.getInvoiceId(), companyId);
            for (Object object : invoiceVats) {
                InvoiceVat invoiceVat = (InvoiceVat) object;
                try {
                    invoiceVat.remove();
                } catch (RemoveException e) {
                    log.error("-> Delete InvoiceVat invoiceId=" + invoice.getInvoiceId() + " FAIL");
                }
            }
        } catch (FinderException e) {
            log.debug("->Not InvoiceVats invoiceId=" + invoice.getInvoiceId() + " OK");
        }
        try {
            createInvoiceVats(invoice, companyId);
        } catch (FinderException e) {
            log.error("-> Read InvoicePositions invoiceId=" + invoice.getInvoiceId() + " FAIL");
        }
    }

    private void createInvoiceVats(Invoice invoice, Integer companyId) throws FinderException {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        Collection invoicePositions = invoicePositionHome.findByInvoiceId(invoice.getInvoiceId(), companyId);
        for (Object object : invoicePositions) {
            InvoicePosition invoicePosition = (InvoicePosition) object;
            createInvoiceVat(invoice, invoicePosition);
        }
    }

    private void createInvoiceVat(Invoice invoice, InvoicePosition invoicePosition) {
        InvoiceVatPK invoiceVatPK = new InvoiceVatPK();
        invoiceVatPK.invoiceId = invoice.getInvoiceId();
        invoiceVatPK.vatId = invoicePosition.getVatId();

        BigDecimal totalPrice = new BigDecimal(0);
        if (FinanceConstants.NetGrossFLag.NET.equal(invoice.getNetGross())) {
            totalPrice = invoicePosition.getTotalPrice();
        }

        if (FinanceConstants.NetGrossFLag.GROSS.equal(invoice.getNetGross())) {
            totalPrice = InvoiceUtil.i.calculateTotalPriceForInvoicePositions(
                    invoicePosition.getTotalPriceGross(),
                    invoicePosition.getVatRate());
        }

        InvoiceVatHome invoiceVatHome = getInvoiceVatHome();
        try {
            InvoiceVat invoiceVat = invoiceVatHome.findByPrimaryKey(invoiceVatPK);
            BigDecimal newAmount = invoiceVat.getAmount().add(totalPrice);
            invoiceVat.setAmount(newAmount);
        } catch (FinderException e) {
            InvoiceVatDTO invoiceVatDTO = new InvoiceVatDTO();
            invoiceVatDTO.put("amount", totalPrice);
            invoiceVatDTO.put("companyId", invoicePosition.getCompanyId());
            invoiceVatDTO.put("invoiceId", invoice.getInvoiceId());
            invoiceVatDTO.put("vatId", invoicePosition.getVatId());
            invoiceVatDTO.put("vatRate", invoicePosition.getVatRate());

            ExtendedCRUDDirector.i.create(invoiceVatDTO, resultDTO, false);
        }

    }

    private InvoiceVatHome getInvoiceVatHome() {
        return (InvoiceVatHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEVAT);
    }

    private Invoice getInvoice(Integer invoiceId) {
        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        try {
            return invoiceHome.findByPrimaryKey(invoiceId);
        } catch (FinderException e) {
            log.debug("-> Execute InvoiceHome.findByPrimaryKey(" + invoiceId + ") FAIL");
        }

        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
