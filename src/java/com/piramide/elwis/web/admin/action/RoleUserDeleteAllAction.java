package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.RoleUserCmd;
import com.piramide.elwis.web.admin.form.RoleUserForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author : ivan
 * @version : $Id RoleUserDeleteAllAction ${time}
 */
public class RoleUserDeleteAllAction extends RoleListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("RoleUserDeleteAllAction.execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)");


        //execute the super action that checks if the role exists in database
        ActionForward forward = super.execute(mapping, form, request, response);

        if (null == forward) {
            return mapping.findForward("Cancel");
        }

        if (!"Fail".equals(forward.getName())) {
            RoleUserForm myFormRole = (RoleUserForm) form;

            //gets roleId from request.param
            Integer roleId = Integer.valueOf(request.getParameter("roleId"));

            ActionErrors errors = new ActionErrors();
            //if delete users button is pressed into page RoleUserList.jsp
            if ("true".equals(myFormRole.getIsSubmit())) {

                //getAditionals contains array of userId's have selected for deleted form actual role
                if (myFormRole.getAditionals().length == 0) {
                    errors.add("userImportForm", new ActionError("User.selectAtLeastOneToDelete"));
                }
            }
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("GoBack");
            }

            //if no have errors and delete button is pressed then read all
            //user information can be deleted
            if (errors.isEmpty() && "true".equals(myFormRole.getIsSubmit())) {
                List users = Arrays.asList(myFormRole.getAditionals());

                RoleUserCmd cmd = new RoleUserCmd();
                cmd.setOp("readAllUserInformation");
                cmd.putParam("roleId", roleId);
                cmd.putParam("users", users);
                try {
                    ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);

                    List userNameList = new ArrayList();
                    if (null != myResultDTO.get("userNameList")) {
                        userNameList = (List) myResultDTO.get("userNameList");
                    }

                    request.setAttribute("userNameList", userNameList);
                    request.setAttribute("roleId", myResultDTO.get("roleId"));
                    request.setAttribute("userNameListSize", String.valueOf(userNameList.size()));
                    return mapping.findForward("Delete");
                } catch (AppLevelException e) {
                    //return mapping.findForward("Fail");
                }
            }
        }
        return forward;
    }
}
