package com.piramide.elwis.web.financemanager.action;

import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.web.common.action.ListAction;
import org.alfacentauro.fantabulous.web.form.SearchForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: RemindersListAction.java 11-nov-2008 14:44:59 $
 */
public class RemindersListAction extends ListAction {
    protected static Log log = LogFactory.getLog(RemindersListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing RemindersListAction........" + request.getParameterMap());
        SearchForm listForm = (SearchForm) form;

        //add reminder document generated filter
        Object paramGenerated = listForm.getParameter("isReminderGenerated");
        //remove filters, this is necessary
        addFilter("withDocument", "");
        addFilter("withoutDocument", "");

        if (SalesConstants.REMINDER_GENERATED.equals(paramGenerated)) {
            addFilter("withDocument", "true");
        } else if (SalesConstants.REMINDER_NOT_GENERATED.equals(paramGenerated)) {
            addFilter("withoutDocument", "true");
        }

        return super.execute(mapping, listForm, request, response);
    }

}
