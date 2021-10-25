package com.piramide.elwis.web.bmapp.action.contacts;

import com.piramide.elwis.cmd.contactmanager.AddressCmd;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.bmapp.util.MappingUtil;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.contactmanager.action.AddressAction;
import com.piramide.elwis.web.contactmanager.form.AddressForm;
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
public class AddressUpdateRESTAction extends AddressAction {
    protected Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Executing  AddressUpdateRESTAction..." + request.getParameterMap());

        AddressForm addressForm = (AddressForm) form;
        Map dtoMap = getRESTDtoMap(addressForm);
        log.debug("DDDDDDDDDDDDDDDDDDDDDEFAULT:" + addressForm.getDtoMap());
        log.debug("FFFFFFFFFFFFF:" + addressForm.getCountryId());
        log.debug("FFFFFFFFFFFFF:" + addressForm.getCity());


        ActionErrors readErrors = readCurrentAddressInfo(addressForm, mapping, request);

        if (!readErrors.isEmpty()) {
            saveRESTErrors(addressForm, readErrors, request);
            return mapping.findForward("Fail");
        }

        log.debug("ADRESSSSSSSSS:" + addressForm.getDtoMap());

        mappingRESTValues(dtoMap, addressForm, request);

        log.debug("ADRESSSS22222:" + addressForm.getDtoMap());

        ActionErrors errors = addressForm.validate(mapping, request);

        if (!errors.isEmpty()) {
            saveRESTErrors(addressForm, errors, request);
            return mapping.getInputForward();
        }

        return super.execute(mapping, addressForm, request, response);
    }

    private Map getRESTDtoMap(DefaultForm defaultForm) {
        Map dtoMap = new HashMap();
        dtoMap.putAll(defaultForm.getDtoMap());
        return dtoMap;
    }

    private void mappingRESTValues(Map dtoMap, AddressForm addressForm, HttpServletRequest request) {
        User user = RequestUtils.getUser(request);
        Integer sessionUserId = Integer.valueOf(user.getValue(Constants.USERID).toString());

        MappingUtil.mappingProperty("name1", dtoMap, addressForm);
        MappingUtil.mappingProperty("name2", dtoMap, addressForm);
        MappingUtil.mappingProperty("name3", dtoMap, addressForm);
        MappingUtil.mappingProperty("searchName", dtoMap, addressForm);
        MappingUtil.mappingProperty("keywords", dtoMap, addressForm);
        MappingUtil.mappingProperty("education", dtoMap, addressForm);
        MappingUtil.mappingProperty("languageId", dtoMap, addressForm);
        MappingUtil.mappingProperty("street", dtoMap, addressForm);
        MappingUtil.mappingProperty("houseNumber", dtoMap, addressForm);
        MappingUtil.mappingProperty("additionalAddressLine", dtoMap, addressForm);
        MappingUtil.mappingProperty("zip", dtoMap, addressForm);
        MappingUtil.mappingProperty("city", dtoMap, addressForm);
        MappingUtil.mappingProperty("cityId", dtoMap, addressForm);
        MappingUtil.mappingProperty("countryId", dtoMap, addressForm);
        MappingUtil.mappingProperty("countryCode", dtoMap, addressForm);
        MappingUtil.mappingProperty("salutationId", dtoMap, addressForm);
        MappingUtil.mappingProperty("titleId", dtoMap, addressForm);
        MappingUtil.mappingProperty("accessUserGroupIds", dtoMap, addressForm);

        MappingUtil.mappingProperty("note", dtoMap, addressForm);
        MappingUtil.mappingProperty("wayDescription", dtoMap, addressForm);

        MappingUtil.mappingProperty("birthday", dtoMap, addressForm);
        MappingUtil.mappingProperty("version", dtoMap, addressForm);

        //define telecom type additional data
        MappingUtil.mappingTelecomTypes(addressForm.getTelecomMap(), request);

        //telecoms
        addressForm.setDto("telecomTypeId", "");
        addressForm.setDto("telecomValue", "");
        addressForm.setDto("telecomDescription", "");

        addressForm.setDto("lastModificationUserId", sessionUserId);
        addressForm.setDto("lastUpdateDate", new Date());
        addressForm.setDto("op", "update");
        addressForm.setDto("save", "update");
    }

    private ActionErrors readCurrentAddressInfo(AddressForm addressForm, ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();

        Integer addressId = new Integer(addressForm.getDto("addressId").toString());

        AddressCmd addressCmd = new AddressCmd();
        addressCmd.putParam("addressId", addressId);

        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(addressCmd, null);
        } catch (AppLevelException e) {
            log.error("Error executing TelecomUtilCmd cmd", e);
        }

        if (resultDTO != null) {
            errors = processResuldDtoErrors(resultDTO, mapping, request);
            if (errors.isEmpty()) {

                addressForm.getDtoMap().putAll(resultDTO);

                MappingUtil.mappingPropertyAsString("addressId", resultDTO, addressForm);
                MappingUtil.mappingBirthdayProperty("birthday", resultDTO, addressForm, request);

                MappingUtil.mappingBooleanPropertyFalseAsNULL("isSupplier", resultDTO, addressForm);
                MappingUtil.mappingBooleanPropertyFalseAsNULL("isCustomer", resultDTO, addressForm);
                MappingUtil.mappingBooleanPropertyFalseAsNULL("isCompany", resultDTO, addressForm);
                MappingUtil.mappingBooleanPropertyFalseAsNULL("isActive", resultDTO, addressForm);

                //remove version
                addressForm.getDtoMap().remove("version");
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

    private void saveRESTErrors(AddressForm defaultForm, ActionErrors errors, HttpServletRequest request) {
        defaultForm.setDto("telecomMap", defaultForm.getTelecomMap());

        request.setAttribute("dto", defaultForm.getDtoMap());
        saveErrors(request, errors);
    }

}
