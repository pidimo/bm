package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class CategoryRelationDTO extends ComponentDTO {

    public static final String KEY_CATEGORYRELATION = "categoryRelationPK";

    public CategoryRelationDTO() {
    }

    public CategoryRelationDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new CategoryRelationDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CATEGORYRELATION;
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYRELATION;
    }
}
