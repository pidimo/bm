package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.schedulermanager.Holiday;
import com.piramide.elwis.dto.schedulermanager.HolidayDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;


public class HolidayCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        super.setOp(this.getOp());
        checkDuplicate = false;
        if ("create".equals(getOp()) || "update".equals(getOp())) {
            paramDTO.put("moveToMonday", new Boolean(paramDTO.getAsBool("moveToMonday")));
            String prefix = "A";
            if ("1".equals(paramDTO.get("holidayType"))) {
                prefix = "B";
            }
            paramDTO.put("day", paramDTO.get("day" + prefix));
            paramDTO.put("month", paramDTO.get("month" + prefix));
            paramDTO.put("occurrence", paramDTO.get("occurrence" + prefix));
        }

        Holiday holiday = (Holiday) execute(ctx, new HolidayDTO(paramDTO));

        if (holiday != null && ("".equals(getOp()) || ("update".equals(getOp()) && resultDTO.isFailure()))) {
            String prefix = "A";
            if ("1".equals(resultDTO.get("holidayType").toString())) {
                prefix = "B";
            }
            resultDTO.put("day" + prefix, resultDTO.get("day"));
            resultDTO.put("month" + prefix, resultDTO.get("month"));
            resultDTO.put("occurrence" + prefix, resultDTO.get("occurrence"));
        }
    }
}