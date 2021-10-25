package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;
import java.rmi.RemoteException;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: AddressSourceRemoteHome.java 9121 2009-04-17 00:28:59Z fernando $
 */

public interface AddressSourceRemoteHome extends EJBHome {
    AddressSourceRemote create(ComponentDTO dto) throws CreateException, RemoteException;

    AddressSourceRemote findByPrimaryKey(Integer key) throws FinderException, RemoteException;

    AddressSourceRemote findAll() throws FinderException, RemoteException;
}
