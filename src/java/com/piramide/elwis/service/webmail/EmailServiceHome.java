/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface EmailServiceHome extends EJBLocalHome {
    EmailService create() throws CreateException;
}
