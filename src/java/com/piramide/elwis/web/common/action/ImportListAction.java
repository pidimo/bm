package com.piramide.elwis.web.common.action;

import com.piramide.elwis.web.common.util.ActionForwardParameters;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: ImportListAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ImportListAction extends ListAction {
    protected static Log log = LogFactory.getLog(ImportListAction.class);

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws Exception {

        ActionForward forward = new ActionForward();
        ActionForwardParameters parameters = null;
        log.debug("Evaluate parameters:" + request.getParameterMap());
        if (request.getParameterMap().size() > 0) {
            parameters = new ActionForwardParameters();
            for (Iterator iterator = request.getParameterMap().entrySet().iterator(); iterator.hasNext();) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String param = (String) entry.getKey();
                parameters.add(param, ((String[]) entry.getValue())[0]);
            }
        }
        if (parameters != null) {
            forward = parameters.forward(super.execute(mapping, form, request, response));
        } else {
            forward = super.execute(mapping, form, request, response);
        }
        return forward;
    }
}
