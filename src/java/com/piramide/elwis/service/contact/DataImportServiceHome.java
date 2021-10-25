/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.service.contact;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface DataImportServiceHome extends EJBLocalHome {
    com.piramide.elwis.service.contact.DataImportService create() throws CreateException;
}
