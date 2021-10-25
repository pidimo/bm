package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.URLParameterProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.config.ForwardConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.logic.IterateTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.strutsel.taglib.utils.EvalHelper;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Generate a URL-encoded hyperlink to the specified URI.
 * And additional functionality was added here the posibility of chop and string that are more bigest of maxlength.
 *
 * @author Fernando Montaño
 * @version $Revision: 9703 $ $Date: 2009-09-12 11:46:08 -0400 (Sat, 12 Sep 2009) $
 * @see org.apache.struts.taglib.html.LinkTag
 */

public class LinkTag extends org.apache.struts.taglib.html.LinkTag {
    private Log log = LogFactory.getLog(this.getClass());
    private String maxLength;
    private boolean contextRelative;
    private boolean addModuleParams;
    private boolean addModuleName;

    public LinkTag() {
        maxLength = null;
        setTitle(null);
        contextRelative = false;
        addModuleParams = true;
        addModuleName = true;
    }


    public String getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

    public boolean isAddModuleParams() {
        return addModuleParams;
    }

    public void setAddModuleParams(boolean addModuleParams) {
        this.addModuleParams = addModuleParams;
    }

    public boolean isAddModuleName() {
        return addModuleName;
    }

    public void setAddModuleName(boolean addModuleName) {
        this.addModuleName = addModuleName;
    }

    public boolean isContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public int doStartTag()
            throws JspException {

        evaluateExpressions();
        // Special case for name anchors
        if (linkName != null) {
            StringBuffer results = new StringBuffer("<a name=\"");
            results.append(linkName);
            results.append("\">");
            ResponseUtils.write(pageContext, results.toString());
            return (EVAL_BODY_BUFFERED);
        }

        // Generate the opening anchor element
        StringBuffer results = new StringBuffer("<a href=\"");
        // * @since Struts 1.1
        results.append(this.calculateURL());
        results.append("\"");
        if (target != null) {
            results.append(" target=\"");
            results.append(target);
            results.append("\"");
        }
        if (accesskey != null) {
            results.append(" accesskey=\"");
            results.append(accesskey);
            results.append("\"");
        }
        if (tabindex != null) {
            results.append(" tabindex=\"");
            results.append(tabindex);
            results.append("\"");
        }


        ResponseUtils.write(pageContext, results.toString());

        this.text = null;
        return (EVAL_BODY_BUFFERED);
    }

    /**
     * Save the associated label from the body content.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {

        if (bodyContent != null) {
            String value = bodyContent.getString().trim();
            if (value.length() > 0) {
                text = value;
            }
            //if maxLength is added, then put the complete name in title attribute
            if (maxLength != null) {
                setTitleKey(null);
                setTitle(value);

            }
        }
        return (SKIP_BODY);

    }


    /**
     * Render the end of the hyperlink.
     *
     * @throws JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Prepare the textual content and ending element of this hyperlink
        StringBuffer results = new StringBuffer();

        results.append(prepareStyles());
        results.append(prepareEventHandlers());
        results.append(">");

        if (text != null) {
            if (maxLength != null) {
                results.append(JSPHelper.chopString(text, Integer.parseInt(maxLength)));
            } else {
                results.append(text);
            }
        }
        results.append("</a>");

        // Render the remainder to the output stream
        ResponseUtils.write(pageContext, results.toString());

        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }

    public void release() {
        super.release();
        maxLength = null;
    }


    private Object evalAttr(String attrName, String attrValue, Class attrType)
            throws JspException, NullAttributeException {
        return EvalHelper.eval("link", attrName, attrValue, attrType, this, pageContext);
    }

    private void evaluateExpressions()
            throws JspException {
        try {
            setAccesskey((String) evalAttr("accessKey", getAccesskey(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setAction((String) evalAttr("action", getAction(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setAnchor((String) evalAttr("anchor", getAnchor(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setForward((String) evalAttr("forward", getForward(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setHref((String) evalAttr("href", getHref(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }

        try {
            setIndexId((String) evalAttr("indexId", getIndexId(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setLinkName((String) evalAttr("linkName", getLinkName(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setName((String) evalAttr("name", getName(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnblur((String) evalAttr("onblur", getOnblur(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnclick((String) evalAttr("onclick", getOnclick(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOndblclick((String) evalAttr("ondblclick", getOndblclick(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnfocus((String) evalAttr("onfocus", getOnfocus(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnkeydown((String) evalAttr("onkeydown", getOnkeydown(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnkeypress((String) evalAttr("onkeypress", getOnkeypress(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnkeyup((String) evalAttr("onkeyup", getOnkeyup(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnmousedown((String) evalAttr("onmousedown", getOnmousedown(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnmousemove((String) evalAttr("onmousemove", getOnmousemove(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnmouseout((String) evalAttr("onmouseout", getOnmouseout(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnmouseover((String) evalAttr("onmouseover", getOnmouseover(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setOnmouseup((String) evalAttr("onmouseup", getOnmouseup(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setPage((String) evalAttr("page", getPage(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setParamId((String) evalAttr("paramId", getParamId(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setParamName((String) evalAttr("paramName", getParamName(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setParamProperty((String) evalAttr("paramProperty", getParamProperty(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setParamScope((String) evalAttr("paramScope", getParamScope(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setProperty((String) evalAttr("property", getProperty(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setScope((String) evalAttr("scope", getScope(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setStyle((String) evalAttr("style", getStyle(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setStyleClass((String) evalAttr("styleClass", getStyleClass(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setStyleId((String) evalAttr("styleId", getStyleId(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setTabindex((String) evalAttr("tabindex", getTabindex(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setTarget((String) evalAttr("target", getTarget(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setTitle((String) evalAttr("title", getTitle(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setTitleKey((String) evalAttr("titleKey", getTitleKey(), java.lang.String.class));
        } catch (NullAttributeException ex) {
        }
        try {
            setTransaction(((Boolean) evalAttr("transaction", String.valueOf(getTransaction()), java.lang.Boolean.class)).booleanValue());
        } catch (NullAttributeException ex) {
        }
        try {
            setIndexed(((Boolean) evalAttr("indexed", String.valueOf(getIndexed()), java.lang.Boolean.class)).booleanValue());
        } catch (NullAttributeException ex) {
        }
        try {
            setMaxLength(((String) evalAttr("maxLength", getMaxLength(), java.lang.String.class)));
        } catch (NullAttributeException ex) {
        }
    }

    /**
     * Return the complete URL to which this hyperlink will direct the user.
     * Support for indexed property since Struts 1.1
     *
     * @throws JspException if an exception is thrown calculating the value
     */
    protected String calculateURL() throws JspException {

        // Identify the parameters we will add to the completed URL
        Map params = RequestUtils.computeParameters
                (pageContext, paramId, paramName, paramProperty, paramScope,
                        name, property, scope, transaction);

        // if "indexed=true", add "index=x" parameter to query string
        // * @since Struts 1.1
        if (indexed) {

            // look for outer iterate tag
            IterateTag iterateTag =
                    (IterateTag) findAncestorWithClass(this, IterateTag.class);
            if (iterateTag == null) {
                // This tag should only be nested in an iterate tag
                // If it's not, throw exception
                JspException e = new JspException
                        (messages.getMessage("indexed.noEnclosingIterate"));
                RequestUtils.saveException(pageContext, e);
                throw e;
            }

            //calculate index, and add as a parameter
            if (params == null) {
                params = new HashMap();             //create new HashMap if no other params
            }
            if (indexId != null) {
                params.put(indexId, Integer.toString(iterateTag.getIndex()));
            } else {
                params.put("index", Integer.toString(iterateTag.getIndex()));
            }
        }

        String url = null;
        try {
            url = computeURL(pageContext, forward, href,
                    page, action, params, anchor, false, true);
        } catch (MalformedURLException e) {
            RequestUtils.saveException(pageContext, e);
            throw new JspException
                    (messages.getMessage("rewrite.url", e.toString()));
        }
        return (url);

    }


    /**
     * Compute a hyperlink URL based on the <code>forward</code>,
     * <code>href</code>, <code>action</code> or <code>page</code> parameter
     * that is not null.
     * The returned URL will have already been passed to
     * <code>response.encodeURL()</code> for adding a session identifier.
     *
     * @param pageContext     PageContext for the tag making this call
     * @param forward         Logical forward name for which to look up
     *                        the context-relative URI (if specified)
     * @param href            URL to be utilized unmodified (if specified)
     * @param page            Module-relative page for which a URL should
     *                        be created (if specified)
     * @param action          Logical action name for which to look up
     *                        the context-relative URI (if specified)
     * @param params          Map of parameters to be dynamically included (if any)
     * @param anchor          Anchor to be dynamically included (if any)
     * @param redirect        Is this URL for a <code>response.sendRedirect()</code>?
     * @param encodeSeparator This is only checked if redirect is set to false (never
     *                        encoded for a redirect).  If true, query string parameter separators are encoded
     *                        as &gt;amp;, else &amp; is used.
     * @return URL with session identifier
     * @throws MalformedURLException if a URL cannot be created
     *                               for the specified parameters
     */
    private String computeURL(PageContext pageContext,
                              String forward,
                              String href,
                              String page,
                              String action,
                              Map params,
                              String anchor,
                              boolean redirect,
                              boolean encodeSeparator)
            throws MalformedURLException {

        // Validate that exactly one specifier was included
        int n = 0;
        if (forward != null) {
            n++;
        }
        if (href != null) {
            n++;
        }
        if (page != null) {
            n++;
        }
        if (action != null) {
            n++;
        }
        if (n != 1) {
            throw new MalformedURLException(messages.getMessage("computeURL.specifier"));
        }

        // Look up the module configuration for this request
        ModuleConfig config =
                (ModuleConfig) pageContext.getRequest().getAttribute(Globals.MODULE_KEY);
        if (config == null) { // Backwards compatibility hack
            config =
                    (ModuleConfig) pageContext.getServletContext().getAttribute(Globals.MODULE_KEY);
            pageContext.getRequest().setAttribute(Globals.MODULE_KEY, config);
        }

        // Calculate the appropriate URL
        StringBuffer url = new StringBuffer();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        if (forward != null) {
            ForwardConfig fc = config.findForwardConfig(forward);
            if (fc == null) {
                throw new MalformedURLException(messages.getMessage("computeURL.forward", forward));
            }
            if (fc.getRedirect()) {
                redirect = true;
            }
            if (fc.getPath().startsWith("/")) {
                url.append(request.getContextPath());
                url.append(RequestUtils.forwardURL(request, fc));
            } else {
                url.append(fc.getPath());
            }
        } else if (href != null) {
            url.append(href);
        } else if (action != null) {
            url.append(com.piramide.elwis.web.common.util.RequestUtils.getActionMappingURL(action, pageContext,
                    contextRelative, false, false));

        } else /* if (page != null) */ {
            url.append(request.getContextPath());
            url.append(this.pageURL(request, page));
        }

        // Add anchor if requested (replacing any existing anchor)
        if (anchor != null) {
            String temp = url.toString();
            int hash = temp.indexOf('#');
            if (hash >= 0) {
                url.setLength(hash);
            }
            url.append('#');
            url.append(RequestUtils.encodeURL(anchor));
        }

        // Add dynamic parameters if requested
        if ((params != null) && (params.size() > 0)) {

            // Save any existing anchor
            String temp = url.toString();
            int hash = temp.indexOf('#');
            if (hash >= 0) {
                anchor = temp.substring(hash + 1);
                url.setLength(hash);
                temp = url.toString();
            } else {
                anchor = null;
            }

            // Define the parameter separator
            String separator = null;
            if (redirect) {
                separator = "&";
            } else if (encodeSeparator) {
                separator = "&amp;";
            } else {
                separator = "&";
            }

            // Add the required request parameters
            boolean question = temp.indexOf('?') >= 0;
            Iterator keys = params.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                Object value = params.get(key);
                if (value == null) {
                    if (!question) {
                        url.append('?');
                        question = true;
                    } else {
                        url.append(separator);
                    }
                    url.append(RequestUtils.encodeURL(key));
                    url.append('='); // Interpret null as "no value"
                } else if (value instanceof String) {
                    if (!question) {
                        url.append('?');
                        question = true;
                    } else {
                        url.append(separator);
                    }
                    url.append(RequestUtils.encodeURL(key));
                    url.append('=');
                    url.append(RequestUtils.encodeURL((String) value));
                } else if (value instanceof String[]) {
                    String values[] = (String[]) value;
                    for (int i = 0; i < values.length; i++) {
                        if (!question) {
                            url.append('?');
                            question = true;
                        } else {
                            url.append(separator);
                        }
                        url.append(RequestUtils.encodeURL(key));
                        url.append('=');
                        url.append(RequestUtils.encodeURL(values[i]));
                    }
                } else /* Convert other objects to a string */ {
                    if (!question) {
                        url.append('?');
                        question = true;
                    } else {
                        url.append(separator);
                    }
                    url.append(RequestUtils.encodeURL(key));
                    url.append('=');
                    url.append(RequestUtils.encodeURL(value.toString()));
                }
            }

            // Re-add the saved anchor (if any)
            if (anchor != null) {
                url.append('#');
                url.append(RequestUtils.encodeURL(anchor));
            }

        }

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        URLParameterProcessor.addModuleParameters(url, request, pageContext.getServletContext(), addModuleName, addModuleParams);
        /*ActionMapping mapping = (ActionMapping) pageContext.getRequest().getAttribute(Globals.MAPPING_KEY);

        if(mapping instanceof URLParameterReWriteMapping){
            URLParameterProcessor.addParameterToUrl(url, ((URLParameterReWriteMapping)mapping).getParamReWrite(), (HttpServletRequest) pageContext.getRequest());
            System.out.println("REWrite URl for SubModules...:"+url);
        }*/
        //String tmp = response.encodeRedirectURL(new String(url));

        // Perform URL rewriting to include our session ID (if any)
        if (pageContext.getSession() != null) {
            if (redirect) {
                return (response.encodeRedirectURL(new String(url)));
            } else {
                return (response.encodeURL(new String(url)));
            }
        } else {
            return (new String(url));
        }

    }


    /**
     * <p>Return the context-relative URL that corresponds to the specified
     * <code>page</code> attribute value, calculated based on the
     * <code>pagePattern</code> property of the current module's
     * {@link ModuleConfig}.</p>
     *
     * @param request The servlet request we are processing
     * @param page    The module-relative URL to be substituted in
     *                to the <code>pagePattern</code> pattern for the current module
     *                (<strong>MUST</strong> start with a slash)
     * @return context-relative URL
     * @since Struts 1.1
     */
    private String pageURL(HttpServletRequest request, String page) {

        StringBuffer sb = new StringBuffer();
        ModuleConfig moduleConfig = (ModuleConfig) request.getAttribute(Globals.MODULE_KEY);
        String pagePattern = moduleConfig.getControllerConfig().getPagePattern();
        if (pagePattern == null) {
            if (!contextRelative) {
                sb.append(moduleConfig.getPrefix());
            }
            sb.append(page);
        } else {
            boolean dollar = false;
            for (int i = 0; i < pagePattern.length(); i++) {
                char ch = pagePattern.charAt(i);
                if (dollar) {
                    switch (ch) {
                        case 'M':
                            sb.append(moduleConfig.getPrefix());
                            break;
                        case 'P':
                            sb.append(page);
                            break;
                        case '$':
                            sb.append('$');
                            break;
                        default:
                            ; // Silently swallow
                    }
                    dollar = false;
                    continue;
                } else if (ch == '$') {
                    dollar = true;
                } else {
                    sb.append(ch);
                }
            }
        }
        return (sb.toString());

    }


}
