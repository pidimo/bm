package com.piramide.elwis.web.catalogmanager.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class FinderValueTag extends TagSupport {
    private String forId;

    private Object value;


    @Override
    public int doStartTag() throws JspException {
        return SKIP_BODY;
    }

    @Override
    public int doEndTag() throws JspException {
        CategoryTabLinkTag categoryTabLinkTag =
                (CategoryTabLinkTag) pageContext.getAttribute(CategoryTabLinkTag.getTagName(getForId()));

        categoryTabLinkTag.addFinderValue(getValue());

        return EVAL_PAGE;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getForId() {
        return forId;
    }

    public void setForId(String forId) {
        this.forId = forId;
    }
}
