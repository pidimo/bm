package com.piramide.elwis.web.reports.form;

import com.jatun.titus.listgenerator.structure.type.DBType;
import com.piramide.elwis.cmd.reports.ColumnGroupCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
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
 * @author : ivan
 * @version : $Id ColumnGroupForm ${time}
 */
public class ColumnGroupForm extends DefaultForm {
    Collection structure = new ArrayList();

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        log.debug("Validate columnGroupForm....");
        ActionErrors errors = super.validate(mapping, request);

        Integer resourceListSize = Integer.valueOf((String) getDto("resourceListSize"));
        List columnIdList = new ArrayList();


        List requiredErrors = new ArrayList();
        List duplicatedErrors = new ArrayList();
        List lastSelectedItems = new ArrayList();

        for (int i = 1; i <= resourceListSize.intValue(); i++) {
            Integer column_i = null;
            try {
                //reads  i-esima column
                column_i = Integer.valueOf((String) getDto("columnId_" + i));

                //search in the structure the columnMap object
                Map actualMapColumn = getSelectedColumn(structure, column_i);

                String label = (String) actualMapColumn.get("label");
                String columnReference = (String) actualMapColumn.get("columnReference");
                String tableReference = (String) actualMapColumn.get("tableReference");
                String titusPath = (String) actualMapColumn.get("titusPath");

                String columnLabel = Functions.composeColumnLabelByTitusPath(titusPath, label, request);

                Integer mapColumnType = new Integer(actualMapColumn.get("columnType").toString());
                //if the columnType is Date then enables the select
                if (null != mapColumnType &&
                        (mapColumnType.intValue() == DBType.DBTypeNameAsInt.DATE
                                || mapColumnType.intValue() == DBType.DBTypeNameAsInt.DATETIME)) {
                    request.setAttribute("select_" + i, Boolean.valueOf(true));
                }


                Map selected = new HashMap();
                selected.put("value", column_i);
                selected.put("key", (new Integer(i)).toString());

                if (!columnIdList.contains(column_i)) {
                    columnIdList.add(column_i);
                } else {
                    ActionError error = new ActionError("Report.columnGroup.error.DuplicateColumn", columnLabel);
                    duplicatedErrors.add(error);
                }

                //if cannot have select order for one column
                if (null == getDto("columnOrder_" + i) || "".equals(getDto("columnOrder_" + i).toString().trim())) {
                    ActionError error = new ActionError("Report.columnGroup.error.OrderRequired", columnLabel);
                    requiredErrors.add(error);
                } else {
                    selected.put("order", getDto("columnOrder_" + i));
                }

                lastSelectedItems.add(selected);

            } catch (NullPointerException ne) {

            } catch (NumberFormatException nfe) {

            }

            String columnIdisAxis = (String) getDto("columnIdisAxis_" + i);
            if (null != columnIdisAxis && null == column_i) {
                errors.add("columnGroup", new ActionError("errors.required", getDto("resource")));
                request.setAttribute("lastSelected", lastSelectedItems);
                break;
            }

        }


        if (!duplicatedErrors.isEmpty()) {
            request.setAttribute("lastSelected", lastSelectedItems);
            for (int i = 0; i < duplicatedErrors.size(); i++) {
                ActionError error = (ActionError) duplicatedErrors.get(i);
                errors.add("columnGroupDuplicate_" + i, error);
            }
        } else {
            if (!requiredErrors.isEmpty()) {
                request.setAttribute("lastSelected", lastSelectedItems);
                for (int i = 0; i < requiredErrors.size(); i++) {
                    ActionError error = (ActionError) requiredErrors.get(i);
                    errors.add("columnGroupRequired_" + i, error);
                }
            }
        }

        //vaildate available columns to create groupign
        if (errors.isEmpty()) {
            errors = validateAvailableColumnsToCreateGroups(resourceListSize.intValue());
            if (!errors.isEmpty()) {
                request.setAttribute("lastSelected", lastSelectedItems);
            }
        }
        return errors;
    }


    public void reset(ActionMapping mapping, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        Integer reportId = Integer.valueOf(request.getParameter("reportId"));

        Integer companyId = (Integer) user.getValue("companyId");


        ResultDTO myResultDTO = new ResultDTO();
        ColumnGroupCmd cmd = new ColumnGroupCmd();
        cmd.putParam("reportId", reportId);
        cmd.putParam("companyId", companyId);
        cmd.putParam("op", "constructStructure");

        try {
            myResultDTO = BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {

        }

        Collection structureSelect = (Collection) myResultDTO.get("structureSelect");
        this.structure = structureSelect;
        request.setAttribute("structureSelect", structureSelect);
        request.setAttribute("order", JSPHelper.getColumnGroupOrder(request));
        request.setAttribute("groupDate", JSPHelper.getGroupDateList(request));

    }


    /**
     * finds checkColumnId in mapCollection and return the columnMap object
     *
     * @param mapCollection collection of columnsMaps objects
     * @param checkColumnId id that finds
     * @return Map object that contain column information
     */
    private Map getSelectedColumn(Collection mapCollection, Integer checkColumnId) {
        for (Iterator it = mapCollection.iterator(); it.hasNext();) {
            Map map = (Map) it.next();
            List columns = (List) map.get("columns");
            for (int i = 0; i < columns.size(); i++) {
                Map column = (Map) columns.get(i);
                Integer columnId = (Integer) column.get("columnId");
                if (checkColumnId.equals(columnId)) {
                    return column;
                }
            }
        }
        return null;
    }

    /**
     * Validate available columns to create groups. Only can be created "columns - 1" groups
     *
     * @param groupLength
     * @return errors
     */
    private ActionErrors validateAvailableColumnsToCreateGroups(int groupLength) {
        Integer reportId = new Integer(getDto("reportId").toString());

        ActionErrors errors = new ActionErrors();
        int groupCount = 0;
        for (int i = 1; i <= groupLength; i++) {
            if (!GenericValidator.isBlankOrNull((String) getDto("columnId_" + i))) {
                groupCount++;
            }
        }
        int reportColumns = Functions.getReportColumnsSize(reportId);
        if (groupCount >= reportColumns) {
            errors.add("column", new ActionError("ColumnGroup.error.notAvailableColumns", reportColumns - 1));
        }
        return errors;
    }

}
