package com.piramide.elwis.dto.salesmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
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
public class SaleDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String SALEPK = "saleId";

    public SaleDTO() {
    }

    public SaleDTO(DTO dto) {
        super.putAll(dto);
    }

    public SaleDTO(Integer saleId) {
        setPrimKey(saleId);
    }

    public ComponentDTO createDTO() {
        return new SaleDTO();
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_SALE;
    }

    public String getPrimKeyName() {
        return SALEPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (null != get("title") && !"".equals(get("title").toString().trim())) {
            resultDTO.addResultMessage("msg.NotFound", get("title"));
        } else {
            resultDTO.addResultMessage("Sale.NotFound");
        }
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));
    }
}
