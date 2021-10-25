package com.piramide.elwis.cmd.common.config;

import com.piramide.elwis.domain.common.config.SystemConstant;
import com.piramide.elwis.domain.common.config.SystemConstantHome;
import com.piramide.elwis.dto.common.config.SystemConstantDTO;
import com.piramide.elwis.utils.Constants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Reading the system constants given a system constant type.
 *
 * @author Fernando Montaño
 * @version $Id: SystemConstantCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class SystemConstantCmd extends EJBCommand {

    private Log log = LogFactory.getLog(SystemConstantCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("reading system constant values for type =  " + paramDTO.get("type"));

        try {

            SystemConstantHome systemConstantHome = (SystemConstantHome)
                    EJBFactory.i.getEJBLocalHome(Constants.JNDI_SYSTEM_CONSTANT);

            Collection typeValues = systemConstantHome.findConstantsByType((String) paramDTO.get("type"));

            List result = new ArrayList();
            for (Iterator iterator = typeValues.iterator(); iterator.hasNext();) {
                SystemConstant sc = (SystemConstant) iterator.next();
                result.add(new SystemConstantDTO(sc.getName(), sc.getValue(), sc.getResourceKey(), sc.getType()));
            }

            if (result.isEmpty()) {
                throw new RuntimeException("No system constants with name : " + paramDTO.get("type") + " were found");
            }
            resultDTO.put(paramDTO.get("type"), result);

        } catch (FinderException e) {
            log.debug("I connot find values for system constant type = " + paramDTO.get("type"));
        }
    }

    public boolean isStateful() {
        return false;
    }

}