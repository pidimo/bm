package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: FolderDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FolderDTO extends ComponentDTO {
    public final static String KEY_FOLDERID = "folderId";

    public FolderDTO() {

    }

    public FolderDTO(DTO dto) {
        super.putAll(dto);
    }

    public FolderDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new FolderDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_FOLDER;
    }

    public String getPrimKeyName() {
        return KEY_FOLDERID;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("folderName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("folderName"));
    }
}
