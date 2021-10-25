package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 8, 2005
 * Time: 4:07:21 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SchedulerFreeTextHome extends EJBLocalHome {

    public SchedulerFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;

    SchedulerFreeText findByPrimaryKey(Integer key) throws FinderException;
}

