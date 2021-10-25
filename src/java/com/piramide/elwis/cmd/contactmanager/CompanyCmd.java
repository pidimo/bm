package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.FreeTextCmdUtil;
import com.piramide.elwis.domain.admin.AdminFreeText;
import com.piramide.elwis.domain.admin.AdminFreeTextHome;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * Manages company business logic.
 *
 * @author Yumi
 * @version $Id: CompanyCmd.java 9695 2009-09-10 21:34:43Z fernando $
 */

public class CompanyCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("LoggedCompany command execution" + paramDTO);
        CompanyDTO companyDTO = new CompanyDTO(paramDTO);

        log.debug("addressId of company = " + paramDTO.get("addressId"));
        companyDTO.setPrimKey(paramDTO.get("addressId"));
        Company company = (Company) ExtendedCRUDDirector.i.doCRUD(paramDTO.getOp(), companyDTO, resultDTO, true, false, false, false);

        //free text route page
        FreeTextCmdUtil.i.doCRUD(paramDTO, resultDTO, company, "RoutePage", AdminFreeTextHome.class,
                AdminConstants.JNDI_ADMIN_FREETEXT, FreeTextTypes.FREETEXT_COMPANY, "route");

        if (company != null) {
            resultDTO.put("addressId", company.getCompanyId());

            //updating logo company
            if (paramDTO.get("logoDelete") != null) {
                if (company.getLogo() != null) {
                    try {
                        AdminFreeText logoFreeText = company.getLogo();
                        company.setLogo(null);
                        logoFreeText.remove();
                        resultDTO.put("logoId", null);
                    } catch (RemoveException e) {
                        log.error("unexpected error removing the image freetext", e);
                    }
                }
            } else {
                //update or create free text to LOGO
                ArrayByteWrapper image = (ArrayByteWrapper) paramDTO.get("image");
                if (image != null) {
                    if (company.getLogo() != null) {
                        company.getLogo().setValue(image.getFileData());
                        resultDTO.put("logoId", company.getLogo().getFreeTextId());
                    } else {
                        AdminFreeTextHome adminFreeTextHome = (AdminFreeTextHome)
                                EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_ADMIN_FREETEXT);
                        try {
                            AdminFreeText logoFreeText = adminFreeTextHome.create(image.getFileData(),
                                    company.getCompanyId(), new Integer(FreeTextTypes.FREETEXT_COMPANY));
                            company.setLogo(logoFreeText);
                            resultDTO.put("logoId", company.getLogo().getFreeTextId());
                        } catch (CreateException e) {
                            log.error("Error creating the logo freeeText");
                        }
                    }
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
