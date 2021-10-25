package com.piramide.elwis.web.common.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 * Util to find advanced list forward if 'advancedListForward'  request parameter is defined and is not NULL
 *
 * @author Miky
 * @version $Id: SimpleAdvancedListParameterUtil.java 19-mar-2009 16:37:45 $
 */
public class SimpleAdvancedListParameterUtil {
    private static Log log = LogFactory.getLog(SimpleAdvancedListParameterUtil.class);

    public static ActionForward findAdvancedForward(ActionMapping mapping, HttpServletRequest request) {
        String advancedForward = request.getParameter("advancedListForward");
        log.debug("With advanced forward...:" + advancedForward);
        if (advancedForward != null && mapping.findForward(advancedForward) != null) {
            if ("true".equals(request.getParameter("forceSimple"))) {
                //set in empty the defined advanced forward parameter
                request.getParameterMap().put("advancedListForward", "");
            } else {
                return mapping.findForward(advancedForward);
            }
        }
        return null;
    }

    public static ActionForward secondLevelFindAdvancedForward(ActionMapping mapping, HttpServletRequest request) {
        String advancedForward = request.getParameter("secondAdvancedListForward");
        log.debug("With advanced forward in second level...:" + advancedForward);
        if (advancedForward != null && mapping.findForward(advancedForward) != null) {
            if ("true".equals(request.getParameter("forceSimple"))) {
                //set in empty the defined advanced forward parameter
                request.getParameterMap().put("secondAdvancedListForward", "");
            } else {
                return mapping.findForward(advancedForward);
            }
        }
        return null;
    }
}
