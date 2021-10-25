package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.web.common.util.URLParameterProcessor;
import org.alfacentauro.fantabulous.web.mapping.URLParameterReWriteMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;

/**
 * Generate a URL-encoded hyperlink to the specified URI.
 * And additional functionality was added here the posibility of chop and string that are more bigest of maxlength.
 *
 * @author Fernando Montaño
 * @version $Revision: 9803 $ $Date: 2009-10-06 14:36:02 -0400 (Tue, 06 Oct 2009) $
 * @see org.apache.struts.taglib.html.LinkTag
 */

public class UrlTag extends BodyTagSupport {
    private Log log = LogFactory.getLog(this.getClass());
    private String var;
    private String value;
    private String context;

    private boolean contextRelative;
    private boolean addModuleParams;
    private boolean addModuleName;
    private boolean write;
    private boolean enableEncodeURL;


    public UrlTag() {
        var = null;
        context = null;
        contextRelative = false;
        addModuleParams = true;
        addModuleName = true;
        write = false;
        enableEncodeURL = true;//by default encode the url
    }

    public boolean isEnableEncodeURL() {
        return enableEncodeURL;
    }

    public void setEnableEncodeURL(boolean enableEncodeURL) {
        this.enableEncodeURL = enableEncodeURL;
    }

    public boolean isWrite() {
        return write;
    }

    public void setWrite(boolean write) {
        this.write = write;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int doStartTag()
            throws JspException {

        StringBuffer url = new StringBuffer();

        url.append(com.piramide.elwis.web.common.util.RequestUtils.getActionMappingURL(value, pageContext, contextRelative, false, false));

        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        URLParameterProcessor.addModuleParameters(url, request, pageContext.getServletContext(), addModuleName, addModuleParams);
        ActionMapping mapping = (ActionMapping) pageContext.getRequest().getAttribute(Globals.MAPPING_KEY);

        if (mapping instanceof URLParameterReWriteMapping) {
            URLParameterProcessor.addParameterToUrl(url, ((URLParameterReWriteMapping) mapping).getParamReWriteList(), (HttpServletRequest) pageContext.getRequest(), false);
            URLParameterProcessor.addParameterToUrl(url, ((URLParameterReWriteMapping) mapping).getParamReWriteTextList(), (HttpServletRequest) pageContext.getRequest(), true);


        }
        //String tmp = response.encodeRedirectURL(new String(url));

        // Perform URL rewriting to include our session ID (if any)

        String urlFinal;

        if (pageContext.getSession() != null && enableEncodeURL) {
            urlFinal = response.encodeURL(new String(url));
        } else {
            urlFinal = new String(url);
        }


        if (var == null || write) {
            try {
                pageContext.getOut().print(urlFinal);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            if ("request".equals(context)) {
                request.setAttribute(var, new String(urlFinal));
            } else if ("session".equals(context)) {
                request.getSession().setAttribute(var, new String(urlFinal));
            } else {
                pageContext.setAttribute(var, new String(urlFinal));
            }
        }

        return (EVAL_BODY_BUFFERED);
    }

    /**
     * Save the associated label from the body content.
     *
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doAfterBody() throws JspException {


        return (SKIP_BODY);

    }


    /**
     * Render the end of the hyperlink.
     *
     * @throws javax.servlet.jsp.JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Prepare the textual content and ending element of this hyperlink


        // Evaluate the remainder of this page
        return (EVAL_PAGE);

    }

    public void release() {
        super.release();
    }

}
