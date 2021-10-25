package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public interface ReportQueryParamHome extends EJBLocalHome {
    public ReportQueryParam create(ComponentDTO dto) throws CreateException;
    com.piramide.elwis.domain.reportmanager.ReportQueryParam findByPrimaryKey(Integer key) throws FinderException;
}
