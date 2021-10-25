package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.domain.supportmanager.UserArticleAccessPK;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public class UserArticleAccessDTO extends ComponentDTO {
    public static final String KEY_USERARTICLEACCESSID = "userArticleAccessPK";

    public UserArticleAccessDTO() {
    }

    public UserArticleAccessDTO(DTO dto) {
        super.putAll(dto);
    }

    public UserArticleAccessDTO(UserArticleAccessPK key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new UserArticleAccessDTO();
    }

    public String getJNDIName() {
        return SupportConstants.JNDI_USERARTICLEACCESS;
    }

    public String getPrimKeyName() {
        return KEY_USERARTICLEACCESSID;
    }

    public String getTableName() {
        return SupportConstants.TABLE_USERARTICLEACCESS;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
