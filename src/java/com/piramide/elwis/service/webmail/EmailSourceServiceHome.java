/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface EmailSourceServiceHome extends EJBLocalHome {
    EmailSourceService create() throws CreateException;
}
