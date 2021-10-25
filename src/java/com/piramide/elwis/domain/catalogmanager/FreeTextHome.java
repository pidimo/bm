package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * This Class represents the Home interface of FreeText Entity Bean
 *
 * @author Ivan
 * @version $Id: FreeTextHome.java 2204 2004-08-11 20:26:32Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface FreeTextHome extends EJBLocalHome {
    public FreeText create(byte[] text, Integer companyId, Integer type) throws CreateException;

    com.piramide.elwis.domain.catalogmanager.FreeText findByPrimaryKey(Integer key) throws FinderException;

}
