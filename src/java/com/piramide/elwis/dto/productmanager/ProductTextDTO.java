package com.piramide.elwis.dto.productmanager;

import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductTextDTO extends ComponentDTO {
    public static final String PRODUCTTEXTPK = "productTextPK";

    public ProductTextDTO() {
    }

    public ProductTextDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new ProductTextDTO();
    }

    public String getJNDIName() {
        return ProductConstants.JNDI_PRODUCTTEXT;
    }

    public String getPrimKeyName() {
        return PRODUCTTEXTPK;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("");
    }
}
