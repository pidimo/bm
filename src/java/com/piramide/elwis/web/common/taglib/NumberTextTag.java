package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.web.common.util.FormatUtils;
import org.apache.struts.taglib.html.BaseInputTag;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;
import org.apache.strutsel.taglib.utils.EvalHelper;
import org.apache.taglibs.standard.tag.common.core.NullAttributeException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.jstl.core.Config;
import java.util.Locale;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: NumberTextTag.java 11637 2015-11-26 17:57:42Z ruth $
 */
public class NumberTextTag extends BaseInputTag {

    protected String accept;
    protected String name;
    protected boolean redisplay;
    protected String type;
    protected String disabledEL;

    private String numberType = null;
    private String maxInt = "4";
    private String maxFloat = "2";
    boolean view = false;
    String view1 = null;
    private String placeHolder;

    public NumberTextTag() {
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

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType.trim();
    }

    public String getMaxInt() {
        return maxInt;
    }

    public void setMaxInt(String maxInt) {
        this.maxInt = maxInt;
    }

    public String getMaxFloat() {
        return maxFloat;
    }

    public void setMaxFloat(String maxFloat) {
        this.maxFloat = maxFloat;
    }

    public String getDisabledEL() {
        return disabledEL;
    }

    public void setDisabledEL(String disabledEL) {
        this.disabledEL = disabledEL;
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
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
            results = new StringBuffer("<input style=\"text-align:right\" type=\"");
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
            if ((placeHolder != null)) {
                results.append(" placeholder=\"");
                results.append(placeHolder);
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
                value = formatingNumber(value);
                results.append(ResponseUtils.filter(value.toString()));
            }

            results.append("\"");
            results.append(prepareEventHandlers());
            results.append(prepareStyles());
            results.append(getElementClose());
        }
        if (view) {
            results = new StringBuffer("<input style=\"text-align:right\" type=\"");
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
            } else if (redisplay || !"password".equals(type)) {
                Object value = RequestUtils.lookup(pageContext, name, property, null);
                if (value == null) {
                    value = "";
                }
                value = formatingNumber(value);
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

    private Object formatingNumber(Object obj) {
        Locale locale = (Locale) Config.get(pageContext.getSession(), Config.FMT_LOCALE);
        if ("decimal".equals(numberType.toLowerCase())) {
            return FormatUtils.formatingDecimalNumber(obj, locale, (new Integer(maxInt)).intValue(), (new Integer(maxFloat)).intValue());
        }
        if ("percent".equals(numberType.toLowerCase())) {
            return FormatUtils.formatingPercentNumber(obj, locale, (new Integer(maxInt)).intValue(), (new Integer(maxFloat)).intValue());
        }
        return obj;
    }
}
