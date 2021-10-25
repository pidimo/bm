package com.piramide.elwis.web.common.action;

import com.piramide.elwis.web.admin.session.User;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 19, 2005
 * Time: 5:23:02 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ActionSuperExecute {

    public ActionForward superExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception;

    void mySaveErrors(HttpServletRequest request, ActionErrors errors);

    public User getUser();
}
