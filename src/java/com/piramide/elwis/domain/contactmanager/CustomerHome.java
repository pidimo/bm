package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Represents Customer local home interface
 *
 * @author Fernando Montaño
 * @version $Id: CustomerHome.java 10484 2014-08-28 22:51:28Z miguel $
 */
public interface CustomerHome extends EJBLocalHome {

    public Customer create(ComponentDTO dto) throws CreateException;

    Customer findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    /*Collection findByCustomerCategoryValues(Integer categoryId) throws FinderException;*/

    Collection findByEmployeeIdAndCompanyId(Integer employeeId, Integer companyId) throws FinderException;

    String selectMaxCustomerNumber(Integer companyId) throws FinderException;

    Customer findByCustomerNumber(String customerNumber, Integer companyId) throws FinderException;

    Collection findByPartnerId(Integer partnerId) throws FinderException;

    Collection findByInvoiceAddress(Integer invoiceAddressId) throws FinderException;

    Collection findByInvoiceContactPerson(Integer invoiceAddressId, Integer invoiceContactPersonId) throws FinderException;

    Collection findByEmployeeId(Integer employeeId) throws FinderException;

    Collection findByAdditionalAddressId(Integer additionalAddressId) throws FinderException;

    Collection findCustomerIdByEmployeeIdCompanyId(Integer employeeId, Integer companyId) throws FinderException;
}
