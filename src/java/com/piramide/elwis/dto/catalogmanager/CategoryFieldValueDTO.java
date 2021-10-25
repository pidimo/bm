package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Author: ivan
 * Date: Oct 4, 2006 - 3:17:59 PM
 */
public class CategoryFieldValueDTO extends ComponentDTO {
    public static final String KEY_CATEGORYFIELDVALUEID = "fieldValueId";

    public CategoryFieldValueDTO() {
    }

    public CategoryFieldValueDTO(DTO dto) {
        super.putAll(dto);
    }

    public CategoryFieldValueDTO(Integer key) {
        this.setPrimKey(key);
    }


    public ComponentDTO createDTO() {
        return null;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CATEGORYFIELDVALUE;
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYFIELDVALUEID;
    }
}
