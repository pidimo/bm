/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.service.admin;

import javax.ejb.EJBLocalObject;

public interface DeleteCompanyService extends EJBLocalObject {
    public Boolean initializeSQLGenerator();

    public Boolean deleteCompany(Integer companyId);
}
