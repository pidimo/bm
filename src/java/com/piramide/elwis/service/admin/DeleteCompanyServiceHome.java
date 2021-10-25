/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.service.admin;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface DeleteCompanyServiceHome extends EJBLocalHome {
    com.piramide.elwis.service.admin.DeleteCompanyService create() throws CreateException;
}
