/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SaveEmailServiceHome extends EJBLocalHome {
    SaveEmailService create() throws CreateException;
}
