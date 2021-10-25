package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.dto.salesmanager.PaymentStepDTO;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.SalesConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractCreateCmd extends ProductContractCmd {
    private Log log = LogFactory.getLog(ProductContractCmd.class);

    public void executeInStateless(SessionContext ctx) {
        ProductContractDTO productContracDTO = getProductContractDTO();
        create(productContracDTO, ctx);
    }

    private void create(ProductContractDTO productContractDTO, SessionContext ctx) {

        //update open amount because this value depends of paymethod
        Integer payMethod = (Integer) productContractDTO.get("payMethod");
        BigDecimal price = (BigDecimal) productContractDTO.get("price");
        productContractDTO.put("openAmount", getOpenAmount(payMethod, price));

        ProductContract productContract =
                (ProductContract) ExtendedCRUDDirector.i.create(productContractDTO, resultDTO, false);

        if (SalesConstants.PayMethod.PartialFixed.getConstant() == productContract.getPayMethod()) {
            createPaySteps(productContract, ctx);
        }

        //define the next invoice date
        defineContractNextInvoiceDate(productContract);

        //Note freeText
        if (paramDTO.get("noteText") != null && paramDTO.get("noteText").toString().length() > 0) {
            Integer noteId = createSaleFreeText(paramDTO.get("noteText").toString(), productContract.getCompanyId());
            productContract.setNoteId(noteId);
        }

        if (toBeInvoiced(productContract.getContractTypeId())) {
            getCustomer(productContract.getAddressId());
        } else {
            getSupplier(productContract.getAddressId());
        }
    }

    private BigDecimal getOpenAmount(Integer payMethod, BigDecimal price) {
        if (SalesConstants.PayMethod.Periodic.getConstant() == payMethod) {
            return new BigDecimal(0.0);
        }

        return price;
    }

    private void createPaySteps(ProductContract productContract, SessionContext ctx) {
        List<PaymentStepDTO> paymentStepDTOs = getPaymentStepDTOs(productContract.getInstallment());
        for (PaymentStepDTO dto : paymentStepDTOs) {
            if (null != dto.get("payAmount")) {
                dto.put("contractId", productContract.getContractId());
                ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
