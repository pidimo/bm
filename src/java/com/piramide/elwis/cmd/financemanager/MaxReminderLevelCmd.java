package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.domain.financemanager.ReminderLevelHome;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 * Recovery max reminder level defined in the company
 *
 * @author Miky
 * @version $Id: MaxReminderLevelCmd.java 06-nov-2008 15:18:48 $
 */
public class MaxReminderLevelCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing MaxReminderLevelCmd..........." + paramDTO);

        Integer companyId = new Integer(paramDTO.get("companyId").toString());

        ReminderLevelHome reminderLevelHome = (ReminderLevelHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERLEVEL);
        Integer maxReminderLevel = null;
        try {
            maxReminderLevel = reminderLevelHome.selectMaxLevel(companyId);
        } catch (FinderException e) {
            log.debug("Error in execute ejb select MAX...", e);
        }

        resultDTO.put("maxLevel", maxReminderLevel);
    }

    public boolean isStateful() {
        return false;
    }

}
