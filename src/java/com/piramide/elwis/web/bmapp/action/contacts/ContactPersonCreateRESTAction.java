package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.action.ContactPersonAction;
import com.piramide.elwis.web.contactmanager.form.ContactPersonForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1.5
 */
public class ContactPersonCreateRESTAction extends ContactPersonAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ContactPersonCreateRESTAction..." + request.getParameterMap());

        ContactPersonForm contactPersonForm = (ContactPersonForm) form;
        log.debug("FORM DTO FROM REST:::::::" + contactPersonForm.getDtoMap());

        if ("true".equals(contactPersonForm.getDto("importAddress"))) {
            request.setAttribute("isFromImportAddress", "true");
        }

        setDefaultProperties(contactPersonForm, request);

        ActionErrors errors = contactPersonForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveRESTErrors(contactPersonForm, errors, request);
            return mapping.getInputForward();
        }

        return super.execute(mapping, contactPersonForm, request, response);
    }

    private void setDefaultProperties(ContactPersonForm contactPersonForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        //define telecom type additional data
        MappingUtil.mappingTelecomTypes(contactPersonForm.getTelecomMap(), request);

        //telecoms
        contactPersonForm.setDto("telecomTypeId", "");
        contactPersonForm.setDto("telecomValue", "");
        contactPersonForm.setDto("telecomDescription", "");

        contactPersonForm.setDto("addressType", ContactConstants.ADDRESSTYPE_PERSON);
        contactPersonForm.setDto("isActive", "true");
        contactPersonForm.setDto("recordUserId", sessionUserId);
        contactPersonForm.setDto("lastModificationUserId", sessionUserId);
        contactPersonForm.setDto("companyId", user.getValue("companyId").toString());
        contactPersonForm.setDto("op", "create");
        contactPersonForm.setDto("save", "save");
    }

    private void saveRESTErrors(ContactPersonForm defaultForm, ActionErrors errors, HttpServletRequest request) {
        defaultForm.setDto("contactPersonTelecomMap", defaultForm.getTelecomMap());

        request.setAttribute("dto", defaultForm.getDtoMap());
        saveErrors(request, errors);
    }

}
