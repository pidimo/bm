package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 5, 2005
 * Time: 11:49:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class HolidayDTO extends ComponentDTO {
    public ComponentDTO createDTO() {
        return new HolidayDTO();
    }

    public String getJNDIName() {
        return SchedulerConstants.JNDI_HOLIDAY;
    }

    public HolidayDTO() {
    }

    public HolidayDTO(DTO dto) {
        super(dto);
    }

    public String getPrimKeyName() {
        return "holidayId";
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("title") != null) {
            resultDTO.addResultMessage("msg.NotFound", get("title"));
        } else {
            resultDTO.addResultMessage("scheduler.Holiday.NotFound");
        }
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("title"));

    }
}
