package com.piramide.elwis.cmd.admin;

import com.piramide.elwis.domain.admin.SystemFunction;
import com.piramide.elwis.dto.admin.SystemFunctionDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Reads a system functions by function code.
 *
 * @author Fernando Montaño
 * @version $Id: SystemFunctionReadCodeCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class SystemFunctionReadCodeCmd extends EJBCommand {
    private Log log = LogFactory.getLog(SystemFunctionReadCodeCmd.class);

    public void executeInStateless(SessionContext ctx) {
        log.debug("Code to read =" + paramDTO.get("functionCode"));

        try {
            SystemFunction systemFunction = (SystemFunction) EJBFactory.i.callFinder(new SystemFunctionDTO(),
                    "findByCode", new Object[]{paramDTO.getAsString("functionCode")});

            if (systemFunction != null) {
                resultDTO.put("nameKey", systemFunction.getNameKey());
                resultDTO.put("moduleNameKey", systemFunction.getSystemModule().getNameKey());
            }

        } catch (EJBFactoryException e) {
            log.debug("System function not found");
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
