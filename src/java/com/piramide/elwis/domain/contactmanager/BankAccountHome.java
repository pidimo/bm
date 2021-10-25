/**
 * @author Fernando Montaño   15:52:47
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface BankAccountHome extends EJBLocalHome {
    BankAccount create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.contactmanager.BankAccount findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCompanyId(Integer companyId, Integer addressId) throws FinderException;

    Collection findByAddressId(Integer addressId) throws FinderException;
}
