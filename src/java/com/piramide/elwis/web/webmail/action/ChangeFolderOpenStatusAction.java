package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.common.action.DefaultAction;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: ChangeFolderOpenStatusAction.java 19-may-2009 16:17:26
 */
public class ChangeFolderOpenStatusAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing ChangeFolderOpenStatusAction..............................");
        super.execute(mapping, form, request, response);
        return (null);
    }
}
