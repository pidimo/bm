package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.dto.catalogmanager.BankDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * @author: ivan
 */
public class CopyBankCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Copy Bank catalog...");
        CopyCatalogUtil.i.copyCatalog(source, target, new BankDTO());
    }
}
