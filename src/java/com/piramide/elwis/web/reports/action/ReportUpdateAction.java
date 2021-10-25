package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.cmd.reports.LightlyReportCmd;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : ivan
 * @version : $Id ReportUpdateAction ${time}
 */
public class ReportUpdateAction extends UserReportAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing ReportUpdateAction...");
        ActionForward actionForward = new ActionForward();

        //read the actual type of report
        Integer actualReportType = Integer.valueOf(request.getParameter("reportType"));


        Integer selectedTypeOfReport = actualReportType;

        String selectedFunctionality = null;
        String selectedModule = null;
        Object saveButton = null;

        //read the selected type of report, selected functionality, selected module  in the form
        DefaultForm myForm = (DefaultForm) form;
        if (null != myForm) {
            selectedTypeOfReport = Integer.valueOf((String) myForm.getDto("reportType"));

            selectedFunctionality = (String) myForm.getDto("initialTableReference");
            selectedModule = (String) myForm.getDto("module");
            saveButton = myForm.getDto("save");
        }

        //read actual functionality and initialTableReference from entity
        LightlyReportCmd cmd = new LightlyReportCmd();
        cmd.putParam("reportId", Integer.valueOf(request.getParameter("reportId")));
        cmd.setOp("read");


        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);

        ActionErrors errors = new ActionErrors();


        List errorsCollection = new ArrayList();
        ActionError deleteColumGroupAssignedError = new ActionError("Report.msg.DeleteColumnGroupAssigned");
        ActionError deleteChartAssignedError = new ActionError("Report.msg.DeleteChartAssigned");
        ActionError deleteColumnsAssigned = new ActionError("Report.msg.DeleteColumnAssigned");
        ActionError deleteFiltersAssigned = new ActionError("Report.msg.DeleteFiltersAssigned");
        ActionError deleteColumnGroupAssignedIIError = new ActionError("Report.msg.DeleteColumnGroupAssignedII");

        int msgCounter = 0;
        if (!resultDTO.isFailure() && null != saveButton) {
            String actualModule = (String) resultDTO.get("module");
            String actualFunctionality = (String) resultDTO.get("initialTableReference");


            if (!actualReportType.equals(selectedTypeOfReport)) {

                log.debug("Has changed the type of report...");


                if (null != resultDTO.get("haveColumnGroupAssigned")) {
                    if (!errorsCollection.contains(deleteColumGroupAssignedError)) {
                        errorsCollection.add(deleteColumGroupAssignedError);
                    }

                    request.setAttribute("haveChangeType", Boolean.valueOf(true));
                    msgCounter++;


                }

                if (null != resultDTO.get("haveChartAssigned")) {
                    if (!errorsCollection.contains(deleteChartAssignedError)) {
                        errorsCollection.add(deleteChartAssignedError);
                    }

                    request.setAttribute("haveChangeType", Boolean.valueOf(true));
                    msgCounter++;

                }
            }

            if (!actualFunctionality.equals(selectedFunctionality) || !actualModule.equals(selectedModule)) {

                log.debug("Has changed  the functionality or module of report...");
                if (null != resultDTO.get("haveColumnsAssigned")) {

                    if (!errorsCollection.contains(deleteColumnsAssigned)) {
                        errorsCollection.add(deleteColumnsAssigned);
                    }
                    request.setAttribute("haveChangeColumn", Boolean.valueOf(true));
                    msgCounter++;

                }


                if (null != resultDTO.get("haveColumnGroupAssigned") && null == request.getAttribute("haveChangeType")) {
                    log.debug("Tiene Grupos asignados  y se eliminaran!!!!");
                    if (!errorsCollection.contains(deleteColumnGroupAssignedIIError)) {
                        errorsCollection.add(deleteColumnGroupAssignedIIError);
                    }
                }


                if (null != resultDTO.get("haveChartAssigned") && null == request.getAttribute("haveChangeType")) {
                    if (!errorsCollection.contains(deleteChartAssignedError)) {
                        errorsCollection.add(deleteChartAssignedError);
                    }
                }


                if (null != resultDTO.get("haveFiltersAssigned")) {
                    if (!errorsCollection.contains(deleteFiltersAssigned)) {
                        errorsCollection.add(deleteFiltersAssigned);
                    }

                    request.setAttribute("haveChangeColumn", Boolean.valueOf(true));
                    msgCounter++;
                }

            }

            for (int i = 0; i < errorsCollection.size(); i++) {
                log.debug("settingUp the errors...");
                ActionError err = (ActionError) errorsCollection.get(i);
                errors.add("errorMSG" + 1, err);
            }


            String formMsgCounter = (String) myForm.getDto("formMsgCounter");
            if (null == formMsgCounter || "".equals(formMsgCounter)) {
                formMsgCounter = "0";
            }


            int formMsgCounterInt = new Integer(formMsgCounter).intValue();


            if ((!errors.isEmpty() &&
                    null != myForm.getDto("haveConfirmate") &&
                    !((Boolean.valueOf((String) myForm.getDto("haveConfirmate")).booleanValue()))) ||
                    formMsgCounterInt < msgCounter) {

                saveErrors(request, errors);
                request.setAttribute("haveConfirmate", Boolean.valueOf(true));

                request.setAttribute("msgCounter", new Integer(msgCounter));

                return mapping.getInputForward();
            }
        }


        actionForward = super.execute(mapping, form, request, response);

        //remove tree columns instance
        if ("Success".equals(actionForward.getName())) {
            TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
        }
        return actionForward;
    }

}
