package com.piramide.elwis.domain.supportmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 3:29:00 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportFreeTextHome extends EJBLocalHome {

    public SupportFreeText create(byte[] freeTextValue, Integer companyId, Integer freeTextType) throws CreateException;

    SupportFreeText findByPrimaryKey(Integer key) throws FinderException;

}
