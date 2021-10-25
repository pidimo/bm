package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: FilterDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FilterDTO extends ComponentDTO {
    public static final String KEY_FILTERID = "filterId";

    public FilterDTO() {
    }

    public FilterDTO(DTO dto) {
        super.putAll(dto);
    }

    public FilterDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new FilterDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_FILTER;
    }

    public String getPrimKeyName() {
        return KEY_FILTERID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_FILTER;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("aliasCondition"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("aliasCondition"));
    }
}
