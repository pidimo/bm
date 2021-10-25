package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: CampaignCriterionSimpleUpdateForm.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignCriterionSimpleUpdateForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        boolean required = false;
        ActionErrors errors = new ActionErrors();
        if ("delete".equals(getDto("op"))) {
            return errors;
        }

        String value = (String) getDto("value");
        String rangeFrom = (String) getDto("rangeFrom");
        String rangeTo = (String) getDto("rangeTo");
        String operator = (String) getDto("operator");
        String datePattern = JSPHelper.getMessage(request, "datePattern").trim();

        if ("BETWEEN0".equals(operator) || "BETWEEN1".equals(operator)) {
            if (rangeFrom != null) {
                if (GenericValidator.isBlankOrNull(rangeFrom)) {
                    errors.add("rangeFrom", new ActionError("errors.required", JSPHelper.getMessage(request, "Campaign.range")));
                    required = true;
                }
            } else if (getDto("rangeFromDate") != null) {
                if (GenericValidator.isBlankOrNull((String) getDto("rangeFromDate"))) {
                    errors.add("rangeFrom", new ActionError("errors.required", JSPHelper.getMessage(request, "Campaign.range")));
                    required = true;
                }
            }

            if (rangeTo != null) {
                if (GenericValidator.isBlankOrNull(rangeTo)) {
                    errors.add("rangeTo", new ActionError("errors.required", JSPHelper.getMessage(request, "Campaign.to")));
                    required = true;
                }
            } else if (getDto("rangeToDate") != null) {
                if (GenericValidator.isBlankOrNull((String) getDto("rangeToDate"))) {
                    errors.add("rangeTo", new ActionError("errors.required", JSPHelper.getMessage(request, "Campaign.to")));
                    required = true;
                }
            }
        } else { //when has single value
            if (value != null) {
                if (GenericValidator.isBlankOrNull(value)) {
                    errors.add("value", new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Campaign.singleValue")));
                    required = true;
                }
            }
        }

//if not validate other things: if is in correct format
        if (!required) {
            if ("BETWEEN0".equals(operator) || "BETWEEN1".equals(operator)) {
                Integer rangeToDate = null;
                Integer rangeFromDate = null;
                boolean isDate = true;
                boolean isNumber = true;

                if (CampaignConstants.FIELD_NUMBER.equals(getDto("fieldType").toString())) {
                    if (!GenericValidator.isInt(getDto("rangeFrom").toString()) || !GenericValidator.isInt(getDto("rangeTo").toString())) {
                        isNumber = false;
                        errors.add("errorInteger", new ActionError("errors.integerPositives", JSPHelper.getMessage(request, "Product.range")));
                    }
                    if (isNumber && Integer.parseInt(rangeFrom) > Integer.parseInt(rangeTo)) {
                        errors.add("finishLicenseDate", new ActionError("Common.greaterThan", JSPHelper.getMessage(request, "Campaign.range"), JSPHelper.getMessage(request, "Campaign.to")));

                    }
                } else if (CampaignConstants.FIELD_DATE.equals(getDto("fieldType").toString())) {
                    String pattern = JSPHelper.getMessage(request, "datePattern").trim();
                    if (!GenericValidator.isDate(getDto("rangeFromDate").toString(), pattern, false)
                            || !GenericValidator.isDate(getDto("rangeToDate").toString(), pattern, false)) {
                        isDate = false;
                        errors.add("errorDate", new ActionError("errors.dates", JSPHelper.getMessage(request, "campaign.recorddate"), pattern));
                    }

                    if (isDate) {
                        rangeFromDate = DateUtils.dateToInteger(DateUtils.formatDate(getDto("rangeFromDate").toString(), pattern, false));
                        rangeToDate = DateUtils.dateToInteger(DateUtils.formatDate(getDto("rangeToDate").toString(), pattern, false));
                        if (rangeToDate.intValue() < rangeFromDate.intValue()) {
                            errors.add("startDate", new ActionError("Common.greaterThan", JSPHelper.getMessage(request,
                                    "Appointment.endDate"), JSPHelper.getMessage(request, "Task.startDate")));
                        }
                    }
                    if (errors.isEmpty()) {
                        this.getDtoMap().put("rangeFrom", rangeFromDate);
                        this.getDtoMap().put("rangeTo", rangeToDate);
                    }
                }
            } else {//when does not range
                if (CampaignConstants.FIELD_NUMBER.equals(getDto("fieldType").toString())) {
                    if (!GenericValidator.isInt(value)) {
                        errors.add("value", new ActionError("errors.integer", JSPHelper.getMessage(request, "Campaign.singleValue")));
                    }
                }
                if (CampaignConstants.FIELD_DATE.equals(getDto("fieldType").toString())) {
                    if (!GenericValidator.isDate(value, datePattern, false)) {
                        errors.add("value", new ActionError("errors.date", JSPHelper.getMessage(request, "Campaign.singleValue"),
                                JSPHelper.getMessage(request, "datePattern")));
                        this.getDtoMap().put("error", "true");
                        this.getDtoMap().put("value", value);
                    } else {
                        this.getDtoMap().put("value", DateUtils.dateToInteger((DateUtils.formatDate(value, datePattern))));
                    }
                }
            }
        }
        return errors;
    }
}