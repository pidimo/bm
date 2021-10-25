package com.piramide.elwis.dto.common.config;

import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * System constant DTO
 */
public class SystemConstantDTO extends ComponentDTO {
    public SystemConstantDTO(DTO dto) {
        super(dto);
    }

    public SystemConstantDTO(String name, String value, String resourceKey, String type) {
        put("name", name);
        put("value", value);
        put("resourceKey", resourceKey);
        put("type", type);
    }

    public ComponentDTO createDTO() {
        return null;
    }

    public String getJNDIName() {
        return null;
    }

    public String getPrimKeyName() {
        return null;
    }
}
