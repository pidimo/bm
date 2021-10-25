package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Titus
 * @version BankAccountDTO.java, v 2.0 May 10, 2004 10:58:05 AM
 */
public class BankAccountDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_BANKACCOUNTID = "bankAccountId";

    /**
     * Creates an instance.
     */
    public BankAccountDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public BankAccountDTO(DTO dto) {
        super.putAll(dto);
    }

    public BankAccountDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_BANKACCOUNTID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_BANKACCOUNT;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("BankAccount.Duplicated", get("accountNumber"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("accountNumber"));
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICEPAYMENT, "bankaccountid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("accountNumber"));
    }

    public ComponentDTO createDTO() {
        return new DepartmentDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();

    }


}
