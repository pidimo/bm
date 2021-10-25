package com.piramide.elwis.cmd.admin.copycatalog;

import com.piramide.elwis.domain.admin.Company;

import javax.ejb.SessionContext;

/**
 * @author: ivan
 */
public interface CopyCatalog {
    public void copyCatalog(Company source, Company target, SessionContext sessionContext);
}
