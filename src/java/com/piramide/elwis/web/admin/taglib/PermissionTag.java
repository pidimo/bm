package com.piramide.elwis.web.admin.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PermissionUtil;
import com.piramide.elwis.web.admin.session.User;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * Tag to check if permission is restricted or no.
 *
 * @author Fernando Montaño
 * @version $Id: PermissionTag.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class PermissionTag extends BodyTagSupport {
    private String functionality;
    private String permission;
    private String var;


    public int doStartTag() throws JspTagException {

        User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);

        if (user.getSecurityAccessRights().containsKey(functionality)) {
            Byte accessRight = (Byte) user.getSecurityAccessRights().get(functionality);
            if (!PermissionUtil.hasAccessRight(accessRight, permission)) {
                if (var != null) {
                    pageContext.setAttribute(var, new Boolean(false));
                }
                return SKIP_BODY; //skip the body
            }
        } else {
            if (var != null) {
                pageContext.setAttribute(var, new Boolean(false));
            }
            return SKIP_BODY; //skip the body
        }
        if (var != null) {
            pageContext.setAttribute(var, new Boolean(true));
        }
        return EVAL_BODY_INCLUDE;

    }

    public int doEndTag() {
        return (EVAL_PAGE);
    }

    public void setFunctionality(String functionality) {
        this.functionality = functionality;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }
}
