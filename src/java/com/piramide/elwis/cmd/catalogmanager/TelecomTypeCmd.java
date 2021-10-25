package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.domain.contactmanager.TelecomTypeHome;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TelecomTypeCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        String op = getOp();
        if ("readTelecomTypesByCompany".equals(op)) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            readTelecomTypesByCompany(companyId);
        }
        if ("readTelecomType".equals(op)) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            Integer telecomTypeId = (Integer) paramDTO.get("telecomTypeId");
            readTelecomType(companyId, telecomTypeId);
        }
    }

    private void readTelecomType(Integer companyId, Integer telecomTypeId) {
        TelecomTypeHome home =
                (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

        try {
            TelecomType telecomType = home.findByPrimaryKey(telecomTypeId);
            TelecomTypeDTO dto = new TelecomTypeDTO();
            DTOFactory.i.copyToDTO(telecomType, dto);
            log.debug("-> Read TelecomType telecomTypeId=" + telecomTypeId + " OK");
            resultDTO.put("telecomType", dto);
        } catch (FinderException e) {
            log.debug("-> Read TelecomType telecomTypeId=" + telecomTypeId + " Fail");
            resultDTO.put("telecomType", null);
        }
    }

    private void readTelecomTypesByCompany(Integer companyId) {
        TelecomTypeHome home =
                (TelecomTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

        List<TelecomTypeDTO> telecomTypesDTOs = new ArrayList<TelecomTypeDTO>();

        try {
            Collection telecomTypes = home.findByCompanyId(companyId);
            for (Object object : telecomTypes) {
                TelecomTypeDTO dto = new TelecomTypeDTO();
                TelecomType telecomType = (TelecomType) object;
                DTOFactory.i.copyToDTO(telecomType, dto);
                telecomTypesDTOs.add(dto);
            }
            log.debug("-> Read All TelecomTypes for companyId=" + companyId + " OK");
        } catch (FinderException e) {
            log.debug("-> The company has no registered TelecomTypes");
        }

        resultDTO.put("telecomTypes", telecomTypesDTOs);

    }

    public boolean isStateful() {
        return false;
    }
}