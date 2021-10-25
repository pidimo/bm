package com.piramide.elwis.web.common.validator;

import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.FormatUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.PropertyMessageResources;
import org.apache.struts.validator.Resources;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Additional fields validator for web UI based in Struts framework.
 * This class is an extension to org.apache.struts.validator.FieldChecks to support
 * special application dependant validations.
 *
 * @author Fernando Montaño
 * @version $Id: FieldChecks.java 10102 2011-07-11 00:15:48Z miguel $
 */

public class FieldChecks extends org.apache.struts.validator.FieldChecks {

    private static final Log log = LogFactory.getLog(FieldChecks.class);


    /**
     * Checks if the field is a valid date. Obtaining the date pattern form
     * ApplicationResources property file and applying <code>java.text.SimpleDateFormat</code>.
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return A Date if valid, a null if blank or invalid.
     */
    public static boolean validDate(Object bean,
                                    ValidatorAction va, Field field,
                                    ActionErrors errors,
                                    HttpServletRequest request) {
        log.debug("Validating date in field:" + field.getProperty());
        boolean strict = true;
        boolean _return = true;
        Integer result = null;
        String value = null;

        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        MessageResources messages = (PropertyMessageResources)
                request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);

        String datePattern = Resources.getMessage(messages, locale, "datePattern");


        if (!GenericValidator.isBlankOrNull(value)) {
            strict = !"false".equals(field.getVarValue("strict"));
            Date date = null;
            log.debug("Validating Field:" + field.getProperty() + " - Value:" + value + " - ClassValue:" + value.getClass());

            date = GenericTypeValidator.formatDate(value, datePattern.trim(), strict);
            result = DateUtils.dateToInteger(date);

            if (date == null) {
                log.debug("is invalidd...");
                _return = false;
                //date = new Date();
                ((Map) bean).put(field.getProperty(), value);
                errors.add(field.getKey(), new ActionError(va.getMsg(), Resources.getMessage(request, field.getArg0().getKey()), datePattern));
            } else if (!"true".equals(field.getVarValue("toString"))) {
                ((Map) bean).put(field.getProperty(), result);
            } else {
                ((Map) bean).put(field.getProperty(), result.toString());
            }
            if ("true".equals(field.getVarValue("cloneFormat"))) {
                ((Map) bean).put(field.getProperty() + "_clone", value);
            }
        }
        log.debug("RETURN:" + _return);
        return _return;
    }

    /**
     * Checks if the field is a valid date. Obtaining the date pattern form
     * ApplicationResources property file and applying <code>java.text.SimpleDateFormat</code>.
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return A Date if valid, a null if blank or invalid.
     */
    public static boolean validDates(Object bean,
                                     ValidatorAction va, Field field,
                                     ActionErrors errors,
                                     HttpServletRequest request) {
        log.debug("Validating date in field:" + field.getProperty());
        boolean strict = true;
        boolean _return = true;
        boolean content = false;
        Integer result1 = null;
        Integer result2 = null;
        String value = null;
        Map bean_ = (Map) bean;

        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        MessageResources messages = (PropertyMessageResources) request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
        String datePattern = Resources.getMessage(messages, locale, "datePattern");
        Date date = null;
        Date date2 = null;

        if (!GenericValidator.isBlankOrNull(value)) {
            strict = !"false".equals(field.getVarValue("strict"));
            date = GenericTypeValidator.formatDate(value, datePattern.trim(), strict);
            result1 = DateUtils.dateToInteger(date);
        }

        if (!GenericValidator.isBlankOrNull((String) bean_.get(field.getVarValue("endRangeValue")))) {
            strict = !"false".equals(field.getVarValue("strict"));
            date2 = GenericTypeValidator.formatDate((bean_.get(field.getVarValue("endRangeValue"))).toString(), datePattern.trim(), strict);
            result2 = DateUtils.dateToInteger(date2);
            content = true;
        }

        if ((date == null && !GenericValidator.isBlankOrNull(value)) || (date2 == null && content)) {
            log.debug("is invalid ...");
            _return = false;
            bean_.put(field.getProperty(), value);
            bean_.put(field.getVarValue("endRangeValue"), bean_.get(field.getVarValue("endRangeValue")));
            errors.add(field.getKey(), new ActionError(va.getMsg(), Resources.getMessage(request, field.getArg0().getKey()), datePattern));
        } else if (result1 != null) {
            ((Map) bean).put(field.getProperty(), result1.toString());
        }
        if (result2 != null) {
            ((Map) bean).put(field.getVarValue("endRangeValue"), result2.toString());
        }

        log.debug("RETURN:" + _return);
        return _return;
    }

    /**
     * Checks if the field is a valid date. Obtaining the date pattern form
     * ApplicationResources property file and applying <code>java.text.SimpleDateFormat</code>.
     * Then if the date is valid, it is converted to milliseconds representation of that date.
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return A Date if valid, a null if blank or invalid.
     */
    public static boolean validDatesMillis(Object bean,
                                           ValidatorAction va, Field field,
                                           ActionErrors errors,
                                           HttpServletRequest request) {
        log.debug("Validating date in field:" + field.getProperty());
        boolean strict = true;
        boolean _return = true;
        boolean content = false;
        Integer result1 = null;
        Integer result2 = null;
        String value = null;
        Map bean_ = (Map) bean;

        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        User user = RequestUtils.getUser(request);
        DateTimeZone timeZone = (DateTimeZone) user.getValue("dateTimeZone");

        MessageResources messages = (PropertyMessageResources) request.getAttribute(org.apache.struts.Globals.MESSAGES_KEY);
        String datePattern = Resources.getMessage(messages, locale, "datePattern");
        Date date = null;
        Date date2 = null;

        if (!GenericValidator.isBlankOrNull(value)) {
            strict = !"false".equals(field.getVarValue("strict"));
            date = GenericTypeValidator.formatDate(value, datePattern.trim(), strict);
            result1 = DateUtils.dateToInteger(date);
        }

        if (!GenericValidator.isBlankOrNull((String) bean_.get(field.getVarValue("endRangeValue")))) {
            strict = !"false".equals(field.getVarValue("strict"));
            date2 = GenericTypeValidator.formatDate((bean_.get(field.getVarValue("endRangeValue"))).toString(), datePattern.trim(), strict);
            result2 = DateUtils.dateToInteger(date2);
            content = true;
        }

        if ((date == null && !GenericValidator.isBlankOrNull(value)) || (date2 == null && content)) {
            log.debug("is invalid ...");
            _return = false;
            bean_.put(field.getProperty(), value);
            bean_.put(field.getVarValue("endRangeValue"), bean_.get(field.getVarValue("endRangeValue")));
            errors.add(field.getKey(), new ActionError(va.getMsg(), Resources.getMessage(request, field.getArg0().getKey()), datePattern));
        } else if (result1 != null) {
            ((Map) bean).put(field.getProperty(),
                    Long.toString(DateUtils.integerToDateTime(result1, 0, 0, timeZone).getMillis()));
            log.debug("Field Value = " + ((Map) bean).get(field.getProperty()));
        }
        if (result2 != null) {

            ((Map) bean).put(field.getVarValue("endRangeValue"),
                    Long.toString(DateUtils.integerToDateTime(result2, 23, 59, timeZone).getMillis()));
            //todo 23:59:00.000 is not a valid in the case of end range, it must be 23:59:59:999, change afterwards.
            log.debug("Field End range Value = " + ((Map) bean).get(field.getVarValue("endRangeValue")));
        }


        return _return;
    }


    /**
     * Checks if the field can safely be converted to an int primitive.
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return An Integer positive if valid, a null otherwise.
     */
    public static Integer validateUnsignedInteger(Object bean,
                                                  ValidatorAction va, Field field,
                                                  ActionErrors errors,
                                                  HttpServletRequest request) {
        Integer result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatInt(value);
            if (result == null) {
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
            } else {
                if (result.intValue() < 0) {
                    errors.add(field.getKey(), Resources.getActionError(request, va, field));
                }
            }
        }
        return result;
    }

    /**
     * Checks if the field can safely be converted to an int primitive.
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return An Integer positive if valid, a null otherwise.
     */
    public static Integer validateUnsignedIntegers(Object bean,
                                                   ValidatorAction va, Field field,
                                                   ActionErrors errors,
                                                   HttpServletRequest request) {
        Integer result1 = null;
        Integer result2 = null;
        Integer result = new Integer(1);
        String value = null;
        boolean emptyValue = false;
        Map bean_ = (Map) bean;

        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            result1 = GenericTypeValidator.formatInt(value);
        }

        if (!GenericValidator.isBlankOrNull((String) bean_.get(field.getVarValue("endRangeValue")))) {
            result2 = GenericTypeValidator.formatInt((bean_.get(field.getVarValue("endRangeValue"))).toString());
            emptyValue = true;
        }
        if ((result1 == null && !GenericValidator.isBlankOrNull(value)) || (result2 == null && emptyValue)) {
            errors.add(field.getKey(), Resources.getActionError(request, va, field));
        } else {
            if (result1 != null) {
                if (result1.intValue() < 0) {
                    errors.add(field.getKey(), Resources.getActionError(request, va, field));
                }
            } else if (emptyValue) {
                if (result2.intValue() < 0) {
                    errors.add(field.getVarValue("endRangeValue"), Resources.getActionError(request, va, field));
                }
            }
        }

        if (errors.isEmpty()) {
            result = new Integer(-1);
        }
        return result;
    }

    public static void validatePercentage(Object bean,
                                          ValidatorAction validatorAction,
                                          Field field,
                                          ActionErrors errors,
                                          HttpServletRequest request) {
        String value;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        if (GenericValidator.isBlankOrNull(value)) {
            return;
        }

        String intPartAsString = field.getArg1().getKey();
        String floatPartAsString = field.getArg2().getKey();
        int equalSignIndexForIntegerPart = intPartAsString.lastIndexOf("=");
        int equalSignIndexForFloatPart = floatPartAsString.lastIndexOf("=");

        String resourceKey = field.getArg0().getKey();

        Integer integerPart = new Integer(intPartAsString.substring(equalSignIndexForIntegerPart + 1));
        Integer floatPart = new Integer(floatPartAsString.substring(equalSignIndexForFloatPart + 1));

        ActionError error = validatePercentage(value,
                resourceKey,
                validatorAction.getMsg(),
                integerPart,
                floatPart,
                request);

        if (null != error) {
            errors.add(field.getKey(), error);
            return;
        }

        BigDecimal discount = new BigDecimal(unformatDecimalValue(value, request));
        if (bean instanceof Map) {
            ((Map) bean).put(field.getProperty(), discount);
        }

    }

    public static ActionError validatePercentage(String value,
                                                 String resourceKey,
                                                 String errorResourceKey,
                                                 Integer integerPart,
                                                 Integer floatPart,
                                                 HttpServletRequest request) {
        if (GenericValidator.isBlankOrNull(value)) {
            return null;
        }

        ActionError decimalError = validateDecimalNumber(value, resourceKey, integerPart, floatPart, request);
        if (null != decimalError) {
            return decimalError;
        }

        BigDecimal discount = new BigDecimal(unformatDecimalValue(value, request));

        BigDecimal hundredConstant = new BigDecimal(100.00).setScale(2, RoundingMode.HALF_UP);
        BigDecimal zeroConstant = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);

        if (discount.compareTo(hundredConstant) == 1 || discount.compareTo(zeroConstant) == -1) {
            return new ActionError(errorResourceKey,
                    JSPHelper.getMessage(request, resourceKey));
        }

        return null;
    }

    private static String unformatDecimalValue(String value, HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);
        return FormatUtils.unformatingDecimalNumber(value, locale, 10, 2).toString();
    }

    /**
     * Checks if the field can safely be converted to a float primitive.
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return A Float if valid, a null otherwise.
     */
    public static Float validateUnsignedFloat(Object bean,
                                              ValidatorAction va, Field field,
                                              ActionErrors errors,
                                              HttpServletRequest request) {
        Float result = null;
        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            result = GenericTypeValidator.formatFloat(value);

            if (result == null) {
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
            } else {
                if (result.floatValue() < 0) {
                    errors.add(field.getKey(), Resources.getActionError(request, va, field));
                }
            }
        }
        return result;
    }

    /**
     * Validate if one item selected from listbox exits.
     */
    public static void validateForeignKey(Object bean,
                                          ValidatorAction va, Field field,
                                          ActionErrors errors,
                                          HttpServletRequest request) {

        String value = null;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }
        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                log.debug("property value = " + value);
                Arg arg1 = field.getArg1(va.getName());
                Arg arg2 = field.getArg2(va.getName());
                String table = arg1.getKey().substring(arg1.getKey().lastIndexOf("=") + 1);
                log.debug("Table Name = " + table);
                String column = arg2.getKey().substring(arg2.getKey().lastIndexOf("=") + 1);
                log.debug("Column name = " + column);
                ForeignkeyValidator.i.validate(table.trim(), column.trim(), value, errors,
                        Resources.getActionError(request, va, field));
            } catch (Exception e) {
                log.error("Error  validating foreign key", e);
            }
        }

    }

    public static void validateDecimalNumbers(Object bean,
                                              ValidatorAction va, Field field,
                                              ActionErrors errors,
                                              HttpServletRequest request) {
        String value = null;
        int result1 = -9;
        int result2 = -9;
        Map bean_ = (Map) bean;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        boolean withErrors = false;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        Arg arg1 = field.getArg1(va.getName()); // max int Part
        Arg arg2 = field.getArg2(va.getName()); // max float part
        Arg arg3 = field.getArg3(va.getName()); // module resource
        String maxInt = arg1.getKey().substring(arg1.getKey().lastIndexOf("=") + 1);
        String maxFloat = arg2.getKey().substring(arg2.getKey().lastIndexOf("=") + 1);

        if (!GenericValidator.isBlankOrNull((String) bean_.get(field.getVarValue("endRangeValue")))) {
            result2 = NumberFormatValidator.i.validatePositive(bean_.get(field.getVarValue("endRangeValue")).toString(), locale, new Integer(maxInt).intValue(), new Integer(maxFloat).intValue());
        }

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                log.debug("property value = " + value);
                result1 = NumberFormatValidator.i.validatePositive(value, locale, new Integer(maxInt).intValue(), new Integer(maxFloat).intValue());
            } catch (Exception e) {
                log.error("Error validating DecimalNumber ", e);
            }
        }

        if (result1 == NumberFormatValidator.INVALID || result2 == NumberFormatValidator.INVALID) {
            if (arg3 == null) {
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
            } else {
                errors.add(field.getKey(), new ActionError("errors.decimalNumber.invalid.module", Resources.getMessage(request, field.getArg0(va.getName()).getKey()), Resources.getMessage(request, arg3.getKey())));
            }
            withErrors = true;
        }
        if (result1 == NumberFormatValidator.OUT_OF_RANGE || result2 == NumberFormatValidator.OUT_OF_RANGE) {
            if (arg3 == null) {
                errors.add(field.getKey(), new ActionError("errors.NumberOutOfRange", Resources.getMessage(request,
                        field.getArg0(va.getName()).getKey())));
            } else {
                errors.add(field.getKey(), new ActionError("errors.NumberOutOfRange", Resources.getMessage(request,
                        field.getArg0(va.getName()).getKey())));
            }
            withErrors = true;
        }
        if (result1 == NumberFormatValidator.ONLY_POSITIVE || result2 == NumberFormatValidator.ONLY_POSITIVE) {
            if (arg3 == null) {
                errors.add(field.getKey(), new ActionError("errors.NumberPositives", Resources.getMessage(request,
                        field.getArg0(va.getName()).getKey())));
            } else {
                errors.add(field.getKey(), new ActionError("errors.NumberOutOfRange", Resources.getMessage(request,
                        field.getArg0(va.getName()).getKey())));
            }
            withErrors = true;
        }
        //put the correct BigDecimal
        if (!withErrors) {
            Map beanMap = (Map) bean;
            if (!GenericValidator.isBlankOrNull(value)) {
                beanMap.put(field.getProperty(), FormatUtils.unformatingDecimalNumber(value, locale,
                        new Integer(maxInt).intValue(), new Integer(maxFloat).intValue()).toString());
            }
            if (!GenericValidator.isBlankOrNull((String) bean_.get(field.getVarValue("endRangeValue")))) {
                beanMap.put(field.getVarValue("endRangeValue"), FormatUtils.unformatingDecimalNumber((String) bean_.get(field.getVarValue("endRangeValue")), locale,
                        new Integer(maxInt).intValue(), new Integer(maxFloat).intValue()).toString());
            }
        }
    }

    public static void validateDecimalNumber(Object bean,
                                             ValidatorAction va, Field field,
                                             ActionErrors errors,
                                             HttpServletRequest request) {


        String value = null;
        int result = -9;
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        boolean withErrors = false;
        if (isString(bean)) {
            value = (String) bean;
        } else {
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        Arg arg1 = field.getArg1(va.getName()); // max int Part
        Arg arg2 = field.getArg2(va.getName()); // max float part
        Arg arg3 = field.getArg3(va.getName()); // module resource
        String maxInt = arg1.getKey().substring(arg1.getKey().lastIndexOf("=") + 1);
        String maxFloat = arg2.getKey().substring(arg2.getKey().lastIndexOf("=") + 1);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                log.debug("property value = " + value);
                result = NumberFormatValidator.i.validatePositive(value, locale, new Integer(maxInt).intValue(), new Integer(maxFloat).intValue());
                if (result == NumberFormatValidator.INVALID) {
                    if (arg3 == null) {
                        //errors.add(field.getKey(), Resources.getActionError(request, va, field));
                        errors.add(field.getKey(), new ActionError("error.number.decimal.invalid",
                                JSPHelper.getMessage(request, field.getArg0(va.getName()).getKey()),
                                MessagesUtil.i.getDecimalNumberFormat(locale, new Integer(maxFloat))));

                    } else {
                        errors.add(field.getKey(), new ActionError("error.number.decimal.invalid.module",
                                Resources.getMessage(request, field.getArg0(va.getName()).getKey()),
                                Resources.getMessage(request, arg3.getKey()),
                                MessagesUtil.i.getDecimalNumberFormat(locale, new Integer(maxFloat))));
                    }
                    withErrors = true;
                }
                if (result == NumberFormatValidator.OUT_OF_RANGE) {
                    if (arg3 == null) {
                        errors.add(field.getKey(), new ActionError("errors.NumberOutOfRange", Resources.getMessage(request,
                                field.getArg0(va.getName()).getKey())));
                    } else {
                        errors.add(field.getKey(), new ActionError("errors.NumberOutOfRange", Resources.getMessage(request,
                                field.getArg0(va.getName()).getKey())));
                    }
                    withErrors = true;
                }
                if (result == NumberFormatValidator.ONLY_POSITIVE) {
                    if (arg3 == null) {
                        errors.add(field.getKey(), new ActionError("errors.NumberPositive", Resources.getMessage(request,
                                field.getArg0(va.getName()).getKey())));
                    } else {
                        errors.add(field.getKey(), new ActionError("errors.NumberOutOfRange", Resources.getMessage(request,
                                field.getArg0(va.getName()).getKey())));
                    }
                    withErrors = true;
                }
            } catch (Exception e) {
                log.error("Error validating DecimalNumber ", e);
            }

            //put the correct BigDecimal
            if (!withErrors) {
                Map beanMap = (Map) bean;
                if (!"true".equals(field.getVarValue("toString"))) {
                    beanMap.put(field.getProperty(), FormatUtils.unformatingDecimalNumber(value, locale,
                            new Integer(maxInt).intValue(), new Integer(maxFloat).intValue()));
                } else {
                    beanMap.put(field.getProperty(), FormatUtils.unformatingDecimalNumber(value, locale,
                            new Integer(maxInt).intValue(), new Integer(maxFloat).intValue()).toString());
                }
            }

        }

    }

    /*public static boolean validateBigDecimalNumber(Object bean,
                                                   ValidatorAction validatorAction,
                                                   Field field,
                                                   ActionErrors errors,
                                                   HttpServletRequest request) {
        String value;
        if(isString(bean))
            value = (String) bean;
        else
            value = ValidatorUtil.getValueAsString(bean, field.getProperty());

        field.getArg0(validatorAction.getName())
    }*/

    public static boolean validateTwoFields(Object bean, ValidatorAction va,
                                            Field field, ActionErrors errors,
                                            HttpServletRequest request) {
        String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String value2 = ValidatorUtil.getValueAsString(bean, field.getVarValue("secondProperty"));
        boolean result = false;

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                if (!value.equals(value2)) {
                    log.debug("No son iguales");
                    errors.add(field.getKey(), Resources.getActionError(request, va, field));
                    result = true;
                }
            } catch (Exception e) {
                log.debug("en el catch...");
                errors.add(field.getKey(), Resources.getActionError(request, va, field));
                result = true;
            }
        } else {
            result = true;
        }
        log.debug("THO FIELDS!!!!");
        log.debug("Result:" + result);
        return result;
    }

    private static String[] validateConditions(String condition) {
        int pos = condition.indexOf("==");
        int pos2 = condition.indexOf("!=");

        System.out.println("Condition: " + condition);

        if (pos < pos2 && pos2 > 0) {
            pos = pos2;
        }

        String left = condition.substring(0, pos).trim();
        String operator = condition.substring(pos, pos + 2).trim();
        String rigth = condition.substring(pos + 3);
        System.out.println("Left:" + left + " - operator:" + operator + " - rigth:" + rigth);
        return new String[]{left, operator, rigth};
    }

    private static String calculateValue(String element, Object bean) {
        String value = ValidatorUtil.getValueAsString(bean, element);
        if (value == null) {
            return element;
        }
        return value;
    }

    public static boolean onlyOne(Object bean, ValidatorAction va,
                                  Field field, ActionErrors errors,
                                  HttpServletRequest request) {
        String op = null;
        /*op = field.getVarValue("operation");*/
        /*log.debug("DTO.OP:" + request.getParameter(op != null ? op : "dto(op)"));
        boolean isUpdate = "update".equals(request.getParameter(op != null ? op : "dto(op)"));*/

        String tableDB = field.getVarValue("tableDB");
        String fieldDB = field.getVarValue("fieldDB");
        String validateWhen = field.getVarValue("validateWhen");

        log.debug("validateWhen:" + validateWhen);


        if (fieldDB == null) {
            fieldDB = field.getProperty();
        }


        boolean evaluate = true;

        if (validateWhen != null) {
            String[] conditions = validateWhen.split("&");
            for (int i = 0; i < conditions.length; i++) {
                String conditionStr = conditions[i].trim();
                String[] condition = validateConditions(conditionStr);
                String left = calculateValue(condition[0], bean);
                String operator = condition[1];
                String right = calculateValue(condition[2], bean);
                System.out.println("CALCULATED VALUE--> Left:" + left + " - operator:" + operator + " - rigth:" + right);
                if ("==".equals(operator)) {
                    evaluate = evaluate && left.equals(right);
                } else if ("!=".equals(operator)) {
                    evaluate = evaluate && !left.equals(right);
                }

                if (!evaluate) {
                    break;
                }
            }
        }

        if (evaluate) {
            Map conditions = processConditions(field.getVarValue("conditionsDB"), bean, request, false);
            Map constantConditions = processConditions(field.getVarValue("constantConditionsDB"), bean, request, true);
            String fieldValueForm = ValidatorUtil.getValueAsString(bean, field.getProperty());
            log.debug("conditions:" + conditions + " - constantConditions:" + constantConditions);
            if (!DataBaseValidator.i.isOnlyOne(tableDB, fieldDB, fieldValueForm, conditions, constantConditions)) {
                errors.add(field.getKey(), new ActionError(va.getMsg(), JSPHelper.getMessage(request, field.getArg0().getKey()),
                        JSPHelper.getMessage(request, field.getArg1().getKey())));
                return true;
            }
        }
        return false;
    }

    private static Map processConditions(String conditions, Object bean, HttpServletRequest request, boolean constantCondition) {
        Map map = new HashMap();
        log.debug("CONDITIONS:" + conditions);
        if (conditions != null) {
            for (StringTokenizer tokenizer = new StringTokenizer(conditions, ","); tokenizer.hasMoreTokens();) {
                String dbField;
                String webField = dbField = tokenizer.nextToken().trim();
                int i = webField.indexOf("=");
                if (i > 0) {
                    webField = dbField.substring(i + 1);
                    dbField = dbField.substring(0, i);
                }

                String value = webField;
                if (!constantCondition) {
                    if (webField.indexOf(".") != -1) {
                        String[] var = webField.split("\\.");
                        if ("session".equals(var[0]) && "user".equals(var[1])) {
                            value = RequestUtils.getUser(request).getValue(var[2]).toString();
                        }
                    } else {
                        value = ValidatorUtil.getValueAsString(bean, webField);
                        if (value == null && "companyId".equals(webField)) {
                            User user = RequestUtils.getUser(request);
                            value = user.getValue("companyId").toString();
                        }
                    }
                }


                map.put(dbField, value);
            }
        }
        return map;
    }

    public static boolean unique(Object bean, ValidatorAction va,
                                 Field field, ActionErrors errors,
                                 HttpServletRequest request) {
        String op = null;
        op = field.getVarValue("operation");
        log.debug("DTO.OP:" + request.getParameter(op != null ? op : "dto(op)"));
        boolean isUpdate = "update".equals(request.getParameter(op != null ? op : "dto(op)"));
        String tableDB = field.getVarValue("tableDB");
        String fieldDB = field.getVarValue("fieldDB");

        int posFieldId = field.getVarValue("fieldIdDB").indexOf("=");
        String fieldIdDB = null;
        String fieldIdForm = null;
        String fieldIdValue = null;
        if (isUpdate) {
            if (posFieldId > 0) {
                fieldIdForm = field.getVarValue("fieldIdDB").substring(0, posFieldId).trim();
                fieldIdDB = field.getVarValue("fieldIdDB").substring(posFieldId + 1).trim();
            } else {
                fieldIdDB = fieldIdForm = field.getVarValue("fieldIdDB");
            }
            fieldIdValue = ValidatorUtil.getValueAsString(bean, fieldIdForm);
        }


        String fieldValueDB = ValidatorUtil.getValueAsString(bean, field.getProperty());
        String conditions = field.getVarValue("conditionsDB");
        if (fieldDB == null) {
            fieldDB = field.getProperty();
        }
        Map map = processConditions(conditions, bean, request, false);
        Map constantConditions = processConditions(field.getVarValue("constantConditionsDB"), bean, request, true);
        Map resultMap = new HashMap();
        resultMap.putAll(map);
        resultMap.putAll(constantConditions);
        if (DataBaseValidator.i.isDuplicate(tableDB, fieldDB, fieldValueDB, fieldIdDB, fieldIdValue, resultMap, isUpdate)) {
            errors.add(field.getKey(), new ActionError(va.getMsg(), fieldValueDB));
            return true;
        }
        return false;
    }

    public static boolean checkEntry(Field field, ActionErrors errors, HttpServletRequest request) {
        boolean check = false;
        log.debug(" .....  checkEntries function execute .....");
        String op = null;
        op = field.getVarValue("operation");
        boolean isCreate = "create".equals(request.getParameter(op != null ? op : "dto(op)"));
        String tableDB = field.getVarValue("tableDB");
        String moduleId = field.getVarValue("moduleId");
        User user = RequestUtils.getUser(request);

        if (isCreate) {
            if (!DataBaseValidator.i.checkEntries(tableDB, moduleId, user.getValue("companyId").toString())) {
                errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                check = true;//have error
                log.debug(" ... have error ..." + check);
            }
        }
        return check;
    }

    /**
     * Validate if one item selected from listbox exits (compound key).
     */
    public static void validateCompoundForeignKey(Object bean, ValidatorAction va, Field field, ActionErrors errors,
                                                  HttpServletRequest request) {

        String propertyValue = null;
        if (isString(bean)) {
            propertyValue = (String) bean;
        } else {
            propertyValue = ValidatorUtil.getValueAsString(bean, field.getProperty());
        }

        //only if property is defined.
        if (!GenericValidator.isBlankOrNull(propertyValue)) {
            log.debug("Validating compound foreign key");
            String tableDB = field.getVarValue("tableDB");
            Map values = new LinkedHashMap();
            int i = 0;

            while (!GenericValidator.isBlankOrNull(field.getVarValue("field[" + i + "]"))) {
                String fieldValue = ValidatorUtil.getValueAsString(bean, field.getVarValue("field[" + i + "]"));
                if (!GenericValidator.isBlankOrNull(fieldValue)) {
                    values.put(field.getVarValue("fieldDB[" + i + "]"), fieldValue);
                }
                i++;
            }

            try {
                ForeignkeyValidator.i.validate(tableDB, values, errors, Resources.getActionError(request, va, field));
            } catch (Exception e) {
                log.error("Error  validating compound foreign key", e);
            }
        }


    }

    public static boolean validateEmail(Object bean,
                                        ValidatorAction va,
                                        Field field,
                                        ActionErrors errors,
                                        HttpServletRequest request) {
        String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        if (!EmailValidator.i.isValid(value)) {
            errors.add(field.getKey(),
                    new ActionError(va.getMsg(), JSPHelper.getMessage(request, field.getArg0().getKey()))
            );

            return false;
        }

        return true;
    }

    /**
     * Checks if the list of mails be correct .
     *
     * @param bean    The bean validation is being performed on.
     * @param va      The <code>ValidatorAction</code> that is currently being performed.
     * @param field   The <code>Field</code> object associated with the current
     *                field being validated.
     * @param errors  The <code>ActionErrors</code> object to add errors to if any
     *                validation errors occur.
     * @param request Current request object.
     * @return A true if valid, a false otherwise.
     */
    public static boolean validateEmailList(Object bean,
                                            ValidatorAction va, Field field,
                                            ActionErrors errors,
                                            HttpServletRequest request) {

        log.debug("Validating email in field:" + field.getProperty());
        String value = null;
        boolean _return = true;
        value = ValidatorUtil.getValueAsString(bean, field.getProperty());

        log.debug("List of emails:" + value);

        StringTokenizer st = new StringTokenizer(value.trim(), ",");
        boolean flagQuote = false;
        String dirEmail = "";

        while (st.hasMoreTokens()) {
            String cad = st.nextToken();
            cad = cad.trim();

            if (!GenericValidator.isBlankOrNull(cad)) {

//only email and not start with '\"'
                if ((cad.indexOf("\"") == -1 && !flagQuote) || (cad.indexOf("\"") != -1 && !flagQuote && !cad.startsWith("\""))) {
                    dirEmail = cad;
                    if (!EmailValidator.i.isValid(dirEmail.trim())) {
                        errors.add(field.getKey(), new ActionError(va.getMsg(), dirEmail));
                        _return = false;
                    }
                    dirEmail = "";

// 2 quotes
                } else if (cad.indexOf("\"") != -1 && cad.lastIndexOf("\"") != -1 && (cad.indexOf("\"") != cad.lastIndexOf("\""))) {

                    if (dirEmail.length() > 0) {

                        dirEmail = dirEmail + cad;
                        errors.add(field.getKey(), new ActionError(va.getMsg(), dirEmail));
                        dirEmail = "";
                        flagQuote = false;

                    } else {

                        dirEmail = cad;
                        if (dirEmail.lastIndexOf("<") != -1 && dirEmail.endsWith(">") && (dirEmail.substring(0, dirEmail.lastIndexOf("<")).trim().endsWith("\""))) {
                            int beginStr = dirEmail.lastIndexOf("<");
                            int endStr = dirEmail.lastIndexOf(">");
                            String compoundDirEmail = dirEmail.substring(beginStr + 1, endStr);
                            if (!EmailValidator.i.isValid(compoundDirEmail.trim())) {
                                errors.add(field.getKey(), new ActionError(va.getMsg(), compoundDirEmail));
                                _return = false;
                            }
                        } else {
                            errors.add(field.getKey(), new ActionError(va.getMsg(), dirEmail));
                            _return = false;
                        }

                        dirEmail = "";
                        flagQuote = false;
                    }

// 1 quote and flagQuote in false
                } else if (cad.indexOf("\"") != -1 && !flagQuote) {
                    dirEmail = dirEmail + cad + ", ";
                    flagQuote = true;

// not quote and flagQuote in true
                } else if (cad.indexOf("\"") == -1 && flagQuote) {
                    dirEmail = dirEmail + cad + ", ";

// 1 quote and flagQuote in true
                } else if (cad.indexOf("\"") != -1 && flagQuote) {

                    dirEmail = dirEmail + cad;
                    if (dirEmail.lastIndexOf("<") != -1 && dirEmail.endsWith(">") && (dirEmail.substring(0, dirEmail.lastIndexOf("<")).trim().endsWith("\""))) {
                        int beginStr = dirEmail.lastIndexOf("<");
                        int endStr = dirEmail.lastIndexOf(">");
                        String compoundDirEmail = dirEmail.substring(beginStr + 1, endStr);

                        if (!EmailValidator.i.isValid(compoundDirEmail.trim())) {
                            errors.add(field.getKey(), new ActionError(va.getMsg(), compoundDirEmail));
                            _return = false;
                        }
                    } else {
                        errors.add(field.getKey(), new ActionError(va.getMsg(), dirEmail));
                        _return = false;
                    }
                    dirEmail = "";
                    flagQuote = false;

                }

            }
        }
        return _return;
    }

    public static ActionError validateDecimalValue(DefaultForm defaultForm,
                                                   String value,
                                                   String dtoKey,
                                                   String resourceKey,
                                                   boolean isRequired,
                                                   int integerPartSize,
                                                   int floatPartSize,
                                                   HttpServletRequest request) {
        if (isRequired) {
            if (GenericValidator.isBlankOrNull(value)) {
                return new ActionError("errors.required", JSPHelper.getMessage(request, resourceKey));
            }
        }

        ActionError decimalValidation =
                FieldChecks.validateDecimalNumber(value, "ActionPosition.price", integerPartSize, floatPartSize, request);

        if (null != decimalValidation) {
            return new ActionError("error.decimalNumber.invalid", JSPHelper.getMessage(request,
                    "ActionPosition.price"));
        }

        defaultForm.setDto(dtoKey, new BigDecimal(unformatDecimalValue(value, request)));
        return null;

    }

    public static ActionError validateDecimalNumber(String value,
                                                    String resourceKey,
                                                    int intPart,
                                                    int decimalPart,
                                                    HttpServletRequest request) {
        Locale locale = (Locale) Config.get(request.getSession(), Config.FMT_LOCALE);

        if (!(NumberFormatValidator.VALID == NumberFormatValidator.i.validate(value, locale, intPart, decimalPart))) {
            return new ActionError("error.number.decimal.invalid", JSPHelper.getMessage(request, resourceKey), MessagesUtil.i.getDecimalNumberFormat(locale, decimalPart));
        }

        return null;
    }

    public static ActionError validateDate(String value,
                                           String resourceKey,
                                           HttpServletRequest request) {
        Arg arg0 = new Arg();
        arg0.setKey(resourceKey);

        Field f = new Field();
        f.setProperty("dateProperty");
        f.addArg0(arg0);

        Map bean = new HashMap();
        bean.put("dateProperty", value);

        if (!FieldChecks.validDate(bean, new ValidatorAction(), f, new ActionErrors(), request)) {
            return new ActionError("errors.date", JSPHelper.getMessage(request, resourceKey),
                    JSPHelper.getMessage(request, "datePattern"));
        }

        return null;
    }

    /**
     * This validator actually works like a converted for single check boxes that aims to
     * be a true/false representantion of a bean holding a Boolean as a property.
     * Note: This works when data is sent from client-> server. If you want to have a checkbox
     * automatically checked when reading from database, such a checkbox must have "true" as value.
     *
     * @param bean  the bean to update
     * @param field the field to make the convertion
     */
    public static void booleanCheckBox(Object bean, Field field) {
        String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
        @SuppressWarnings({"unchecked"})
        Map<Object, Object> beanMap = (Map<Object, Object>) bean;
        if (GenericValidator.isBlankOrNull(value)) {
            beanMap.put(field.getProperty(), false);
        } else {
            beanMap.put(field.getProperty(), true);
        }
    }
}
