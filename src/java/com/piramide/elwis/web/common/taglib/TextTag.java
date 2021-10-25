package com.piramide.elwis.web.common.taglib;

import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.strutsel.taglib.utils.EvalHelper;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;

import javax.servlet.jsp.JspException;

/**
 * TextTag based on Struts TextTag
 *
 * @author Yumi
 * @version $Id: TextTag.java 9703 2009-09-12 15:46:08Z fernando $
 * @see org.apache.struts.taglib.html.BaseInputTag
 */
public class TextTag extends BaseInputTag {

    protected boolean view = false;
    protected String view1 = null;
    protected String disabledEL;

    public TextTag() {
        accept = null;
        name = "org.apache.struts.taglib.html.BEAN";
        redisplay = true;
        type = null;
    }

    public String getView() {
        return view1;
    }

    public void setView(String readOnly) {
        this.view1 = readOnly;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getRedisplay() {
        return redisplay;
    }

    public void setRedisplay(boolean redisplay) {
        this.redisplay = redisplay;
    }


    public String getDisabledEL() {
        return disabledEL;
    }

    public void setDisabledEL(String disabledEL) {
        this.disabledEL = disabledEL;
    }

    private Object evalAttr(String attrName, String attrValue, Class attrType)
            throws JspException, NullAttributeException {
        return EvalHelper.eval("text", attrName, attrValue, attrType, this, pageContext);
    }


    private void evaluateExpressions() throws JspException {
        try {
            view = ((Boolean) evalAttr("view1", getView(), java.lang.Boolean.class)).booleanValue();
            setValue((String) evalAttr("value", getValue(), java.lang.String.class).toString());
        } catch (NullAttributeException ex) {
        }

        try {
            setDisabled(((Boolean) evalAttr("disabled", getDisabledEL(), java.lang.Boolean.class)).booleanValue());
        } catch (NullAttributeException ex) {
        }
    }

    public int doStartTag()
            throws JspException {
        evaluateExpressions();
        StringBuffer results = null;
        if (!view) {
            results = new StringBuffer("<input type=\"");
            results.append("text");
            results.append("\" name=\"");
            if (indexed) {
                prepareIndex(results, name);
            }
            results.append(property);
            results.append("\"");
            if (accesskey != null) {
                results.append(" accesskey=\"");
                results.append(accesskey);
                results.append("\"");
            }
            if (accept != null) {
                results.append(" accept=\"");
                results.append(accept);
                results.append("\"");
            }
            if (maxlength != null) {
                results.append(" maxlength=\"");
                results.append(maxlength);
                results.append("\"");
            }
            if (cols != null) {
                results.append(" size=\"");
                results.append(cols);
                results.append("\"");
            }
            if (tabindex != null) {
                results.append(" tabindex=\"");
                results.append(tabindex);
                results.append("\"");
            }
            results.append(" value=\"");
            if (this.value != null) {
                results.append(ResponseUtils.filter(this.value));
            } else if (redisplay || !"password".equals(type)) {
                Object value = RequestUtils.lookup(pageContext, name, property, null);
                if (value == null) {
                    value = "";
                }
                results.append(ResponseUtils.filter(value.toString()));
            }

            results.append("\"");
            results.append(prepareEventHandlers());
            results.append(prepareStyles());
            results.append(getElementClose());
        } else {
            results = new StringBuffer("<input type=\"");
            results.append("hidden");
            results.append("\" name=\"");
            String val = "";
            if (indexed) {
                prepareIndex(results, name);
            }
            results.append(property);
            results.append("\"");
            results.append(" value=\"");
            if (this.value != null) {
                results.append(ResponseUtils.filter(this.value));
                val = this.value;
            } else if (redisplay || !"password".equals(type)) {
                Object value = RequestUtils.lookup(pageContext, name, property, null);
                if (value == null) {
                    value = " ";
                }
                val = ResponseUtils.filter(value.toString());
                results.append(val);
            }
            results.append("\"");
            results.append(prepareEventHandlers());
            results.append(prepareStyles());
            results.append(getElementClose());
            results.append(val);
        }
        ResponseUtils.write(pageContext, results.toString());

        return 2;
    }

    public void release() {
        super.release();
        accept = null;
        name = "org.apache.struts.taglib.html.BEAN";
        redisplay = true;
    }

    protected String accept;
    protected String name;
    protected boolean redisplay;
    protected String type;
}

