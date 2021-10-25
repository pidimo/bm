package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class InvoiceFreeNumberDTO extends ComponentDTO {
    public static String INVOICEFREENUMBERPK = "freeNumberId";

    public InvoiceFreeNumberDTO() {

    }

    public InvoiceFreeNumberDTO(DTO dto) {
        super.putAll(dto);
    }

    public InvoiceFreeNumberDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new InvoiceFreeNumberDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICEFREENUMBER;
    }

    public String getPrimKeyName() {
        return INVOICEFREENUMBERPK;
    }
}
