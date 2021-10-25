package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingPaymentDTO.java 18-feb-2009 13:04:43
 */
public class IncomingPaymentDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String INCOMINGPAYMENTPK = "paymentId";

    public IncomingPaymentDTO() {
    }

    public IncomingPaymentDTO(DTO dto) {
        super.putAll(dto);
    }

    public IncomingPaymentDTO(Integer incomingPaymentId) {
        setPrimKey(incomingPaymentId);
    }

    public ComponentDTO createDTO() {
        return new InvoiceDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INCOMINGPAYMENT;
    }

    public String getPrimKeyName() {
        return INCOMINGPAYMENTPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", String.valueOf(get("amount")));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("amount"));
    }
}
