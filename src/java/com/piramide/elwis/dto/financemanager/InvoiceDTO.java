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
 * @author Ivan
 */
public class InvoiceDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String INVOICEPK = "invoiceId";

    public InvoiceDTO() {
    }

    public InvoiceDTO(DTO dto) {
        super.putAll(dto);
    }

    public InvoiceDTO(Integer invoiceId) {
        setPrimKey(invoiceId);
    }

    public ComponentDTO createDTO() {
        return new InvoiceDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICE;
    }

    public String getPrimKeyName() {
        return INVOICEPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICE, "creditnoteofid");
        return tables;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("number"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("number"));
    }
}
