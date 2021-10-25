package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.web.common.urlencrypt.UrlEncryptCipher;
import com.piramide.elwis.web.common.urlencrypt.filter.UrlEncryptFilter;
import com.piramide.elwis.web.common.util.URLParameterProcessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Jatun S.R.L.
 * Tag to write or store in the page session the URL to be used in the javascript
 * window.location property.
 * This classs has the responsability to encode the URL and add the parameters
 * to the url and then write a jscript compatible url.
 * e.g. window.location = 'urltogo.jsp?param1=' + document.getElementById('fieldId');
 * Using this tag with window.location is neede to add the ' and ' for the value,
 * this quotes are generate automatically by this tag.
 *
 * @author Fernando Montaño
 * @version $Id: JScriptUrlTag.java 9803 2009-10-06 18:36:02Z miguel $
 */

public class JScriptUrlTag extends BodyTagSupport {
    private String var;
    private String url;
    private Map<String, String> params;
    private boolean isHrefObj;
    private boolean addModuleParams;
    private boolean addModuleName;

    private Log log = LogFactory.getLog(this.getClass());

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public boolean isHrefObj() {
        return isHrefObj;
    }

    public void setIsHrefObj(boolean hrefObj) {
        isHrefObj = hrefObj;
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

    public JScriptUrlTag() {
        var = null;
        url = null;
        params = null;
        isHrefObj = false;
        addModuleParams = true;
        addModuleName = true;
    }

    public int doStartTag() throws JspException {
        //instance the map
        params = new LinkedHashMap<String, String>();
        //write a jscript to process the url in the client side
        if (isHrefObj) {
            StringBuilder sb = new StringBuilder("\n<script language=\"JavaScript\">\n");
            sb.append("\tfunction addHrefUrlParam(hrefObj){\n")
                    .append("\t\tif(hrefObj.href.indexOf('?') > 0)\n")
                    .append("\t\t\threfObj += '&';\n")
                    .append("\t\telse\n")
                    .append("\t\t\threfObj += '?';\n")
                    .append("\t\treturn hrefObj;\n")
                    .append("\t}\n")
                    .append("</script>\n");
            JspWriter writer = pageContext.getOut();
            try {
                writer.write(sb.toString());
            } catch (IOException e) {
                log.error("Unexpected error writing the jscript", e);
                throw new JspException();
            }
        }

        //eval the parameters
        return (EVAL_BODY_BUFFERED);
    }

    public int doEndTag() throws JspException {
        String contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        StringBuilder sb = new StringBuilder();
        String finalUrl = url;

        if (isHrefObj) {
            finalUrl = "addHrefUrlParam(" + url + ")";
            sb.append(finalUrl);
        } else {
            StringBuffer urlBuffer = new StringBuffer(url);
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            URLParameterProcessor.addModuleParameters(urlBuffer, request, pageContext.getServletContext(), addModuleName, addModuleParams);

            finalUrl = response.encodeURL(contextPath + urlBuffer.toString());
            sb.append("'");
            sb.append(finalUrl);
        }

        if (params.size() == 0) {
            if (!isHrefObj) {
                sb.append("'");
            }
        } else {
            if (!isHrefObj) {
                sb.append(finalUrl.contains("?") ? "&" : "?").append("'");
            }
            if (UrlEncryptFilter.ENABLE_URL_ENCRYPT) {
                sb.append(encryptJsURLParams());
            } else {
                int pos = 1;
                for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
                    String param = iterator.next();
                    sb.append(" + ").append((pos > 1) ? "'&" : "'").append(param).append("='").append(" + ").append(params.get(param));
                    pos++;
                }
            }
        }
        pageContext.setAttribute(var, sb.toString());


        return EVAL_PAGE;
    }

    public void release() {
        super.release();
        var = null;
        url = null;
        params = null;
    }

    /**
     * Encrypt the params which are added with jscript, using encryption.
     *
     * @return the encrypted URL param storing the jscript
     */
    private String encryptJsURLParams() {
        StringBuilder sb = new StringBuilder();
        sb.append(" + '");
        sb.append(UrlEncryptFilter.JS_PARAM_NAME).append("=");
        String encryptedParams = "";
        String[] values = new String[params.size()];
        int pos = 0;
        for (Iterator<String> iterator = params.keySet().iterator(); iterator.hasNext();) {
            String param = iterator.next();
            encryptedParams += param;
            if (pos + 1 < params.size()) {
                encryptedParams += "&";//params separator
            }
            values[pos] = params.get(param);
            pos++;
        }
        sb.append(UrlEncryptCipher.i.encrypt(encryptedParams))
                .append(UrlEncryptFilter.JS_PARAMS_VALUES_SEPARATOR);

        sb.append("'");

        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            sb.append(" + ");
            if (i > 0) {
                sb.append("'").append(UrlEncryptFilter.JS_VALUES_SEPARATOR).append("' + ");
            }
            sb.append(value);

        }
        sb.append(" + '").append(UrlEncryptFilter.JS_END_SEPARATOR).append("'");
        return sb.toString();
    }

}
