package com.piramide.elwis.web.common.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * Jatun S.R.L.
 * Use to add params to JSCriptUrlTag, to be used to
 * generate the jscript string ready to be used to assign to window.location
 *
 * @author Fernando Montaño
 * @version $Id: JScriptUrlParamTag.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class JScriptUrlParamTag extends SimpleTagSupport {
    private String param;
    private String value;

    public JScriptUrlParamTag() {
        param = null;
        value = null;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void doTag() throws JspException {
        JScriptUrlTag parent = (JScriptUrlTag) findAncestorWithClass(this, JScriptUrlTag.class);
        if (parent != null) {
            parent.getParams().put(param, value);
        }

    }

}
