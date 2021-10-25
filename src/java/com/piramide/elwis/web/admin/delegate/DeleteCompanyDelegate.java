package com.piramide.elwis.web.admin.delegate;

import com.piramide.elwis.service.admin.DeleteCompanyService;
import com.piramide.elwis.service.admin.DeleteCompanyServiceHome;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class DeleteCompanyDelegate {
    private Log log = LogFactory.getLog(DeleteCompanyDelegate.class);

    public static DeleteCompanyDelegate i = new DeleteCompanyDelegate();


    private DeleteCompanyDelegate() {
    }

    private DeleteCompanyService getService() {
        DeleteCompanyServiceHome home =
                (DeleteCompanyServiceHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_DELETECOMPANYSERVICE);

        try {
            return home.create();
        } catch (CreateException e) {
            log.debug("-> Create DeleteCompanyService FAIL ", e);
        }

        return null;
    }

    public Boolean initialize() {
        DeleteCompanyService service = getService();
        return service.initializeSQLGenerator();
    }

    public Boolean deleteCompany(Integer companyId) {
        DeleteCompanyService service = getService();
        return service.deleteCompany(companyId);
    }
}
