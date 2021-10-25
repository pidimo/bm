package com.piramide.elwis.web.common.action;

import com.piramide.elwis.web.common.util.SimpleAdvancedListParameterUtil;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 * Action that have selector behaviour, if 'secondAdvancedListForward' request parameter is not NULL
 * this is forward according to configured parameter. Else List is executed.
 *
 * @author Miky
 * @version $Id: SecondSimpleAdvancedListAction.java 19-mar-2009 16:53:34 $
 */
public class SecondSimpleAdvancedListAction extends ListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Excecuting SecondSimpleAdvancedListAction......" + request.getParameterMap());

        ActionForward advancedForward = SimpleAdvancedListParameterUtil.secondLevelFindAdvancedForward(mapping, request);
        if (advancedForward != null) {
            return advancedForward;
        }
        return super.execute(mapping, form, request, response);
    }
}