package com.piramide.elwis.cmd.common;

import com.piramide.elwis.domain.admin.*;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.alfacentauro.fantabulous.db.QueryUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: yumi
 * Date: 07-sep-2006
 * Time: 10:15:22
 * To change this template use File | Settings | File Templates.
 */

public class ModuleEntriesLimitUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());


    public void executeInStateless(SessionContext ctx) {
        String functionality = (String) paramDTO.get("functionality");
        Integer companyId = (Integer) paramDTO.get("companyId");
        List record = new ArrayList();
        boolean canCreate = false;
        CompanyModule companyModule = null;
        SystemFunction systemFunction = null;

        String sql = "select count(*) as count from " + paramDTO.get("mainTable").toString() + " where companyid=" + paramDTO.get("companyId");
        record = QueryUtil.i.executeQuery(sql.toString());
        Map mapi = (Map) record.get(0);

        SystemFunctionHome systemFunctionHome = (SystemFunctionHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_SYSTEMFUNCTION);
        CompanyModuleHome companyModuleHome = (CompanyModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANYMODULE);
        try {
            systemFunction = systemFunctionHome.findByCode(functionality);
            CompanyModulePK pk = new CompanyModulePK();
            pk.companyId = companyId;
            pk.moduleId = systemFunction.getModuleId();
            companyModule = companyModuleHome.findByPrimaryKey(pk);
            log.debug("... mainTableRecordLimits " + companyModule.getMainTableRecordsLimit());

            if (companyModule.getMainTableRecordsLimit() != null) {
                if (companyModule.getMainTableRecordsLimit().intValue() > new Integer(mapi.get("count").toString())) {
                    canCreate = true;
                } else {
                    canCreate = false;
                }
            } else {
                canCreate = true; // amount entries no defined
            }
            resultDTO.put("canCreate", canCreate);
        } catch (FinderException e) {
            log.debug(" ... systemModule or companyModule not found ...");
            resultDTO.setResultAsFailure();
            return;
        }
    }

    public boolean isStateful() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}