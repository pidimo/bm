/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface SentEmailServiceHome extends EJBLocalHome {
    SentEmailService create() throws CreateException;
}
