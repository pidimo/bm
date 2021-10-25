package com.piramide.elwis.web.common.taglib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import javax.servlet.jsp.JspException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Show actionerrors and action mensages  Messages.
 *
 * @author Fernando Montaño
 * @version $Id: MessagesTag.java 10617 2015-06-23 23:32:23Z miguel $
 */

public class MessagesTag extends org.apache.struts.taglib.html.MessagesTag {

    private Log log = LogFactory.getLog(MessagesTag.class);

    /**
     * Construct an iterator for the specified collection, and begin
     * looping through the body once per element.
     *
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {
        // Initialize for a new request.
        processed = false;

        // Were any messages specified?
        ActionMessages messages = new ActionMessages();

        // Make a local copy of the name attribute that we can modify.
        String name = this.name;

        if (message != null && "true".equalsIgnoreCase(message)) {
            name = Globals.MESSAGE_KEY;
        }

        try {


            //this code was added to read messages stored in session
            ActionMessages sessionMessages = (ActionMessages)
                    pageContext.getSession().getAttribute(Globals.ERROR_KEY);

            if (null != sessionMessages && !sessionMessages.isEmpty()) {

                messages.add(sessionMessages);
                pageContext.getSession().removeAttribute(Globals.ERROR_KEY);
            }
            if (null != sessionMessages && sessionMessages.isEmpty()) {
                pageContext.getSession().removeAttribute(Globals.ERROR_KEY);
            }


            messages.add(RequestUtils.getActionMessages(pageContext, name));

        } catch (JspException e) {
            RequestUtils.saveException(pageContext, e);
            throw e;
        }

        // Acquire the collection we are going to iterate over
        this.iterator = (property == null) ? messages.get() : messages.get(property);

        // Store the first value and evaluate, or skip the body if none
        if (!this.iterator.hasNext()) {
            return SKIP_BODY;
        }

        ActionMessage report = (ActionMessage) this.iterator.next();
        report = preProcessActionMessage(report);

        /*ResponseUtils.write(pageContext, "<!--");
        ResponseUtils.write(pageContext, report.getKey());
        ResponseUtils.write(pageContext, "-->");*/
        String msg = null;

        if ("customMsg.Referenced".equals(report.getKey()) || "msg.Referenced".equals(report.getKey())) {
            msg = getReferencedMessage(report);
        } else {
            msg = RequestUtils.message(pageContext, bundle,
                    locale, report.getKey(),
                    filterMessageParam(report.getValues()));
        }

        if (msg != null) {
            pageContext.setAttribute(id, msg);
        } else {
            pageContext.removeAttribute(id);
        }

        if (header != null && header.length() > 0) {
            String headerMessage =
                    RequestUtils.message(pageContext, bundle, locale, header);

            if (headerMessage != null) {
                ResponseUtils.write(pageContext, headerMessage);
            }
        }

        // Set the processed variable to true so the
        // doEndTag() knows processing took place
        processed = true;

        return (EVAL_BODY_AGAIN);
    }

    /**
     * Make the next collection element available and loop, or
     * finish the iterations if there are no more elements.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {
        // Render the output from this iteration to the output stream
        if (bodyContent != null) {
            ResponseUtils.writePrevious(pageContext, bodyContent.getString());
            bodyContent.clearBody();
        }

        // Decide whether to iterate or quit
        if (iterator.hasNext()) {
            ActionMessage report = (ActionMessage) iterator.next();
            report = preProcessActionMessage(report);

            String msg = null;
            if ("customMsg.Referenced".equals(report.getKey()) || "msg.Referenced".equals(report.getKey())) {
                msg = getReferencedMessage(report);
            } else {
                msg = RequestUtils.message(pageContext, bundle,
                        locale, report.getKey(),
                        filterMessageParam(report.getValues()));
            }
            pageContext.setAttribute(id, msg);

            return (EVAL_BODY_AGAIN);
        } else {
            return (SKIP_BODY);
        }

    }


    /**
     * Clean up after processing this enumeration.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {
        if (processed && footer != null && footer.length() > 0) {
            String footerMessage = RequestUtils.message(pageContext, bundle,
                    locale, footer);
            if (footerMessage != null) {
                // Print the results to our output writer
                ResponseUtils.write(pageContext, footerMessage);
            }
        }
        // Continue processing this page
        return (EVAL_PAGE);
    }

    private String getReferencedMessage(ActionMessage message) throws JspException {

        String resourceMessage = RequestUtils.message(pageContext, bundle,
                locale, message.getKey(), filterMessageParam(message.getValues()));

        List referencedByTablesNames = new ArrayList();
        Map reqDTO = (Map) pageContext.getRequest().getAttribute("dto");
        if (reqDTO != null) {
            referencedByTablesNames = (List) reqDTO.get("referencedByTables");
        }

        StringBuffer tables = new StringBuffer("<br>");
        tables.append(RequestUtils.message(pageContext, bundle, locale, "Common.referencedBy"))
                .append(": ");
        int i = 0;
        for (Iterator iterator = referencedByTablesNames.iterator(); iterator.hasNext();) {
            String table = (String) iterator.next();
            String tableMessage = RequestUtils.message(pageContext, bundle, locale, "ReferencedBy.table." + table);
            tables.append(tableMessage != null ? tableMessage : table);
            if (i < referencedByTablesNames.size() - 1) {
                tables.append(", ");
            }
            i++;
        }
        tables.append(".");

        return resourceMessage + (referencedByTablesNames.size() > 0 ? new String(tables) : "");


    }

    /**
     * Filter the special characters like <, >, etc.
     *
     * @param params the parameters array
     * @return the params array.
     */
    private Object[] filterMessageParam(Object[] params) {
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if (param != null && param instanceof String) {
                    params[i] = ResponseUtils.filter((String) param);
                }
            }
            return params;
        } else {
            return null;
        }
    }

    protected ActionMessage preProcessActionMessage(ActionMessage actionMessage) {
        return actionMessage;
    }

}
