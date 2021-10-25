package com.piramide.elwis.cmd.schedulermanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.schedulermanager.RecurException;
import com.piramide.elwis.domain.schedulermanager.RecurExceptionHome;
import com.piramide.elwis.domain.schedulermanager.Recurrence;
import com.piramide.elwis.dto.schedulermanager.RecurExceptionDTO;
import com.piramide.elwis.dto.schedulermanager.RecurrenceDTO;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 26, 2005
 * Time: 10:09:16 AM
 * To change this template use File | Settings | File Templates.
 */

public class RecurrenceUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing   RecurrenceUpdate  command .. ");

        log.debug("Operation = " + getOp());
        RecurrenceDTO recurrenceDTO = new RecurrenceDTO();
        String sp = SchedulerConstants.RECURRENCE_VALUES_SEPARATOR;
        StringBuffer ruleValue = new StringBuffer();
        StringBuffer rV = new StringBuffer();

        if (SchedulerConstants.RECURRENCE_WEEKLY.equals(paramDTO.get("ruleType"))) { //week =2
            recurrenceDTO = new RecurrenceDTO();
            log.debug("week option..");
            for (int i = 0; i < 8; i++) {
                if ("on".equals(paramDTO.get((new Integer(i)).toString()))) {
                    rV.append(i).append(sp);
                }
            }
            recurrenceDTO.put("recurEvery", paramDTO.get("recurEveryWeek"));
            recurrenceDTO.put("ruleValue", rV.substring(0, rV.length() - 1));

        } else if (SchedulerConstants.RECURRENCE_MONTHLY.equals(paramDTO.get("ruleType"))) {//Monthly =3
            recurrenceDTO = new RecurrenceDTO();
            log.debug("monthly option..");
            recurrenceDTO.put("recurEvery", paramDTO.get("recurEveryMonth"));

            if (paramDTO.get("ruleValueTypeMonth") != null && !"".equals(paramDTO.get("ruleValueTypeMonth"))) {
                if ("1".equals(paramDTO.get("ruleValueTypeMonth"))) {
                    recurrenceDTO.put("ruleValue", paramDTO.get("ruleValueDay"));
                } else {
                    ruleValue = new StringBuffer();
                    ruleValue.append(paramDTO.get("ruleValueWeek"));
                    ruleValue.append(sp).append(paramDTO.get("daysWeek"));
                    recurrenceDTO.put("ruleValue", new String(ruleValue));
                }
                recurrenceDTO.put("ruleValueType", paramDTO.get("ruleValueTypeMonth"));
            } else {
                recurrenceDTO.put("ruleValueType", paramDTO.get("ruleValueTypeYear"));
            }

        } else if (SchedulerConstants.RECURRENCE_YEARLY.equals(paramDTO.get("ruleType"))) {//Yearly
            log.debug("year option..");
            recurrenceDTO = new RecurrenceDTO();
            recurrenceDTO.put("recurEvery", paramDTO.get("recurEveryYear"));
            recurrenceDTO.put("ruleValueType", paramDTO.get("ruleValueTypeYear"));
            recurrenceDTO.put("ruleValue", paramDTO.get("ruleValue"));
        } else {
            recurrenceDTO.put("recurEvery", paramDTO.get("recurEveryDay"));
        }

        recurrenceDTO.put("ruleType", paramDTO.get("ruleType"));
        recurrenceDTO.put("rangeType", paramDTO.get("rangeType"));
        recurrenceDTO.put("appointmentId", paramDTO.get("appointmentId"));
        recurrenceDTO.put("companyId", paramDTO.get("companyId"));

        if (SchedulerConstants.RECUR_RANGE_NO_ENDING.equals(paramDTO.get("rangeType"))) {
            recurrenceDTO.put("rangeValue", null);
        } else if (SchedulerConstants.RECUR_RANGE_AFTER_OCCURRENCE.equals(paramDTO.get("rangeType"))) {
            recurrenceDTO.put("rangeValue", paramDTO.get("rangeValueText"));
        } else if (SchedulerConstants.RECUR_RANGE_DATE.equals(paramDTO.get("rangeType"))) {
            recurrenceDTO.put("rangeValue", paramDTO.get("rangeValueDate"));
        }

        Recurrence recurrence = (Recurrence) ExtendedCRUDDirector.i.update(recurrenceDTO, resultDTO, false, false, false, "fail");
//manejo de las excepciones
        if (paramDTO.get("exceptionList") != null) {
            List exception = (List) paramDTO.get("exceptionList");
            RecurExceptionHome exceptionHome = (RecurExceptionHome) EJBFactory.i.getEJBLocalHome(SchedulerConstants.JNDI_RECUREXCEPTION);
            try {
                Collection e = exceptionHome.findByAppointmentId(recurrence.getAppointmentId());
                for (Iterator iterator = e.iterator(); iterator.hasNext();) {
                    RecurException recurException = (RecurException) iterator.next();
                    recurException.remove();
                }
            } catch (FinderException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (RemoveException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            if (exception.size() > 0) {
                log.debug("recurException ... ");
                RecurExceptionDTO exceptionDTO = null;
                for (Iterator iterator = exception.iterator(); iterator.hasNext();) {
                    exceptionDTO = new RecurExceptionDTO();
                    exceptionDTO.put("appointmentId", recurrence.getAppointmentId());
                    exceptionDTO.put("dateValue", (String) iterator.next());
                    exceptionDTO.put("companyId", recurrence.getCompanyId());
                    ExtendedCRUDDirector.i.create(exceptionDTO, resultDTO, false);
                }
            }
        }

        /**
         * There is no necesity to update here the recurrence reminder, because it'll do in the respective
         * appointment Cmd.
         * AppointmentCalculateReminderCmd should not be invoked twice.
         */
    }

    public boolean isStateful() {
        return false;
    }
}
