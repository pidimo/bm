package com.piramide.elwis.web.contactmanager.action;

import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.common.ModuleEntriesLimitUtilCmd;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.mapping.CheckEntriesMapping;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import com.piramide.elwis.web.contactmanager.form.AddressContactPersonHelper;
import com.piramide.elwis.web.contactmanager.form.ContactPersonForm;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;


/**
 * Action Class for interact with ContactPersonForm.
 * This class manage add and remove telecoms actions if it has executed, otherwise call super class to execute
 * the respective command.
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonAction.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class ContactPersonAction extends ContactManagerAction {
    private Log log = LogFactory.getLog(this.getClass());

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("ContactPerson Action execution");

        ContactPersonForm contactPersonForm = (ContactPersonForm) form;

        //because contact person do not change addres information, invalid date.
        contactPersonForm.getDtoMap().put("birtdayDate", "");
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        //mantain predetermined telecoms
        AddressContactPersonHelper.restorePredeterminedTelecoms(contactPersonForm.getTelecomMap());
        if ((contactPersonForm.getDto("addTelecom")) != null) {
            String telecomTypeId = (String) contactPersonForm.getDto("telecomTypeId");
            String data = (String) contactPersonForm.getDto("telecomValue");
            String description = (String) contactPersonForm.getDto("telecomDescription");

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
                                !contactPersonForm.getTelecomMap().containsKey(telecomTypeId));

                        contactPersonForm.setTelecomMap(TelecomWrapperDTO.addToMapTelecomDTO(contactPersonForm.getTelecomMap(),
                                telecomDTO, telecomTypeDTO));


                        contactPersonForm.getDtoMap().remove("telecomValue");
                        contactPersonForm.getDtoMap().remove("telecomTypeId");
                        contactPersonForm.getDtoMap().remove("telecomDescription");

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

        Map params = contactPersonForm.getDtoMap();
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
                        String telecomValue = contactPersonForm.getTelecom(indexedTelecomTypeId).
                                getTelecom(new Integer(indexedTelecomPosition).intValue()).getData();
                        removeErrors.add("telecomValueEmailReferenced",
                                new ActionError("error.Telecom.Email.addressGroup.reference", telecomValue));
                    }
                }

                if (!removeErrors.isEmpty()) {
                    saveErrors(request, removeErrors);
                } else {
                    //remove the telecom
                    ((TelecomWrapperDTO) (contactPersonForm.getTelecomMap().get(indexedTelecomTypeId))).
                            removeTelecomDTO(new Integer(indexedTelecomPosition).intValue());
                }
                //remove delete param
                contactPersonForm.getDtoMap().remove(param);
                return mapping.findForward("addtelecom");
            }
        }

        //set telecoms
        contactPersonForm.getDtoMap().put("contactPersonTelecomMap", contactPersonForm.getTelecomMap());

        if (!"true".equals(contactPersonForm.getDto("importAddress"))) {
            if (mapping instanceof com.piramide.elwis.web.common.mapping.CheckEntriesMapping) {
                log.debug(" ... check entries limit ... ");

                CheckEntriesMapping entriesMapping = new CheckEntriesMapping();
                entriesMapping = (CheckEntriesMapping) mapping;
                ModuleEntriesLimitUtilCmd limitUtilCmd = new ModuleEntriesLimitUtilCmd();
                limitUtilCmd.putParam("companyId", new Integer(user.getValue(Constants.COMPANYID).toString()));
                limitUtilCmd.putParam("mainTable", entriesMapping.getMainTable());
                limitUtilCmd.putParam("functionality", "CONTACT");

                try {
                    BusinessDelegate.i.execute(limitUtilCmd, request);
                } catch (AppLevelException e) {
                    log.debug(" ... error execute ModuleEntriesUtilCmd ...");
                }

                if (!limitUtilCmd.getResultDTO().isFailure()) {
                    if (!((Boolean) limitUtilCmd.getResultDTO().get("canCreate")).booleanValue()) {
                        log.debug(" ... can't create ...");
                        ActionErrors errors = new ActionErrors();
                        errors.add(ActionErrors.GLOBAL_ERROR, new ActionError("Common.message.checkEntries"));
                        saveErrors(request, errors);
                        return mapping.findForward("redirect");
                    }
                }
            }
        }
        return super.execute(mapping, contactPersonForm, request, response);
    }
}
