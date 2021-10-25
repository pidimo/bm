package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class MailAccountDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static String KEY_MAILACCOUNTID = "mailAccountId";

    public MailAccountDTO() {
    }

    public MailAccountDTO(DTO dto) {
        super.putAll(dto);
    }

    public MailAccountDTO(Integer primaryKey) {
        setPrimKey(primaryKey);
    }

    public ComponentDTO createDTO() {
        return new MailAccountDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_MAILACCOUNT;
    }

    public String getPrimKeyName() {
        return KEY_MAILACCOUNTID;
    }


    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("email"));
    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        String duplicatedMSG = get("email").toString() + " " + get("login").toString();
        resultDTO.addResultMessage("msg.Duplicated", duplicatedMSG);
    }

    public HashMap referencedValues() {
        HashMap referencedBy = new HashMap();
        referencedBy.put(WebMailConstants.TABLE_SIGNATURE, "mailaccountid");
        return referencedBy;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        String msg = get("email").toString() + " " + get("login").toString();
        resultDTO.addResultMessage("customMsg.Referenced", msg);
    }
}
