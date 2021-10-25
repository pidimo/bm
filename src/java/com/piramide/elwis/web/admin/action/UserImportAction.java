package com.piramide.elwis.web.admin.action;

import com.piramide.elwis.cmd.admin.RoleUserCmd;
import com.piramide.elwis.web.admin.form.RoleUserForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * @author : ivan
 * @version : $Id UserImportAction ${time}
 */
public class UserImportAction extends RoleListAction {
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {

        ActionForward forward = new ActionForward();

        //execute the super action that checks if the role exists in database
        forward = super.execute(mapping, form, request, response);


        if (!"Fail".equals(forward.getName())) {

            RoleUserForm myFormRole = (RoleUserForm) form;

            //gets roleId from request.param
            Integer roleId = Integer.valueOf(request.getParameter("roleId"));

            ActionErrors errors = new ActionErrors();
            //if add new users button is pressed into page UserImportList.jsp then myFormRole.getSubmit equals "true"
            if ("true".equals(myFormRole.getIsSubmit())) {

                //myFormRole.getAditionals contains array of userId's have selected for add the role
                if (myFormRole.getAditionals().length == 0) {
                    errors.add("userImportForm", new ActionError("User.selectAtLeastOneToAdd"));
                }

                //gets actuals users assigned to role
                RoleUserCmd cmd = new RoleUserCmd();
                cmd.setOp("checkSelected");
                cmd.putParam("roleId", roleId);
                try {
                    ResultDTO myResultDTO = BusinessDelegate.i.execute(cmd, request);
                    List assignedUsers = (List) myResultDTO.get("usersByRole");
                    List aditionalsList = Arrays.asList(myFormRole.getAditionals());
                    for (int i = 0; i < aditionalsList.size(); i++) {
                        String actual = aditionalsList.get(i).toString();

                        //if any user key exists into assigned user then throws errors "alreasy users have assigned"
                        if (assignedUsers.contains(actual)) {
                            errors.add("userImportForm", new ActionError("User.alreadySelected"));
                            break;
                        }
                    }
                } catch (AppLevelException e) {
                }
            }

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.findForward("GoBack");
            }

            List users = Arrays.asList(myFormRole.getAditionals());

            //if have users selected and cannot errors
            // add new users button is pressed into page UserImportList.jsp
            if (null != users &&
                    !users.isEmpty() &&
                    errors.isEmpty() &&
                    "true".equals(myFormRole.getIsSubmit())) {

                //assign to actual role all users have selected
                RoleUserCmd cmd = new RoleUserCmd();
                cmd.setOp("create");
                cmd.putParam("roleId", roleId);
                cmd.putParam("userKeys", users);
                try {
                    ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                    if ("Fail".equals(resultDTO.getForward()))
                    //only if cannot find role
                    {
                        return mapping.findForward("Fail");
                    } else
                    //all users have assigned to actual role
                    {
                        return mapping.findForward("Saved");
                    }
                } catch (AppLevelException e) {
                }
            }
        }


        return forward;
    }
}
