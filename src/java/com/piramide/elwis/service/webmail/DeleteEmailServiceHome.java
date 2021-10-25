/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface DeleteEmailServiceHome extends EJBLocalHome {
    DeleteEmailService create() throws CreateException;
}
