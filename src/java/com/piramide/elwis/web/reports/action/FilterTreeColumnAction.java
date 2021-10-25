package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.Titus;
import com.jatun.titus.listgenerator.structure.Field;
import com.jatun.titus.listgenerator.structure.StructureManager;
import com.jatun.titus.listgenerator.structure.Table;
import com.jatun.titus.listgenerator.util.TitusPathUtil;
import com.jatun.titus.listgenerator.view.TableTreeView;
import com.jatun.titus.listgenerator.view.TreeModelView;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.reports.el.Functions;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Jatun s.r.l.
 *
 * @author miky
 * @version $Id: FilterTreeColumnAction.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class FilterTreeColumnAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing FilterTreeColumnAction................");

        ActionForward forward;
        forward = null;

        StructureManager structureManager = Titus.getStructureManager(request.getSession().getServletContext());
        TableTreeView treeView = TableTreeView.getInstance(request.getParameter("reportId"), request.getSession());
        String initialReportTableName = treeView.getTreeModel().getName();
        String jsPath = request.getParameter("jsPath");
        String nodeName = treeView.getTitusNodePath(jsPath);
        String completePath = treeView.getTitusPath(jsPath);

        log.debug("-------------------------------------");
        log.debug("jsPath:" + jsPath);
        log.debug("nodeName:" + nodeName);
        log.debug("completePath:" + completePath);

        //process to dynamic column
        if (TitusPathUtil.isDynamicColumn(completePath)) {
            if (!structureManager.existDynamicColumnField(completePath, Functions.getCategoryDynamicColumnParams(request))) {
                setNotFoundColumnInResponse(response);
                return forward;
            }
        }

        String label = Functions.composeColumnLabelByTitusPath(completePath, null, request);

        TreeModelView treeModelView = treeView.getTreeview(jsPath);
        log.debug("RELATION TABLE::::::" + TitusPathUtil.getTableName(treeModelView.getName()));

        log.debug("RELATION TABLE OWNER:::::" + TitusPathUtil.getTableNameOwner(completePath));
        log.debug("SUPERIOR RELATION TABLE:" + TitusPathUtil.getTableNameOfSuperiorForeingRelation(completePath));

        Field field = structureManager.getFieldByTitusPath(nodeName);
        Table table = field.getParentTable();

        //compose operator
        StringBuffer operator = new StringBuffer();
        List operatorList = Functions.getReportFilterOperators(table.getName(), field.getName(), request);
        if (operatorList.isEmpty()) {
            operator.append(ReportConstants.KEY_NOT_OPERATOR);
        } else {
            for (ListIterator iterator = operatorList.listIterator(); iterator.hasNext();) {
                LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
                operator.append(ReportConstants.KEY_SEPARATOR_LABEL).
                        append(labelValueBean.getLabel()).
                        append(ReportConstants.KEY_SEPARATOR_LABEL);

                operator.append(ReportConstants.KEY_SEPARATOR_VALUE).
                        append(labelValueBean.getValue()).
                        append(ReportConstants.KEY_SEPARATOR_VALUE);

                if (iterator.hasNext()) {
                    operator.append(ReportConstants.KEY_SEPARATOR);
                }
            }
        }

        //compose constant filter values
        StringBuffer filterConstantValue = null;
        if (Functions.isConstantFilter(table.getName(), field.getName(), request)) {
            List filterValuesList = Functions.getFilterConstantValues(table.getName(), field.getName(), request);
            filterConstantValue = new StringBuffer();
            for (Iterator iterator = filterValuesList.iterator(); iterator.hasNext();) {
                LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
                filterConstantValue.append(ReportConstants.KEY_SEPARATOR_LABEL).
                        append(labelValueBean.getLabel()).
                        append(ReportConstants.KEY_SEPARATOR_LABEL);

                filterConstantValue.append(ReportConstants.KEY_SEPARATOR_VALUE).
                        append(labelValueBean.getValue()).
                        append(ReportConstants.KEY_SEPARATOR_VALUE);

                if (iterator.hasNext()) {
                    filterConstantValue.append(ReportConstants.KEY_SEPARATOR);
                }
            }
        }

        //filter type
        Integer filterType = ReportConstants.FILTER_WITH_CONSTANT_VALUE;
        if (Functions.isDbFilter(field.getFilter())) {
            filterType = ReportConstants.FILTER_WITH_DB_VALUE;
        }

        //compose xml doc
        response.setContentType("text/xml");
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<filter>\n")
                .append("<label>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(label))
                .append("</label>\n")
                .append("<column>")
                .append(field.getName())
                .append("</column>\n")
                .append("<columntype>")
                .append(field.getType().getType())
                .append("</columntype>\n")
                .append("<table>")
                .append(table.getName())
                .append("</table>\n")
                .append("<path><![CDATA[")
                .append(Functions.encodeFieldPath(completePath))
                .append("]]></path>\n")
                .append("<operator>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(operator.toString()))
                .append("</operator>\n")
                .append("<filtertype>")
                .append(filterType)
                .append("</filtertype>\n");
        if (filterConstantValue != null) {
            //If use <![CDATA[ ]]> tags in xml, ajaxResponseFilter not is required
            xmlResponse.append("<filterconstant><![CDATA[")
                    .append(filterConstantValue.toString())
                    .append("]]></filterconstant>");
        }
        if (Functions.isDbFilterInSelect(completePath, request)) {
            xmlResponse.append("<dbfiltervalue><![CDATA[")
                    .append(composeDbFilterValuesToViewInSelect(completePath, request))
                    .append("]]></dbfiltervalue>");
        }

        xmlResponse.append("</filter>");

        try {
            PrintWriter write = response.getWriter();
            log.debug("xml:\n" + xmlResponse);
            write.write(xmlResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return forward;
    }

    /**
     * compose db filter values to view in an select
     *
     * @param filterFieldPath path of field
     * @param request
     * @return String
     * @throws Exception
     */
    private String composeDbFilterValuesToViewInSelect(String filterFieldPath, HttpServletRequest request) throws Exception {

        StringBuffer result = new StringBuffer();

        List labelValueList = Functions.getValuesOfDbFilterToSelect(filterFieldPath, request);
        for (Iterator iterator = labelValueList.iterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            result.append(ReportConstants.KEY_SEPARATOR_LABEL).
                    append(labelValueBean.getLabel()).
                    append(ReportConstants.KEY_SEPARATOR_LABEL);

            result.append(ReportConstants.KEY_SEPARATOR_VALUE).
                    append(labelValueBean.getValue()).
                    append(ReportConstants.KEY_SEPARATOR_VALUE);

            if (iterator.hasNext()) {
                result.append(ReportConstants.KEY_SEPARATOR);
            }
        }
        return result.toString();
    }

    private void setNotFoundColumnInResponse(HttpServletResponse response) {
        //compose xml doc
        response.setContentType("text/xml");
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<?xml version=\"1.0\" ?>\n");

        xmlResponse.append("<failcolumn>\n")
                .append("Error: Dynamic column not found...")
                .append("</failcolumn>\n");

        try {
            PrintWriter write = response.getWriter();
            log.debug("xml:\n" + xmlResponse);
            write.write(xmlResponse.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
