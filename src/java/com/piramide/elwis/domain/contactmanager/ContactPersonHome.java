package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Represents ContactPerson home interface
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonHome.java 10428 2014-04-22 01:15:31Z miguel $
 */
public interface ContactPersonHome extends EJBLocalHome {

    ContactPerson create(ComponentDTO dto) throws CreateException;

    ContactPerson findByPrimaryKey(ContactPersonPK key) throws FinderException;

    ContactPerson findBySearchNameAndPersonType(String addressSearchName, Integer personTypeId, Integer companyId) throws FinderException;

    ContactPerson findBySearchNameAndNotPersonType(String addressSearchName, Integer personTypeId, Integer companyId) throws FinderException;

    Collection findByAddressAsContactPerson(Integer contactPersonId, Integer companyId) throws FinderException;

    Collection findByAdditionalAddressId(Integer additionalAddressId) throws FinderException;
}
