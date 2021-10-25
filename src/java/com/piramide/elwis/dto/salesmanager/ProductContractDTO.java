package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.FinanceConstants;
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
public class ProductContractDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String PRODUCTCONTRACTPK = "contractId";

    public ProductContractDTO() {
    }

    public ProductContractDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProductContractDTO(Integer contractId) {
        setPrimKey(contractId);
    }

    public ComponentDTO createDTO() {
        return new ProductContractDTO();
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_PRODUCTCONTRACT;
    }

    public String getPrimKeyName() {
        return PRODUCTCONTRACTPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICEPOSITION, "contractid");
        return tables;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("ProductContract.NotFound");
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (null != get("addressName")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("addressName"));
        } else if (null != get("productName")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
        } else {
            resultDTO.addResultMessage("msg.Referenced");
        }
    }
}
