package com.piramide.elwis.web.supportmanager.action;

import com.piramide.elwis.web.common.util.SimpleAdvancedListParameterUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * This acction only to redirect to simple or advanced appoinment search list
 *
 * @author Miky
 * @version $Id: CaseSimpleAdvancedListAction.java 17-mar-2009 14:30:27 $
 */
public class CaseSimpleAdvancedListAction extends CaseAdvancedListAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing CaseSimpleAdvancedListAction ..." + request.getParameterMap());

        ActionForward advancedForward = SimpleAdvancedListParameterUtil.findAdvancedForward(mapping, request);
        if (advancedForward != null) {
            return advancedForward;
        }

        //execute simple search
        return super.execute(mapping, form, request, response);
    }
}
