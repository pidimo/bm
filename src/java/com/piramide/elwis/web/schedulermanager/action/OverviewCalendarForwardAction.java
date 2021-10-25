package com.piramide.elwis.web.schedulermanager.action;

import com.piramide.elwis.cmd.common.UserInfoCmd;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.ForwardAction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: OverviewCalendarForwardAction.java 27-jun-2008 11:20:47 $
 */
public class OverviewCalendarForwardAction extends ForwardAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing OverviewCalendarForwardAction................" + request.getParameterMap());

        DefaultForm defaultForm = (DefaultForm) form;

        //set in form type if this is not defined
        if (GenericValidator.isBlankOrNull((String) defaultForm.getDto("outputViewType"))) {

            int type = AbstractAppointmentUIAction.DAILY_VIEW_TYPE;
            User user = RequestUtils.getUser(request);
            Integer userId = (Integer) user.getValue("userId");

            UserInfoCmd userInfoCmd = new UserInfoCmd();
            userInfoCmd.putParam("sameUser", "false");
            userInfoCmd.putParam("isAppointment", CampaignConstants.TRUEVALUE);
            userInfoCmd.putParam("otherUserId", user.getValue("userId"));
            userInfoCmd.putParam("userId", userId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(userInfoCmd, request);
                if (!resultDTO.isFailure()) {
                    Integer calendarDefaultView = (Integer) userInfoCmd.getResultDTO().get("calendarDefaultView");
                    log.debug("User conf default view:" + calendarDefaultView);
                    if (calendarDefaultView != null) {
                        type = calendarDefaultView.intValue();
                    }
                }
            } catch (AppLevelException e) {
                log.debug("Error in execute cmd..");
            }
            defaultForm.setDto("outputViewType", String.valueOf(type));
        }

        return super.execute(mapping, form, request, response);
    }

}
