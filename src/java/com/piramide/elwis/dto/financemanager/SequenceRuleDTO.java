package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
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
public class SequenceRuleDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String SEQUENCERULEPK = "numberId";

    public SequenceRuleDTO() {
    }

    public SequenceRuleDTO(DTO dto) {
        super.putAll(dto);
    }

    public SequenceRuleDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new SequenceRuleDTO();
    }

    public String getJNDIName() {
        return FinanceConstants.JNDI_SEQUENCERULE;
    }

    public String getPrimKeyName() {
        return SEQUENCERULEPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_COMPANY, "creditnoteruleid, invoiceruleid");
        tables.put(FinanceConstants.TABLE_INVOICE, "sequenceruleid");
        tables.put(FinanceConstants.TABLE_INVOICEFREENUMBER, "sequenceruleid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("label"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("label"));
    }
}
