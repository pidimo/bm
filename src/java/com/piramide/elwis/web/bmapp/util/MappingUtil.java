package com.piramide.elwis.web.bmapp.util;

import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Util to mapping DTO property values
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class MappingUtil {
    private static Log log = LogFactory.getLog(MappingUtil.class);

    public static void mappingProperty(String propertyName, Map dtoMap, DefaultForm defaultForm) {
        if (dtoMap.containsKey(propertyName)) {
            defaultForm.setDto(propertyName, dtoMap.get(propertyName));
        }
    }

    public static void mappingPropertyAsString(String propertyName, Map dtoMap, DefaultForm defaultForm) {
        if (dtoMap.get(propertyName) != null) {
            defaultForm.setDto(propertyName, dtoMap.get(propertyName).toString());
        }
    }

    public static void mappingPropertyBooleanAsOn(String propertyName, Map dtoMap, DefaultForm defaultForm) {
        defaultForm.getDtoMap().remove(propertyName);

        if (dtoMap.get(propertyName) != null) {
            String value = dtoMap.get(propertyName).toString().toLowerCase();

            if ("true".equals(value) || "1".equals(value) || "on".equals(value)) {
                defaultForm.setDto(propertyName, "on");
            }
        }
    }

    public static Map getAllPropertiesAsString(ResultDTO resultDTO) {
        Map map = new HashMap();

        for (Object key : resultDTO.keySet()) {
            if (resultDTO.get(key) != null) {
                map.put(key, resultDTO.get(key).toString());
            } else {
                map.put(key, resultDTO.get(key));
            }
        }
        return map;
    }

    public static void mappingPropertyAsString(String property, ResultDTO resultDTO, DefaultForm defaultForm) {
        if (resultDTO.get(property) != null) {
            defaultForm.setDto(property, resultDTO.get(property).toString());
        }
    }

    public static void mappingBooleanPropertyFalseAsNULL(String property, ResultDTO resultDTO, DefaultForm defaultForm) {
        if (resultDTO.get(property) != null) {

            if (resultDTO.get(property) instanceof Boolean) {
                Boolean isBoolean = (Boolean) resultDTO.get(property);
                defaultForm.setDto(property, (isBoolean) ? isBoolean.toString() : null);
            } else {
                defaultForm.setDto(property, ("true".equals(resultDTO.get(property).toString()) ? "true" : null));
            }
        }
    }

    public static void mappingBirthdayProperty(String property, ResultDTO resultDTO, DefaultForm defaultForm, HttpServletRequest request) {
        if (resultDTO.get(property) != null && !GenericValidator.isBlankOrNull(resultDTO.get(property).toString())) {

            Integer valueAsInteger = new Integer(resultDTO.get(property).toString());
            String datePattern = JSPHelper.getMessage(request, "datePattern");

            if ("true".equals(resultDTO.get("dateWithoutYear"))) {
                datePattern = JSPHelper.getMessage(request, "withoutYearPattern");
            }

            Date date = DateUtils.integerToDate(valueAsInteger);
            String valueFormatted = DateUtils.parseDate(date, datePattern);

            defaultForm.setDto(property, valueFormatted);
        }
    }

    public static void mappingDateProperty(String property, ResultDTO resultDTO, DefaultForm defaultForm, HttpServletRequest request) {
        if (resultDTO.get(property) != null && !GenericValidator.isBlankOrNull(resultDTO.get(property).toString())) {

            Integer valueAsInteger = new Integer(resultDTO.get(property).toString());
            String datePattern = JSPHelper.getMessage(request, "datePattern");

            Date date = DateUtils.integerToDate(valueAsInteger);
            String valueFormatted = DateUtils.parseDate(date, datePattern);

            defaultForm.setDto(property, valueFormatted);
        }
    }

    public static void mappingTelecomTypes(Map<String, TelecomWrapperDTO> formTelecomMap, HttpServletRequest request) {

        if (formTelecomMap != null) {
            Map<String, TelecomWrapperDTO> currentTelecomTypesMap = AddressContactPersonHelper.getDefaultTelecomTypes(request);

            for (String telecomTypeId : formTelecomMap.keySet()) {
                if (currentTelecomTypesMap.containsKey(telecomTypeId)) {
                    TelecomWrapperDTO formTelecomWrapperDTO = formTelecomMap.get(telecomTypeId);
                    TelecomWrapperDTO telecomWrapperDTO = currentTelecomTypesMap.get(telecomTypeId);

                    formTelecomWrapperDTO.setTelecomTypeType(telecomWrapperDTO.getTelecomTypeType());
                    formTelecomWrapperDTO.setTelecomTypePosition(telecomWrapperDTO.getTelecomTypePosition());
                }
            }
        }
    }

}
