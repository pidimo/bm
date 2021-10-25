package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface ColumnGroupHome extends EJBLocalHome {

    public ColumnGroup create(ComponentDTO dto) throws CreateException;

    ColumnGroup findByPrimaryKey(Integer key) throws FinderException;

    ColumnGroup findByColumnKey(Integer companyId, Integer columnId) throws FinderException;
}
