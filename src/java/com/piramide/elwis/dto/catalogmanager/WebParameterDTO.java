package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebParameterDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_WEBPARAMETERID = "webParameterId";

    public WebParameterDTO() {
    }

    public WebParameterDTO(DTO dto) {
        super.putAll(dto);
    }

    public WebParameterDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new WebParameterDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_WEBPARAMETER;
    }

    public String getPrimKeyName() {
        return KEY_WEBPARAMETERID;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_WEBPARAMETER;
    }

    public HashMap referencedValues() {
        return new HashMap();
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Referenced");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("generalMsg.NotFound");
    }

}
