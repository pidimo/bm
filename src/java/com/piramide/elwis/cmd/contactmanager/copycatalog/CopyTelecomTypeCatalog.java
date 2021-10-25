package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.contactmanager.TelecomType;
import com.piramide.elwis.domain.contactmanager.TelecomTypeHome;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: ivan
 */
public class CopyTelecomTypeCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Copy telecom type catalog.");
        TelecomTypeHome telecomTypeHome = (TelecomTypeHome)
                EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TELECOMTYPE);

        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        Collection sourceTelecomTypes = null;
        try {
            sourceTelecomTypes = telecomTypeHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot read telecom types of source company.");
        }


        Collection targetTelecomTypes = null;
        try {
            targetTelecomTypes = telecomTypeHome.findByCompanyId(target.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read telecom types of target company.");
        }

        Collection newSourceTelecomTypes = new ArrayList();
        if (null != targetTelecomTypes && null != sourceTelecomTypes) {
            for (Object obj : sourceTelecomTypes) {
                TelecomType sourceTelecomType = (TelecomType) obj;
                if (!exists(sourceTelecomType.getTelecomTypeName(), targetTelecomTypes)) {
                    newSourceTelecomTypes.add(sourceTelecomType);
                }
            }
        }

        if (!newSourceTelecomTypes.isEmpty()) {
            for (Object obj : newSourceTelecomTypes) {
                TelecomType sourceTelecomType = (TelecomType) obj;
                Integer targetLangTextId = CopyCatalogUtil.i.buildLangText(sourceTelecomType.getLangTextId(), target.getCompanyId());

                LangText defaultTargetLangText = null;

                try {
                    defaultTargetLangText = langTextHome.findByIsDefault(targetLangTextId);
                } catch (FinderException e) {
                    log.debug("Cannot  read defaultLangText in target company");
                }

                TelecomTypeDTO dto = new TelecomTypeDTO();
                DTOFactory.i.copyToDTO(sourceTelecomType, dto);
                dto.put("companyId", target.getCompanyId());
                dto.remove("telecomTypeId");
                dto.put("langTextId", targetLangTextId);
                if (null != defaultTargetLangText) {
                    dto.put("telecomTypeName", defaultTargetLangText.getText());
                }

                ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
            }
        }
    }


    private boolean exists(String telecomTypeName, Collection telecomTypes) {
        for (Object obj : telecomTypes) {
            TelecomType telecomType = (TelecomType) obj;
            if (telecomType.getTelecomTypeName().equals(telecomTypeName)) {
                return true;
            }
        }
        return false;
    }
}
