package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.SystemModule;
import com.piramide.elwis.domain.admin.SystemModuleHome;
import com.piramide.elwis.utils.AdminConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SystemModuleCmd extends EJBCommand {
    private Log log = LogFactory.getLog(SystemModuleCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        String op = getOp();
        if ("readSystemModules".equals(op)) {
            readSystemModules();
        }
    }

    private void readSystemModules() {
        SystemModuleHome systemModuleHome =
                (SystemModuleHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_SYSTEMMODULE);
        List<Map> elements = new ArrayList<Map>();
        try {
            Collection modules = systemModuleHome.findAll();
            for (Object object : modules) {
                SystemModule module = (SystemModule) object;
                Map element = new HashMap();
                element.put("nameKey", module.getNameKey());
                element.put("moduleId", module.getModuleId());
                elements.add(element);
            }
        } catch (FinderException e) {
            log.error("-> Cannot read system modules", e);
        }

        resultDTO.put("systemModules", elements);
    }

    public boolean isStateful() {
        return false;
    }
}
