package com.piramide.elwis.web.reports.action;

import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorConstants;
import com.jatun.titus.customreportgenerator.util.CustomReportGeneratorHelper;
import com.piramide.elwis.cmd.reports.ReaderChartFieldCmd;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.reports.el.Functions;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.ListIterator;

/**
 * Jatun S.R.L.
 * class to respose chart ajax request
 *
 * @author Miky
 * @version $Id: LoadChartTypeAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class LoadChartTypeAction extends DefaultAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing LoadChartTypeAction................" + request.getParameterMap());

        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        //verif if exist report
        if (null != request.getParameter("reportId")) {
            errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                    request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
        } else {
            errors.add("reportid", new ActionError("Report.NotFound"));
        }

        String chartType = request.getParameter("type");
        String orientation = request.getParameter("orientation");
        boolean onlyOrientation = (request.getParameter("onlyOrientation") != null && "true".equals(request.getParameter("onlyOrientation")));

        if (orientation.equals("")) {
            //default orientation
            orientation = String.valueOf(CustomReportGeneratorConstants.CHART_ORIENTATION_HORIZONTAL);
        }
        String TypeView = Functions.composeChartTypeView(Integer.parseInt(chartType), orientation, request);


        //compose xml doc
        response.setContentType("text/xml");
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<?xml version=\"1.0\" ?>\n");
        xmlResponse.append("<chart>\n")
                .append("<view>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(TypeView))
                .append("</view>\n");

        if (errors.isEmpty()) {
            if (!onlyOrientation) {
                xmlResponse.append(writeChartFields(chartType, request));
            }
        } else {
            xmlResponse.append("<reportfail>")
                    .append("errors")
                    .append("</reportfail>\n");
        }
        xmlResponse.append("</chart>");

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
     * compose chart fields to response
     *
     * @param chartType
     * @param request
     * @return
     * @throws Exception
     */
    private StringBuffer writeChartFields(String chartType, HttpServletRequest request) throws Exception {

        User user = RequestUtils.getUser(request);

        ReaderChartFieldCmd chartFieldCmd = new ReaderChartFieldCmd();
        chartFieldCmd.putParam("reportId", request.getParameter("reportId"));
        chartFieldCmd.putParam("companyId", user.getValue("companyId"));

        BusinessDelegate.i.execute(chartFieldCmd, request);
        List totalizeDtoList = (List) chartFieldCmd.getResultDTO().get("totalizeList");
        List columnDtoList = (List) chartFieldCmd.getResultDTO().get("columnList");

        String totalizersStr = parserListToString(Functions.getChartTotalizersWithResource(totalizeDtoList, request));
        String columnsStr = parserListToString(Functions.getChartColumnGroupsWithResource(columnDtoList, null, request));

        //write in xml
        StringBuffer xmlResponse = new StringBuffer();
        xmlResponse.append("<totalize>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(totalizersStr))
                .append("</totalize>\n")
                .append("<columngroup>")
                .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(columnsStr))
                .append("</columngroup>\n");

        if (CustomReportGeneratorConstants.CHARTGROUP_TIMESERIES == CustomReportGeneratorHelper.getChartGroupType(Integer.parseInt(chartType))) {
            String timeColumnsStr = parserListToString(Functions.getChartColumnGroupsWithResource(columnDtoList, chartType, request));
            xmlResponse.append("<timegroup>\n")
                    .append(com.piramide.elwis.web.common.el.Functions.ajaxResponseFilter(timeColumnsStr))
                    .append("</timegroup>\n");
        }
        return xmlResponse;
    }

    /**
     * set list values in an string
     *
     * @param listLabelValueBean
     * @return String
     */
    private String parserListToString(List listLabelValueBean) {
        StringBuffer res = new StringBuffer();
        for (ListIterator iterator = listLabelValueBean.listIterator(); iterator.hasNext();) {
            LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
            res.append(ReportConstants.KEY_SEPARATOR_LABEL).
                    append(labelValueBean.getLabel()).
                    append(ReportConstants.KEY_SEPARATOR_LABEL);

            res.append(ReportConstants.KEY_SEPARATOR_VALUE).
                    append(labelValueBean.getValue()).
                    append(ReportConstants.KEY_SEPARATOR_VALUE);

            if (iterator.hasNext()) {
                res.append(ReportConstants.KEY_SEPARATOR);
            }
        }

        if (res.length() == 0) {
            res.append(ReportConstants.KEY_EMPTY);
        }
        return res.toString();
    }
}
