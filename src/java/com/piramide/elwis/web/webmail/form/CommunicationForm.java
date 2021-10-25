package com.piramide.elwis.web.webmail.form;

import com.piramide.elwis.cmd.webmailmanager.CommunicationManagerCmd;
import com.piramide.elwis.cmd.webmailmanager.EmailTelecomCheckerCmd;
import com.piramide.elwis.cmd.webmailmanager.MailCommunicationCmd;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author : ivan
 *         Date: Jun 7, 2006
 *         Time: 4:33:21 PM
 */
public class CommunicationForm extends WebmailDefaultForm {
    List<Map> defaultElements;
    List<Map> addedElements;
    User user;

    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        log.debug("validate(ActionMapping, HttpServletRequest)");
        defaultElements = createStructureForDefaultContacts();
        addedElements = createStructureForAddedContacts();
        user = (User) request.getSession().getAttribute(Constants.USER_KEY);

        ActionErrors errors = new ActionErrors();

        ActionError alreadyEmailCommunicarionError = checkEmailCommunicationAlreadyExists(request);
        if (null != alreadyEmailCommunicarionError) {
            errors.add("alreadyExistsEmailCommunication", alreadyEmailCommunicarionError);
        }

        ActionError duplicateError = checkDuplicatedElements();
        if (null == alreadyEmailCommunicarionError && null != duplicateError) {
            errors.add("duplicatedError", duplicateError);
        }

        if (errors.isEmpty()) {
            checkAddressOrContactPerson(errors);
        }

        if (errors.isEmpty()) {
            checkTelecomType(request, errors);
        }


        if (!errors.isEmpty()) {
            super.validate(mapping, request);


            MailCommunicationCmd cmd = new MailCommunicationCmd();
            cmd.setOp("");
            cmd.putParam("mailId", getDto("mailId"));
            cmd.putParam("email", getDto("email"));
            cmd.putParam("companyId", user.getValue("companyId"));
            cmd.putParam("inOut", getDto("inOut"));
            cmd.putParam("read", getDto("read"));
            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
                this.getDtoMap().putAll(resultDTO);
                Map actualDto = (Map) request.getAttribute("dto");
                if (null == actualDto) {
                    actualDto = new HashMap();
                }
                actualDto.putAll(resultDTO);
                actualDto.putAll(this.getDtoMap());

                request.setAttribute("dto", actualDto);
            } catch (AppLevelException e) {
                e.printStackTrace();
            }
        } else {
            Map newDtoMap = clearDtoMap();
            getDtoMap().clear();
            getDtoMap().putAll(newDtoMap);
            setDto("otherContacts", addedElements);
        }

        return errors;
    }

    /**
     * This method verifies that the communications are not duplicated by email
     *
     * @param request to execute EJBComand Object
     * @return Object org.apache.struts.action.ActionError if exists communication created for
     *         address and contactperson
     *         returns null in another case
     */
    private ActionError checkEmailCommunicationAlreadyExists(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
        CommunicationManagerCmd cmd = new CommunicationManagerCmd();
        cmd.setOp("readActualEmailCommunications");
        cmd.putParam("mailId", getDto("mailId"));
        cmd.putParam("companyId", user.getValue("companyId"));
        ResultDTO myResultDTO = new ResultDTO();
        try {
            myResultDTO = BusinessDelegate.i.execute(cmd, request);
        } catch (AppLevelException e) {
            log.debug("Cannot execute CommunicationManagerCmd ", e);
        }

        List<Map> actualCommunications = (List<Map>) myResultDTO.get("actualEmailCommunications");

        ActionError error = null;

        Map element = null;
        String nameKey = "";
        for (Map map : addedElements) {
            String name = "";
            Integer addressId = (Integer) map.get("addressId");
            Integer contactPersonId = (Integer) map.get("contactPersonId");

            if (null != contactPersonId) {
                String key = contactPersonId + "-" + addressId;
                name = (String) map.get("addressName") + " - " + (String) map.get("contactPersonName");
                element = getElemetOfList(actualCommunications, key);
            } else {
                String key = addressId + "-" + contactPersonId;
                name = (String) map.get("addressName");
                element = getElemetOfList(actualCommunications, key);
            }

            if (null != element) {
                nameKey = name;
                break;
            }
        }

        if (null == element && !actualCommunications.isEmpty()) {
            Map map = getDuplicateElement(defaultElements);
            if (null != map && null != contain(actualCommunications, map)) {
                Integer contactPersonId = (Integer) map.get("contactPersonId");
                if (null != contactPersonId) {
                    nameKey = map.get("addressName") + " - " + map.get("contactPersonName");
                } else {
                    nameKey = (String) map.get("addressName");
                }
                map.put("email", getDto("email"));
                element = map;
            }
        }
        if (null != element) {
            String key = (String) element.get("email");
            error = new ActionError("Webmail.mailContact.duplicateCommunication", nameKey, key);
        }

        return error;

    }

    /**
     * This method verifies if all the selected contacts
     * (they are address or contact person ) were not eliminated by another user
     *
     * @param errors Object org.apache.struts.action.ActionErrors where the errors you would added
     */
    private void checkAddressOrContactPerson(ActionErrors errors) {
        ActionError error = null;
        for (Map map : addedElements) {
            Integer addressId = (Integer) map.get("addressId");
            Integer contactPersonId = (Integer) map.get("contactPersonId");

            if (null != contactPersonId) {
                Map values = new LinkedHashMap();
                values.put("contactpersonid", addressId);
                values.put("addressid", contactPersonId);

                if (!ForeignkeyValidator.i.validate(ContactConstants.TABLE_CONTACTPERSON, values,
                        new ActionErrors(),
                        new ActionError("", "")).isEmpty()) {

                    String msg = map.get("addressName").toString() + " - " + map.get("contactPersonName").toString();
                    error = new ActionError("Webmail.address.AddressNotFound", msg);
                    errors.add("addressOrContacPersonError", error);
                }
            } else {
                if (!ForeignkeyValidator.i.exists(ContactConstants.TABLE_ADDRESS, "addressid", addressId)) {
                    error = new ActionError("Webmail.address.AddressNotFound", map.get("addressName"));
                    errors.add("addressOrContacPersonError", error);
                }
            }
        }
    }

    /**
     * This method verifies if the telecomtype selected were not eliminated by another user,
     * and it verifies that telecomtype is selected,
     * if the option to add telecom to the contact this true;
     * besides to verify that if the electronic mail this adding
     * itself to the contact this twice does not exist with he himself telecomtype.
     *
     * @param request HttpServletRequest to execute EJB command
     * @param errors  Object org.apache.struts.action.ActionErrors where the errors you would added
     */
    private void checkTelecomType(HttpServletRequest request, ActionErrors errors) {
        for (Map map : addedElements) {
            ActionError error = null;
            Integer telecomTypeId = (Integer) map.get("telecomTypeId");
            if (null != telecomTypeId) {
                if (!ForeignkeyValidator.i.exists(CatalogConstants.TABLE_TELECOMTYPE, "telecomtypeid", telecomTypeId)) {
                    error = new ActionError("Webmail.TelecomType.TelecomTypeNotFound", map.get("telecomTypeName"));
                    errors.add("telecomTypeError", error);
                } else {
                    EmailTelecomCheckerCmd cmd = new EmailTelecomCheckerCmd();
                    cmd.putParam("email", getDto("email"));
                    cmd.putParam("companyId", user.getValue("companyId"));
                    cmd.putParam("addressId", map.get("addressId"));
                    cmd.putParam("contactPersonId", map.get("contactPersonId"));
                    cmd.putParam("telecomTypeId", telecomTypeId);
                    try {
                        BusinessDelegate.i.execute(cmd, request);
                    } catch (AppLevelException e) {
                        log.debug("Cannot execute TelecomTypeCmd  ", e);
                    }
                    if (cmd.hasDuplicated()) {
                        String name = (String) map.get("addressName");
                        Integer contactPersonId = (Integer) map.get("contactPersonId");
                        if (null != contactPersonId) {
                            name = map.get("addressName") + " - " +
                                    map.get("contactPersonName");
                        }
                        error = new ActionError("Webmail.telecomType.telecom.duplicated", getDto("email"), name);
                        errors.add("telecomTypeError", error);
                    }
                }
            }
            if (null == telecomTypeId && ((Boolean) map.get("addEmail"))) {
                String msg = (String) map.get("addressName");
                error = new ActionError("Webmail.telecomType.Requiered", msg);
                errors.add("telecomTypeError", error);
            }
        }
    }

    /**
     * This method verifies if the user has selected contacts duplicated in the user interface
     *
     * @return Object org.apache.struts.action.ActionError if telecomtype selected can be deleted by another user, or
     *         not selected an telecomtype but the option add telecom to  the contact is true,
     *         returns null in another case
     */
    private ActionError checkDuplicatedElements() {
        ActionError error = null;
        Map element = getDuplicateElement(addedElements);

        if (null == element) {
            element = getDuplicateElement(defaultElements);
        }

        if (null == element) {
            element = getIntersectElement(addedElements, defaultElements);
        }

        if (null != element) {
            if (null != element.get("contactPersonId")) {
                String msg = element.get("addressName").toString() + " - " + element.get("contactPersonName").toString();
                error = new ActionError("Webmail.mailContact.duplicateSelectedItems", msg);

            } else {
                error = new ActionError("Webmail.mailContact.duplicateSelectedItems", element.get("addressName"));
            }
        }

        return error;
    }

    /**
     * This method returns the first duplicated element that can exist in a list of objects java.util.Map.
     *
     * @param l list that contain map objects
     * @return the first duplicated java.util.Map  Object
     *         null if there is no maps duplicated
     */
    private Map getDuplicateElement(List<Map> l) {
        for (Map map : l) {
            List<Map> auxList = new ArrayList<Map>(l);
            auxList.remove(map);
            if (null != contain(auxList, map)) {
                return map;
            }
        }
        return null;
    }

    /**
     * This method verifies if the l1 and l2 have equal elements
     *
     * @param l1
     * @param l2
     * @return returns the element that is common in the list 1 and list 2,
     *         null in another case
     */
    private Map getIntersectElement(List<Map> l1, List<Map> l2) {
        for (Map map1 : l1) {
            if (null != contain(l2, map1)) {
                return map1;
            }
        }
        return null;
    }

    /**
     * This method verifies if the maps list contains the Map element
     *
     * @param maps
     * @param element
     * @return true if list of maps contains map element
     */
    private Map contain(List<Map> maps, Map element) {
        log.debug("contain('" + maps + "', '" + element + "')");
        for (Map map : maps) {
            if (isEqualMap(map, element)) {
                return map;
            }
        }
        return null;
    }


    /**
     * This method compares two objects of type java.util.MAP,
     * in which are the pairs:  addressI, contactPersonId.
     * <p/>
     * If both objects have values in the pairs such addressId and contactPersonId then it returns true
     * false in another case
     * <p/>
     * definition
     * m1 == m2 = true <=>
     * <p/>
     * m1.get(addressId) == m2.get(addressId) And m1.get(contactPersonId) == m2.get(contactPersonId) = true
     *
     * @param m1 java.util.Map object
     * @param m2 java.util.Map object
     * @return true or false if addressId and contactPersonId are equal in both java.util.Map objects
     */
    private boolean isEqualMap(Map m1, Map m2) {
        log.debug("isEqualMap('" + m1 + "', '" + m2 + "')");
        Integer addressId1 = null;
        try {
            addressId1 = new Integer(String.valueOf(m1.get("addressId")));
        } catch (NumberFormatException nfe) {
        }
        Integer contactPersonId1 = null;
        try {
            contactPersonId1 = new Integer(String.valueOf(m1.get("contactPersonId")));
        } catch (NumberFormatException nfe) {
        }

        Integer addressId2 = null;
        try {
            addressId2 = new Integer(String.valueOf(m2.get("addressId")));
        } catch (NumberFormatException nfe) {
        }

        Integer contactPersonId2 = null;
        try {
            contactPersonId2 = new Integer(String.valueOf(m2.get("contactPersonId")));
        } catch (NumberFormatException nfe) {
        }

        boolean contactPersonEvaluation = false;
        try {
            contactPersonEvaluation = contactPersonId1.equals(contactPersonId2);
        } catch (NullPointerException npe) {
            try {
                contactPersonEvaluation = contactPersonId2.equals(contactPersonId1);
            } catch (NullPointerException npea) {
                contactPersonEvaluation = contactPersonId1 == contactPersonId2;
            }
        }

        boolean addressEvaluation = false;
        try {
            addressEvaluation = addressId1.equals(addressId2);
        } catch (NullPointerException npe) {
            try {
                addressEvaluation = addressId2.equals(addressId1);
            } catch (NullPointerException npea) {
                addressEvaluation = addressId1 == addressId2;
            }
        }

        return addressEvaluation && contactPersonEvaluation;
    }


    private Map getElemetOfList(List<Map> list, String key) {
        for (Map map : list) {
            String addressId = "null";
            if (null != map.get("addressId")) {
                addressId = map.get("addressId").toString();
            }

            String contactPersonId = "null";
            if (null != map.get("contactPersonId")) {
                contactPersonId = map.get("contactPersonId").toString();
            }

            String myKey = addressId + "-" + contactPersonId;
            if (key.equals(myKey)) {
                return map;
            }
        }
        return null;
    }

    private List<Map> createStructureForAddedContacts() {
        log.debug("createStructureForContacts()");
        List<Map> structure = new ArrayList<Map>();

        int visibleIndex = 0;
        if (null != getDto("visibleIndex")) {
            try {
                visibleIndex = new Integer(getDto("visibleIndex").toString());
            } catch (NumberFormatException nfe) {
                log.debug("Invalid visibleIndex");
            }
        }

        for (int i = 1; i <= visibleIndex; i++) {
            Map map = new HashMap();
            String checkbox = (String) getDto("checkBoxContact_" + i);
            String addEmail = (String) getDto("addEmail_" + i);
            String addressName = (String) getDto("contact_" + i);
            String contactPersonName = (String) getDto("contactPerson_" + i);
            String telecomTypeName = (String) getDto("telecomTypeName_" + i);

            Integer telecomTypeId = null;
            try {
                telecomTypeId = new Integer((String) getDto("telecomTypeId_" + i));
            } catch (NumberFormatException nfe) {
            } catch (NullPointerException npe) {
            }

            Integer addressId = null;
            try {
                addressId = new Integer((String) getDto("addressId_" + i));
            } catch (NumberFormatException nfe) {
            } catch (NullPointerException npe) {
            }

            Integer contactPersonId = null;
            try {
                contactPersonId = new Integer((String) getDto("contactPersonId_" + i));
            } catch (NumberFormatException nfe) {
            } catch (NullPointerException npe) {
            }

            if (null != checkbox && !"".equals(checkbox)) {
                map.put("addressId", addressId);
                map.put("addressName", addressName);
                map.put("contactPersonId", contactPersonId);
                map.put("contactPersonName", contactPersonName);
                if ("true".equals(addEmail)) {
                    map.put("addEmail", true);
                    map.put("telecomTypeId", telecomTypeId);
                    map.put("telecomTypeName", telecomTypeName);
                } else {
                    map.put("addEmail", false);
                    map.put("telecomTypeId", null);
                    map.put("telecomTypeName", null);
                }
                structure.add(map);
            }
        }

        return structure;
    }

    private List<Map> createStructureForDefaultContacts() {
        log.debug("createStructureForDefaultContacts()");
        List<Map> result = new ArrayList<Map>();
        int size = 0;
        if (null != getDto("size")) {
            try {
                size = new Integer(getDto("size").toString());
            } catch (NumberFormatException nfe) {
            }
        }

        for (int i = 1; i <= size; i++) {
            String value = (String) getDto("checkBox_" + i);
            String name = (String) getDto("name_def_" + i);
            String contactPersonName = (String) getDto("contactPerson_def_" + i);
            if (null != value) {
                Map map = parse(value);
                map.put("addressName", name);
                map.put("contactPersonName", contactPersonName);
                result.add(map);
            }
        }

        return result;
    }

    private Map clearDtoMap() {
        int visibleIndex = 0;
        if (null != getDto("visibleIndex")) {
            try {
                visibleIndex = new Integer(getDto("visibleIndex").toString());
            } catch (NumberFormatException nfe) {
                log.debug("Invalid visibleIndex");
            }
        }
        List<String> l = new ArrayList<String>();
        for (int i = visibleIndex + 1; i <= 50; i++) {
            l.add("checkBoxContact_" + i);
            l.add("addEmail_" + i);
            l.add("telecomTypeId_" + i);
            l.add("addressId_" + i);
            l.add("contact_" + i);
            l.add("contactPersonId_" + i);
            l.add("contactPerson_" + i);
        }

        Map newDtoMap = new HashMap();
        for (Iterator it = getDtoMap().entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            String key = (String) entry.getKey();
            if (!l.contains(key)) {
                newDtoMap.put(key, getDtoMap().get(key));
            }
        }
        return newDtoMap;
    }

    private Map parse(String cad) {
        StringTokenizer tokenizer = new StringTokenizer(cad, "_");
        List elements = new ArrayList();
        while (tokenizer.hasMoreElements()) {
            elements.add(tokenizer.nextElement());
        }

        Map map = new HashMap();
        for (int i = 0; i < elements.size(); i++) {
            String element = (String) elements.get(i);
            StringTokenizer newTokenizer = new StringTokenizer(element, "=");

            String key = newTokenizer.nextElement().toString();
            Object obj = null;
            try {
                obj = newTokenizer.nextElement();
            } catch (NoSuchElementException e) {

            }
            Integer value = null;

            boolean isInteger = true;
            if (null != obj && !"".equals(obj.toString())) {
                try {
                    value = Integer.valueOf(obj.toString());
                } catch (NumberFormatException nfe) {
                    isInteger = false;
                }
            }
            if (isInteger) {
                map.put(key, value);
            } else {
                map.put(key, obj);
            }
        }

        return map;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }
}
