package com.piramide.elwis.dto.common;

import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: AttachmentDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class AttachmentDTO extends ComponentDTO {
    public static final String KEY_ATTACHMENTID = "attachmentId";

    public AttachmentDTO() {
    }

    public AttachmentDTO(DTO dto) {
        super.putAll(dto);
    }

    public AttachmentDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new AttachmentDTO();
    }

    public String getJNDIName() {
        return Constants.JNDI_ATTACHMENT;
    }

    public String getPrimKeyName() {
        return KEY_ATTACHMENTID;
    }

    public String getTableName() {
        return Constants.TABLE_ATTACHMENT;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {

    }

}
