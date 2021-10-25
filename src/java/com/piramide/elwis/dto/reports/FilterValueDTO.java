package com.piramide.elwis.dto.reports;

import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: FilterValueDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FilterValueDTO extends ComponentDTO {
    public static final String KEY_FILTERVALUEID = "filterValueId";

    public FilterValueDTO() {
    }

    public FilterValueDTO(DTO dto) {
        super.putAll(dto);
    }

    public FilterValueDTO(Integer key) {
        setPrimKey(key);
    }

    public ComponentDTO createDTO() {
        return new FilterValueDTO();
    }

    public String getJNDIName() {
        return ReportConstants.JNDI_FILTERVALUE;
    }

    public String getPrimKeyName() {
        return KEY_FILTERVALUEID;
    }

    public String getTableName() {
        return ReportConstants.TABLE_FILTERVALUE;
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        //resultDTO.addResultMessage("customMsg.NotFound", "algo.que.modificar.en.FilterValueDTO.addNotFoundMsgTo");
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        //resultDTO.addResultMessage("customMsg.Referenced", "algo.que.modificar.en.FilterValueDTO.addReferencedMsgTo");
    }
}
