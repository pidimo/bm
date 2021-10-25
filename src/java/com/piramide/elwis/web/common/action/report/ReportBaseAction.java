package com.piramide.elwis.web.common.action.report;

import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.jatun.titus.web.util.SortGroupColumn;
import com.piramide.elwis.web.common.util.JSPHelper;
import org.alfacentauro.fantabulous.format.structure.Column;
import org.alfacentauro.fantabulous.structure.ListStructure;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: ReportBaseAction.java 07-abr-2009 14:22:10
 */
public abstract class ReportBaseAction extends org.alfacentauro.fantabulous.web.action.FantabulousAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("........................Executing ReportBaseAction.....................");
        return (super.execute(mapping, form, request, response));
    }

    public void processSortingAndGrouping(ReportGeneratorForm reportGeneratorForm, ListStructure listStructure, Locale locale) {
        log.debug("Processing the sorting/Grouping columns map ........ ... " + reportGeneratorForm.getReportSGParamMap());
        SortedMap<Integer, SortGroupColumn> orderGroupingMap = removeDuplicate(reportGeneratorForm.getReportSGParamMap());

        ArrayList reportFields = reportGeneratorForm.getReportFieldParams();
        if (orderGroupingMap.keySet().size() > 0) {
            ArrayList<Integer> orderGroupingKeyList = new ArrayList<Integer>(orderGroupingMap.keySet());
            int removedSize = 0, groupsNumber = 0;
            for (int i = 0; i < orderGroupingKeyList.size(); i++) {
                Integer key = orderGroupingKeyList.get(i);
                SortGroupColumn sortGroupColumn = orderGroupingMap.get(key);
                removedSize += changeSortingGrouping(listStructure, sortGroupColumn, null, i == 0, reportFields, locale, i);
                if (sortGroupColumn.getGroupingColumn()) {
                    groupsNumber++;
                }
            }
            if (groupsNumber > 0) {
                changeColumnSizes(reportGeneratorForm, removedSize, groupsNumber, 0);
            }
        }
        log.debug("reacalculating fields width.......................");
        //Recalculate fields size...
        int maxWidth = 0, totalWidth = 0, maxWidthFieldPosition = 0;
        for (int k = 0; k < reportFields.size(); k++) {
            HashMap reportFieldMap = (HashMap) reportFields.get(k);
            int fieldWidth = Integer.parseInt(reportFieldMap.get("width").toString());
            totalWidth += fieldWidth;
            if (fieldWidth > maxWidth) {
                maxWidthFieldPosition = k;
                maxWidth = fieldWidth;
            }
        }
        int delta = 100 - totalWidth;
        if (delta > 0) {
            ((HashMap) reportFields.get(maxWidthFieldPosition)).put("width", String.valueOf(maxWidth + delta));
        } else if (delta < 0) {
            ((HashMap) reportFields.get(maxWidthFieldPosition)).put("width", String.valueOf(maxWidth - delta));
        }
    }

    private Integer changeSortingGrouping(ListStructure listStructure, SortGroupColumn sortGroupColumn, String groupSortingColumnName, boolean clearOrders,
                                          ArrayList reportFields, Locale locale, int sortColumnNumber) {
        log.debug("change grouping or sorting..... " + listStructure.getListName() + " colName: " + sortGroupColumn.getColumnName() + " isColAscendent: " + sortGroupColumn.getAscendingOrder() + " orderColumnName: " + sortGroupColumn.getOrderColumnName());
        Integer removedSize = 0;
        String sortGroupColumn_orderColumn = sortGroupColumn.getColumnName();
        if (sortGroupColumn.getOrderColumnName() != null) {
            sortGroupColumn_orderColumn = sortGroupColumn.getOrderColumnName();
        }
        setOrderColumn(listStructure, sortGroupColumn_orderColumn, sortGroupColumn.getAscendingOrder(), clearOrders);

        if (groupSortingColumnName != null) {
            setOrderColumn(listStructure, groupSortingColumnName, true, false);
        }
        if (sortGroupColumn.getGroupingColumn()) {
            int i = 0;
            while (i < reportFields.size()) {
                HashMap fieldInfo = (HashMap) reportFields.get(i);
                if (fieldInfo.get("name").toString().equals(sortGroupColumn.getColumnName())) {
                    fieldInfo.put("isGrouping", "true");
                    if (fieldInfo.get("totalizatorOp") != null) {
                        fieldInfo.remove("totalizatorOp");
                    }

                    removedSize = Integer.parseInt(fieldInfo.get("width").toString());
                    fieldInfo.put("width", "0");
                    fieldInfo.put("fieldPosition", String.valueOf(((sortColumnNumber + 1) * reportFields.size()) + 20));
                    //If date..
                    if (sortGroupColumn.getGroupDateBy() != null) {
                        String datePattern = JSPHelper.getMessage(locale, "datePattern");
                        StringBuffer conditionMethod = new StringBuffer();
                        String pattern = "", patternKey = "";
                        if (sortGroupColumn.getGroupDateBy().equals(ReportGeneratorConstants.DATE_FILTER_WEEK)) {
                            patternKey = "ReportGenerator.WeekDatePattern";
                        } else if (sortGroupColumn.getGroupDateBy().equals(ReportGeneratorConstants.DATE_FILTER_MONTH)) {
                            patternKey = "ReportGenerator.MonthDatePattern";
                        } else if (sortGroupColumn.getGroupDateBy().equals(ReportGeneratorConstants.DATE_FILTER_YEAR)) {
                            patternKey = "ReportGenerator.YearDatePattern";
                        }

                        pattern = JSPHelper.getMessage(locale, patternKey);

                        if (fieldInfo.get("type").equals(ReportGeneratorConstants.FIELD_TYPE_DATEINT)) {
                            conditionMethod.append("com.piramide.elwis.utils.ReportHelper.getGroupingDate ")
                                    .append(sortGroupColumn.getColumnName())
                                    .append(" [")
                                    .append(pattern)
                                    .append("] [")
                                    .append(datePattern)
                                    .append("] [")
                                    .append(locale.toString())
                                    .append("]");
                            fieldInfo.put("conditionMethod", conditionMethod.toString());

                        } else if (fieldInfo.get("type").equals(ReportGeneratorConstants.FIELD_TYPE_DATELONG)) {
                            fieldInfo.put("patternKey", patternKey);
                        }
                    }

                    i = reportFields.size();
                }
                i++;
            }
        }
        return (removedSize);
    }

    private void setOrderColumn(ListStructure listStructure, String columnName, boolean isAscendent, boolean clearOrders) {
        if (clearOrders) {
            listStructure.getOrder().clear();
        }
        ArrayList orders = new ArrayList(listStructure.getOrder());
        Column column = listStructure.getColumn(columnName);
        column.setAscendent(isAscendent);
        orders.add(column);
        listStructure.setOrder(orders);
    }

    /**
     * Recalculate the column sizes
     *
     * @param reportGeneratorForm ReportGeneratorForm
     * @param removedSize         Size (columns width) to remove from the report
     * @param groupsNumber        Number of grouping columns
     * @param addedSize           Width to add to the report
     */
    private void changeColumnSizes(ReportGeneratorForm reportGeneratorForm, int removedSize, int groupsNumber, int addedSize) {
        log.debug("ChangeColumnSizes.................... groupsNumber: " + groupsNumber + " removedSize: " + removedSize +
                " addedSize " + addedSize + " reportFields: " + reportGeneratorForm.getReportFieldParams().size());
        ArrayList reportFields = reportGeneratorForm.getReportFieldParams();
        int toDistrib = removedSize - addedSize;
        if (toDistrib != 0) {
            int offset = 0;
            if (Math.abs(toDistrib) > 0 && (reportFields.size() - groupsNumber) > 0) {
                while ((Math.abs(toDistrib) - (offset * (reportFields.size() - groupsNumber))) > 0) {
                    offset++;
                }
            }
            for (int i = 0; i < reportFields.size(); i++) {
                HashMap reportField = (HashMap) reportFields.get(i);
                if (!reportField.get("width").toString().equals("0")) {
                    if (toDistrib > 0) {
                        reportField.put("width", String.valueOf(Integer.parseInt(reportField.get("width").toString()) + (offset - 1)));
                    } else if (toDistrib < 0) {
                        reportField.put("width", String.valueOf(Integer.parseInt(reportField.get("width").toString()) - offset));
                    }
                }
            }
        }
    }

    private SortedMap<Integer, SortGroupColumn> removeDuplicate(SortedMap<Integer, SortGroupColumn> sortGroupMap) {
        List columns = new ArrayList();
        SortedMap<Integer, SortGroupColumn> res = new TreeMap<Integer, SortGroupColumn>();
        for (Integer key : sortGroupMap.keySet()) {
            SortGroupColumn sortGroupColumn = sortGroupMap.get(key);
            if (!columns.contains(sortGroupColumn.getColumnName())) {
                res.put(key, sortGroupColumn);
                columns.add(sortGroupColumn.getColumnName());
            }
        }
        return (res);
    }
}
