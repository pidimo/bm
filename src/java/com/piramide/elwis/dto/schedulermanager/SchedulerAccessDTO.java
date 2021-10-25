package com.piramide.elwis.dto.schedulermanager;

import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class SchedulerAccessDTO extends ComponentDTO {
    public static final String KEY_SCHEDULER_ACCESS = "schedulerAccessPK";

    public SchedulerAccessDTO() {
    }

    public SchedulerAccessDTO(DTO dto) {
        super.putAll(dto);
    }

    @Override
    public ComponentDTO createDTO() {
        return new SchedulerAccessDTO();
    }

    @Override
    public String getJNDIName() {
        return SchedulerConstants.JNDI_SCHEDULERACCESS;
    }

    @Override
    public String getPrimKeyName() {
        return KEY_SCHEDULER_ACCESS;
    }
}
