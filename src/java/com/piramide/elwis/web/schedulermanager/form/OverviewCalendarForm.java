package com.piramide.elwis.web.schedulermanager.form;

import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.schedulermanager.el.Functions;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Jatun S.R.L.
 * Validate overview calendar of other user
 *
 * @author Miky
 * @version $Id: OverviewCalendarForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class OverviewCalendarForm extends DefaultForm {

    public void setSelectedUser(Object[] obj) {
        List list = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            list.add(obj[i]);
        }
        setDto("listSelectedUser", list);
    }

    public Object[] getSelectedUser() {
        List list = (List) getDto("listSelectedUser");
        if (list != null) {
            return list.toArray();
        } else {
            return new Object[]{};
        }
    }

    public void setAvailableUser(Object[] obj) {
        List list = new ArrayList();
        for (int i = 0; i < obj.length; i++) {
            list.add(obj[i]);
        }
        setDto("listAvailableUser", list);
    }

    public Object[] getAvailableUser() {
        List list = (List) getDto("listAvailableUser");
        if (list != null) {
            return list.toArray();
        } else {
            return new Object[]{};
        }
    }

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Excecuting validate OverviewCalendarForm......." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (GenericValidator.isBlankOrNull((String) getDto("outputViewType"))) {
            errors.add("required", new ActionError("errors.required", JSPHelper.getMessage(request, "Scheduler.overviewCalendar.outputView")));
        }

        //validate users
        List<String> validSelectedUsers = new ArrayList<String>();

        List<LabelValueBean> overviewCalendarUsers = Functions.getOverviewCalendarUsers(request);
        List formSelectedUsers = (List) getDto("listSelectedUser");

        List<LabelValueBean> availableUserList = new ArrayList<LabelValueBean>();
        List<LabelValueBean> selectedUserList = new ArrayList<LabelValueBean>();

        if (formSelectedUsers != null) {
            for (Iterator iterator = overviewCalendarUsers.iterator(); iterator.hasNext();) {
                LabelValueBean labelValueBean = (LabelValueBean) iterator.next();
                boolean existUser = false;

                for (Iterator iterator2 = formSelectedUsers.iterator(); iterator2.hasNext();) {
                    String selectedUserId = (String) iterator2.next();
                    if (labelValueBean.getValue().equals(selectedUserId)) {
                        existUser = true;
                        validSelectedUsers.add(selectedUserId);
                        selectedUserList.add(labelValueBean);
                        break;
                    }
                }

                if (!existUser) {
                    availableUserList.add(labelValueBean);
                }
            }
        } else {
            availableUserList = overviewCalendarUsers;
        }

        if (validSelectedUsers.size() > 0) {
            setDto("overviewUsers", validSelectedUsers);
        } else {
            errors.add("atleast", new ActionError("Scheduler.overviewCalendar.shouldSelectAtLeastOne"));
        }

        if (!errors.isEmpty()) {
            request.setAttribute("selectedList", selectedUserList);
            request.setAttribute("availableUserList", availableUserList);
        }

        return errors;
    }
}
