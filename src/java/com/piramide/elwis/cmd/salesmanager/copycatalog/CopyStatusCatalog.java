package com.piramide.elwis.cmd.salesmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.dto.salesmanager.StatusDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author: ivan
 */
public class CopyStatusCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        CopyCatalogUtil.i.copyCatalog(source, target, new StatusDTO());
    }
}
