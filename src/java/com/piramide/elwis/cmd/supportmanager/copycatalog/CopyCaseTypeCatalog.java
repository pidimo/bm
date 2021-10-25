package com.piramide.elwis.cmd.supportmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.supportmanager.SupportCaseType;
import com.piramide.elwis.domain.supportmanager.SupportCaseTypeHome;
import com.piramide.elwis.dto.supportmanager.SupportCaseTypeDTO;
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
public class CopyCaseTypeCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        SupportCaseTypeHome supportCaseTypeHome =
                (SupportCaseTypeHome) EJBFactory.i.getEJBLocalHome(SupportConstants.JNDI_SUPPORT_CASE_TYPE);
        Collection sourceElements = null;
        try {
            sourceElements = supportCaseTypeHome.findSupportCatalogByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read source support case types  for " + source.getCompanyId());
        }

        if (null != sourceElements) {
            for (Object obj : sourceElements) {
                SupportCaseType sourceElement = (SupportCaseType) obj;

                Integer targetLangTextId = CopyCatalogUtil.i.buildLangText(sourceElement.getLangTextId(),
                        target.getCompanyId());

                SupportCaseTypeDTO targetElementDTO = new SupportCaseTypeDTO();

                DTOFactory.i.copyToDTO(sourceElement, targetElementDTO);
                targetElementDTO.put("companyId", target.getCompanyId());
                targetElementDTO.put("langTextId", targetLangTextId);
                targetElementDTO.remove("caseTypeId");

                try {
                    supportCaseTypeHome.create(targetElementDTO);
                } catch (CreateException e) {
                    log.debug("Cannot create Support Case Type for target company " + target.getCompanyId());
                }
            }
        }
    }
}
