package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.cmd.utils.CriterionOperatorType;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author Titus
 * @version CampaignCriterionSimpleForm.java, v 2.0 Jun 30, 2004 4:16:52 PM
 */

public class CampaignCriterionSimpleForm extends DefaultForm {
    private Log log = LogFactory.getLog(CampaignCriterionSimpleForm.class);

    private String operator;
    private String value;
    private String categoryId;
    private String categoryName;
    private String rangeForm;
    private String rangeTo;
    private String standard;
    private String campaignId;
    public List withoutErrors = new ArrayList();
    public StringBuffer message = new StringBuffer();


    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public CampaignCriterionSimpleForm() {
        super();
        categoryName = "";
        log.debug("CampaignCriterionSimpleForm constructor called...");
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRangeForm() {
        return rangeForm;
    }

    public void setRangeForm(String rangeForm) {
        this.rangeForm = rangeForm;
    }

    public String getRangeTo() {
        return rangeTo;
    }

    public void setRangeTo(String rangeTo) {
        this.rangeTo = rangeTo;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Object[] getCriteriaList() {
        List list = (List) getDto("criteriaList");
        if (list != null) {
            return list.toArray();
        } else {
            return new Object[]{};
        }
    }

    public int getSize() {
        return getCriteriaList().length;
    }

    public void setCriteriaList(Object[] array) {
        if (array != null) {
            setDto("criteriaList", Arrays.asList(array));
        }
    }

    public void setWithoutErrorsList(Object[] withoutErrors) {

        if (withoutErrors != null) {
            setDto("criteriaList", Arrays.asList(withoutErrors));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("enter to validate ....");

        ActionErrors errors = new ActionErrors();
        errors = super.validate(mapping, request);
        String datePattern = JSPHelper.getMessage(request, "datePattern").trim();
        int errorValue = 0;

        //find the actual list of criterias was selected
        if (getDto("criteriaList") == null) {
            errors.add("criteriaList", new ActionError("errors.required",
                    JSPHelper.getMessage(request, "Campaign.alreadySelected")));
        } else {
            //the value of field is numeric
            if (CampaignConstants.FIELD_NUMBER.equals(getDto("typefield").toString())) {
                log.debug("enter numeric");
                if (!validateFieldValueNumber(withoutErrors, message)) {
                    errors.add("errorNumeric", new ActionError("CampaignCriterion.Message", message.toString()));
                }
                // the value of field is date
            } else if (CampaignConstants.FIELD_DATE.equals(getDto("typefield").toString())) {
                log.debug(" .. date Type ... criterias: " + getDto("criteriaList"));
                errorValue = validateFieldValueDate(datePattern, withoutErrors, message);
                if (errorValue != 0) {
                    errors.add("errorDate", new ActionError("CampaignCriterion.Message", message.toString()));
                }
            }// having errors on the criteriaList

            if (!errors.isEmpty()) {
                List items = (List) getDto("criteriaList");
                setCriteriaList(items.toArray());
                setWithoutErrorsList(withoutErrors.toArray());
                request.setAttribute("lista", withoutErrors.toArray());
            }
        }
        return errors;
    }

    private boolean validateFieldValueNumber(List withoutErrors, StringBuffer message) {
        boolean result = true;
        boolean error = false;
        List criterias = (List) getDto("criteriaList");
        message.append("     ");

        for (Iterator iterator = criterias.iterator(); iterator.hasNext();) {
            String o = (String) iterator.next();
            List values = JSPHelper.getCriteriaValues(o);
            error = false;

            if (values.size() == 2) {
                if (!GenericValidator.isInt(values.get(1).toString())) {
                    result = false;
                    error = true;
                    message.append(values.get(1));
                } else {
                    withoutErrors.add(new LabelValueBean(CriterionOperatorType.CriteriaSimpleOperator(values.get(0).toString()) + values.get(1).toString(), o));
                }
            }
            if (values.size() == 3) {
                if (!GenericValidator.isInt(values.get(1).toString()) || !GenericValidator.isInt(values.get(2).toString())) {
                    result = false;
                    error = true;
                    if (!"BETWEEN0".equals(values.get(0))) {
                        message.append("[").append(values.get(1).toString()).append(" - ").append(values.get(2).toString()).append("]");
                    } else {
                        message.append(values.get(1).toString()).append(" - ").append(values.get(2).toString());
                    }
                } else if (Integer.parseInt(values.get(1).toString()) > Integer.parseInt(values.get(2).toString())) {
                    error = true;
                    if (!"BETWEEN0".equals(values.get(0))) {
                        message.append("[").append(values.get(1).toString()).append(" - ").append(values.get(2).toString()).append("]");
                    } else {
                        message.append(values.get(1).toString()).append(" - ").append(values.get(2).toString());
                    }
                } else {
                    if (!"BETWEEN0".equals(values.get(0))) {
                        withoutErrors.add(new LabelValueBean("[" + values.get(1).toString() + " - " + values.get(2).toString() + "]", o));
                    } else {
                        withoutErrors.add(new LabelValueBean(values.get(1).toString() + " - " + values.get(2).toString(), o));
                    }
                }
            }
            if (error && iterator.hasNext()) {
                message.append("     ");
            }
        }
        return result;
    }

    //1= the format is wrong
    //2= id final date is less to the initial date
    //0= others
    private int validateFieldValueDate(String pattern, List withoutErrors, StringBuffer message) {
        log.debug("validate dates ... function");

        int result = 0;
        boolean flag = false;
        boolean flagBetween = false;
        Date aDate1 = null;
        Date aDate2 = null;
        List criterias = (List) getDto("criteriaList");
        List list = new ArrayList();
        message.append("     ");

        log.debug("size object list ...date type . " + criterias.size());
        for (Iterator iterator = criterias.iterator(); iterator.hasNext();) {
            String o = (String) iterator.next();
            List values = JSPHelper.getCriteriaValues(o);
            result = 0;
            log.debug("string value : " + o);
            log.debug("values :" + values);

            if (values.size() == 2) { // if is the first case ( > < = >= ...)

                if (!GenericValidator.isDate(values.get(1).toString(), pattern, false)) {
                    result = 1;
                    message.append(values.get(1));
                } else {
                    aDate1 = GenericTypeValidator.formatDate(values.get(1).toString(), pattern, false);
                    log.debug("formated date " + DateUtils.dateToInteger(aDate1).toString());
                    list.add(JSPHelper.putSimpleCriteriaValueDate(values.get(0).toString(),
                            DateUtils.dateToInteger(aDate1).toString()));
                    withoutErrors.add(new LabelValueBean(CriterionOperatorType.CriteriaSimpleOperator(values.get(0).toString()) + values.get(1).toString(), o));
                }
            } else if (values.size() == 3) {//para rangos BETWEEN
                if (!GenericValidator.isDate(values.get(1).toString(), pattern, false) ||
                        !GenericValidator.isDate(values.get(2).toString(), pattern, false)) {
                    result = 1;
                    if (!"BETWEEN0".equals(values.get(0))) {
                        message.append("[").append(values.get(1)).append("-").append(values.get(2)).append("]");
                    } else {
                        message.append(values.get(1)).append("-").append(values.get(2));
                    }
                } else {
                    aDate1 = GenericTypeValidator.formatDate(values.get(1).toString(), pattern, false);
                    aDate2 = GenericTypeValidator.formatDate(values.get(2).toString(), pattern, false);

                    log.debug("formated date from: " + DateUtils.dateToInteger(aDate1).toString());
                    log.debug("formated date to : " + DateUtils.dateToInteger(aDate2).toString());
                    String operator = values.get(0).toString();
                    list.add(JSPHelper.putSimpleRangeCriteriaValueDate(operator,
                            DateUtils.dateToInteger(aDate1).toString(), DateUtils.dateToInteger(aDate2).toString()));

                    if (aDate2.compareTo(aDate1) < 0) {
                        result = 2;
                        if (!"BETWEEN0".equals(values.get(0))) {
                            message.append("[").append(values.get(1)).append("-").append(values.get(2)).append("]");
                        } else {
                            message.append(values.get(1)).append("-").append(values.get(2));
                        }

                    } else {
                        if (!"BETWEEN0".equals(values.get(0))) {
                            withoutErrors.add(new LabelValueBean("[" + values.get(1).toString() + " - " + values.get(2).toString() + "]", o));
                        } else {
                            withoutErrors.add(new LabelValueBean(values.get(1).toString() + " - " + values.get(2).toString(), o));
                        }
                    }
                }
            }
            if (result == 1) {
                flag = true;
            }
            if (result == 2) {
                flagBetween = true;
            }

            if (result != 0 && iterator.hasNext()) {
                message.append("     ");
            }
        }
        this.getDtoMap().put("criteriaList", list);
        if (flag && flagBetween) {
            result = 3;
        }
        return result;
    }
}