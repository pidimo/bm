package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: TelecomTypeTranslationCmd.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 15-03-2005 11:19:46 AM ivan Exp $
 */
public class TelecomTypeTranslationCmd extends GeneralTranslationCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) throws AppLevelException {
        super.setOp(this.getOp());
        super.executeInStateless(ctx, "langTextId", "telecomTypeId", "telecomTypeName", paramDTO, TelecomTypeDTO.class);

        ResultDTO myResultDTO = super.resultDTO;

        if (null != myResultDTO.get(NEW_LANGTEXT)) {
            paramDTO.put("langTextId", myResultDTO.get(NEW_LANGTEXT));
        }

        if ("update".equals(this.getOp())
                && !"Fail".equals(resultDTO.getForward())
                && !"".equals(paramDTO.get("langTextId"))) {
            UpdateTelecomTypeStatus();
            super.sincronizeFirstTranslation(ctx,
                    paramDTO,
                    "telecomTypeName",
                    "telecomTypeId",
                    "langTextId",
                    TelecomTypeDTO.class);
        }

    }

    private void UpdateTelecomTypeStatus() {
        log.debug("Update company TelecomTypeStatus field...");

        Long actualTime = new Long(System.currentTimeMillis());

        Company company = (Company) EJBFactory.i.callFinder(new CompanyDTO(),
                "findByPrimaryKey",
                new Object[]{Integer.valueOf(paramDTO.get("companyId").toString())});

        company.setTelecomTypeStatus(actualTime.toString());
    }

    public boolean isStateful() {
        return super.isStateful();
    }
}
