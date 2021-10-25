package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.domain.contactmanager.RecentPK;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * DTO of recents address updated by some user.
 *
 * @author Fernando Montaño
 * @version $Id: RecentDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */

public class RecentDTO extends ComponentDTO {

    public static final String KEY_RECENTID = "recentPK";

    /**
     * Creates an instance.
     */
    public RecentDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public RecentDTO(DTO dto) {
        super.putAll(dto);
    }

    public RecentDTO(RecentPK recentPK) {
        setPrimKey(recentPK);
    }

    public String getPrimKeyName() {
        return KEY_RECENTID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_RECENT;
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", "Recent");
    }

    public ComponentDTO createDTO() {
        return new RecentDTO();
    }

    public void parseValues() {
        //setting primary key
        setPrimKey(new RecentPK(new Integer(get("addressId").toString()), new Integer(get("userId").toString())));

    }
}