package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.domain.admin.CompanyModule;
import com.piramide.elwis.domain.admin.SystemModule;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.SystemLanguage;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author: ivan
 */
public class ReadTemplateCompanyCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        String op = this.getOp();
        if ("readTrialTemplateCompany".equals(op)) {
            readTrialTemplateCompany();
        }
        if ("checkTrialTemplateByLanguage".equals(op)) {
            checkTrialTemplateCompanyByLanguage();
        }

    }

    private void checkTrialTemplateCompanyByLanguage() {
        Map systemLanguages = SystemLanguage.systemLanguages;

        List<String> isoLanguages = new ArrayList<String>();

        for (Object o : systemLanguages.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            isoLanguages.add(entry.getKey().toString());
        }

        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        for (String isoSystemLanguage : isoLanguages) {
            try {
                companyHome.findByCopyTemplateLanguage(
                        AdminConstants.CompanyTemplateTypes.trialTemplate.getConstant(),
                        isoSystemLanguage);
            } catch (FinderException e) {
                log.debug("Cannot read Company template for " + isoSystemLanguage);
                resultDTO.setResultAsFailure();
                break;
            }
        }
    }

    /**
     * this method read trial company template and puts in <code>ResultDTO</code> allowed users and
     * modules enabled for this company.
     * <p/>
     * <code>
     * resultDTO.put("usersAllowed", usersAllowed); //usersAllowed property
     * resultDTO.put("modules", modules); //modules of trial template company
     * </code>
     */
    private void readTrialTemplateCompany() {
        String language = (String) paramDTO.get("language");
        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);
        try {
            Company trialTemplate =
                    companyHome.findByCopyTemplateLanguage(AdminConstants.CompanyTemplateTypes.trialTemplate.getConstant(),
                            language);

            Integer usersAllowed = trialTemplate.getUsersAllowed();
            Collection templateModules = trialTemplate.getCompanyModules();
            List<Map> modules = new ArrayList<Map>();
            if (null != templateModules) {
                for (Object obj : templateModules) {
                    Map systemModuleAsMap = new HashMap();
                    CompanyModule templateModule = (CompanyModule) obj;
                    if (templateModule.getActive()) {
                        SystemModule systemModule = templateModule.getSystemModule();
                        systemModuleAsMap.put("moduleId", systemModule.getModuleId());
                        systemModuleAsMap.put("nameKey", systemModule.getNameKey());
                        systemModuleAsMap.put("usersAllowed", templateModule.getMainTableRecordsLimit());
                        modules.add(systemModuleAsMap);
                    }
                }
            }
            resultDTO.put("usersAllowed", usersAllowed);
            resultDTO.put("modules", modules);
            resultDTO.put("companyTemplateId", trialTemplate.getCompanyId());

        } catch (FinderException e) {
            log.warn("Cannot read Trial template company, administrator user must be create trial template....");
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
