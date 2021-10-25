package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.BuildCatalogStructure;
import com.piramide.elwis.cmd.admin.copycatalog.util.structure.Catalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.structure.Module;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.admin.CompanyHome;
import com.piramide.elwis.domain.admin.CompanyModule;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author: ivan
 */
public class CopyCatalogCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        Integer sourceCompanyId = new Integer(paramDTO.get("sourceCompanyId").toString());
        Integer targetCompanyId = new Integer(paramDTO.get("targetCompanyId").toString());

        log.debug("Copy catalog configuration...");
        log.debug("Source company " + sourceCompanyId);
        log.debug("Target company " + targetCompanyId);

        CompanyHome companyHome = (CompanyHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_COMPANY);

        Company sourceCompany = null;
        Company targetCompany = null;

        try {
            sourceCompany = companyHome.findByPrimaryKey(sourceCompanyId);
            targetCompany = companyHome.findByPrimaryKey(targetCompanyId);
        } catch (FinderException e) {
            log.warn("Cannot read Target or Source companies.");
        }
        if (null != sourceCompany && null != targetCompany) {
            List<Module> modules = readCopyCatalogConfiguration();
            for (Module module : modules) {
                if (checkAccessToModule(targetCompany, module.getPath(), module.getModuleId())) {
                    copyCatalogs(module, sourceCompany, targetCompany, sessionContext);
                }
            }
        }
    }

    private void copyCatalogs(Module module,
                              Company sourceCompany,
                              Company targetCompany,
                              SessionContext sessionContext) {

        log.debug("Copy module " + module.getPath());
        for (Catalog catalog : module.getCatalogs()) {
            CopyCatalog copyCatalog = getCopyCatalogInstance(catalog.getCopyClassName());
            if (null != copyCatalog) {
                copyCatalog.copyCatalog(sourceCompany, targetCompany, sessionContext);
            } else {
                log.error(catalog.getCopyClassName() +
                        " cannot implements com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog");
            }
        }
    }

    private boolean checkAccessToModule(Company targetCompany, String modulePath, String moduleId) {
        Collection modules = targetCompany.getCompanyModules();
        for (Iterator iterator = modules.iterator(); iterator.hasNext();) {
            CompanyModule companyModule = (CompanyModule) iterator.next();
            if (null == moduleId) {
                if (modulePath.equals(companyModule.getSystemModule().getPath())) {
                    log.debug("can copy module : " + companyModule.getSystemModule().getPath());
                    return true;
                }
            } else if (companyModule.getSystemModule().getModuleId().toString().equals(moduleId)) {
                log.debug("using module identifier, can copy module : " +
                        companyModule.getSystemModule().getPath());
                return true;
            }
        }
        return false;
    }

    private CopyCatalog getCopyCatalogInstance(String className) {
        CopyCatalog result = null;
        try {
            result = (CopyCatalog) Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    private List<Module> readCopyCatalogConfiguration() {
        List<Module> modules = new ArrayList<Module>();
        try {
            BuildCatalogStructure build = new BuildCatalogStructure();
            modules = build.buildStructure();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return modules;
    }

    public boolean isStateful() {
        return false;
    }
}
