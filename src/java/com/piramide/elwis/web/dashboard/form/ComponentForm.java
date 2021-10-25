package com.piramide.elwis.web.dashboard.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.dashboard.component.configuration.reader.Builder;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Column;
import com.piramide.elwis.web.dashboard.component.configuration.structure.Component;
import com.piramide.elwis.web.dashboard.component.configuration.structure.ConfigurableFilter;
import com.piramide.elwis.web.dashboard.component.web.struts.form.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * @author : ivan
 *         Date: Aug 31, 2006
 *         Time: 2:36:52 PM
 */
public class ComponentForm extends DefaultForm {
    private Log log = LogFactory.getLog(this.getClass());

    private String[] selectedColumns;
    private String[] availableColumns;
    private List<Column> availableColumnsOBJ = new ArrayList<Column>();

    public ActionErrors validate(ActionMapping actionMapping, HttpServletRequest request) {
        updateSelectedColumns(request);

        ActionErrors errors = new ActionErrors();

        Component xmlComponent = Builder.i.findComponentById(getXmlComponentId());

        List<ConfigurableFilter> filters = xmlComponent.getConfigurableFilters();
        int i = 0;
        for (ConfigurableFilter filter : filters) {
            i++;
            if (filter.isRangeView()) {

                String firstValue = (String) getDto(filter.getName() + "_0");
                String secondValue = (String) getDto(filter.getName() + "_1");

                ActionError msgError = validateRangeValues(firstValue, secondValue, filter.getResourceKey(), request);
                if (null != msgError) {
                    errors.add("filter_range_error" + i, msgError);
                }
            }
            if (!filter.isRangeView() && !filter.isBooleanType()) {
                String value = (String) getDto(filter.getName());
                ActionError msgError = validateSingleValues(value, filter, request);
                if (null != msgError) {
                    errors.add("filter_single_error" + i, msgError);
                }
            }
        }

        return errors;
    }

    private ActionError validateRangeValues(String v1, String v2, String resourceKey, HttpServletRequest request) {
        ActionError errorMsg = null;
        boolean isV1BlankOrNull = GenericValidator.isBlankOrNull(v1);
        boolean isV2BlankOrNull = GenericValidator.isBlankOrNull(v2);

        if (isV1BlankOrNull || isV2BlankOrNull) {
            //error some values is blank or null
            errorMsg = new ActionError("dashboard.error.range.required", JSPHelper.getMessage(request, resourceKey));
        } else {
            boolean isV1Int = GenericValidator.isInt(v1);
            boolean isV2Int = GenericValidator.isInt(v2);

            if (!isV1Int || !isV2Int) {
                //error v1 or v2 not int values
                errorMsg = new ActionError("dashboard.error.range.intvalue", JSPHelper.getMessage(request, resourceKey));
            } else {
                int v1Int = new Integer(v1);
                int v2Int = new Integer(v2);

                if (v1Int > v2Int) {
                    //error v1 cannor grather than v2
                    errorMsg = new ActionError("dashboard.error.range.order", JSPHelper.getMessage(request, resourceKey));
                }
            }
        }
        return errorMsg;
    }

    private ActionError validateSingleValues(String v, ConfigurableFilter filter, HttpServletRequest request) {
        ActionError msgError = null;
        if (!GenericValidator.isBlankOrNull(v)) {
            if (filter.isIntegerType()) {
                //validate value for integer
                if (!GenericValidator.isInt(v)) {
                    msgError = new ActionError("dashboard.error.single.intvalue", JSPHelper.getMessage(request, filter.getResourceKey()));
                }
            }
            if (filter.isDateType()) {
                //validate value for date
                Locale locale = new Locale("en");
                if (!GenericValidator.isDate(v, locale)) {
                    msgError = new ActionError("dashboard.error.single.datevalue", filter.getResourceKey());
                }
            }
        }
        return msgError;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);

        Component xmlComponent = Builder.i.findComponentById(getXmlComponentId());
        availableColumnsOBJ = xmlComponent.getVisibleColumns();

        List<ConfigurableFilter> filters = xmlComponent.getConfigurableFilters();
        request.setAttribute("filters", filters);

        for (ConfigurableFilter f : filters) {
            if (f.isRangeView()) {
                if (null != f.getInitialValue() && null != f.getFinalValue()) {
                    this.setDto(f.getName() + "_0", f.getInitialValue());
                    this.setDto(f.getName() + "_1", f.getFinalValue());
                }
            } else if (null != f.getInitialValue()) {
                this.setDto(f.getName(), f.getInitialValue());
            }
        }
    }

    private void updateSelectedColumns(HttpServletRequest request) {
        List<String> selectedColumnsFromJSP = new ArrayList<String>();
        try {
            selectedColumnsFromJSP = Arrays.asList(selectedColumns);
        } catch (NullPointerException npe) {
        }

        List<String> availableColumnsFromJSP = new ArrayList<String>();
        try {
            availableColumnsFromJSP = Arrays.asList(availableColumns);
        } catch (NullPointerException npe) {
        }

        List<Column> updateAvailableColumns = new ArrayList<Column>();
        for (String id : availableColumnsFromJSP) {
            Column col = getColumnFromList(new Integer(id), availableColumnsOBJ);
            if (null != col) {
                updateAvailableColumns.add(col);
            }
        }

        List<Column> updateSelectedColumns = new ArrayList<Column>();
        for (String id : selectedColumnsFromJSP) {
            Column col = getColumnFromList(new Integer(id), availableColumnsOBJ);
            if (null != col) {
                updateSelectedColumns.add(col);
            }
        }

        List<Column> updateOrderableColumns = new ArrayList<Column>();
        for (Column col : availableColumnsOBJ) {
            if (col.isOrderable()) {
                Column cloneColumn = new Column(col);
                if (null != getDto(col.getName())) {
                    cloneColumn.setOrder(getDto(col.getName()).toString());
                }
                updateOrderableColumns.add(cloneColumn);
            }
        }

        for (Column col : updateAvailableColumns) {
            if (getDtoMap().containsKey(col.getName())) {
                getDtoMap().remove(col.getName());
            }
        }
        request.setAttribute("availableColumns", updateAvailableColumns);
        request.setAttribute("selectedColumns", updateSelectedColumns);
        request.setAttribute("orderableColumns", updateOrderableColumns);
        setVisibleColumns(updateSelectedColumns);
    }

    private Column getColumnFromList(int idColumn, List<Column> list) {
        Column myColumnClone;
        for (Column col : list) {
            if (idColumn == col.getId()) {
                if (null != getDto(col.getName())
                        && !"".equals(getDto(col.getName()).toString().trim())) {
                    myColumnClone = new Column(col);
                    myColumnClone.setOrder(getDto(col.getName()).toString());
                    return myColumnClone;
                }
                return col;
            }
        }
        return null;
    }

    public String[] getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(String[] selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    public String[] getAvailableColumns() {
        return availableColumns;
    }

    public void setAvailableColumns(String[] availableColumns) {
        this.availableColumns = availableColumns;
    }
}
