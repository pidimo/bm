package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.domain.contactmanager.DuplicateAddressPK;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DuplicateAddressDTO extends ComponentDTO {
    public static final String KEY_DUPLICATEADDRESSID = "duplicateAddressPK";

    public DuplicateAddressDTO() {
    }

    public DuplicateAddressDTO(DTO dto) {
        super.putAll(dto);
    }

    public DuplicateAddressDTO(DuplicateAddressPK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new DuplicateAddressDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_DUPLICATEADDRESS;
    }

    public String getPrimKeyName() {
        return KEY_DUPLICATEADDRESSID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_DUPLICATEADDRESS;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }
}
