/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.service.webmail;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface DownloadEmailServiceHome extends EJBLocalHome {
    DownloadEmailService create() throws CreateException;
}
