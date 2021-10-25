package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * @author Ivan
 * @version $Id: FavoriteDTO.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 22-may-2004 19:06:48 Ivan Exp $
 */

public class FavoriteDTO extends ComponentDTO {

    public static final String KEY_FAVORITELIST = "favoriteList";
    public static final String KEY_FAVORITEID = "favoriteId";

    /**
     * Creates an instance.
     */
    public FavoriteDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public FavoriteDTO(DTO dto) {
        super.putAll(dto);
    }


    public FavoriteDTO(Integer favoriteId) {
        setPrimKey(favoriteId);
    }

    public String getPrimKeyName() {
        return KEY_FAVORITEID;
    }

    public String getDTOListName() {
        return KEY_FAVORITELIST;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_FAVORITE;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        if (get("addressType") != null) { // if contain the address type  set not found message
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {
                if (get("name1") != null && get("name2") != null) {
                    resultDTO.addResultMessage("Favorite.duplicated", get("name1") + ", " + get("name2"));
                } else {
                    resultDTO.addResultMessage("msg.NotFound", "Contact");
                }
            } else {
                if (get("name1") != null) {
                    resultDTO.addResultMessage("Favorite.duplicated", get("name1") + " " +
                            ((get("name2") != null) ? get("name2") : "") + " " +
                            ((get("name3") != null) ? get("name3") : ""));
                } else {
                    resultDTO.addResultMessage("msg.NotFound", "Favorite");
                }
            }
        }
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "Favorite");
    }

    public ComponentDTO createDTO() {
        return new FavoriteDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }
}