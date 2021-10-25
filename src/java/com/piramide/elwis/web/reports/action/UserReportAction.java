package com.piramide.elwis.web.reports.action;

import com.jatun.titus.listgenerator.view.TableTreeView;
import com.piramide.elwis.utils.ReportConstants;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.action.AddressAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Alfacentauro Team
 *
 * @author Alvaro
 * @version $Id: UserReportAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class UserReportAction extends DefaultAction {
    private Log log = LogFactory.getLog(AddressAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        DefaultForm myForm = (DefaultForm) form;

        ActionForward forward = new ActionForward();

        ActionErrors errors = new ActionErrors();

        Object saveButton = myForm.getDto("save");

        Object moduleButton = myForm.getDto("module");

        if (isCancelled(request)) {
            return mapping.findForward("Cancel");
        }

        if (null != saveButton) {
            //save button on detail tab of report is pressed

            errors = myForm.validate(mapping, request);

            if (!errors.isEmpty()) {

                saveErrors(request, errors);

                forward = mapping.getInputForward();

            } else {
                //not have errors in the report form

                forward = super.execute(mapping, form, request, response);

                if (null != forward.getName() && forward.getName().equals("Success")) {
                    Map values = (Map) request.getAttribute("dto");
                    request.setAttribute("reportId", values.get("reportId"));

                    //remove tree columns instance
                    TableTreeView.removeInstance(request.getParameter("reportId"), request.getSession());
                }
            }

        } else if (null != moduleButton) {

            String op = (String) myForm.getDto("op");
            if (!"create".equals(op)) {
                if (null != request.getParameter("reportId")) {
                    errors = ForeignkeyValidator.i.validate(ReportConstants.TABLE_REPORT, "reportid",
                            request.getParameter("reportId"), errors, new ActionError("Report.NotFound"));
                    if (!errors.isEmpty()) {
                        saveErrors(request, errors);
                        return mapping.findForward("MainSearch");
                    }

                } else {
                    errors.add("reportid", new ActionError("Report.NotFound"));
                    saveErrors(request, errors);
                    return mapping.findForward("MainSearch");
                }
            }

            errors.add("city", new ActionError("Address.error.cityNotFound")); // put any error to return to input page
            saveErrors(request, errors);
            request.setAttribute("skipErrors", "true"); //skip to show the errors in the page
            forward = mapping.getInputForward();

        }

        return forward;

    }
}
