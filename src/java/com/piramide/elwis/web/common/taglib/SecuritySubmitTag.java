package com.piramide.elwis.web.common.taglib;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.PermissionUtil;
import com.piramide.elwis.web.admin.session.User;
import org.apache.struts.taglib.html.SubmitTag;

import javax.servlet.jsp.JspException;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: SecuritySubmitTag.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class SecuritySubmitTag extends SubmitTag {
    private String functionality;
    private String operation;

    public int doStartTag() throws JspException {
        return super.doStartTag();
    }

    public int doAfterBody() throws JspException {
        return super.doAfterBody();
    }


    public int doEndTag() throws JspException {
        String bodyMessage = super.bodyContent.getString().trim();

        if (bodyMessage != null && !"".equals(bodyMessage)) {
            value = bodyMessage;
        }

        String permission = "";
        User user = (User) pageContext.getSession().getAttribute(Constants.USER_KEY);

        if (PermissionUtil.PERMISSION_CREATE.equals(operation.toUpperCase())) {
            permission = PermissionUtil.PERMISSION_CREATE;
        }
        if (PermissionUtil.PERMISSION_DELETE.equals(operation.toUpperCase())) {
            permission = PermissionUtil.PERMISSION_DELETE;
        }
        if (PermissionUtil.PERMISSION_UPDATE.equals(operation.toUpperCase())) {
            permission = PermissionUtil.PERMISSION_UPDATE;
        }
        if (PermissionUtil.PERMISSION_EXECUTE.equals(operation.toUpperCase())) {
            permission = PermissionUtil.PERMISSION_EXECUTE;
        }

        if (user.getSecurityAccessRights().containsKey(functionality)) {

            Byte accessRight = (Byte) user.getSecurityAccessRights().get(functionality);
            if (!PermissionUtil.hasAccessRight(accessRight, permission)) {
                return EVAL_PAGE; //skip the body
            }
        } else {
            return EVAL_PAGE; //skip the body
        }
        return super.doEndTag();

    }

    public String getFunctionality() {
        return functionality;
    }

    public void setFunctionality(String functionality) {
        this.functionality = functionality;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}
