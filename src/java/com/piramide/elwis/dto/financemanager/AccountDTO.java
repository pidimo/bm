package com.piramide.elwis.dto.financemanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class AccountDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String ACCOUNTPK = "accountId";

    public AccountDTO() {
    }

    public AccountDTO(DTO dto) {
        super.putAll(dto);
    }

    public AccountDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new AccountDTO();
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_ACCOUNT;
    }

    public String getPrimKeyName() {
        return ACCOUNTPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ProductConstants.TABLE_PRODUCT, "accountid");
        tables.put(FinanceConstants.TABLE_INVOICEPOSITION, "accountid");
        tables.put(ContactConstants.TABLE_BANKACCOUNT, "accountid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }
}
