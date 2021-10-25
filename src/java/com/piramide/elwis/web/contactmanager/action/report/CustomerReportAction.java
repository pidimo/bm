package com.piramide.elwis.web.contactmanager.action.report;

import com.piramide.elwis.web.common.action.report.ReportAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Oct 21, 2005
 * Time: 2:52:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class CustomerReportAction extends ReportAction {
    private Log log = LogFactory.getLog(CustomerReportAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        //Initialize the fantabulous filter in empty
        log.debug("---      CustomerReportAction      execute  ....");
        super.initializeFilter();
        SearchForm searchForm = (SearchForm) form;
        ActionErrors errors = new ActionErrors();
        String turnOver1, turnOver2, numberEmployee1, numberEmployee2 = "";

        errors = searchForm.validate(mapping, request);
        saveErrors(request, errors);

        if (!errors.isEmpty()) {
            return mapping.findForward("Success");
        }

        if (request.getParameter("parameter(numberEmployee1)") != null) {
            numberEmployee1 = searchForm.getParameter("numberEmployee1").toString();
            searchForm.getParams().put("numberEmployee1", null);
            searchForm.setParameter("numberEmployee1", numberEmployee1);
        }
        if (request.getParameter("parameter(numberEmployee2)") != null) {
            numberEmployee2 = searchForm.getParameter("numberEmployee2").toString();
            searchForm.getParams().put("numberEmployee2", null);
            searchForm.setParameter("numberEmployee2", numberEmployee2);
        }
        if (request.getParameter("parameter(turnOver1)") != null) {
            turnOver1 = searchForm.getParameter("turnOver1").toString();
            searchForm.getParams().put("turnOver1", null);
            searchForm.getParams().put("turnOver1", turnOver1);
        }
        if (request.getParameter("parameter(turnOver2)") != null) {
            turnOver2 = searchForm.getParameter("turnOver2").toString();
            searchForm.getParams().put("turnOver2", null);
            searchForm.getParams().put("turnOver2", turnOver2);
        }

        if (request.getParameter("parameter(generate)") != null) {
            return super.execute(mapping, searchForm, request, response);
        } else {
            return mapping.findForward("Success");
        }
    }
}

