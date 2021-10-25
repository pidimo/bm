package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceVatDTO extends ComponentDTO {
    public static final String INVOICEVATID = "invoiceVatPK";

    public InvoiceVatDTO() {

    }

    public InvoiceVatDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new InvoiceVatDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICEVAT;
    }

    public String getPrimKeyName() {
        return INVOICEVATID;
    }
}
