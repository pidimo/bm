package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AddressGroupHome.java 10417 2014-04-09 02:17:26Z miguel ${NAME}, 14-03-2005 02:22:45 PM alvaro Exp $
 */

public interface AddressGroupHome extends EJBLocalHome {
    AddressGroup findByPrimaryKey(Integer key) throws FinderException;

    public AddressGroup create(ComponentDTO dto) throws CreateException;

    public Collection findByAddressId(Integer addressId) throws FinderException;

    public Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;
}
