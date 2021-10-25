package com.piramide.elwis.cmd.supportmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.Priority;
import com.piramide.elwis.domain.catalogmanager.PriorityHome;
import com.piramide.elwis.dto.catalogmanager.PriorityDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author: ivan
 */
public class CopyPriorityCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        PriorityHome priorityHome =
                (PriorityHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRIORITY);

        Collection sourcePriorities = null;
        try {
            sourcePriorities = priorityHome.findPriorityByType(source.getCompanyId(), "SUPPORT");
        } catch (FinderException e) {
            log.warn("Cannot read support priorities of source company");
        }
        if (null != sourcePriorities) {
            for (Object obj : sourcePriorities) {
                Priority sourcePriority = (Priority) obj;
                Integer targetLangTextId = CopyCatalogUtil.i.buildLangText(sourcePriority.getLangTextId(),
                        target.getCompanyId());

                PriorityDTO dto = new PriorityDTO();
                DTOFactory.i.copyToDTO(sourcePriority, dto);
                dto.put("companyId", target.getCompanyId());
                dto.put("langTextId", targetLangTextId);
                ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
            }
        }
    }
}
