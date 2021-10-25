package com.piramide.elwis.web.reports.form;

import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.StructureManager;
import com.jatun.titus.listgenerator.structure.converter.ResultValue;
import com.jatun.titus.listgenerator.structure.type.DBType;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.reports.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: FilterCreateForm.java 10342 2013-03-29 00:12:40Z miguel $
 */

public class FilterCreateForm extends DefaultForm {

    public void setMultipleSelect(Object[] obj) {
        List list = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            list.add(obj[i]);
        }
        setDto("listMultipleSelect", list);
    }

    public Object[] getMultipleSelect() {
        List list = (List) getDto("listMultipleSelect");
        if (list != null) {
            return list.toArray();
        } else {
            return new Object[]{};
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate FilterCreateForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        //validate init table reference
        if (getDto("op").equals("create")) {
            LightlyReportCmd cmd = new LightlyReportCmd();
            cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
            cmd.setOp("read");

            ResultDTO resultDTO = null;
            try {
                resultDTO = BusinessDelegate.i.execute(cmd, request);
            } catch (AppLevelException e) {
                log.debug("Error in execute LightlyReportCmd....");
            }
            if (resultDTO != null && resultDTO.containsKey("initialTableReference")) {

                TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
                if (treeView != null) {
                    String module = (String) resultDTO.get("module");
                    String functionality = (String) resultDTO.get("initialTableReference");

                    String treeViewFunctionality = treeView.getTreeModel().getName();
                    log.debug("FUNCT::" + functionality);
                    log.debug("MOD::" + module);
                    log.debug("TREEEEEEE FUNCT:" + treeViewFunctionality);
                    log.debug("TREEEEEEE MOD:" + treeView.getModule());
                    if (!module.equals(treeView.getModule()) || !functionality.equals(treeViewFunctionality)) {
                        errors.add("initTableError", new ActionError("Common.error.concurrency"));
                        setDto("columnReference", null); //to be that field be required

                        //remove instance to update the tree
                        TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
                        return errors;
                    }
                }
            }
        }

        //validate form
        List valuesList = new LinkedList();
        String filterOperator = null;
        if (getDto("opSelect") != null && !GenericValidator.isBlankOrNull(getDto("columnReference").toString())) {
            int columnType = Integer.parseInt(getDto("columnType").toString());
            String opSelectValue = getDto("opSelect").toString();
            String strView = getValueByKey(opSelectValue, ReportConstants.KEY_SEPARATOR_SHOWVIEW);
            filterOperator = getValueByKey(opSelectValue, ReportConstants.KEY_SEPARATOR_OP);
            if (filterOperator != null && strView != null) {
                Integer view = new Integer(strView);
                if (view.equals(ReportConstants.SHOW_ONE_BOX)) {
                    String value;
                    if (opSelectValue.indexOf(ReportConstants.KEY_ISDATETYPE) != -1) {
                        value = (String) getDto("date");
                        String dateValue = validateFilterValue(value, columnType, errors, request);
                        if (errors.isEmpty()) {
                            valuesList.add(dateValue);
                        }
                    } else {
                        value = (String) getDto("value");
                        valuesList.add(validateFilterValue(value, columnType, errors, request));
                    }

                } else if (view.equals(ReportConstants.SHOW_TWO_BOX)) {
                    String value1;
                    String value2;
                    if (opSelectValue.indexOf(ReportConstants.KEY_ISDATETYPE) != -1) {
                        value1 = (String) getDto("date1");
                        value2 = (String) getDto("date2");
                        String dateValue1 = validateFilterValue(value1, columnType, errors, request);
                        String dateValue2 = validateFilterValue(value2, columnType, errors, request);
                        if (errors.isEmpty()) {
                            valuesList.add(dateValue1);
                            valuesList.add(dateValue2);
                        }
                    } else {
                        value1 = (String) getDto("value1");
                        value2 = (String) getDto("value2");
                        value1 = validateFilterValue(value1, columnType, errors, request);
                        if (errors.isEmpty()) {
                            value2 = validateFilterValue(value2, columnType, errors, request);
                        }
                        valuesList.add(value1);
                        valuesList.add(value2);
                    }
                } else if (view.equals(ReportConstants.SHOW_SELECT)) {
                    String sValue = (String) getDto("select1");
                    valuesList.add(validateFilterValue(sValue, columnType, errors, request));

                } else if (view.equals(ReportConstants.SHOW_MULTIPLESELECT)) {
                    List selectList = (List) getDto("listMultipleSelect");
                    if (selectList != null) {
                        for (Iterator iterator = selectList.iterator(); iterator.hasNext();) {
                            String sValue = (String) iterator.next();
                            validateFilterValue(sValue, columnType, errors, request);

                            valuesList.add(sValue);
                            if (!errors.isEmpty()) {
                                break;
                            }
                        }
                    } else {
                        errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.filter.value")));
                    }
                } else if (view.equals(ReportConstants.SHOW_POPUP)) {
                    String value = (String) getDto("popupIdValue");
                    if (GenericValidator.isBlankOrNull(value)) {
                        if (isAllowedNullValues()) {
                            valuesList.add(value);
                        } else {
                            errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.filter.value")));
                        }
                    } else {
                        valuesList.add(value);
                    }
                }
            } else {
                errors.add("operator", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.filter.operator")));
            }

            if (!errors.isEmpty()) {
                request.setAttribute("hasErrors", "true");
            }
        } else {
            errors.add("field", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.filter.field")));
        }

        //set in dto
        setDto("operator", filterOperator);
        if (errors.isEmpty()) {
            //decode field path
            if (getDto("path") != null) {
                setDto("path", Functions.decodeFieldPath(getDto("path").toString()));
            }

            //filter type rewrite if is necessary
            if (Functions.isDbFilter(getDto("tableReference").toString(), getDto("columnReference").toString(), request) &&
                    Functions.isDBFilterOperator(filterOperator)) {
                valuesList = composePkValues(valuesList);
                setDto("filterType", ReportConstants.FILTER_WITH_DB_VALUE);
            } else {
                setDto("filterType", ReportConstants.FILTER_WITH_CONSTANT_VALUE);
            }

            if (valuesList.size() > 0) {
                setDto("values", valuesList);
            }

            //add categoryId only to dynamic columns
            String titusPath = (String) getDto("path");
            if (titusPath != null && TitusPathUtil.isDynamicColumn(titusPath)) {
                StructureManager manager = Titus.getStructureManager(request.getSession().getServletContext());
                if (manager.existDynamicColumnField(titusPath, Functions.getCategoryDynamicColumnParams(request))) {
                    String categoryId = TitusPathUtil.getDynamicColumnId(titusPath);
                    if (categoryId != null) {
                        setDto("categoryId", categoryId);
                    }
                } else {
                    errors.add("fieldNotFound", new ActionError("Report.filter.dynamicColumn.error.deleted"));
                    request.setAttribute("hasErrors", "true");
                }
            }
        }

        return errors;
    }

    private String validateFilterValue(String value, int columnType, ActionErrors errors, HttpServletRequest request) {
        String newValue = value;

        if (GenericValidator.isBlankOrNull(value)) {
            if (!isAllowedNullValues()) {
                errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Report.filter.value")));
            }
        } else if (columnType != DBType.DBTypeNameAsInt.STRING) {

            if (columnType == DBType.DBTypeNameAsInt.INTEGER ||
                    columnType == DBType.DBTypeNameAsInt.SHORT) {
                if (!GenericValidator.isInt(value)) {
                    errors.add("int", new ActionError("errors.integer", JSPHelper.getMessage(request, "Report.filter.value")));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.LONG) {
                if (!GenericValidator.isLong(value)) {
                    errors.add("long", new ActionError("errors.long", JSPHelper.getMessage(request, "Report.filter.value")));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.FLOAT) {
                if (!GenericValidator.isFloat(value)) {
                    errors.add("float", new ActionError("errors.float", JSPHelper.getMessage(request, "Report.filter.value")));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.DECIMAL) {

                String fieldPath = Functions.decodeFieldPath(getDto("path").toString());
                StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
                Field field = structureManager.getFieldByTitusPath(fieldPath);
                if (field.getConverter() != null) {

                    ///validate with converter
                    ResultValue resultValue = Functions.viewToDbConverterInSimpleField(Functions.decodeFieldPath(getDto("path").toString()), value, request);
                    if (resultValue.hasErrors()) {
                        if (resultValue.errorIsResource()) {
                            if (resultValue.getResourceParams() != null) {
                                errors.add("decimal", new ActionError(resultValue.getErrorMessage(), resultValue.getResourceParams()));
                            } else {
                                errors.add("decimal", new ActionError(resultValue.getErrorMessage()));
                            }
                        } else {
                            errors.add("decimal", new ActionError("errors.showMessage", resultValue.getErrorMessage()));
                        }
                    } else {
                        newValue = resultValue.getValue().toString();
                    }
                } else {
                    if (!GenericValidator.isFloat(value)) {
                        errors.add("float", new ActionError("errors.float", JSPHelper.getMessage(request, "Report.filter.value")));
                    }
                }

            } else if (columnType == DBType.DBTypeNameAsInt.DOUBLE) {
                if (!GenericValidator.isDouble(value)) {
                    errors.add("double", new ActionError("errors.double", JSPHelper.getMessage(request, "Report.filter.value")));
                }
            } else if (columnType == DBType.DBTypeNameAsInt.DATE ||
                    columnType == DBType.DBTypeNameAsInt.DATETIME) {

                ///validate with converter
                ResultValue resultValue = Functions.viewToDbConverterInSimpleField(Functions.decodeFieldPath(getDto("path").toString()), value, request);
                if (resultValue.hasErrors()) {
                    //errors.add("dateText", new ActionError("errors.date.module", value, JSPHelper.getMessage(request, "Report.filter.value"), datePattern));
                    if (resultValue.errorIsResource()) {
                        if (resultValue.getResourceParams() != null) {
                            errors.add("dateText", new ActionError(resultValue.getErrorMessage(), resultValue.getResourceParams()));
                        } else {
                            errors.add("dateText", new ActionError(resultValue.getErrorMessage()));
                        }
                    } else {
                        errors.add("dateText", new ActionError("errors.showMessage", resultValue.getErrorMessage()));
                    }
                } else {
                    newValue = resultValue.getValue().toString();
                }
            } else if (columnType == DBType.DBTypeNameAsInt.BOOLEAN) {
                log.debug("bbbbbbbbbbbbbbbbbbb");
                //log.debug(Boolean.valueOf("-1"));//todo:revise validate

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

    private List composePkValues(List valuesList) {
        List pkValuesList = new LinkedList();
        if (valuesList.size() > 0 && ((String) valuesList.get(0)).indexOf(ReportConstants.PRIMARYKEY_SEPARATOR) != -1) {
            for (int i = 0; i < valuesList.size(); i++) {
                String pkValue = (String) valuesList.get(i);
                String[] array = pkValue.split(ReportConstants.PRIMARYKEY_SEPARATOR_REGULAREXP);
                for (int j = 0; j < array.length; j++) {
                    Map map = new LinkedHashMap();
                    map.put("value", array[j]);
                    map.put("pkSeq", new Integer(i + 1));
                    pkValuesList.add(map);
                }
            }
        } else {
            pkValuesList = valuesList;
        }
        return pkValuesList;
    }

    protected boolean isAllowedNullValues() {
        return false;
    }
}

