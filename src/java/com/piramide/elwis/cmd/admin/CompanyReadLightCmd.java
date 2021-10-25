package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Cmd to read basic company info
 *
 * @author Miky
 * @version $Id: CompanyReadLightCmd.java  21-dic-2010 16:39:14$
 */
public class CompanyReadLightCmd  extends EJBCommand {

    private Log log = LogFactory.getLog(CompanyReadLightCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read company command ... ");
        log.debug("companyId to read = " + paramDTO.get("companyId"));
        CompanyDTO companyDTO = new CompanyDTO();
        companyDTO.putAll(paramDTO);
        Company company = (Company) ExtendedCRUDDirector.i.read(companyDTO, resultDTO, false);
    }

    public boolean isStateful() {
        return false;
    }
}
