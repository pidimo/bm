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
public class InvoiceTemplateDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String INVOICETEMPLATEPK = "templateId";

    public InvoiceTemplateDTO() {
    }

    public InvoiceTemplateDTO(DTO dto) {
        super.putAll(dto);
    }

    public InvoiceTemplateDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new InvoiceTemplateDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_INVOICETEMPLATE;
    }

    public String getPrimKeyName() {
        return INVOICETEMPLATEPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICE, "templateid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("title"));
    }
}
