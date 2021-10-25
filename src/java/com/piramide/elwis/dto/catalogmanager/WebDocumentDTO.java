package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5
 */
public class WebDocumentDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_WEBDOCUMENTID = "webDocumentId";

    public WebDocumentDTO() {
    }

    public WebDocumentDTO(DTO dto) {
        super.putAll(dto);
    }

    public WebDocumentDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new WebDocumentDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_WEBDOCUMENT;
    }

    public String getPrimKeyName() {
        return KEY_WEBDOCUMENTID;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_WEBDOCUMENT;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CONTACT, "webdocumentid");
        return tables;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

}
