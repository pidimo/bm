package com.piramide.elwis.domain.reportmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface ColumnTotalizeHome extends EJBLocalHome {
    public ColumnTotalize create(ColumnTotalizePK pk, Integer companyId) throws CreateException;

    ColumnTotalize findByPrimaryKey(ColumnTotalizePK key) throws FinderException;
}
