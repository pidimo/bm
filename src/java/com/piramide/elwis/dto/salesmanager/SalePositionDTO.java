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
public class SalePositionDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String SALEPOSITIONPK = "salePositionId";

    public SalePositionDTO() {
    }

    public SalePositionDTO(DTO dto) {
        super.putAll(dto);
    }

    public SalePositionDTO(Integer salePositionId) {
        setPrimKey(salePositionId);
    }

    public ComponentDTO createDTO() {
        return new SalePositionDTO();
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_SALEPOSITION;
    }

    public String getPrimKeyName() {
        return SALEPOSITIONPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (null != get("productName")) {
            resultDTO.addResultMessage("msg.NotFound", get("productName"));
        } else if (null != get("customerName")) {
            resultDTO.addResultMessage("msg.NotFound", get("customerName"));
        } else {
            resultDTO.addResultMessage("generalMsg.NotFound");
        }
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICEPOSITION, "salepositionid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (null != get("productName")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
        } else if (null != get("customerName")) {
            resultDTO.addResultMessage("customMsg.Referenced", get("customerName"));
        } else {
            resultDTO.addResultMessage("msg.Referenced");
        }
    }
}
