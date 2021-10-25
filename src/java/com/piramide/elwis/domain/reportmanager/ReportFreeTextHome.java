package com.piramide.elwis.domain.reportmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: ReportFreeTextHome.java 9695 2009-09-10 21:34:43Z fernando $
 */

public interface ReportFreeTextHome extends EJBLocalHome {
    public ReportFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;

    ReportFreeText findByPrimaryKey(Integer key) throws FinderException;
}
