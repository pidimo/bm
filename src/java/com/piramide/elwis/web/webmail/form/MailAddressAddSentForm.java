package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.contactmanager.AddressDuplicatedCmd;
import com.piramide.elwis.domain.contactmanager.ContactPerson;
import com.piramide.elwis.domain.contactmanager.ContactPersonHome;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.WebMailConstants;
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
 *
 * @author miky
 * @version $Id: MailAddressAddSentForm.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class MailAddressAddSentForm extends WebmailDefaultForm {

    public void setTempEmailIds(Object[] emailId) {
        log.debug("Set tempEmailIds ....... " + emailId);
        if (emailId != null) {
            setDto("tempEmailIds", Arrays.asList(emailId));
        } else {
            setDto("tempEmailIds", new ArrayList());
        }
    }

    public Object[] getTempEmailIds() {
        List emailIds = (List) getDto("tempEmailIds");
        return (emailIds != null ? emailIds.toArray() : new ArrayList().toArray());
    }


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("Executing validate MailAddressAddSentForm........." + getDtoMap());

        ActionErrors errors = super.validate(mapping, request);
        List listAddress = new ArrayList();
        List tempEmailIds = (List) getDto("tempEmailIds");
        boolean flagData = false;

        if (tempEmailIds != null) {
            for (Iterator iterator = tempEmailIds.iterator(); iterator.hasNext();) {

                String identify = iterator.next().toString();
                String nameAddress = getDto("email_" + identify).toString();

                String addressType = getDto("addressType_" + identify).toString();
                String companyId = getDto("companyId").toString();
                String userId = getDto("userMailId").toString();
                String isUpdate = null;

                if (getDto("isUpdate_" + identify) != null && getDto("addressId_" + identify) != null && !"".equals(getDto("addressId_" + identify).toString())) {

                    isUpdate = "true";
                    Integer addressId = Integer.valueOf(getDto("addressId_" + identify).toString());

                    //control of concurrence
                    if (getDto("contactPersonId_" + identify) != null && !"".equals(getDto("contactPersonId_" + identify).toString())) {

                        ContactPersonHome contactPersonHome = (ContactPersonHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
                        Integer contactPersonId = Integer.valueOf(getDto("contactPersonId_" + identify).toString());
                        ContactPersonPK contactPersonPK = new ContactPersonPK(addressId, contactPersonId);
                        try {
                            ContactPerson contactPerson = contactPersonHome.findByPrimaryKey(contactPersonPK);
                        } catch (FinderException fe) {
                            errors.add("Person", new ActionError("Webmail.Address.NotFound", getDto("EditName_" + identify).toString()));
                        }

                    } else if (!ForeignkeyValidator.i.exists(CatalogConstants.TABLE_ADDRESS, "addressid", addressId)) {
                        errors.add("Person", new ActionError("Webmail.Address.NotFound", getDto("EditName_" + identify).toString()));
                    }

                } else {

                    isUpdate = "false";
                    if (addressType.equals(ContactConstants.ADDRESSTYPE_PERSON)) {

                        String name1 = getDto("PerName1_" + identify).toString();
                        String name2 = getDto("PerName2_" + identify).toString();
                        String name3 = getDto("PerName3_" + identify).toString();

                        if (GenericValidator.isBlankOrNull(name1)) {
                            errors.add("Person", new ActionError("Webmail.Contact.Person.LastNameRequired", nameAddress));
                        } else if (addressIsDuplicate(request, name1, name2, name3, addressType, companyId, userId)) {
                            errors.add("duplicatedPerson", new ActionError("Webmail.Address.Person.AlreadyExists",
                                    name1 + "" + ((name2 != null && !"".equals(name2)) ? ", " + name2 : "")));
                        }


                    }
                    if (addressType.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {

                        String name1 = getDto("OrgName1_" + identify).toString();
                        String name2 = getDto("OrgName2_" + identify).toString();
                        String name3 = getDto("OrgName3_" + identify).toString();

                        if (GenericValidator.isBlankOrNull(name1)) {
                            errors.add("Organization", new ActionError("Webmail.Contact.Organization.NameRequired", nameAddress));
                        } else if (addressIsDuplicate(request, name1, name2, name3, addressType, companyId, userId)) {
                            errors.add("duplicatedOrganization", new ActionError("Webmail.Address.Organization.AlreadyExists",
                                    name1 + " " + ((name2 != null) ? name2 : "") +
                                            " " + ((name3 != null) ? name3 : "")));
                        }


                    }
                }

                if ((!addressType.equals(WebMailConstants.BLANK_KEY) && isUpdate.equals("false")) || isUpdate.equals("true")) {

                    flagData = true;
                    // validate telecomTypeId
                    if (GenericValidator.isBlankOrNull(getDto("telecomTypeId_" + identify).toString())) {
                        errors.add("TelecomType", new ActionError("error.Telecom.values.required", nameAddress));
                    } else {// control concurrence
                        Integer telecomTypeId = Integer.valueOf(getDto("telecomTypeId_" + identify).toString());
                        if (!ForeignkeyValidator.i.exists(CatalogConstants.TABLE_TELECOMTYPE, "telecomtypeid", telecomTypeId)) {
                            errors.add("TelecomType", new ActionError("Webmail.TelecomType.TelecomTypeNotFound", nameAddress));
                        }
                    }
                }

                Map addressMap = new HashMap(4);
                addressMap.put("address", nameAddress);
                addressMap.put("emailIdTemp", identify);
                addressMap.put("typeAddress", addressType);
                addressMap.put("isUpdate", isUpdate);
                listAddress.add(addressMap);

            }
            // check entry duplicated
            if (entryDuplicated().size() > 0) {
                List list = entryDuplicated();
                for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                    errors.add("entryDuplicated", new ActionError("Webmail.Address.DuplicatedEntry", iterator.next().toString()));
                }
            }

            //if have not data
            if (!flagData) {
                errors.add("haveNotData", new ActionError("Webmail.Address.error.haveNotData"));
            }

            request.setAttribute("listAddress", listAddress);
        }

        return errors;
    }

    /**
     * verif if already be registered this person or organization
     *
     * @param request
     * @param name1
     * @param name2
     * @param name3
     * @param addressType
     * @param companyId
     * @param userId
     * @return true or false
     */
    private boolean addressIsDuplicate(HttpServletRequest request,
                                       String name1, String name2, String name3,
                                       String addressType, String companyId, String userId) {

        boolean res = false;
        // validate address duplicated only if user is in create operation and do not have any errors.
        log.debug("Checking duplicated contacts before creation..................user:" + userId);
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

    /**
     * verif if in the form exist duplicated entries
     *
     * @return List of duplicated entry
     */
    private List entryDuplicated() {

        List listRes = new ArrayList();
        List addressVerifList = new ArrayList(); // list of emails verify
        List tempEmailIds = (List) getDto("tempEmailIds");

        for (Iterator iterator = tempEmailIds.iterator(); iterator.hasNext();) {

            String identify = iterator.next().toString();
            String nameAddress = getDto("email_" + identify).toString();

            String addressType = getDto("addressType_" + identify).toString();

            if (addressType.equals(ContactConstants.ADDRESSTYPE_PERSON)) {

                String name1 = getDto("PerName1_" + identify).toString();
                String name2 = getDto("PerName2_" + identify).toString();
                String name3 = getDto("PerName3_" + identify).toString();

                if (!GenericValidator.isBlankOrNull(name1)
                        || !GenericValidator.isBlankOrNull(name2)
                        || !GenericValidator.isBlankOrNull(name3))

                {
                    for (Iterator iterator1 = tempEmailIds.iterator(); iterator1.hasNext();) {
                        String identifyTemp = (String) iterator1.next();
                        String addressTemp = getDto("email_" + identifyTemp).toString();

                        String addressTypeTemp = getDto("addressType_" + identifyTemp).toString();
                        if (!nameAddress.equals(addressTemp) && !addressVerificated(addressVerifList, addressTemp)) {
                            if (addressTypeTemp.equals(ContactConstants.ADDRESSTYPE_PERSON)) {
                                String name1Temp = getDto("PerName1_" + identifyTemp).toString();
                                String name2Temp = getDto("PerName2_" + identifyTemp).toString();
                                String name3Temp = getDto("PerName3_" + identifyTemp).toString();

                                if (name1.equals(name1Temp) && name2.equals(name2Temp) && name3.equals(name3Temp)) {
                                    listRes.add(name1 + " " + name2 + " " + name3);
                                }

                            }
                        }
                    }
                }

            } else if (addressType.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {
                String name1 = getDto("OrgName1_" + identify).toString();
                String name2 = getDto("OrgName2_" + identify).toString();
                String name3 = getDto("OrgName3_" + identify).toString();

                if (!GenericValidator.isBlankOrNull(name1)
                        || !GenericValidator.isBlankOrNull(name2)
                        || !GenericValidator.isBlankOrNull(name3)) {

                    for (Iterator iterator2 = tempEmailIds.iterator(); iterator2.hasNext();) {
                        String identifyTemp = (String) iterator2.next();
                        String addressTemp = getDto("email_" + identifyTemp).toString();

                        String addressTypeTemp = getDto("addressType_" + identifyTemp).toString();
                        if (!nameAddress.equals(addressTemp) && !addressVerificated(addressVerifList, addressTemp)) {
                            if (addressTypeTemp.equals(ContactConstants.ADDRESSTYPE_ORGANIZATION)) {
                                String name1Temp = getDto("OrgName1_" + identifyTemp).toString();
                                String name2Temp = getDto("OrgName2_" + identifyTemp).toString();
                                String name3Temp = getDto("OrgName3_" + identifyTemp).toString();

                                if (name1.equals(name1Temp) && name2.equals(name2Temp) && name3.equals(name3Temp)) {
                                    listRes.add(name1 + " " + name2 + " " + name3);
                                }

                            }
                        }
                    }
                }
            }

            addressVerifList.add(nameAddress);

        }

        return listRes;
    }

    /**
     * help to verify the duplicate entry, view if this email address already verified
     *
     * @param list    list of verified email address
     * @param address the email address
     * @return true or false
     */
    private boolean addressVerificated(List list, String address) {
        if (list.size() > 0) {
            for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                String email = iterator.next().toString();
                if (email.equals(address)) {
                    return true;
                }
            }
        }
        return false;
    }

}
