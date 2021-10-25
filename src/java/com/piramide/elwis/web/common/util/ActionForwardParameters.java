package com.piramide.elwis.web.common.util;

import com.piramide.elwis.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForward;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/**
 * User: alejandro
 * Date: Jul 4, 2004
 * Time: 10:16:56 PM
 */

/**
 * Simple utility class to add parameters to an ActionForward
 * from within an action. This action is useful when you from
 * within an action need to pass parameters to the jsp page or another
 * action  you are forwarding to.
 */
public class ActionForwardParameters {
    private Log log = LogFactory.getLog(ActionForwardParameters.class);
    /**
     * Encupsulates parameters for ActionForward.
     */

    private Map<String, String> params = new HashMap<String, String>();

    /**
     * add all the parameters and values from the hashtable to the
     * actionforward
     *
     * @param parametersValues a hastable with key value pairs
     * @return ActionForwardParameters object
     */
    public ActionForwardParameters add(Hashtable parametersValues) {
        for (Iterator i = parametersValues.keySet().iterator(); i.hasNext();) {
            String key = (String) i.next();
            String value = (String) parametersValues.get(key);
            try {
                params.put(key, value != null ? URLEncoder.encode(value, Constants.CHARSET_ENCODING) : "");
            } catch (UnsupportedEncodingException e) {
                log.error("Error enoding param", e);
            }
        }

        return this;
    }

    public ActionForwardParameters add(String parameter, String value) {
        try {
            params.put(parameter, value != null ? URLEncoder.encode(value, Constants.CHARSET_ENCODING) : "");
        } catch (UnsupportedEncodingException e) {
            log.error("Error enoding param", e);
        }
        return this;
    }

    /**
     * Add parameters to provided ActionForward
     *
     * @param forward ActionForward to add parameters to
     * @return ActionForward with added parameters to URL
     */
    public ActionForward forward(ActionForward forward) {
        StringBuilder path = new StringBuilder(forward.getPath());
        Iterator iter = params.entrySet().iterator();
        //add first parameter, if avaliable
        if (path.indexOf("?") <= 0) {
            path.append("?");
        } else {
            path.append("&");
        }

        //add other parameters
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            path.append(entry.getKey()).append("=").append(entry.getValue());
            if (iter.hasNext()) {
                path.append("&");
            }
        }
        return new ActionForward(forward.getName(), path.toString(), forward.getRedirect(), forward.getContextRelative());
    }
}
