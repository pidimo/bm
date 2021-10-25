/**
 * @author Yumi
 * @version $Id: ContactHome.java 11332 2015-10-27 23:44:46Z miguel ${NAME}.java, v 2.0 13-may-2004 18:25:50 Yumi Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ContactHome extends EJBLocalHome {

    public Contact create(ComponentDTO dto) throws CreateException;

    Contact findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    Collection findByProcessId(Integer processId) throws FinderException;

    Integer selectMaxStartDate(Integer processId, Integer contactId) throws FinderException;

    Integer selectMaxContactIdByStartDate(Integer processId, Integer addressId, Integer dateStart) throws FinderException;

    Integer selectCountContactIdByAction(Integer contactId, Integer companyId) throws FinderException;

    Contact findByDocumentFreeTextId(Integer freeTextId) throws FinderException;

    Collection findByAddress(Integer addressId) throws FinderException;

    Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;

    Collection findByEmployeeId(Integer employeeId) throws FinderException;

    Collection findByAdditionalAddressId(Integer additionalAddressID) throws FinderException;

    Contact findByWebGenerateUUID(String webGenerateUUID) throws FinderException;
}
