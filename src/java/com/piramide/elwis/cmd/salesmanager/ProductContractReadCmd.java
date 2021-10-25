package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.PaymentStep;
import com.piramide.elwis.domain.salesmanager.PaymentStepHome;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public class ProductContractReadCmd extends ProductContractCmd {
    private Log log = LogFactory.getLog(ProductContractReadCmd.class);

    public void executeInStateless(SessionContext ctx) {
        ProductContractDTO productContracDTO = getProductContractDTO();
        boolean withReferences = null != paramDTO.get("withReferences")
                && "true".equals(paramDTO.get("withReferences").toString());
        read(productContracDTO, withReferences, ctx);
    }

    private void read(ProductContractDTO productContractDTO, boolean withReferences, SessionContext ctx) {
        ProductContract productContract =
                (ProductContract) ExtendedCRUDDirector.i.read(productContractDTO, resultDTO, withReferences);

        //productContract was deleted by another user
        if (null == productContract) {
            return;
        }

        //use in ui to show contract owner name
        resultDTO.put("addressName", readAddressName(productContract.getAddressId(), ctx));

        //use in ui to show productName
        resultDTO.put("productName", getSalePositionProductName(productContract.getSalePositionId(), ctx));

        //use in user ui to identifier readonly fields
        resultDTO.put("hasInvoicePositions", String.valueOf(!getPositiveInvoicePositions(productContract).isEmpty()));

        //use to validate price when this change
        resultDTO.put("totalPaid", getTotalPaid(productContract).abs());

        //used to add grouping value in select box from ui
        resultDTO.put("groupingSelect", productContract.getGrouping());

        //use in user ui to enable matchCalendarPeriod field
        if (SalesConstants.PayMethod.Periodic.equal(productContract.getPayMethod()) &&
                productContract.getMatchCalendarPeriod() != null) {
            resultDTO.put("showMatchCalendarPeriod", "true");
        }

        //if the productContract is related to salePosition, adding productName of salePosition
        if (null != productContract.getSalePositionId()) {
            SalePositionDTO salePositionDTO = getSalePositionDTO(productContract.getSalePositionId(), ctx);
            resultDTO.put("salePositionId", productContract.getSalePositionId());
            resultDTO.put("productName", salePositionDTO.get("productName"));
            resultDTO.put("productId", salePositionDTO.get("productId"));
            resultDTO.put("customerName", salePositionDTO.get("customerName"));
            resultDTO.put("salePositionCustomerId", salePositionDTO.get("customerId"));

            //if the SalePosition is related to the sale,
            // then adding the name of the sale so that you will find in the user's ui
            if (null != salePositionDTO.get("saleId")) {
                resultDTO.put("saleId", salePositionDTO.get("saleId"));
                resultDTO.put("saleName", readSaleName((Integer) salePositionDTO.get("saleId")));
            }
        }

        //Note freeText
        if (productContract.getNoteId() != null) {
            resultDTO.put("noteText", new String(productContract.getSalesFreeText().getValue()));
        }

        if (SalesConstants.PayMethod.PartialFixed.getConstant() == productContract.getPayMethod()) {
            readPaymentSteps(productContract);
        }
    }

    private void readPaymentSteps(ProductContract productContract) {
        PaymentStepHome paymentStepHome =
                (PaymentStepHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PAYMENTSTEP);

        try {
            Collection paymentSteps = paymentStepHome.findByContractId(productContract.getContractId(),
                    productContract.getCompanyId());

            int steptsInvoicedCounter = 0;
            int i = 1;
            if (null != paymentSteps) {
                for (Object object : paymentSteps) {
                    PaymentStep paymentStep = (PaymentStep) object;
                    resultDTO.put("payStepId_" + i, paymentStep.getPayStepId());
                    resultDTO.put("payAmount_" + i, paymentStep.getPayAmount());
                    resultDTO.put("payDate_" + i, paymentStep.getPayDate());

                    //use in user ui to identifier readonly fields
                    List invoicePositions = getInvoicePositions(paymentStep, productContract);
                    resultDTO.put("hasInvoicePosition_" + i, String.valueOf(!invoicePositions.isEmpty()));

                    //use to calculate new Percentage when contract price change in productContractForm
                    if (!invoicePositions.isEmpty()) {
                        resultDTO.put("totalPriceFromInvoicePosition_" + i, getTotalPaid(invoicePositions));
                        steptsInvoicedCounter++;
                    }
                    i++;
                }
            }

            //use in user ui to detect valid installment changes 
            resultDTO.put("steptsInvoicedCounter", steptsInvoicedCounter);
        } catch (FinderException e) {
            log.debug("-> Read PaymentSteps [contractId=" + productContract.getContractId() + "] FAIL");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
