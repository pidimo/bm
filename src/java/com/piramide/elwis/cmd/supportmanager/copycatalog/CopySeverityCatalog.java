package com.piramide.elwis.cmd.supportmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.supportmanager.SupportCaseSeverity;
import com.piramide.elwis.domain.supportmanager.SupportCaseSeverityHome;
import com.piramide.elwis.dto.supportmanager.SupportCaseSeverityDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopySeverityCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        SupportCaseSeverityHome severityHome =
                (SupportCaseSeverityHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE_SEVERTITY);

        Collection sourceElements = null;

        try {
            sourceElements = severityHome.findSupportCatalogByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read source SupportCaseSeverity from " + source.getCompanyId());
        }

        if (null != sourceElements) {
            for (Object obj : sourceElements) {
                SupportCaseSeverity sourceElement = (SupportCaseSeverity) obj;
                Integer targetLangTextId = CopyCatalogUtil.i.buildLangText(sourceElement.getLangTextId(),
                        target.getCompanyId());

                SupportCaseSeverityDTO targetElementDTO = new SupportCaseSeverityDTO();
                DTOFactory.i.copyToDTO(sourceElement, targetElementDTO);
                targetElementDTO.put("companyId", target.getCompanyId());
                targetElementDTO.put("langTextId", targetLangTextId);
                targetElementDTO.remove("severityId");

                try {
                    severityHome.create(targetElementDTO);
                } catch (CreateException e) {
                    log.debug("Cannot create target SupportCaseSeverity for " + target.getCompanyId());
                }
            }
        }
    }
}
