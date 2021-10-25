package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author: ivan
 * <p/>
 * Jatun S.R.L
 */
public class InvoicePaymentDTO extends ComponentDTO {
    public static final String INVOICEPAYMENTPK = "paymentId";

    public InvoicePaymentDTO() {
    }

    public InvoicePaymentDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new InvoicePaymentDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICEPAYMENT;
    }

    public String getPrimKeyName() {
        return INVOICEPAYMENTPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("InvoicePayment.notfound");
    }
}
