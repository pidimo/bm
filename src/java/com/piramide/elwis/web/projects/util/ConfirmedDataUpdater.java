package com.piramide.elwis.web.projects.util;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import net.java.dev.strutsejb.web.DefaultForm;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ConfirmedDataUpdater implements WorkFlowDataUpdater {
    public void update(DefaultForm defaultForm, HttpServletRequest request) {
        User sessionUser = RequestUtils.getUser(request);
        Integer sessionUserId = (Integer) sessionUser.getValue(Constants.USERID);
        defaultForm.setDto("confirmedById", sessionUserId);
    }
}
