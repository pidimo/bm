package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * interface to access bean
 *
 * @author ernesto
 * @version EmployeeBean.java, v 2.0 Apr 23, 2004 11:24:32 AM
 */
public interface EmployeeHome extends EJBLocalHome {

    Employee create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.Employee findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCompanyId(Integer companyId) throws FinderException;

    Collection findByHealthFundId(Integer healthFundId) throws FinderException;

}
