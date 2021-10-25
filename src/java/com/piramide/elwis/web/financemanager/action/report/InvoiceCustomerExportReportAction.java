package com.piramide.elwis.web.financemanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.cmd.financemanager.InvoiceCSVExportCmd;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.financemanager.util.InvoiceCSVExportReportUtil;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 0.2
 */
public class InvoiceCustomerExportReportAction extends CustomCSVExportReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing InvoiceCustomerExportReportAction.... " + request.getParameterMap());

        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionErrors errors = searchForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Success");
        }

        errors = exportDataValidation(searchForm, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.findForward("Fail");
        }

        //add export type as param
        reportData.getParams().put("EXPORTDATATYPE_PARAM", InvoiceCSVExportReportUtil.EXPORTDATATYPE_CUSTOMER);

        return super.execute(mapping, form, request, response);
    }

    @Override
    protected boolean isMasterValidation() {
        return false;
    }

    @Override
    protected String getFileName(ReportData reportData, HttpServletRequest request) {
        String fileName = JSPHelper.getMessage(request, "Finance.report.invoiceCustomerExport.fileName", composeRangeDate(request));
        return fileName.trim() + ".txt";
    }

    @Override
    protected void operationsAfterReportGeneration(ReportGeneratorForm reportGeneratorForm, HttpServletRequest request) {
        log.debug("Execute Mark invoice position as exported ...... Export:" + reportGeneratorForm.getParameter("exported"));

        if (reportGeneratorForm.getParameter("exported") != null) {
            List<Integer> invoicePositionExportedIds = InvoiceCSVExportReportUtil.getInvoicePositionExportedIds(
                    getSearchParameterAsInt(reportGeneratorForm, "startInvoiceDate"),
                    getSearchParameterAsInt(reportGeneratorForm, "endInvoiceDate"),
                    request);

            if (!invoicePositionExportedIds.isEmpty()) {
                InvoiceCSVExportCmd csvExportCmd = new InvoiceCSVExportCmd();
                csvExportCmd.setOp("markPositionsAsExported");
                csvExportCmd.putParam("invoicePositionExportedIds", invoicePositionExportedIds);

                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(csvExportCmd, request);
                } catch (AppLevelException e) {
                    log.error("-> Execute " + InvoiceCSVExportCmd.class.getName() + " FAIL", e);
                }
            }
        }
    }

    private ActionErrors exportDataValidation(ReportGeneratorForm searchForm, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Map errorDataResultMap = InvoiceCSVExportReportUtil.validateInvoicePositionExportedData(
                getSearchParameterAsInt(searchForm, "startInvoiceDate"),
                getSearchParameterAsInt(searchForm, "endInvoiceDate"),
                request,
                InvoiceCSVExportReportUtil.EXPORTDATATYPE_CUSTOMER);

        if (!errorDataResultMap.isEmpty()) {
            errors.add("error", new ActionError("InvoiceExport.error.msg"));

            for (Iterator iterator = errorDataResultMap.keySet().iterator(); iterator.hasNext();) {
                String listErrorKey = (String) iterator.next();
                request.setAttribute(listErrorKey, errorDataResultMap.get(listErrorKey));
            }
        }
        return errors;
    }

}
