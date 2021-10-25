package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class PaymentStepDTO extends ComponentDTO {
    public static final String PAYMENTSTEPPK = "payStepId";

    public PaymentStepDTO() {
    }

    public PaymentStepDTO(DTO dto) {
        super.putAll(dto);
    }

    public PaymentStepDTO(Integer payStepId) {
        setPrimKey(payStepId);
    }

    public ComponentDTO createDTO() {
        return new PaymentStepDTO();
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_PAYMENTSTEP;
    }

    public String getPrimKeyName() {
        return PAYMENTSTEPPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("PaymentStep.msg.notFound");
    }
}
