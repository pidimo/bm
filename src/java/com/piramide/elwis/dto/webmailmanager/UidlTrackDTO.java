package com.piramide.elwis.dto.webmailmanager;

import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class UidlTrackDTO extends ComponentDTO {
    public static final String UIDLTRACK_PK = "uidlTrackId";

    public UidlTrackDTO() {
    }

    public UidlTrackDTO(DTO dto) {
        super.putAll(dto);
    }

    public UidlTrackDTO(Integer uidlTrackId) {
        setPrimKey(uidlTrackId);
    }

    public ComponentDTO createDTO() {
        return new UidlTrackDTO();
    }

    public String getJNDIName() {
        return WebMailConstants.JNDI_UIDLTRACK;
    }

    public String getPrimKeyName() {
        return UIDLTRACK_PK;
    }
}
