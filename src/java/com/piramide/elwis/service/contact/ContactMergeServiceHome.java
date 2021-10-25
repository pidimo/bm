package com.piramide.elwis.service.contact;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface ContactMergeServiceHome extends EJBLocalHome {
    com.piramide.elwis.service.contact.ContactMergeService create() throws CreateException;
}
