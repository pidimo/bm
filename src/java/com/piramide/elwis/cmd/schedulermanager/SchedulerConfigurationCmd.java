package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.dto.admin.UserDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: SchedulerConfigurationCmd.java 7936 2007-10-27 16:08:39Z fernando ${CLASS_NAME}.java,v 1.2 03-05-2005 04:59:51 PM ivan Exp $
 */
public class SchedulerConfigurationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing Scheduler configuration command...");

        log.debug("paramDTO ... " + paramDTO);


        Integer userId = Integer.valueOf(paramDTO.get("userId").toString());
        Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());
        String op = (String) paramDTO.get("op");
        User user = (User) EJBFactory.i.callFinder(new UserDTO(), "findByPrimaryKey", new Object[]{userId});

        if ("update".equals(op)) {
            user.setCalendarDefaultView(Integer.valueOf(paramDTO.get("calendarDefaultView").toString()));
            user.setDayFragmentation(Integer.valueOf(paramDTO.get("dayFragmentation").toString()));
            user.setInitialDayOfWork(Integer.valueOf(paramDTO.get("initialDayOfWork").toString()));
            user.setFinalDayOfWork(Integer.valueOf(paramDTO.get("finalDayOfWork").toString()));
            Integer countryId = null;
            try {
                countryId = Integer.valueOf(paramDTO.get("holidayCountryId").toString());
            } catch (Exception e) {
            }
            user.setHolidayCountryId(countryId);
            resultDTO.addResultMessage("Common.changesOK");
        }

        resultDTO.put("calendarDefaultView", user.getCalendarDefaultView());
        resultDTO.put("dayFragmentation", user.getDayFragmentation());
        resultDTO.put("initialDayOfWork", user.getInitialDayOfWork());
        resultDTO.put("finalDayOfWork", user.getFinalDayOfWork());
        resultDTO.put("holidayCountryId", user.getHolidayCountryId());
        resultDTO.put("operation", "update");
    }

    public boolean isStateful() {
        return false;
    }
}
