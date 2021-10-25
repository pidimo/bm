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
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public class ReportJrxmlUpdateAction extends ReportJrxmlAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        log.debug("Executing ReportJrxmlUpdateAction..." + request.getParameterMap());
        ActionForward actionForward = new ActionForward();

        String selectedFunctionality = null;
        String selectedModule = null;
        Object saveButton = null;

        //read the selected type of report, selected functionality, selected module  in the form
        DefaultForm myForm = (DefaultForm) form;
        if (null != myForm) {
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
        ActionError deleteFiltersAssigned = new ActionError("Report.msg.DeleteFiltersAssigned");

        int msgCounter = 0;
        if (!resultDTO.isFailure() && null != saveButton) {
            String actualModule = (String) resultDTO.get("module");
            String actualFunctionality = (String) resultDTO.get("initialTableReference");

            if (!actualFunctionality.equals(selectedFunctionality) || !actualModule.equals(selectedModule)) {
                log.debug("Has changed  the functionality or module of report...");

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
