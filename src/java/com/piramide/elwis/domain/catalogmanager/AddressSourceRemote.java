package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: AddressSourceRemote.java 9121 2009-04-17 00:28:59Z fernando $
 */

public interface AddressSourceRemote extends EJBObject {
    Integer getAddressSourceId() throws RemoteException;

    void setAddressSourceId(Integer addressSourceId) throws RemoteException;

    String getAddressSourceName() throws RemoteException;

    void setAddressSourceName(String name) throws RemoteException;

    Integer getCompanyId() throws RemoteException;

    void setCompanyId(Integer companyId) throws RemoteException;

    Integer getVersion() throws RemoteException;

    void setVersion(Integer version) throws RemoteException;
}
