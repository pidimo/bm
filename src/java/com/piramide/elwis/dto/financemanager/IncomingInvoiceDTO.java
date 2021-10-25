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
 * @version $Id: IncomingInvoiceDTO.java 18-feb-2009 12:54:47
 */
public class IncomingInvoiceDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String INCOMINGINVOICEPK = "incomingInvoiceId";

    public IncomingInvoiceDTO() {
    }

    public IncomingInvoiceDTO(DTO dto) {
        super.putAll(dto);
    }

    public IncomingInvoiceDTO(Integer incomingInvoiceId) {
        setPrimKey(incomingInvoiceId);
    }

    public ComponentDTO createDTO() {
        return new InvoiceDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INCOMINGINVOICE;
    }

    public String getPrimKeyName() {
        return INCOMINGINVOICEPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("invoiceNumber"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("invoiceNumber"));
    }
}
