package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class DataImportForwardAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        DefaultForm dataImportForm = (DefaultForm) form;
        setDTOValues(dataImportForm, request);
        return mapping.findForward("Success");
    }

    private void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        //default value
        defaultForm.setDto("checkDuplicate", Boolean.TRUE);
    }
}
