package com.piramide.elwis.web.catalogmanager.form;

import com.piramide.elwis.utils.SchedulerConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: HolidayForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class HolidayForm extends DefaultForm {
    private static final int[] maxDaysByMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Validate HOLIDAYFORM");
        Map dto = getDtoMap();
        ActionErrors errors = super.validate(mapping, request);
        if (!errors.isEmpty()) {
            return errors;
        }

        int day = 0;
        int month = 0;
        int type = Integer.parseInt((String) dto.get("holidayType"));
        if (!GenericValidator.isInRange(type, 0, 1)) {
            errors.add("fail", new ActionError("Common.invalid"));
            return errors;
        }

        String prefix = "A";
        if (type == 1) {
            prefix = "B";
        }
        if (log.isDebugEnabled()) {
            log.debug("PREFIX:" + prefix);
            log.debug("FORM:" + dto);
        }
        boolean proccess = true;
        if (GenericValidator.isBlankOrNull((String) dto.get("day" + prefix))) {
            errors.add("dto(day" + prefix + ")", new ActionError("errors.required", JSPHelper.getMessage(request, "Common.day")));
            proccess = false;
        }
        if (GenericValidator.isBlankOrNull((String) dto.get("month" + prefix))) {
            errors.add("dto(month" + prefix + ")", new ActionError("errors.required", JSPHelper.getMessage(request, "Common.month")));
            proccess = false;
        }

        if (!proccess) {
            return errors;
        }
        Exception ex = null;

        try {
            day = Integer.parseInt((String) dto.get("day" + prefix));
        } catch (Exception e) {
            ex = e;
            errors.add("dto(day" + prefix + ")", new ActionError("errors.invalid", JSPHelper.getMessage(request, "Common.day")));
        }

        try {
            month = Integer.parseInt((String) dto.get("month" + prefix));
            if (!GenericValidator.isInRange(month, 1, 12)) {
                errors.add("dto(month" + prefix + ")", new ActionError("errors.invalid", JSPHelper.getMessage(request, "Common.month")));
            }
        } catch (Exception e) {
            ex = e;
            errors.add("dto(month" + prefix + ")", new ActionError("errors.invalid", JSPHelper.getMessage(request, "Common.month")));
        }

        if (ex != null) {
            return errors;
        }


        int occurrence = 0;

        if (type == 0) { //DateType
            log.debug("IS DATE TYPE...");
            int maxDay = maxDaysByMonth[month - 1];
            if (!GenericValidator.isBlankOrNull((String) dto.get("occurrenceA"))) {
                try {
                    occurrence = Integer.parseInt((String) dto.get("occurrenceA"));
                } catch (Exception e) {
                    errors.add("dto(occurrenceA)", new ActionError("errors.integer", JSPHelper.getMessage(request, "Common.year")));
                }
            }

            if (occurrence > 0 && month == 2 && !((occurrence % 4 == 0 && occurrence % 100 != 0) || occurrence % 400 == 0))//Is not leap Year
            {
                maxDay--;
            }
            log.debug("DAY:" + day + ">" + maxDay);

            if (day > maxDay) {
                errors.add("dto(dayA)", new ActionError("error.greaterThan",
                        JSPHelper.getMessage(request, "Common.day"),
                        new Integer(maxDay)));
            }
        } else {
            if (!GenericValidator.isBlankOrNull((String) dto.get("occurrenceB"))) {
                try {
                    occurrence = Integer.parseInt((String) dto.get("occurrenceB"));
                } catch (Exception e) {
                    errors.add("dto(occurrenceB)", new ActionError("errors.invalid", JSPHelper.getMessage(request, "holiday.error.occurrenceInMonth")));
                }
            } else {
                errors.add("dto(occurrenceB)", new ActionError("errors.required", JSPHelper.getMessage(request, "holiday.error.occurrenceInMonth")));
            }
            if (occurrence == 0) {
                return errors;
            }

            if (occurrence > 0 && !GenericValidator.isInRange(occurrence, SchedulerConstants.MONTHLY_OCCUR_1ST, SchedulerConstants.MONTHLY_OCCUR_LAST) || //Week of month
                    !GenericValidator.isInRange(day, 1, 7))//Day of week
            {
                errors.add("fail", new ActionError("Common.invalid.id"));
            }
        }

        return errors;
    }
}
