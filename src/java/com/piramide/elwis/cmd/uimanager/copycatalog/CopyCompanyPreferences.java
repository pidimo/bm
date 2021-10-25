package com.piramide.elwis.cmd.uimanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.domain.admin.AdminFreeText;
import com.piramide.elwis.domain.admin.AdminFreeTextHome;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyCompanyPreferences implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        AdminFreeText sourceLogo = source.getLogo();
        if (null != sourceLogo) {
            try {
                AdminFreeText targetLogo = createFreeText(sourceLogo.getValue(),
                        target.getCompanyId(),
                        sourceLogo.getType());
                target.setLogo(targetLogo);
            } catch (CreateException e) {
                log.debug("Cannot create targetLogo for companyId = " + target.getCompanyId());
            }
        }

        AdminFreeText sourceRoutePage = source.getRoutePage();
        if (null != sourceRoutePage) {
            try {
                AdminFreeText targetRoutePage = createFreeText(sourceRoutePage.getValue(),
                        target.getCompanyId(),
                        sourceRoutePage.getType());
                target.setRoutePage(targetRoutePage);
            } catch (CreateException e) {
                log.debug("Cannot create target route page  for companyId=" + target.getCompanyId());
            }
        }

        target.setMaxAttachSize(source.getMaxAttachSize());
        target.setRowsPerPage(source.getRowsPerPage());
        target.setTimeout(source.getTimeout());
    }

    private AdminFreeText createFreeText(byte[] file, Integer companyId, Integer type) throws CreateException {
        AdminFreeTextHome freeTextHome = (AdminFreeTextHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ADMIN_FREETEXT);
        return freeTextHome.create(file, companyId, type);
    }
}
