package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.contactmanager.AddressDuplicatedCmd;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.TelecomType;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.common.validator.EmailValidator;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.ejb.FinderException;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Alfacentauro Team
 * <p/>
 * this class validate the entryes for add address
 *
 * @author miky
 * @version $Id: AddressAddForm.java 10020 2010-12-16 22:08:00Z ivan $
 */
public class AddressAddForm extends WebmailDefaultForm {


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {

        log.debug("Executing validate MailAddressAddSentForm........." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);

        if (getDto("cancel") == null) {

            String addressType = getDto("addressType").toString();
            String companyId = getDto("companyId").toString();
            String userId = getDto("userMailId").toString();
            String isUpdate = null;
            Integer addressId = null;
            Integer contactPersonId = null;
            boolean bandCheckEmail = true;

            if (getDto("isUpdate") != null && getDto("addressId") != null && !"".equals(getDto("addressId").toString())) {
                isUpdate = "true";

                addressId = Integer.valueOf(getDto("addressId").toString());

                //control of concurrence
                if (getDto("contactPersonId") != null && !"".equals(getDto("contactPersonId").toString())) {

                    ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
                    contactPersonId = Integer.valueOf(getDto("contactPersonId").toString());
                    ContactPersonPK contactPersonPK = new ContactPersonPK(addressId, contactPersonId);
                    try {
                        ContactPerson contactPerson = contactPersonHome.findByPrimaryKey(contactPersonPK);
                    } catch (FinderException fe) {
                        errors.add("Person", new ActionError("Webmail.Address.NotFound", getDto("EditName").toString()));
                        bandCheckEmail = false;
                    }

                } else if (!ForeignkeyValidator.i.exists(CatalogConstants.TABLE_ADDRESS, "addressid", addressId)) {
                    errors.add("Person", new ActionError("Webmail.Address.NotFound", getDto("EditName").toString()));
                    bandCheckEmail = false;
                }

            } else {

                isUpdate = "false";
                if (addressType.equals(ContactConstants.ADDRESSTYPE_PERSON)) {

                    String name1 = getDto("PerName1").toString();
                    String name2 = getDto("PerName2").toString();
                    String name3 = getDto("PerName3").toString();

                    if (GenericValidator.isBlankOrNull(name1)) {
                        errors.add("name1", new ActionError("errors.required",
                                JSPHelper.getMessage(request, "Contact.Person.lastname")));
                    } else if (addressIsDuplicate(request, name1, name2, name3, addressType, companyId, userId)) {
                        errors.add("duplicatedPerson", new ActionError("Webmail.Address.Person.AlreadyExists",
                                name1 + "" + ((name2 != null && !"".equals(name2)) ? ", " + name2 : "")));
                    }


                }
                if (addressType.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {

                    String name1 = getDto("OrgName1").toString();
                    String name2 = getDto("OrgName2").toString();
                    String name3 = getDto("OrgName3").toString();

                    if (GenericValidator.isBlankOrNull(name1)) {
                        errors.add("name1", new ActionError("errors.required", JSPHelper.getMessage(request,
                                "Contact.Organization.name")));
                    } else if (addressIsDuplicate(request, name1, name2, name3, addressType, companyId, userId)) {
                        errors.add("duplicatedOrganization", new ActionError("Webmail.Address.Organization.AlreadyExists",
                                name1 + " " + ((name2 != null) ? name2 : "") +
                                        " " + ((name3 != null) ? name3 : "")));
                    }


                }
            }

            //validate email
            if (GenericValidator.isBlankOrNull(getDto("email").toString())) {
                errors.add("email", new ActionError("errors.required", JSPHelper.getMessage(request, "Common.email")));
            } else if (!EmailValidator.i.isValid(getDto("email").toString())) {
                errors.add("email", new ActionError("errors.email", getDto("email").toString()));
            } else if (isUpdate.equals("true") && bandCheckEmail) {
                TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
                if (contactPersonId != null) { //check in contact person
                    try {
                        Collection telecoms = telecomHome.findContactPersonTelecomsByTelecomTypeType(addressId, contactPersonId, TelecomType.EMAIL_TYPE);
                        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                            Telecom telecom = (Telecom) iterator.next();
                            if (telecom.getData().equals(getDto("email").toString())) {
                                errors.add("email", new ActionError("Webmail.Contact.emailAlreadyExists", getDto("email").toString(), getDto("EditName").toString()));
                            }
                        }
                    } catch (FinderException fe) {
                    }
                } else { // check in address
                    try {
                        Collection telecoms = telecomHome.findAddressTelecomsByTelecomTypeType(addressId, TelecomType.EMAIL_TYPE);
                        for (Iterator iterator = telecoms.iterator(); iterator.hasNext();) {
                            Telecom telecom = (Telecom) iterator.next();
                            if (telecom.getData().equals(getDto("email").toString())) {
                                errors.add("email", new ActionError("Webmail.Contact.emailAlreadyExists", getDto("email").toString(), getDto("EditName").toString()));
                            }
                        }
                    } catch (FinderException fe) {
                    }
                }
            }

            // validate telecomTypeId
            if (GenericValidator.isBlankOrNull(getDto("telecomTypeId").toString())) {

                errors.add("TelecomType", new ActionError("error.Telecom.values.required", ""));
            } else {// control concurrence
                Integer telecomTypeId = Integer.valueOf(getDto("telecomTypeId").toString());
                if (!ForeignkeyValidator.i.exists(CatalogConstants.TABLE_TELECOMTYPE, "telecomtypeid", telecomTypeId)) {
                    errors.add("TelecomType", new ActionError("error.TelecomTypeNotFound", ""));
                }
            }

            Map addressMap = new HashMap(2);
            addressMap.put("typeAddress", addressType);
            addressMap.put("isUpdate", isUpdate);

            request.setAttribute("addressData", addressMap);
        }

        return errors;
    }

    private boolean addressIsDuplicate(HttpServletRequest request,
                                       String name1, String name2, String name3,
                                       String addressType, String companyId, String userId) {

        boolean res = false;
        // validate address duplicated only if user is in create operation and do not have any errors.
        log.debug("Checking duplicated contacts before creation");
        AddressDuplicatedCmd duplicatedCmd = new AddressDuplicatedCmd(); // command to execute;
        duplicatedCmd.putParam("name1", name1);
        duplicatedCmd.putParam("name2", name2);
        duplicatedCmd.putParam("name3", name3);
        duplicatedCmd.putParam("addressType", addressType);
        duplicatedCmd.putParam("companyId", companyId);
        duplicatedCmd.putParam("userId", userId);
        ResultDTO resultDTO = null;
        try {
            resultDTO = BusinessDelegate.i.execute(duplicatedCmd, request);
            List result = (List) resultDTO.get("duplicatedAddresses");
            if (result.size() > 0) {
                res = true;
            }
        } catch (AppLevelException e) {
            log.error("Unexpected error checking duplicated contacts", e);
        }

        return res;
    }

}
