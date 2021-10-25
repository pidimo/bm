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
public class ContractTypeDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String CONTRACTTYPEPK = "contractTypeId";

    public ContractTypeDTO() {
    }

    public ContractTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public ContractTypeDTO(Integer id) {
        setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new ContractTypeDTO();
    }

    public String getJNDIName() {
        return SalesConstants.JNDI_CONTRACTTYPE;
    }

    public String getPrimKeyName() {
        return CONTRACTTYPEPK;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "contracttypeid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }
}
