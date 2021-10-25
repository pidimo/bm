package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class PayConditionTextDTO extends ComponentDTO {
    public static final String PAYCONDITIONPK = "payConditionPK";

    public PayConditionTextDTO() {
    }

    public PayConditionTextDTO(DTO dto) {
        super.putAll(dto);
    }


    public ComponentDTO createDTO() {
        return new PayConditionDTO();
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PAYCONDITIONTEXT;
    }

    public String getPrimKeyName() {
        return PAYCONDITIONPK;
    }
}
