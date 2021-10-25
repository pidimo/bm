package com.piramide.elwis.web.common.taglib;


import com.piramide.elwis.web.common.util.URLParameterProcessor;
import org.alfacentauro.fantabulous.controller.OrderParam;
import org.alfacentauro.fantabulous.controller.ResultList;
import org.alfacentauro.fantabulous.web.mapping.URLParameterReWriteMapping;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This Form tag extends some functionality to struts form tag to support url rewriting for module params.
 * B
 *
 * @author Fernando Montaño
 * @version $Id: FormTag.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class FormTag extends BodyTagSupport {
    protected String action, enctype, focus, focusIndex;
    protected ModuleConfig moduleConfig;
    protected static String lineEnd = System.getProperty("line.separator");
    protected ActionMapping mapping;
    protected static MessageResources messages = MessageResources.getMessageResources("org.apache.struts.taglib.html.LocalStrings");
    protected String method, name, onreset, onsubmit;
    protected String scope, style, styleClass, styleId;
    protected ActionServlet servlet;
    protected String target, type, beanName, beanScope;
    protected String beanType;


    private Log log = LogFactory.getLog(this.getClass());
    private Map<String, String> hiddenProperties;
    private boolean addModuleParams;


    public FormTag() {
        action = null;
        moduleConfig = null;
        enctype = null;
        focus = null;
        focusIndex = null;
        mapping = null;
        method = null;
        name = null;
        onreset = null;
        onsubmit = null;
        scope = null;
        servlet = null;
        style = null;
        styleClass = null;
        styleId = null;
        target = null;
        type = null;
        beanName = null;
        beanScope = null;
        beanType = null;


        addModuleParams = true;
        hiddenProperties = null;
    }


    public String getBeanName() {
        return beanName;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getEnctype() {
        return enctype;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnreset() {
        return onreset;
    }

    public void setOnreset(String onReset) {
        onreset = onReset;
    }

    public String getOnsubmit() {
        return onsubmit;
    }

    public void setOnsubmit(String onSubmit) {
        onsubmit = onSubmit;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyleId() {
        return styleId;
    }

    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private boolean isXhtml() {
        return RequestUtils.isXhtml(pageContext);
    }

    public String getFocusIndex() {
        return focusIndex;
    }

    public void setFocusIndex(String focusIndex) {
        this.focusIndex = focusIndex;
    }


    public Map<String, String> getHiddenProperties() {
        return hiddenProperties;
    }

    public void setHiddenProperties(Map<String, String> hiddenProperties) {
        this.hiddenProperties = hiddenProperties;
    }

    public boolean isAddModuleParams() {
        return addModuleParams;
    }

    public void setAddModuleParams(boolean addModuleParams) {
        this.addModuleParams = addModuleParams;
    }


    public void release() {
        super.release();
        action = null;
        moduleConfig = null;
        enctype = null;
        focus = null;
        focusIndex = null;
        mapping = null;
        method = null;
        name = null;
        onreset = null;
        onsubmit = null;
        scope = null;
        servlet = null;
        style = null;
        styleClass = null;
        styleId = null;
        target = null;
        type = null;


        hiddenProperties = null;


    }


    public int doStartTag() throws JspException {
        hiddenProperties = new HashMap<String, String>();
        // Look up the form bean name, scope, and type if necessary
        this.lookup();
        // Store this tag itself as a page attribute
        pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);
        this.initFormBean();
        return EVAL_BODY_BUFFERED;//catch all body content in the buffer bodyContent.


    }


    public int doEndTag() throws JspException {
        pageContext.removeAttribute("org.apache.struts.taglib.html.BEAN", 2);
        pageContext.removeAttribute("org.apache.struts.taglib.html.FORM", 2);

        StringBuffer results = new StringBuffer();

        results.append(this.renderFormStartElement());

        results.append(this.renderToken());

        if (bodyContent != null) {
            results.append(bodyContent.getString());
        } else //it should never be null
        {
            throw new JspException("The body content is null or there exists errors in the jsp page which contains the Form tag, please check it");
        }

        results.append("</form>");

        if (focus != null) {
            results.append(renderFocusJavascript());
        }
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        }
        catch (IOException e) {
            throw new JspException(messages.getMessage("common.io", e.toString()));
        }

        return EVAL_PAGE;
    }

    /**
     * Generates the opening form element with appropriate attributes.
     * Also in the action attribute the hidden values are added as url parameters, this with the
     * purpose of hidden such values in the html output. Then such url is or should be encrypted at all.
     */
    protected String renderFormStartElement() {

        HttpServletResponse response =
                (HttpServletResponse) this.pageContext.getResponse();

        StringBuffer results = new StringBuffer("<form");
        results.append(" name=\"");
        results.append(beanName);
        results.append("\"");
        results.append(" method=\"");
        results.append(method == null ? "post" : method);
        results.append("\" action=\"");


        StringBuffer buffer = new StringBuffer(RequestUtils.getActionMappingURL(this.action, this.pageContext));

        URLParameterProcessor.addModuleParameters(buffer, (HttpServletRequest) this.pageContext.getRequest(),
                this.pageContext.getServletContext(), true, addModuleParams);
        ActionMapping mapping = (ActionMapping) pageContext.getRequest().getAttribute(Globals.MAPPING_KEY);

        /**
         * Adding rewriting paramenters to the action params.
         */
        if (mapping instanceof URLParameterReWriteMapping) {
            URLParameterProcessor.addParameterToUrl(buffer, ((URLParameterReWriteMapping) mapping).getParamReWriteList(), (HttpServletRequest) pageContext.getRequest(), false);
            URLParameterProcessor.addParameterToUrl(buffer, ((URLParameterReWriteMapping) mapping).getParamReWriteTextList(), (HttpServletRequest) pageContext.getRequest(), true);
        }
        if (mapping != null) {
            String listName;
            String parameter = mapping.getParameter();
            if (parameter != null) {
                if (parameter.indexOf("@") > 0) {
                    listName = parameter.substring(parameter.indexOf("@") + 1);
                } else {
                    listName = parameter;
                }

                ResultList resultList = (ResultList) pageContext.getRequest().getAttribute(listName);
                if (resultList != null) {
                    List order = resultList.getParameters().getOrderParameters();
                    if (!order.isEmpty()) {
                        List<OrderParam> orderParams = resultList.getParameters().getOrderParameters();
                        for (OrderParam orderParam : orderParams) {
                            if (buffer.indexOf("?") == -1) {
                                buffer.append("?");
                            } else {
                                buffer.append("&");
                            }
                            buffer.append("orderParam(")
                                    .append(orderParam.getPosition())
                                    .append(")=")
                                    .append(orderParam.getColumn())
                                    .append("-")
                                    .append(orderParam.isAscending());
                        }
                    }
                }
            }
        }

        addHiddenPropertiesToAction(buffer);
        results.append(response.encodeURL(new String(buffer)));
        results.append("\"");

        if (styleClass != null) {
            results.append(" class=\"");
            results.append(styleClass);
            results.append("\"");
        }
        if (enctype != null) {
            results.append(" enctype=\"");
            results.append(enctype);
            results.append("\"");
        }
        if (onreset != null) {
            results.append(" onreset=\"");
            results.append(onreset);
            results.append("\"");
        }
        if (onsubmit != null) {
            results.append(" onsubmit=\"");
            results.append(onsubmit);
            results.append("\"");
        }
        if (style != null) {
            results.append(" style=\"");
            results.append(style);
            results.append("\"");
        }
        if (styleId != null) {
            results.append(" id=\"");
            results.append(styleId);
            results.append("\"");
        }
        if (target != null) {
            results.append(" target=\"");
            results.append(target);
            results.append("\"");
        }
        results.append(">");
        return results.toString();
    }

    /**
     * Add the hidden properties stored in the hiddenProperties Map to the url of the action.
     *
     * @param url the url where add the hidden values
     */
    private void addHiddenPropertiesToAction(StringBuffer url) {

        if (hiddenProperties.size() > 0) {
            String urlStr = url.toString();
            if (urlStr.indexOf("?") != -1) {
                url.append("&");
            } else {
                url.append("?");
            }
            int i = 1;
            for (String prop : hiddenProperties.keySet()) {
                url.append(prop)
                        .append("=")
                        .append(hiddenProperties.get(prop));
                if (i < hiddenProperties.size()) {
                    url.append("&");
                }
                i++;
            }
        }


    }

    protected void initFormBean() throws JspException {
        int scope = 3;
        if ("request".equalsIgnoreCase(beanScope)) {
            scope = 2;
        }
        Object bean = pageContext.getAttribute(beanName, scope);
        if (bean == null) {
            if (type != null) {
                try {
                    bean = RequestUtils.applicationInstance(beanType);
                    if (bean != null) {
                        ((ActionForm) bean).setServlet(servlet);
                    }
                }
                catch (Exception e) {
                    throw new JspException(messages.getMessage("formTag.create", type, e.toString()));
                }
            } else {
                bean = RequestUtils.createActionForm((HttpServletRequest) pageContext.getRequest(), mapping, moduleConfig, servlet);
            }
            if (bean instanceof ActionForm) {
                ((ActionForm) bean).reset(mapping, (HttpServletRequest) pageContext.getRequest());
            }
            if (bean == null) {
                throw new JspException(messages.getMessage("formTag.create", beanType));
            }
            pageContext.setAttribute(beanName, bean, scope);
        }
        pageContext.setAttribute("org.apache.struts.taglib.html.BEAN", bean, 2);
    }

    protected void lookup() throws JspException {
        moduleConfig = RequestUtils.getModuleConfig(pageContext);
        if (moduleConfig == null) {
            JspException e = new JspException(messages.getMessage("formTag.collections"));
            pageContext.setAttribute("org.apache.struts.action.EXCEPTION", e, 2);
            throw e;
        }
        servlet = (ActionServlet) pageContext.getServletContext().getAttribute("org.apache.struts.action.ACTION_SERVLET");
        String mappingName = RequestUtils.getActionMappingName(action);
        mapping = (ActionMapping) moduleConfig.findActionConfig(mappingName);
        if (mapping == null) {
            JspException e = new JspException(messages.getMessage("formTag.mapping", mappingName));
            pageContext.setAttribute("org.apache.struts.action.EXCEPTION", e, 2);
            throw e;
        }
        if (name != null) {
            if (type == null) {
                JspException e = new JspException(messages.getMessage("formTag.nameType"));
                pageContext.setAttribute("org.apache.struts.action.EXCEPTION", e, 2);
                throw e;
            } else {
                beanName = name;
                beanScope = scope != null ? scope : "session";
                beanType = type;
                return;
            }
        }
        FormBeanConfig formBeanConfig = moduleConfig.findFormBeanConfig(mapping.getName());
        if (formBeanConfig == null) {
            JspException e = new JspException(messages.getMessage("formTag.formBean", mapping.getName()));
            pageContext.setAttribute("org.apache.struts.action.EXCEPTION", e, 2);
            throw e;
        } else {
            beanName = mapping.getAttribute();
            beanScope = mapping.getScope();
            beanType = formBeanConfig.getType();
            return;
        }
    }

    protected String renderToken() {
        StringBuffer results = new StringBuffer();
        HttpSession session = pageContext.getSession();
        if (session != null) {
            String token = (String) session.getAttribute("org.apache.struts.action.TOKEN");
            if (token != null) {
                results.append("<input type=\"hidden\" name=\"");
                results.append("org.apache.struts.taglib.html.TOKEN");
                results.append("\" value=\"");
                results.append(token);
                if (isXhtml()) {
                    results.append("\" />");
                } else {
                    results.append("\">");
                }
            }
        }
        return results.toString();
    }

    protected String renderFocusJavascript() {
        StringBuffer results = new StringBuffer();
        results.append(lineEnd);
        results.append("<script type=\"text/javascript\"");
        if (!isXhtml()) {
            results.append(" language=\"JavaScript\"");
        }
        results.append(">");
        results.append(lineEnd);
        if (!isXhtml()) {
            results.append("  <!--");
            results.append(lineEnd);
        }
        StringBuffer focusControl = new StringBuffer("document.forms[\"");
        focusControl.append(beanName);
        focusControl.append("\"].elements[\"");
        focusControl.append(focus);
        focusControl.append("\"]");
        results.append("  var focusControl = ");
        results.append(focusControl.toString());
        results.append(";");
        results.append(lineEnd);
        results.append(lineEnd);
        results.append("  if (focusControl.type != \"hidden\") {");
        results.append(lineEnd);
        String index = "";
        if (focusIndex != null) {
            StringBuffer sb = new StringBuffer("[");
            sb.append(focusIndex);
            sb.append("]");
            index = sb.toString();
        }
        results.append("     focusControl");
        results.append(index);
        results.append(".focus();");
        results.append(lineEnd);
        results.append("  }");
        results.append(lineEnd);
        if (!isXhtml()) {
            results.append("  // -->");
            results.append(lineEnd);
        }
        results.append("</script>");
        results.append(lineEnd);
        return results.toString();
    }


}