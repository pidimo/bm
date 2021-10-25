package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.deduplication.DeduplicationFactory;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.delegate.DeduplicationAddressDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public class DedupliContactProcessAction extends DefaultAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing  DedupliContactProcessAction..." + request.getParameterMap());

        ActionForward forward;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);
        Integer userId = (Integer) user.getValue(Constants.USERID);

        boolean existDuplicates =  DeduplicationAddressDelegate.i.companyHasContactDuplicates(companyId);
        if (existDuplicates) {
            ActionErrors errors = new ActionErrors();
            errors.add("containDuplicates", new ActionError("DedupliContact.error.canNotExecuteCheck"));
            saveErrors(request, errors);
        } else {
            Integer dedupliContactId = DeduplicationAddressDelegate.i.initializeContactDeduplication(companyId, userId);
            boolean foundDuplicates = DeduplicationFactory.i.addressDeduplicateProcess(dedupliContactId, companyId, request);
            DeduplicationAddressDelegate.i.finishContactDeduplication(dedupliContactId);

            addEndProcessMessage(foundDuplicates, request);
        }

        return mapping.findForward("Success");
    }

    private void addEndProcessMessage(boolean foundDuplicates, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (foundDuplicates) {
            errors.add("foundDupli", new ActionError("DedupliContact.message.processFoundDuplicates"));
        } else {
            errors.add("notFoundDupli", new ActionError("DedupliContact.message.processNotFoundDuplicates"));
        }
        saveErrors(request, errors);
    }
}