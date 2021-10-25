package com.piramide.elwis.web.projects.action.report;

import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: TimeRegistrationsReportAction.java 09-mar-2009 11:28:07
 */
public class TimeRegistrationsReportAction extends ReportAction {
    Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing TimeRegistrationsReportAction.................. " + request.getParameterMap());
        ReportGeneratorForm reportGeneratorForm = (ReportGeneratorForm) form;
        ActionForward actionForward = mapping.findForward("Success");

        if (reportGeneratorForm.getParams().get("generate") != null) {
            log.debug("GENERATING............................");
            int addedSize = calculateExceededSizeFromDynamicReportColumns(reportGeneratorForm);

            changeColumnSizes(reportGeneratorForm, addedSize);

            //time totals subreport
            ReportActionUtil reportActionUtil = new ReportActionUtil();
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Locale locale = new Locale(user.getValue("locale").toString());
            DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");
            ResourceBundle resourceBundle = reportActionUtil.getResourceBundle(locale.toString());
            Integer companyId = new Integer(user.getValue(Constants.COMPANYID).toString());

            String reportFormat = reportGeneratorForm.getReportFormat();
            String reportPageSize = reportGeneratorForm.getReportPageSize();
            String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_LANDSCAPE;
            String tempDirectory = reportActionUtil.getTempDirectory();
            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), reportGeneratorForm);

            //Fantabulous list parameters
            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, companyId.toString());
            parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
            parameters.addSearchParameters(getParameters(reportGeneratorForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));

            actionForward = super.execute(mapping, form, request, response);
        }

        return (actionForward);
    }

    private int calculateExceededSizeFromDynamicReportColumns(ReportGeneratorForm reportGeneratorForm) {
        int addedSize = 0;

        if (reportGeneratorForm.getParams().get("showDescription_flag") == null) {
            reportGeneratorForm.removeReportFieldParamMap("freeTextValue");
        } else {
            addedSize = addedSize + Integer.parseInt(reportGeneratorForm.getReportFieldParamMap("freeTextValue").get("width").toString());
        }

        if (reportGeneratorForm.getParams().get("showTimes_flag") == null) {
            reportGeneratorForm.removeReportFieldParamMap("fromDateTime");
            reportGeneratorForm.removeReportFieldParamMap("toDateTime");
        } else {
            addedSize = addedSize + Integer.parseInt(reportGeneratorForm.getReportFieldParamMap("fromDateTime").get("width").toString());
            addedSize = addedSize + Integer.parseInt(reportGeneratorForm.getReportFieldParamMap("toDateTime").get("width").toString());
        }

        return addedSize;
    }

    /**
     * Recalculate the column sizes
     *
     * @param reportGeneratorForm ReportGeneratorForm
     * @param addedSize           Width to add to the report
     */
    private void changeColumnSizes(ReportGeneratorForm reportGeneratorForm, int addedSize) {
        log.debug("ChangeColumnSizes....................  addedSize " + addedSize + " reportFields: " + reportGeneratorForm.getReportFieldParams().size());
        ArrayList reportFields = reportGeneratorForm.getReportFieldParams();

        if (addedSize != 0) {
            int offset = 0;
            while (addedSize - (offset * reportFields.size()) > 0) {
                offset++;
            }
            for (int i = 0; i < reportFields.size(); i++) {
                HashMap reportField = (HashMap) reportFields.get(i);
                if (!reportField.get("width").toString().equals("0")) {
                    reportField.put("width", String.valueOf(Integer.parseInt(reportField.get("width").toString()) - (offset)));
                }
            }
        }
    }
}