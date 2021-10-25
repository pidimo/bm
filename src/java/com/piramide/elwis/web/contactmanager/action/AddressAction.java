package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.action.DefaultAction;
import com.piramide.elwis.web.common.util.ActionForwardParameters;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.util.MessagesUtil;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import com.piramide.elwis.web.contactmanager.form.AddressForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Action Class for interact with AddressForm.
 * This class manage add and remove telecoms actions if it has executed, otherwise call super class to execute
 * the respective command.
 *
 * @author Ernesto
 * @version $Id: AddressAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class AddressAction extends DefaultAction {
    private Log log = LogFactory.getLog(AddressAction.class);

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ADDRESS Action executing...");
        AddressForm addressForm = (AddressForm) form;
        if (isCancelled(request)) {
            log.debug("action was canceled");

            /* If save after return from "Edit personal info" in contact person update page,
            return to source address as contact person*/

            if (addressForm.getDto("sourceAddressId") != null && !"".equals(addressForm.getDto("sourceAddressId"))) {
                log.debug("sourceAddressId = " + addressForm.getDto("sourceAddressId"));
                return new ActionForwardParameters()
                        .add("dto(addressId)", addressForm.getDto("sourceAddressId").toString())
                        .add("dto(contactPersonId)", addressForm.getDto("addressId").toString())
                        .add("dto(name1)", addressForm.getDto("name1").toString())
                        .add("dto(name2)", addressForm.getDto("name2").toString())
                        .add("contactId", addressForm.getDto("sourceAddressId").toString())
                        .forward(mapping.findForward("ContactPersonUpdate"));
            }
            // if save after return "Edit personal " in employee update  page
            if (addressForm.getDto("sourceEmployeeId") != null && !"".equals(addressForm.getDto("sourceEmployeeId"))) {
                log.debug("sourceEmployeeId =" + addressForm.getDto("sourceEmployeeId"));
                return new ActionForwardParameters()
                        .add("dto(employeeId)", addressForm.getDto("sourceEmployeeId").toString())
                        .add("dto(addressId)", addressForm.getDto("companyId").toString())
                        .add("dto(name1)", addressForm.getDto("name1").toString())
                        .add("dto(name2)", addressForm.getDto("name2").toString())
                        .add("contactId", addressForm.getDto("companyId").toString())
                        .forward(mapping.findForward("EmployeeUpdate"));
            }
            return (mapping.findForward("Cancel"));
        }

        //mantain predetermined telecoms
        AddressContactPersonHelper.restorePredeterminedTelecoms(addressForm.getTelecomMap());
        if (addressForm.getDto("delete") != null) {
            log.debug("Delete button has been pressed");

            AddressDTO addressDTO = new AddressDTO();
            addressDTO.putAll(addressForm.getDtoMap());
            ResultDTO resultDTO = new ResultDTO();
            IntegrityReferentialChecker.i.check(addressDTO, resultDTO);
            if (resultDTO.isFailure()) {
                saveErrors(request, MessagesUtil.i.convertToActionErrors(mapping, request, resultDTO));
                Map reqDTO = (Map) request.getAttribute("dto");
                if (reqDTO == null) {
                    reqDTO = new HashMap();
                    request.setAttribute("dto", reqDTO);
                }
                reqDTO.putAll(resultDTO);
                return mapping.getInputForward();
            }

            return new ActionForwardParameters()
                    //.add("dto(withReferences)", "true")
                    .add("dto(addressId)", addressForm.getDto("addressId").toString())
                    .add("dto(addressType)", addressForm.getDto("addressType").toString())
                    .add("dto(name1)", addressForm.getDto("name1").toString())
                    .add("dto(name2)", addressForm.getDto("name2").toString())
                    .add("dto(name3)", addressForm.getDto("name3").toString())
                    .forward(mapping.findForward("ConfirmDeletion"));
        }


        if ((addressForm.getDto("addTelecom")) != null) {

            String telecomTypeId = (String) addressForm.getDto("telecomTypeId");
            String data = (String) addressForm.getDto("telecomValue");
            String description = (String) addressForm.getDto("telecomDescription");
            User user = (User) request.getSession().getAttribute(Constants.USER_KEY);


            log.debug("Adding a Telecom...");

            ActionErrors errors = new ActionErrors();
            boolean isValidtelecom = true;
            /// validate if telecomtype exists
            if ("".equals(telecomTypeId) || telecomTypeId == null) {
                errors.add("telecomType", new ActionError("errors.required",
                        JSPHelper.getMessage(request, "Telecom.telecomType")));
                isValidtelecom = false;
            }
            //telecom value is required
            if ("".equals(data.trim())) {
                errors.add("telecomData", new ActionError("errors.required",
                        JSPHelper.getMessage(request, "Telecom.value")));
                isValidtelecom = false;
            }

            if (isValidtelecom) {
                log.debug("creating TelecomDTO");
                TelecomTypeSelectCmd cmd = new TelecomTypeSelectCmd();
                cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
                cmd.putParam("companyId", user.getValue("companyId"));
                cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, user.getValue("locale"));
                cmd.putParam("telecomTypeId", telecomTypeId);
                try {
                    ResultDTO resultDto = BusinessDelegate.i.execute(cmd, request);
                    TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) resultDto.get(TelecomTypeSelectCmd.RESULT);
                    if (telecomTypeDTO != null) { //it was found

                        TelecomDTO telecomDTO = new TelecomDTO(data, description,
                                !addressForm.getTelecomMap().containsKey(telecomTypeId));

                        addressForm.setTelecomMap(TelecomWrapperDTO.addToMapTelecomDTO(addressForm.getTelecomMap(),
                                telecomDTO, telecomTypeDTO));


                        addressForm.getDtoMap().remove("telecomValue");
                        addressForm.getDtoMap().remove("telecomTypeId");
                        addressForm.getDtoMap().remove("telecomDescription");

                    } else { //it was no found
                        errors.add("telecomType", new ActionError("error.SelectedNotFound",
                                JSPHelper.getMessage(request, "Telecom.telecomType")));
                    }
                } catch (AppLevelException e) {
                    log.error("Error executing", e);
                }


            }

            if (!errors.isEmpty()) {
                saveErrors(request, errors);
            }

            return mapping.findForward("addtelecom");
        }


        Map params = addressForm.getDtoMap();
        String removeTelecomValue;
        // iterating to check remove button pressed.
        ActionErrors removeErrors = new ActionErrors();
        for (Iterator iterator = params.keySet().iterator(); iterator.hasNext();) {
            String param = (String) iterator.next();
            if (param.startsWith("removeTelecom")) { //remove telecom
                log.debug("Removing telecom");
                removeTelecomValue = param.substring("removeTelecom".length());
                String indexedTelecomTypeId = removeTelecomValue.substring(0, removeTelecomValue.indexOf("@"));
                log.debug("telecomTypeId = " + indexedTelecomTypeId);
                String indexedTelecomId = removeTelecomValue.substring(removeTelecomValue.indexOf("@") + 1,
                        removeTelecomValue.indexOf("#"));
                log.debug("telecomId = " + indexedTelecomId);
                String indexedTelecomPosition = removeTelecomValue.substring(removeTelecomValue.indexOf("#") + 1);
                log.debug("position = " + indexedTelecomPosition);

                //trying to delete an registered telecom, then check if such telecom is referenced
                if (indexedTelecomId != null && !"".equals(indexedTelecomId)) {
                    if (ForeignkeyValidator.i.exists(WebMailConstants.TABLE_ADDRESSGROUP, "telecomid",
                            indexedTelecomId)) {
                        String telecomValue = addressForm.getTelecom(indexedTelecomTypeId).
                                getTelecom(new Integer(indexedTelecomPosition).intValue()).getData();
                        removeErrors.add("telecomValueEmailReferenced",
                                new ActionError("error.Telecom.Email.addressGroup.reference", telecomValue));
                    }
                }

                if (!removeErrors.isEmpty()) {
                    saveErrors(request, removeErrors);
                } else {
                    //remove the telecom
                    ((TelecomWrapperDTO) (addressForm.getTelecomMap().get(indexedTelecomTypeId))).
                            removeTelecomDTO(new Integer(indexedTelecomPosition).intValue());
                }
                //remove delete param
                addressForm.getDtoMap().remove(param);
                return mapping.findForward("addtelecom");
            }
        }

        ActionErrors errors = new ActionErrors();
        //checking foreignkeys
        if (addressForm.getDto("save") != null) {

            if (addressForm.getDto("cityId") != null && !"".equals(addressForm.getDto("cityId"))) {
                errors = ForeignkeyValidator.i.validate(CatalogConstants.TABLE_CITY, "cityid",
                        addressForm.getDto("cityId"), errors, new ActionError("error.SelectedNotFound",
                                JSPHelper.getMessage(request, "Contact.city")));
            }
            if (!errors.isEmpty()) {
                saveErrors(request, errors);
                return mapping.getInputForward();
            }
        }

        //put telecoms to dtoMap
        addressForm.getDtoMap().put("telecomMap", addressForm.getTelecomMap());
        ActionForward forward = super.execute(mapping, form, request, response); //execute the command
        //redirecting if needed
        if (forward != null && "Success".equals(forward.getName())) {
            if (addressForm.getDto("sourceAddressId") != null && !"".equals(addressForm.getDto("sourceAddressId"))) {
                log.debug("sourceAddressId = " + addressForm.getDto("sourceAddressId"));
                return new ActionForwardParameters()
                        .add("dto(addressId)", addressForm.getDto("sourceAddressId").toString())
                        .add("dto(contactPersonId)", addressForm.getDto("addressId").toString())
                        .add("dto(name1)", addressForm.getDto("name1").toString())
                        .add("dto(name2)", addressForm.getDto("name2").toString())
                        .add("contactId", addressForm.getDto("sourceAddressId").toString())
                        .forward(mapping.findForward("ContactPersonUpdate"));
            }
            // if save after return "Edit personal " in employee update  page
            if (addressForm.getDto("sourceEmployeeId") != null && !"".equals(addressForm.getDto("sourceEmployeeId"))) {
                log.debug("sourceEmployeeId =" + addressForm.getDto("sourceEmployeeId"));
                return new ActionForwardParameters()
                        .add("dto(employeeId)", addressForm.getDto("sourceEmployeeId").toString())
                        .add("dto(addressId)", addressForm.getDto("companyId").toString())
                        .add("dto(name1)", addressForm.getDto("name1").toString())
                        .add("dto(name2)", addressForm.getDto("name2").toString())
                        .add("contactId", addressForm.getDto("companyId").toString())
                        .forward(mapping.findForward("EmployeeUpdate"));
            }
        }

        Map values = (Map) request.getAttribute("dto");
        //puting the contactId to rewrite url.
        if (values != null) {
            request.setAttribute("contactId", values.get("addressId"));
        }

        return forward;
    }
}
