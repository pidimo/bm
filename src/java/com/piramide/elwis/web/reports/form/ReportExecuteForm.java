package com.piramide.elwis.web.reports.form;

import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.StructureManager;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.type.DBType;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.reports.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Report execute form, validate execute fields
 *
 * @author Miky
 * @version $Id: ReportExecuteForm.java 10342 2013-03-29 00:12:40Z miguel $
 */
public class ReportExecuteForm extends DefaultForm {

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate ReportExecuteForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        errors = validateReportFilters(request, errors);
        return errors;
    }

    private ActionErrors validateReportFilters(HttpServletRequest request, ActionErrors errors) {
        log.debug("Executing validateReportFilters method.....");
        Map filterValueChangedMap = new HashMap();

        Integer reportId = new Integer(getDto("reportId").toString());
        List reportFilterDTOs = Functions.getAllReportFilters(reportId.toString(), request);
        for (Object reportFilterDTO : reportFilterDTOs) {
            Map filterDto = (Map) reportFilterDTO;
            String filterId = filterDto.get("filterId").toString();

            if (getDto("keyFilter" + filterId) != null) {

                //form dto attributes
                String date_dtoKey = "date" + filterId;
                String value_dtoKey = "value" + filterId;
                String date1_dtoKey = "date1" + filterId;
                String date2_dtoKey = "date2" + filterId;
                String value1_dtoKey = "value1" + filterId;
                String value2_dtoKey = "value2" + filterId;
                String select1_dtoKey = "select1" + filterId;
                String popupIdValue_dtoKey = "popupIdValue" + filterId;
                String multipleSelect_dtoKey = "multipleSelect" + filterId;

                String filterType_dtoKey = "filterType" + filterId;

                //validate form
                List valuesList = new LinkedList();
                String filterOperator = null;

                int columnType = Integer.parseInt(getDto("columnType" + filterId).toString());
                String opSelectValue = getDto("opSelect" + filterId).toString();
                String strView = getValueByKey(opSelectValue, ReportConstants.KEY_SEPARATOR_SHOWVIEW);
                filterOperator = getValueByKey(opSelectValue, ReportConstants.KEY_SEPARATOR_OP);

                if (strView != null) {
                    Integer view = new Integer(strView);
                    if (view.equals(ReportConstants.SHOW_ONE_BOX)) {
                        String value;
                        if (opSelectValue.indexOf(ReportConstants.KEY_ISDATETYPE) != -1) {
                            value = (String) getDto(date_dtoKey);
                            String dateValue = validateFilterValue(value, columnType, errors, request, filterId);
                            if (errors.isEmpty()) {
                                valuesList.add(dateValue);
                            }
                        } else {
                            value = (String) getDto(value_dtoKey);
                            valuesList.add(validateFilterValue(value, columnType, errors, request, filterId));
                        }

                    } else if (view.equals(ReportConstants.SHOW_TWO_BOX)) {
                        String value1;
                        String value2;
                        if (opSelectValue.indexOf(ReportConstants.KEY_ISDATETYPE) != -1) {
                            value1 = (String) getDto(date1_dtoKey);
                            value2 = (String) getDto(date2_dtoKey);
                            String dateValue1 = validateFilterValue(value1, columnType, errors, request, filterId);
                            String dateValue2 = validateFilterValue(value2, columnType, errors, request, filterId);
                            if (errors.isEmpty()) {
                                valuesList.add(dateValue1);
                                valuesList.add(dateValue2);
                            }
                        } else {
                            value1 = (String) getDto(value1_dtoKey);
                            value2 = (String) getDto(value2_dtoKey);
                            value1 = validateFilterValue(value1, columnType, errors, request, filterId);
                            value2 = validateFilterValue(value2, columnType, errors, request, filterId);

                            valuesList.add(value1);
                            valuesList.add(value2);
                        }
                    } else if (view.equals(ReportConstants.SHOW_SELECT)) {
                        String sValue = (String) getDto(select1_dtoKey);
                        valuesList.add(validateFilterValue(sValue, columnType, errors, request, filterId));

                    } else if (view.equals(ReportConstants.SHOW_MULTIPLESELECT)) {

                        String mSelect = (String) getDto(multipleSelect_dtoKey);
                        if (!GenericValidator.isBlankOrNull(mSelect)) {
                            String mSelectValues[] = mSelect.split(ReportConstants.FILTERVALUE_SEPARATOR_REGULAREXP);
                            for (int i = 0; i < mSelectValues.length; i++) {
                                String sValue = mSelectValues[i];
                                validateFilterValue(sValue, columnType, errors, request, filterId);
                                valuesList.add(sValue);
                            }

                            request.setAttribute("mSelectValue" + filterId, mSelect);
                        } else {
                            errors.add("required", composeFilterError("errors.required", filterId, request));
                        }

                    } else if (view.equals(ReportConstants.SHOW_POPUP)) {
                        String value = (String) getDto(popupIdValue_dtoKey);
                        if (GenericValidator.isBlankOrNull(value)) {
                            if (isAllowedNullValues()) {
                                valuesList.add(value);
                            } else {
                                errors.add("required", composeFilterError("errors.required", filterId, request));
                            }
                        } else {
                            valuesList.add(value);
                        }

                    }

                }

                if (errors.isEmpty()) {

                    //filter type rewrite if is necessary
                    if (getDto(filterType_dtoKey).toString().equals(ReportConstants.FILTER_WITH_DB_VALUE.toString()) &&
                            Functions.isDBFilterOperator(filterOperator)) {
                        valuesList = composePkResultValues(valuesList);
                    }

                    filterValueChangedMap.put(filterId, valuesList);
                }
            }
        }

        //set in request filter changed values
        request.setAttribute("filterValuesMap", filterValueChangedMap);

        return errors;
    }


    private String validateFilterValue(String value, int columnType, ActionErrors errors, HttpServletRequest request, String filterId) {
        String newValue = value;
        String path_dtoKey = "path" + filterId;

        if (GenericValidator.isBlankOrNull(value)) {
            if (!isAllowedNullValues()) {
                errors.add("required", composeFilterError("errors.required", filterId, request));
            }
        } else if (columnType != DBType.DBTypeNameAsInt.STRING) {

            if (columnType == DBType.DBTypeNameAsInt.INTEGER ||
                    columnType == DBType.DBTypeNameAsInt.SHORT) {
                if (!GenericValidator.isInt(value)) {
                    errors.add("int", composeFilterError("errors.integer", filterId, request));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.LONG) {
                if (!GenericValidator.isLong(value)) {
                    errors.add("long", composeFilterError("errors.long", filterId, request));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.FLOAT) {
                if (!GenericValidator.isFloat(value)) {
                    errors.add("float", composeFilterError("errors.float", filterId, request));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.DECIMAL) {

                String fieldPath = Functions.decodeFieldPath(getDto(path_dtoKey).toString());
                StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
                Field field = structureManager.getFieldByTitusPath(fieldPath);
                if (field.getConverter() != null) {

                    ///validate with converter
                    ResultValue resultValue = Functions.viewToDbConverterInSimpleField(Functions.decodeFieldPath(getDto(path_dtoKey).toString()), value, request);
                    if (resultValue.hasErrors()) {
                        if (resultValue.errorIsResource()) {
                            if (resultValue.getResourceParams() != null) {
                                errors.add("decimal", composeConverterFilterError(resultValue.getErrorMessage(), filterId, resultValue.getResourceParams(), request));
                            } else {
                                errors.add("decimal", composeConverterFilterError(resultValue.getErrorMessage(), filterId, null, request));
                            }
                        } else {
                            errors.add("decimal", composeConverterFilterError(resultValue.getErrorMessage(), filterId));
                        }
                    } else {
                        newValue = resultValue.getValue().toString();
                    }
                } else {
                    if (!GenericValidator.isFloat(value)) {
                        errors.add("float", composeFilterError("errors.float", filterId, request));
                    }
                }

            } else if (columnType == DBType.DBTypeNameAsInt.DOUBLE) {
                if (!GenericValidator.isDouble(value)) {
                    errors.add("double", composeFilterError("errors.double", filterId, request));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.DATE ||
                    columnType == DBType.DBTypeNameAsInt.DATETIME) {

                ///validate with converter
                ResultValue resultValue = Functions.viewToDbConverterInSimpleField(Functions.decodeFieldPath(getDto(path_dtoKey).toString()), value, request);
                if (resultValue.hasErrors()) {

                    if (resultValue.errorIsResource()) {
                        if (resultValue.getResourceParams() != null) {
                            errors.add("dateText", composeConverterFilterError(resultValue.getErrorMessage(), filterId, resultValue.getResourceParams(), request));
                        } else {
                            errors.add("dateText", composeConverterFilterError(resultValue.getErrorMessage(), filterId, null, request));
                        }
                    } else {
                        errors.add("dateText", composeConverterFilterError(resultValue.getErrorMessage(), filterId));
                    }
                } else {
                    newValue = resultValue.getValue().toString();
                }
            } else if (columnType == DBType.DBTypeNameAsInt.BOOLEAN) {
                log.debug("boolean validation...");
                //log.debug(Boolean.valueOf("-1"));

            } else {
                errors.add("common", new ActionError("msg.ServerError"));
                log.debug("not valid type..........");
            }
        }
        return newValue;
    }

    /**
     * text parser, get substring that be between the key text
     *
     * @param text text to parser
     * @param key  key to parser
     * @return String data
     */
    private String getValueByKey(String text, String key) {
        String res = null;
        int firstIndex = text.indexOf(key);
        int lastIindex = text.lastIndexOf(key);
        if (firstIndex != lastIindex) {
            res = text.substring((firstIndex + key.length()), lastIindex).trim();
        }

        return res;
    }

    private List composePkResultValues(List valuesList) {
        List pkValuesList = new LinkedList();
        if (valuesList.size() > 0 && ((String) valuesList.get(0)).indexOf(ReportConstants.PRIMARYKEY_SEPARATOR) != -1) {
            for (int i = 0; i < valuesList.size(); i++) {
                String pkValue = (String) valuesList.get(i);
                String[] array = pkValue.split(ReportConstants.PRIMARYKEY_SEPARATOR_REGULAREXP);
                for (int j = 0; j < array.length; j++) {
                    pkValuesList.add(array[j]);
                }
            }
        } else {
            pkValuesList = valuesList;
        }
        return pkValuesList;
    }

    private ActionError composeFilterError(String key, String filterId, HttpServletRequest request) {
        String filterColumnLabel = (String) getDto("columnLabel" + filterId);
        String valueMessage = JSPHelper.getMessage(request, "Report.filter.value");
        return new ActionError("Report.execute.filterError", filterColumnLabel, JSPHelper.getMessage(request, key, valueMessage));
    }

    private ActionError composeConverterFilterError(String key, String filterId, Object params[], HttpServletRequest request) {
        String filterColumnLabel = (String) getDto("columnLabel" + filterId);
        String converterErrorMessage = (params != null ? JSPHelper.getMessage(request, key, params) : JSPHelper.getMessage(request, key));
        return new ActionError("Report.execute.filterError", filterColumnLabel, converterErrorMessage);
    }

    private ActionError composeConverterFilterError(String errorMessage, String filterId) {
        String filterColumnLabel = (String) getDto("columnLabel" + filterId);
        return new ActionError("Report.execute.filterError", filterColumnLabel, errorMessage);
    }

    protected boolean isAllowedNullValues() {
        return false;
    }

}

