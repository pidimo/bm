package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.action.AddressAction;
import com.piramide.elwis.web.contactmanager.form.AddressForm;
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
 * @version 5.4.1
 */
public class AddressCreateRESTAction extends AddressAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AddressCreateRESTAction..." + request.getParameterMap());

        AddressForm addressForm = (AddressForm) form;
        log.debug("FORM DTO FROM REST:::::::" + addressForm.getDtoMap());

        setDefaultProperties(addressForm, request);

        log.debug("FORM DTO AFTER MAPPING::::::::" + addressForm.getDtoMap());

        ActionErrors errors = addressForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveRESTErrors(addressForm, errors, request);
            return mapping.getInputForward();
        }

        return super.execute(mapping, addressForm, request, response);
    }

    private void setDefaultProperties(AddressForm addressForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        //define telecom type additional data
        MappingUtil.mappingTelecomTypes(addressForm.getTelecomMap(), request);

        //telecoms
        addressForm.setDto("telecomTypeId", "");
        addressForm.setDto("telecomValue", "");
        addressForm.setDto("telecomDescription", "");

        addressForm.setDto("userTypeValue", "0");
        addressForm.setDto("recordUserId", sessionUserId);
        addressForm.setDto("companyId", user.getValue("companyId").toString());
        addressForm.setDto("op", "create");
        addressForm.setDto("save", "save");
    }

    private void saveRESTErrors(AddressForm defaultForm, ActionErrors errors, HttpServletRequest request) {
        defaultForm.setDto("telecomMap", defaultForm.getTelecomMap());

        request.setAttribute("dto", defaultForm.getDtoMap());
        saveErrors(request, errors);
    }

}
