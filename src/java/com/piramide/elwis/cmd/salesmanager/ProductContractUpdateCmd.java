package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.PaymentStep;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.domain.salesmanager.ProductContractHome;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractUpdateCmd extends ProductContractCmd {

    private Log log = LogFactory.getLog(ProductContractUpdateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ProductContractUpdateCmd.... " + paramDTO);

        ProductContractDTO productContractDTO = getProductContractDTO();

        if (isCancelContractProcess()) {
            cancelProductContract(productContractDTO, ctx);
        } else {
            update(productContractDTO, ctx);
        }
    }

    private void update(ProductContractDTO productContractDTO,
                        SessionContext ctx) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);

        ProductContract productContract = null;
        try {
            productContract = productContractHome.findByPrimaryKey((Integer) productContractDTO.get("contractId"));
        } catch (FinderException e) {
            log.debug("->Execute ProductContractHome.findByPrimaryKey[" +
                    productContractDTO.get("contractId") + " FAIL]");
        }

        //productcontract was deleted by another user
        if (null == productContract) {
            productContractDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        //cannot update some fields of productcontract because already assigned invoiceposition
        if (!canUpdateProductContract(productContractDTO, productContract)) {
            resultDTO.setResultAsFailure();
            ProductContractReadCmd productContractReadCmd = new ProductContractReadCmd();
            productContractReadCmd.putParam("contractId", productContract.getContractId());
            productContractReadCmd.executeInStateless(ctx);
            resultDTO.putAll(productContractReadCmd.getResultDTO());
            return;
        }

        ProductContract newProductContract =
                (ProductContract) ExtendedCRUDDirector.i.update(productContractDTO, resultDTO, false, true, true, "Fail");

        //version error
        if (resultDTO.isFailure()) {
            ProductContractReadCmd productContractReadCmd = new ProductContractReadCmd();
            productContractReadCmd.putParam("contractId", newProductContract.getContractId());
            productContractReadCmd.executeInStateless(ctx);
            resultDTO.putAll(productContractReadCmd.getResultDTO());
            return;
        }

        //define the next invoice date
        defineContractNextInvoiceDate(newProductContract);

        //Note freeText
        if (null == productContract.getNoteId()) {
            Integer freetextId = createSaleFreeText(paramDTO.get("noteText").toString(), productContract.getCompanyId());
            productContract.setNoteId(freetextId);
        } else if (null != paramDTO.get("noteText")) {
            productContract.getSalesFreeText().setValue(paramDTO.get("noteText").toString().getBytes());
        }

        ProductContractUtilCmd productContractUtilCmd = new ProductContractUtilCmd();
        productContractUtilCmd.putParam(paramDTO);
        productContractUtilCmd.setOp("updateOpenAmount");
        productContractUtilCmd.putParam("contractId", productContract.getContractId());
        productContractUtilCmd.executeInStateless(ctx);

        if (toBeInvoiced(productContract.getContractTypeId())) {
            getCustomer(productContract.getAddressId());
        } else {
            getSupplier(productContract.getAddressId());
        }
    }

    private boolean canUpdateProductContract(ProductContractDTO productContractDTO, ProductContract productContract) {
        if (!isProductContractRelatedWithInvoicePositions(productContract)) {
            return true;
        }

        //paymethod has changed
        Integer dtoPayMethod = (Integer) productContractDTO.get("payMethod");
        if (hasChangePayMethod(dtoPayMethod, productContract.getPayMethod())) {
            resultDTO.addResultMessage("ProductContract.error.cannotChangePayMethod");
        }

        //validate only when the price changes in the value of user ui
        BigDecimal dtoPrice = (BigDecimal) productContractDTO.get("price");
        BigDecimal dtoOpenAmount = (BigDecimal) productContractDTO.get("openAmount");
        BigDecimal totalPaid = getTotalPaid(productContract);
        if (!SalesConstants.PayMethod.Periodic.equal(productContract.getPayMethod()) &&
                hasChangeBigDecimalValue(dtoPrice, productContract.getPrice()) &&
                dtoPrice.compareTo(totalPaid) == -1) {
            resultDTO.addResultMessage("ProductContract.error.PriceLessThanTotalPaid");
        }

        if (SalesConstants.PayMethod.Single.equal(dtoPayMethod)) {
            if (hasChangeBigDecimalValue(dtoPrice, productContract.getPrice()) && isProductContractRelatedWithInvoicePositions(productContract)) {
                resultDTO.addResultMessage("ProductContract.error.cannotChangePrice");
            }

            if (hasChangeBigDecimalValue(dtoOpenAmount, productContract.getOpenAmount()) && isProductContractRelatedWithInvoicePositions(productContract)) {
                resultDTO.addResultMessage("ProductContract.error.cannotChangeOpenAmount");
            }
        }


        Integer dtoStartDate = (Integer) productContractDTO.get("payStartDate");
        if (SalesConstants.PayMethod.Periodic.equal(dtoPayMethod) &&
                hasChangeIntegerValues(dtoStartDate, productContract.getPayStartDate())) {
            resultDTO.addResultMessage("ProductContract.error.cannotChangeStartDate");
        }

        if (SalesConstants.PayMethod.PartialPeriodic.equal(dtoPayMethod)) {
            Integer dtoInstallment = (Integer) productContractDTO.get("installment");
            Integer paidNumber = getPositiveInvoicePositions(productContract).size();
            if (hasChangeIntegerValues(dtoInstallment, productContract.getInstallment()) &&
                    dtoInstallment < paidNumber) {
                resultDTO.addResultMessage("ProductContract.error.installmentLessThanPaid", paidNumber.toString());
            }

            if (hasChangeIntegerValues(dtoStartDate, productContract.getPayStartDate())) {
                resultDTO.addResultMessage("ProductContract.error.cannotChangeStartDate");
            }
        }

        if (SalesConstants.PayMethod.PartialFixed.equal(dtoPayMethod)) {
            Integer dtoInstallment = (Integer) productContractDTO.get("installment");
            Integer paidNumber = getPositiveInvoicePositions(productContract).size();
            if (hasChangeIntegerValues(dtoInstallment, productContract.getInstallment()) &&
                    dtoInstallment < paidNumber) {
                resultDTO.addResultMessage("ProductContract.error.installmentLessThanPaid", paidNumber.toString());
            }


            List<PaymentStepDTO> paymentStepDTOs = getPaymentStepDTOs(dtoInstallment);
            for (PaymentStepDTO paymentStepDTO : paymentStepDTOs) {
                Integer dtoPayStepId = (Integer) paymentStepDTO.get("payStepId");
                if (null == dtoPayStepId) {
                    continue;
                }

                PaymentStep paymentStep = getPaymentStep(dtoPayStepId);
                if (null == paymentStep) {
                    continue;
                }

                BigDecimal dtoPayAmount = (BigDecimal) paymentStepDTO.get("payAmount");
                Integer dtoPayDate = (Integer) paymentStepDTO.get("payDate");

                if ((hasChangeIntegerValues(dtoPayDate, paymentStep.getPayDate()) ||
                        hasChangeBigDecimalValue(dtoPayAmount, paymentStep.getPayAmount())) &&
                        null != getPositiveInvoicePosition(paymentStep) && null == paymentStepDTO.get("canUpdatePayAmount")) {
                    resultDTO.addResultMessage("ProductContract.error.cannotChangePaymentStep");
                    break;
                }
            }
        }

        return !resultDTO.hasResultMessage();
    }

    private void cancelProductContract(ProductContractDTO productContractDTO, SessionContext ctx) {
        log.debug("Cancel the contract process...");

        ProductContract productContract = findProductContract((Integer) productContractDTO.get("contractId"));

        if (productContract == null) {
            productContractDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        //cancel the contract
        productContract.setCancelledContract(true);

        ProductContractReadCmd productContractReadCmd = new ProductContractReadCmd();
        productContractReadCmd.putParam("contractId", productContract.getContractId());
        productContractReadCmd.executeInStateless(ctx);
        resultDTO.putAll(productContractReadCmd.getResultDTO());
    }

    private boolean isCancelContractProcess() {
        return "true".equals(paramDTO.get("cancelContractProcess"));
    }

    private ProductContract findProductContract(Integer contractId) {
        ProductContractHome productContractHome = (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        ProductContract productContract = null;

        if (contractId != null) {
            try {
                productContract = productContractHome.findByPrimaryKey(contractId);
            } catch (FinderException e) {
                log.debug("Error in find contract " + contractId, e);
            }
        }
        return productContract;
    }


    private boolean hasChangeBigDecimalValue(BigDecimal dtoBigDecimalValue, BigDecimal entityBigDecimalValue) {
        return (null != dtoBigDecimalValue && null != entityBigDecimalValue && dtoBigDecimalValue.compareTo(entityBigDecimalValue) != 0) ||
                (null == dtoBigDecimalValue && null != entityBigDecimalValue) ||
                (null != dtoBigDecimalValue && null == entityBigDecimalValue);
    }

    private boolean hasChangePayMethod(Integer dtoPayMethod, Integer entityPayMethod) {
        return !dtoPayMethod.equals(entityPayMethod);
    }

    private boolean hasChangeIntegerValues(Integer dtoIntegerValue, Integer entityIntegerValue) {
        return (null != dtoIntegerValue && null != entityIntegerValue && !dtoIntegerValue.equals(entityIntegerValue)) ||
                (null == dtoIntegerValue && null != entityIntegerValue) ||
                (null != dtoIntegerValue && null == entityIntegerValue);

    }

    public boolean isStateful() {
        return false;
    }
}
