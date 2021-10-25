/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface UtilServiceHome extends EJBLocalHome {
    UtilService create() throws CreateException;
}
