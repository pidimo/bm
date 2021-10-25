package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface ReportHome extends EJBLocalHome {
    Report findByPrimaryKey(Integer key) throws FinderException;

    public Report create(ComponentDTO dto) throws CreateException;

    Collection findByEmployeeId(Integer employeeId) throws FinderException;

}
