package com.piramide.elwis.service.contact;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface DeduplicationAddressServiceHome extends EJBLocalHome {
    com.piramide.elwis.service.contact.DeduplicationAddressService create() throws CreateException;
}
