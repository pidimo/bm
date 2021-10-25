package com.piramide.elwis.web.schedulermanager.action.report;

import com.jatun.titus.reportgenerator.util.ReportData;
import com.jatun.titus.reportgenerator.util.ReportGeneratorConstants;
import com.jatun.titus.web.form.ReportGeneratorForm;
import com.piramide.elwis.cmd.schedulermanager.TaskDescriptionReadCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.report.ReportAction;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.ReportActionUtil;
import com.piramide.elwis.web.contactmanager.action.report.CustomerReportAction;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.alfacentauro.fantabulous.controller.Parameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.joda.time.DateTimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.*;

/**
 * Jatun Team
 *
 * @author alvaro
 * @version $Id: TaskSingleReportAction 17-jun-2008 16:10:23
 */
public class TaskSingleReportAction extends ReportAction {
    private Log log = LogFactory.getLog(CustomerReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CashMovementsReportAction..............");
        ReportGeneratorForm searchForm = (ReportGeneratorForm) form;
        ActionForward actionForward = null;

        if (searchForm.getParams().get("generate") != null) {

            ActionErrors errors = new ActionErrors();
            ReportActionUtil reportActionUtil = new ReportActionUtil();

            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
            Locale locale = new Locale(user.getValue("locale").toString());
            DateTimeZone timeZone = (DateTimeZone) user.getValueMap().get("dateTimeZone");
            ResourceBundle resourceBundle = reportActionUtil.getResourceBundle(locale.toString());
            Integer companyId = new Integer(user.getValue(Constants.COMPANYID).toString());


            //Fantabulous list parameters
            Parameters parameters = new Parameters();
            parameters.addSearchParameter(Constants.COMPANYID, companyId.toString());
            parameters.addSearchParameter(Constants.USERID, user.getValue(Constants.USERID).toString());
            parameters.addSearchParameters(getParameters(searchForm.getParams()));
            parameters.addSearchParameters(getParameters(getStaticFilter()));
            parameters.addSearchParameters(getParameters(getFilter()));

            String reportFormat = searchForm.getReportFormat();
            String reportPageSize = searchForm.getReportPageSize();
            String reportPageOrientation = ReportGeneratorConstants.PAGE_ORIENTATION_PORTRAIT;
            String tempDirectory = reportActionUtil.getTempDirectory();
            HashMap<String, ReportData> subreports = new HashMap<String, ReportData>();

            HashMap reportParams = reportActionUtil.getReportParams(timeZone.toString(), locale.getLanguage(), searchForm);

            //load status map
            ArrayList statusList = JSPHelper.getPriorityStatusList(request);
            HashMap statusMap = new HashMap();
            for (Iterator iterator = statusList.iterator(); iterator.hasNext();) {
                LabelValueBean lvBean = (LabelValueBean) iterator.next();
                statusMap.put(lvBean.getValue(), lvBean.getLabel());
            }
            reportParams.put("TASK_STATUS_MAP", statusMap);
            //load task description
            TaskDescriptionReadCmd taskDescriptionReadCmd = new TaskDescriptionReadCmd();
            taskDescriptionReadCmd.putParam("taskId", new Integer(searchForm.getParams().get("taskId").toString()));
            ResultDTO tDRDTO = BusinessDelegate.i.execute(taskDescriptionReadCmd, request);
            reportParams.put("TASK_DESCRIPTION", tDRDTO.get("taskDescription"));
            //MasterReport
            InputStream templateInputStreamReceipt = request.getSession().getServletContext().getResourceAsStream(
                    "/common/scheduler/jaspertemplates/TaskSingleReportTemplate.jrxml");

            HashMap reportConfigParams = reportActionUtil.getReportConfig(templateInputStreamReceipt, reportFormat,
                    tempDirectory, reportPageSize,
                    locale.toString(), reportPageOrientation,
                    "Task_report", false,
                    resourceBundle);

            reportConfigParams.put("reportTitleString", searchForm.getReportTitle());
            reportActionUtil.generateReport(getServlet(),
                    request, response,
                    reportConfigParams, reportParams,
                    "taskSingleReportList", parameters,
                    subreports);
            //End master report


        } else {
            actionForward = mapping.findForward("Success");
        }
        return (actionForward);
    }
}
