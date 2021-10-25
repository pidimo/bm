package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.domain.contactmanager.UserAddressAccessPK;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public class UserAddressAccessDTO extends ComponentDTO {
    public static final String KEY_USERADRESSACCESSID = "userAddressAccessPK";

    public UserAddressAccessDTO() {
    }

    public UserAddressAccessDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserAddressAccessDTO(UserAddressAccessPK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new UserAddressAccessDTO();
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_USERADDRESSACCESS;
    }

    public String getPrimKeyName() {
        return KEY_USERADRESSACCESSID;
    }

    public String getTableName() {
        return ContactConstants.TABLE_USERADDRESSACCESS;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
