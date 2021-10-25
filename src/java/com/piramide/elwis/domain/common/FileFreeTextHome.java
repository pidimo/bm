package com.piramide.elwis.domain.common;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Mar 16, 2005
 * Time: 10:20:49 AM
 * To change this template use File | Settings | File Templates.
 */

public interface FileFreeTextHome extends EJBLocalHome {
    public FileFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;

    FileFreeText findByPrimaryKey(Integer key) throws FinderException;

    FileFreeText findByPrimaryKeyAndType(Integer key, Integer type) throws FinderException;
}
