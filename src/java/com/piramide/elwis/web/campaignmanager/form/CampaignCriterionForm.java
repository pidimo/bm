package com.piramide.elwis.web.campaignmanager.form;

import com.piramide.elwis.domain.catalogmanager.Category;
import com.piramide.elwis.domain.catalogmanager.CategoryHome;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.campaignmanager.el.Functions;
import com.piramide.elwis.web.common.util.JSPHelper;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignCriterionForm.java 10245 2012-07-19 23:10:58Z miguel $
 */

public class CampaignCriterionForm extends DefaultForm {

    private Log log = LogFactory.getLog(this.getClass());

    public Object[] getMultipleSelectValue() {
        List list = (List) getDto("multipleSelectvalue");
        if (list != null) {
            return list.toArray();
        }
        return new Object[]{};
    }

    public void setMultipleSelectValue(Object[] multipleSelectValue) {
        if (multipleSelectValue != null) {
            setDto("multipleSelectValue", Arrays.asList(multipleSelectValue));
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        boolean isPopUp = false;
        log.debug(" ... validate function execute..");

        if ("update".equals(getDto("op"))) {
            if ("13".equals(getDto("IU_Type"))) {
                setDto("operator", getDto("operator_productInUse"));
                setDto("fieldValue", getDto("inUse"));
            } else if ("12".equals(getDto("IU_Type"))) {
                setDto("operator", getDto("operator_contactType"));
                setDto("fieldValue", getDto("code"));
            } else if ("1".equals(getDto("fieldType"))) {
                setDto("operator", getDto("operator_number"));
            } else if ("2".equals(getDto("fieldType"))) {
                setDto("operator", getDto("operator_decimal"));
            } else if ("3".equals(getDto("fieldType"))) {
                setDto("operator", getDto("operator_date"));
            } else if ("4".equals(getDto("IU_Type")) || "11".equals(getDto("IU_Type")) || "14".equals(getDto("IU_Type"))) {
                setDto("operator", getDto("operator_text"));
            } else if ("9".equals(getDto("IU_Type"))) {
                setDto("operator", getDto("operator_multiple"));
            } else if (CampaignConstants.FIELD_RELATION_EXISTS.equals(getDto("fieldType"))) {
                setDto("operator", getDto("operator_relationExists"));
            }
        }

        if ("11".equals(getDto("IU_Type"))) {
            setDto("fieldValue", getDto("partnerId"));
            isPopUp = true;
        } else if ("14".equals(getDto("IU_Type"))) {
            setDto("fieldValue", getDto("productId"));
            isPopUp = true;
        }

        setDto("isPopUp", isPopUp);

        if ("-100".equals(getDto("categoryId"))) {
            setDto("categoryId", null);
        }
        errors = super.validate(mapping, request);
        if ((getDto("categoryId") == null || CampaignConstants.EMPTY.equals(getDto("categoryId")))) {
            setDto("categoryId", "-100");
        }
        if (("EQUAL".equals(getDto("operator")) || "NOTIN".equals(getDto("operator")) || "DISTINCT".equals(getDto("operator")))
                && "9".equals(getDto("IU_Type")) && CampaignConstants.EMPTY.equals(request.getParameter("multipleSelectValue"))) {
            Locale locale = request.getLocale();
            errors.add("error", new ActionError("errors.required", JSPHelper.getMessage(locale, "Common.value")));
        } else if ("EQUAL".equals(getDto("operator")) && "9".equals(getDto("IU_Type"))) {
            setDto("fieldValue", request.getParameter("multipleSelectValue"));
        }
        if (errors.isEmpty()) {
            validateValues(errors, request);
        } else if (getDto("categoryId") != null && !"-100".equals(getDto("categoryId")) && !CampaignConstants.EMPTY.equals(getDto("operator"))) {
            ActionErrors e = new ActionErrors();
            CategoryHome categoryHome = (CategoryHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CATEGORY);
            try {
                Category category = categoryHome.findByPrimaryKey(new Integer(getDto("categoryId").toString()));
            } catch (FinderException a) {
                setDto("categoryNULL", CampaignConstants.TRUEVALUE);
                log.debug(" ... category not found ...   ");
            }
        }
        return errors;
    }

    public void validateValues(ActionErrors errors, HttpServletRequest request) {
        String fieldType = getDto("fieldType").toString();
        String operator = getDto("operator").toString();
        String campCriterionValueId = getDto("campCriterionValueId").toString();

        log.debug(" ... validateValues function ...");
        //1=text, 2=number, 3=decimalNumber, 6=date, 9=multipleSelect, 8=simpleSelect IU_type
        if (fieldType.equals("1") && !"12".equals(getDto("IU_Type")) && !"13".equals(getDto("IU_Type"))) {    // if type is number.
            log.debug(" ... field number  validate ....");
            if ("1".equals(getDto("IU_Type"))) {
                setDto("fieldValue", getDto("number_value"));
            } else {
                if (CampaignConstants.EMPTY.equals(getDto("numberValue_1").toString().trim()) || CampaignConstants.EMPTY.equals(getDto("numberValue_2").toString().trim())) {
                    errors.add("between_number", new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Campaign.value")));

                } else if (!CampaignConstants.EMPTY.equals(getDto("numberValue_1").toString().trim()) && !CampaignConstants.EMPTY.equals(getDto("numberValue_2").toString().trim())) {
                    if (Integer.parseInt(getDto("numberValue_1").toString()) > Integer.parseInt(getDto("numberValue_2").toString())) {
                        errors.add("between_number", new ActionError("Common.greaterThan",
                                JSPHelper.getMessage(request, "PayCondition.secondCondition"),
                                JSPHelper.getMessage(request, "PayCondition.firstCondition")));
                    } else {
                        setDto("fieldValue", getDto("numberValue_1") + "," + getDto("numberValue_2"));
                    }
                }
            }

        } else if (fieldType.equals("2")) { // if type is decimal number.
            log.debug(" ... field decimal validate ...");
            if ("2".equals(getDto("IU_Type"))) {
                setDto("fieldValue", getDto("decimalNumber_value"));
            } else {
                if (CampaignConstants.EMPTY.equals(getDto("decimalNumberValue_1")) || CampaignConstants.EMPTY.equals(getDto("decimalNumberValue_2"))) {
                    errors.add("between_number", new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Campaign.value")));
                } else if (new Float(getDto("decimalNumberValue_1").toString()).floatValue() > new Float(getDto("decimalNumberValue_2").toString()).floatValue()) {
                    errors.add("between_number", new ActionError("Common.greaterThan",
                            JSPHelper.getMessage(request, "PayCondition.secondCondition"),
                            JSPHelper.getMessage(request, "PayCondition.firstCondition")));
                } else {
                    setDto("fieldValue", getDto("decimalNumberValue_1") + "," + getDto("decimalNumberValue_2"));
                }
            }
        } else if (fieldType.equals("3")) {
            log.debug(" ... field date validate  ...");
            if ("3".equals(getDto("IU_Type"))) {
                setDto("fieldValue", getDto("date_value").toString());
            } else {
                if (CampaignConstants.EMPTY.equals(getDto("dateValue_1")) || CampaignConstants.EMPTY.equals(getDto("dateValue_2"))) {
                    errors.add("between_number", new ActionError("errors.required",
                            JSPHelper.getMessage(request, "Campaign.value")));
                } else if ((new Integer(getDto("dateValue_1").toString())) > (new Integer(getDto("dateValue_2").toString()))) {
                    errors.add("dates", new ActionError("Common.greaterThan",
                            JSPHelper.getMessage(request, "PayCondition.secondCondition"),
                            JSPHelper.getMessage(request, "PayCondition.firstCondition")));
                } else {
                    setDto("fieldValue", getDto("dateValue_1") + "," + getDto("dateValue_2"));
                }
            }
        } else if (fieldType.equals("4") && !"9".equals(getDto("IU_Type"))) {// && !"10".equals(getDto("IU_Type"))){
            setDto("fieldValue", getDto("text_value"));

        } else if (CampaignConstants.FIELD_RELATION_EXISTS.equals(fieldType)) {
            if (CampaignConstants.CriteriaComparator.RELATION_EXISTS.equal(operator)) {
                setDto("fieldValue", Functions.getCampaignCriterionValueRelationField(campCriterionValueId));
            }
        }
    }
}