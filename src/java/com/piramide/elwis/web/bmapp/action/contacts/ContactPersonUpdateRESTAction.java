package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.cmd.contactmanager.ContactPersonCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.action.ContactPersonAction;
import com.piramide.elwis.web.contactmanager.form.ContactPersonForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4.1
 */
public class ContactPersonUpdateRESTAction extends ContactPersonAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  ContactPersonUpdateRESTAction..." + request.getParameterMap());

        ContactPersonForm contactPersonForm = (ContactPersonForm) form;
        log.debug("FORM DTO FROM REST:::::::" + contactPersonForm.getDtoMap());

        Map dtoMap = getRESTDtoMap(contactPersonForm);

        ActionErrors readErrors = readCurrentContactPersonInfo(contactPersonForm, mapping, request);

        if (!readErrors.isEmpty()) {
            saveRESTErrors(contactPersonForm, readErrors, request);
            return mapping.findForward("Fail");
        }

        log.debug("FORM DTO AFTER MAPPING::::::::" + contactPersonForm.getDtoMap());

        mappingRESTValues(dtoMap, contactPersonForm, request);

        log.debug("FORM DTO AFTER MAPPING 2222::::::" + contactPersonForm.getDtoMap());

        ActionErrors errors = contactPersonForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveRESTErrors(contactPersonForm, errors, request);
            return mapping.getInputForward();
        }

        return super.execute(mapping, contactPersonForm, request, response);
    }

    private Map getRESTDtoMap(DefaultForm defaultForm) {
        Map dtoMap = new HashMap();
        dtoMap.putAll(defaultForm.getDtoMap());
        return dtoMap;
    }

    private void mappingRESTValues(Map dtoMap, ContactPersonForm contactPersonForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        MappingUtil.mappingProperty("departmentId", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("function", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("personTypeId", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("addAddressLine", dtoMap, contactPersonForm);

        MappingUtil.mappingProperty("name1", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("name2", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("name3", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("searchName", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("keywords", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("education", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("languageId", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("salutationId", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("titleId", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("note", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("birthday", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("privateVersion", dtoMap, contactPersonForm);
        MappingUtil.mappingProperty("version", dtoMap, contactPersonForm);

        //define telecom type additional data
        MappingUtil.mappingTelecomTypes(contactPersonForm.getTelecomMap(), request);

        //telecoms
        contactPersonForm.setDto("telecomTypeId", "");
        contactPersonForm.setDto("telecomValue", "");
        contactPersonForm.setDto("telecomDescription", "");

        contactPersonForm.setDto("lastModificationUserId", sessionUserId);
        contactPersonForm.setDto("lastUpdateDate", new Date());
        contactPersonForm.setDto("op", "update");
        contactPersonForm.setDto("save", "save");
    }

    private ActionErrors readCurrentContactPersonInfo(ContactPersonForm contactPersonForm, ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Integer addressId = new Integer(contactPersonForm.getDto("addressId").toString());
        Integer contactPersonId = new Integer(contactPersonForm.getDto("contactPersonId").toString());

        ContactPersonCmd contactPersonCmd = new ContactPersonCmd();
        contactPersonCmd.putParam("addressId", addressId);
        contactPersonCmd.putParam("contactPersonId", contactPersonId);

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(contactPersonCmd, null);
        } catch (AppLevelException e) {
            log.error("Error executing TelecomUtilCmd cmd", e);
        }

        if (resultDTO != null) {
            errors = processResuldDtoErrors(resultDTO, mapping, request);
            if (errors.isEmpty()) {

                contactPersonForm.getDtoMap().putAll(resultDTO);

                MappingUtil.mappingBirthdayProperty("birthday", resultDTO, contactPersonForm, request);

                MappingUtil.mappingBooleanPropertyFalseAsNULL("isSupplier", resultDTO, contactPersonForm);
                MappingUtil.mappingBooleanPropertyFalseAsNULL("isCustomer", resultDTO, contactPersonForm);
                MappingUtil.mappingBooleanPropertyFalseAsNULL("isCompany", resultDTO, contactPersonForm);
                MappingUtil.mappingBooleanPropertyFalseAsNULL("isActive", resultDTO, contactPersonForm);

                //remove version
                contactPersonForm.getDtoMap().remove("version");
                contactPersonForm.getDtoMap().remove("privateVersion");
            }
        }

        return errors;
    }

    private ActionErrors processResuldDtoErrors(ResultDTO resultDTO, ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (resultDTO.isFailure()) {
            errors = MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO);
        }
        return errors;
    }

    private void saveRESTErrors(ContactPersonForm defaultForm, ActionErrors errors, HttpServletRequest request) {
        defaultForm.setDto("contactPersonTelecomMap", defaultForm.getTelecomMap());

        request.setAttribute("dto", defaultForm.getDtoMap());
        saveErrors(request, errors);
    }

}
