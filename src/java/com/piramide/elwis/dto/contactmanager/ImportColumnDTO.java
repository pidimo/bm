package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * <code>DTO</code> Object for <code>ImportColumn</code> Entity Bean.
 *
 * @author Ivan Alban
 * @version 4.2.1
 */
public class ImportColumnDTO extends ComponentDTO {
    public static final String IMPORT_COLUMN_PK = "importColumnId";

    public ImportColumnDTO() {
    }

    public ImportColumnDTO(DTO dto) {
        super.putAll(dto);
    }

    public ImportColumnDTO(Integer id) {
        super.setPrimKey(id);
    }

    public ComponentDTO createDTO() {
        return new ImportColumnDTO();
    }


    public String getJNDIName() {
        return ContactConstants.JNDI_IMPORTCOLUMN;
    }

    public String getPrimKeyName() {
        return IMPORT_COLUMN_PK;
    }
}
