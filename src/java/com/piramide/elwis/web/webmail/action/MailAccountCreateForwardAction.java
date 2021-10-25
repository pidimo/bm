package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.web.common.action.DefaultForwardAction;
import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by IntelliJ IDEA.
 * User: ivan
 * Date: 18-10-2009
 * Time: 07:03:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class MailAccountCreateForwardAction extends DefaultForwardAction {
    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        defaultForm.setDto("usePOPConfiguration", true);
    }
}
