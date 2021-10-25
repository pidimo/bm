package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTextDTO extends ComponentDTO {
    public static final String INVOICETEXTPK = "invoiceTextPK";

    public InvoiceTextDTO() {
    }

    public InvoiceTextDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new InvoiceTextDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICETEXT;
    }

    public String getPrimKeyName() {
        return INVOICETEXTPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("TemplateFile.error.find");
    }
}
