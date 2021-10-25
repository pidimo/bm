package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.util.PayMethodUtil;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.salesmanager.PaymentStep;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3
 */
public class ProductContractUtilCmd extends ProductContractCmd {

    private Log log = LogFactory.getLog(ProductContractUtilCmd.class);

    @Override
    public void executeInStateless(SessionContext sessionContext) {
        if ("getProductContractDTO".equals(getOp())) {
            Integer contractId = EJBCommandUtil.i.getValueAsInteger(this, "contractId");
            getProductContractDTO(contractId);
        }

        if ("getPaymentStepDTO".equals(getOp())) {
            Integer paymentStepId = EJBCommandUtil.i.getValueAsInteger(this, "paymentStepId");
            getPaymentStepDTO(paymentStepId);
        }

        if ("updateOpenAmount".equals(getOp())) {
            Integer contractId = EJBCommandUtil.i.getValueAsInteger(this, "contractId");
            updateOpenAmount(contractId, sessionContext);
        }

        if ("sumInvoicePositions".equals(getOp())) {
            Integer contractId = EJBCommandUtil.i.getValueAsInteger(this, "contractId");
            Integer paymentStepId = EJBCommandUtil.i.getValueAsInteger(this, "paymentStepId");
            if (null == paymentStepId) {
                sumInvoicePositions(contractId);
            } else {
                sumInvoicePositions(contractId, paymentStepId);
            }
        }

        if ("getPaymentStepDTOs".equals(getOp())) {
            Integer contractId = EJBCommandUtil.i.getValueAsInteger(this, "contractId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getPaymentStepDTOs(contractId, companyId);
        }
    }

    private void sumInvoicePositions(Integer contractId) {
        ProductContract productContract = getProductContract(contractId);
        List invoicePositions = getAllInvoicePositions(productContract);
        BigDecimal result = sumInvoicePositions(invoicePositions);
        resultDTO.put("sumInvoicePositions", result);
    }

    private void sumInvoicePositions(Integer contractId, Integer paymentStepId) {
        ProductContract productContract = getProductContract(contractId);
        PaymentStep paymentStep = getPaymentStep(paymentStepId);
        List invoicePositions = getInvoicePositions(paymentStep, productContract);
        BigDecimal result = sumInvoicePositions(invoicePositions);
        resultDTO.put("sumInvoicePositions", result);
    }

    private void getProductContractDTO(Integer contractId) {
        ProductContract productContract = getProductContract(contractId);
        ProductContractDTO dto = null;
        if (null != productContract) {
            dto = new ProductContractDTO();
            DTOFactory.i.copyToDTO(productContract, dto);
        }
        resultDTO.put("getProductContractDTO", dto);
    }

    private void getPaymentStepDTO(Integer paymentStepId) {
        PaymentStep paymentStep = getPaymentStep(paymentStepId);

        PaymentStepDTO dto = null;
        if (null != paymentStep) {
            ProductContract productContract = getProductContract(paymentStep.getContractId());
            dto = new PaymentStepDTO();
            InvoicePosition invoicePosition = getPositiveInvoicePosition(paymentStep);
            if (null != invoicePosition) {
                dto.put("hasInvoicePosition", true);
            } else {
                dto.put("hasInvoicePosition", false);
            }

            dto.put("amounType", productContract.getAmounType());
            DTOFactory.i.copyToDTO(paymentStep, dto);
        }

        resultDTO.put("getPaymentStepDTO", dto);
    }

    private void getPaymentStepDTOs(Integer contractId, Integer companyId) {
        List paymentSteps = getPaymentSteps(contractId, companyId);

        List<PaymentStepDTO> paymentStepDTOs = new ArrayList<PaymentStepDTO>();

        for (int i = 0; i < paymentSteps.size(); i++) {
            PaymentStep paymentStep = (PaymentStep) paymentSteps.get(i);
            ProductContract productContract = getProductContract(paymentStep.getContractId());
            InvoicePosition invoicePosition = getPositiveInvoicePosition(paymentStep);

            PaymentStepDTO dto = new PaymentStepDTO();
            if (null != invoicePosition) {
                dto.put("hasInvoicePosition", true);
            } else {
                dto.put("hasInvoicePosition", false);
            }

            dto.put("amounType", productContract.getAmounType());
            DTOFactory.i.copyToDTO(paymentStep, dto);
            paymentStepDTOs.add(dto);
        }

        resultDTO.put("getPaymentStepDTOs", paymentStepDTOs);
    }


    private void updateOpenAmount(Integer contractId, SessionContext ctx) {

        ProductContract productContract = getProductContract(contractId);

        if (SalesConstants.PayMethod.Single.equal(productContract.getPayMethod())) {
            BigDecimal totalPaid = getTotalPaid(productContract);
            BigDecimal openAmount = calculateOpenAmount(productContract.getPrice(), totalPaid);
            productContract.setOpenAmount(openAmount);

            if (null != productContract.getPaymentSteps()) {
                removePaymentSteps(productContract.getContractId(), productContract.getCompanyId(), ctx);
            }

            productContract.setPayPeriod(null);
            productContract.setContractEndDate(null);
            productContract.setPayStartDate(null);
            productContract.setInstallment(null);
            productContract.setAmounType(null);
            productContract.setMatchCalendarPeriod(null);
        }

        if (SalesConstants.PayMethod.Periodic.equal(productContract.getPayMethod())) {
            productContract.setOpenAmount(new BigDecimal(0.0));
            if (null != productContract.getPaymentSteps()) {
                removePaymentSteps(productContract.getContractId(), productContract.getCompanyId(), ctx);
            }

            productContract.setInstallment(null);
            productContract.setAmounType(null);

            boolean isMatchPeriod = PayMethodUtil.isMatchPeriod(productContract.getPayPeriod());

            if (isMatchPeriod) {
                boolean invoicedUntilDateIsInPeriod = true;
                boolean payStartDateIsInPeriod = PayMethodUtil.isMatchPeriodStart(
                        DateUtils.integerToDateTime(productContract.getPayStartDate()),
                        productContract.getPayPeriod());

                if (null != productContract.getInvoicedUntil() && isMatchPeriod) {
                    invoicedUntilDateIsInPeriod = PayMethodUtil.isMatchPeriodEnd(
                            DateUtils.integerToDateTime(productContract.getInvoicedUntil()),
                            productContract.getPayPeriod());
                }

                if (payStartDateIsInPeriod && invoicedUntilDateIsInPeriod) {
                    productContract.setMatchCalendarPeriod(null);
                }
            } else {
                productContract.setMatchCalendarPeriod(null);
            }
        }

        if (SalesConstants.PayMethod.PartialPeriodic.equal(productContract.getPayMethod())) {
            BigDecimal totalPaid = getTotalPaid(productContract);
            BigDecimal openAmount = calculateOpenAmount(productContract.getPrice(), totalPaid);
            productContract.setOpenAmount(openAmount);
            if (null != productContract.getPaymentSteps()) {
                removePaymentSteps(productContract.getContractId(), productContract.getCompanyId(), ctx);
            }

            productContract.setContractEndDate(null);
            productContract.setAmounType(null);
            productContract.setMatchCalendarPeriod(null);
        }

        if (SalesConstants.PayMethod.PartialFixed.equal(productContract.getPayMethod())) {
            List<PaymentStepDTO> paymentStepDTOs = getPaymentStepDTOs(productContract.getInstallment());
            BigDecimal totalPaid = getTotalPaid(productContract);
            BigDecimal openAmount = calculateOpenAmount(productContract.getPrice(), totalPaid);
            productContract.setOpenAmount(openAmount);
            updatePaymentSteps(productContract, paymentStepDTOs);

            productContract.setPayPeriod(null);
            productContract.setContractEndDate(null);
            productContract.setPayStartDate(null);
            productContract.setMatchCalendarPeriod(null);
        }

    }

    private void updatePaymentSteps(ProductContract productContract, List<PaymentStepDTO> paymentStepDTOs) {
        List<Integer> payStepsIdsFromEntity = getPaymentStepIdsFromEntity(productContract);
        List<Integer> payStepsIdsWithInvoice = getPaymentStepIdsWithInvoice(productContract);
        List<Integer> payStepIdsFromDTO = getPaymensStepIdsFromDTO(paymentStepDTOs);

        //update the invoiced payment steps accorting to the associated invoice positions
        for (int i = 0; i < payStepsIdsWithInvoice.size(); i++) {
            Integer id = payStepsIdsWithInvoice.get(i);
            PaymentStep invoicedPaymentStep = getPaymentStep(id);
            BigDecimal newValue = calculateInvoicedPaymentStepValue(invoicedPaymentStep, productContract);
            invoicedPaymentStep.setPayAmount(newValue);
        }


        //remove ids than contain relacion witn saleposition
        payStepIdsFromDTO.removeAll(payStepsIdsWithInvoice);
        payStepsIdsFromEntity.removeAll(payStepsIdsWithInvoice);

        //remove ids that come from ui
        payStepsIdsFromEntity.removeAll(payStepIdsFromDTO);

        log.debug("-> PaySteps Ids to delete " + payStepsIdsFromEntity);
        log.debug("-> PaySteps Ids to update " + payStepIdsFromDTO);

        //remove unused PaymentSteps
        for (Integer id : payStepsIdsFromEntity) {
            PaymentStep paymentStep = getPaymentStep(id);
            if (null != paymentStep) {
                try {
                    paymentStep.remove();
                } catch (RemoveException e) {
                    log.error("-> Execute  PaymentStep.remove() [payStepId=" + id + "] FAIL", e);
                    break;
                }
            }
        }
        //update and create paymentSteps that come from ui
        for (PaymentStepDTO paymentStepDTO : paymentStepDTOs) {

            Integer payStepId = (Integer) paymentStepDTO.get("payStepId");
            BigDecimal payAmount = (BigDecimal) paymentStepDTO.get("payAmount");
            Integer payDate = (Integer) paymentStepDTO.get("payDate");

            //update payment steps
            if (null != payStepId && payStepIdsFromDTO.contains(payStepId)) {
                PaymentStep paymentStep = getPaymentStep(payStepId);
                log.debug("->old payDate " + paymentStep.getPayDate());
                paymentStep.setPayAmount(payAmount);
                paymentStep.setPayDate(payDate);
                log.debug("->new payDate " + paymentStep.getPayDate());
                continue;
            }

            //create new pyment steps
            if (null == payStepId && null != payAmount) {
                paymentStepDTO.put("contractId", productContract.getContractId());
                paymentStepDTO.put("compayId", productContract.getCompanyId());
                ExtendedCRUDDirector.i.create(paymentStepDTO, resultDTO, false);
            }
        }
    }

    private List<Integer> getPaymentStepIdsFromEntity(ProductContract productContract) {
        Collection steps = productContract.getPaymentSteps();
        List<Integer> ids = new ArrayList<Integer>();
        for (Object object : steps) {
            PaymentStep paymentStep = (PaymentStep) object;
            ids.add(paymentStep.getPayStepId());
        }

        return ids;
    }

    private List<Integer> getPaymentStepIdsWithInvoice(ProductContract productContract) {
        Collection stepsWithPaid = getStepsAlreadyPaid(productContract);
        List<Integer> ids = new ArrayList<Integer>();
        for (Object object : stepsWithPaid) {
            PaymentStep paymentStep = (PaymentStep) object;
            ids.add(paymentStep.getPayStepId());
        }
        return ids;
    }

    private List<Integer> getPaymensStepIdsFromDTO(List<PaymentStepDTO> paymentStepDTOs) {
        List<Integer> ids = new ArrayList<Integer>();
        for (PaymentStepDTO paymentStepDTO : paymentStepDTOs) {
            Integer payStepId = (Integer) paymentStepDTO.get("payStepId");
            BigDecimal payAmount = (BigDecimal) paymentStepDTO.get("payAmount");
            if (null != payStepId && null != payAmount) {
                ids.add(payStepId);
            }
        }

        return ids;
    }

    public boolean isStateful() {
        return false;
    }
}
