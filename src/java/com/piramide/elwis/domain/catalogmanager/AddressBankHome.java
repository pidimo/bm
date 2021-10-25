package com.piramide.elwis.domain.catalogmanager;

/**
 * @author Ivan
 * @version $Id: AddressBankHome.java 1066 2004-06-05 14:20:27Z mauren ${NAME}.java, v 2.0 28-abr-2004 11:58:50 Ivan Exp $
 */

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface AddressBankHome extends EJBLocalHome {
    public AddressBank create(ComponentDTO dto) throws CreateException;

    AddressBank findByPrimaryKey(Integer key) throws FinderException;
}
