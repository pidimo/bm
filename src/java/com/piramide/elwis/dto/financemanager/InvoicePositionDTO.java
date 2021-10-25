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
public class InvoicePositionDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String INVOICEPOSITIONPK = "positionId";

    public InvoicePositionDTO() {
    }

    public InvoicePositionDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new InvoicePositionDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICEPOSITION;
    }

    public String getPrimKeyName() {
        return INVOICEPOSITIONPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("productName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("InvoicePosition.Referenced");
    }
}
