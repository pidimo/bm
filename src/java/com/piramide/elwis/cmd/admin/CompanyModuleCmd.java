package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.admin.CompanyModule;
import com.piramide.elwis.domain.admin.CompanyModuleHome;
import com.piramide.elwis.domain.admin.CompanyModulePK;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class CompanyModuleCmd extends EJBCommand {

    private Log log = LogFactory.getLog(CompanyModuleCmd.class);


    @Override
    public void executeInStateless(SessionContext sessionContext) {
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        if ("hasAssignedFinanceModule".equals(getOp())) {
            hasAssignedFinanceModule(companyId);
        }

        if ("hasAssignedLexwareModule".equals(getOp())) {
            hasAssignedLexwareModule(companyId);
        }
    }

    private void hasAssignedFinanceModule(Integer companyId) {
        resultDTO.put("hasAssignedFinanceModule", hasAsignedModule(companyId, 11));
    }

    private void hasAssignedLexwareModule(Integer companyId) {
        resultDTO.put("hasAssignedLexwareModule", hasAsignedModule(companyId, 13));
    }

    private boolean hasAsignedModule(Integer companyId, Integer moduleId) {
        CompanyModule companyModule = getCompanyModule(companyId, moduleId);
        if (null == companyModule) {
            return false;
        }

        return companyModule.getActive();
    }

    private CompanyModule getCompanyModule(Integer companyId, Integer moduleId) {
        CompanyModuleHome companyModuleHome =
                (CompanyModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANYMODULE);
        CompanyModulePK pk = new CompanyModulePK();
        pk.companyId = companyId;
        pk.moduleId = moduleId;

        try {
            return companyModuleHome.findByPrimaryKey(pk);
        } catch (FinderException e) {
            return null;
        }
    }

    public boolean isStateful() {
        return false;
    }
}
