package com.piramide.elwis.web.webmail.action;

import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.cmd.webmailmanager.AddressGroupCmd;
import com.piramide.elwis.dto.webmailmanager.AddressGroupDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.webmail.form.AddressGroupForm;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.struts.action.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: AddAddressGroupAction.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class AddAddressGroupAction extends AddressGroupListAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                 HttpServletResponse response) throws Exception {
        log.debug("Executing addAddressGroupAction.....");

        boolean successCreate = false;

        AddressGroupForm f = (AddressGroupForm) form;
        Object flagAdd = request.getParameter("Add");

        if (flagAdd != null && flagAdd.equals("true")) {
            //Read the addressGroups of the mailGroupAddr
            AddressGroupDTO dto1 = new AddressGroupDTO();
            dto1.put("op", "getAddressGroups");
            dto1.put("mailGroupAddrId", request.getParameter("dto(mailGroupAddrId)"));
            AddressGroupCmd addressGroupCmd1 = new AddressGroupCmd();
            addressGroupCmd1.putParam(dto1);
            ResultDTO resultDTO = new ResultDTO();
            resultDTO = BusinessDelegate.i.execute(addressGroupCmd1, request);
            ArrayList addressGroups = (ArrayList) resultDTO.get("addressGroups");

            //Convert the selectedMails to a Collection of DTO's
            ArrayList selectedMailsAL = toDto((Object) f.getSelectedEmails());
            if (selectedMailsAL.size() == 0) {
                ActionErrors errors = new ActionErrors();
                errors.add("AddressGroup", new ActionError("Webmail.addressGroup.selectValidContacts"));
                saveErrors(request, errors);
            } else {
                //Control of concurrence
                ArrayList addressDeleted = verifyConcurrence(selectedMailsAL, request);
                if (addressDeleted.size() > 0) {
                    ActionErrors errors = new ActionErrors();
                    Iterator i = addressDeleted.iterator();
                    while (i.hasNext()) {
                        DTO dto_i = new DTO();
                        dto_i = (DTO) i.next();
                        String addressName = getAddressName(dto_i.get("ID").toString(), request);

                        if (dto_i.get("ERRORTYPE").toString().equals("CONTACTPERSON")) {
                            errors.add("AddressGroup", new ActionError("Webmail.addressGroup.contactPersonNotFound", addressName));
                        } else if (dto_i.get("ERRORTYPE").toString().equals("ADDRESS")) {
                            errors.add("AddressGroup", new ActionError("Webmail.addressGroup.addressNotFound", addressName));
                        } else if (dto_i.get("ERRORTYPE").toString().equals("TELECOMID")) {
                            errors.add("AddressGroup", new ActionError("Webmail.addressGroup.telecomNotFound", addressName));
                        } else if (dto_i.get("ERRORTYPE").toString().equals("TELECOMALL")) {
                            errors.add("AddressGroup", new ActionError("Webmail.addressGroup.contactWithoutTelecoms", addressName));
                        }

                    }
                    saveErrors(request, errors);
                } else {
                    //Verify if the addresses are already in the group
                    Collection[] collections = filterCollections(selectedMailsAL, addressGroups);
                    if (collections[1].size() == 0) {
                        //Executes the command create
                        AddressGroupDTO dto = new AddressGroupDTO();
                        dto.put("selectedMails", collections[0]);
                        dto.put("op", "create");
                        dto.put("mailGroupAddrId", request.getParameter("dto(mailGroupAddrId)"));
                        dto.put("companyId",
                                Integer.valueOf(
                                        RequestUtils.getUser(request).getValue(Constants.COMPANYID).toString())
                        );

                        AddressGroupCmd addressGroupCmd = new AddressGroupCmd();
                        addressGroupCmd.putParam(dto);
                        BusinessDelegate.i.execute(addressGroupCmd, request);
                        successCreate = true;
                    } else {
                        ActionErrors errors = new ActionErrors();
                        Iterator i = collections[1].iterator();
                        while (i.hasNext()) {
                            HashMap hm_i = (HashMap) i.next();
                            String addressName = getAddressName(hm_i.get("addressId").toString(), request);
                            if (hm_i.get("ERRORTYPE").toString().equals("CONTACTPERSON")) {
                                errors.add("AddressGroup", new ActionError("Webmail.addressGroup.contactPersonAlreadyExists", addressName));
                            } else {
                                errors.add("AddressGroup", new ActionError("Webmail.addressGroup.contactAlreadyExists", addressName));
                            }
                        }
                        saveErrors(request, errors);
                    }
                }

            }
        }
        super.setAdd(true);
        ActionForward actionForward = mapping.findForward("Success");
        if (successCreate) {
            actionForward = mapping.findForward("ToAddressGroupList");
        } else {
            actionForward = super.execute(mapping, form, request, response);
        }
        return (actionForward);
    }

    /**
     * Converts the data received of the form to an array of Collections, addressGroupDTO's and the names of the addresses
     *
     * @param data
     * @return addressGroupDTO's
     */
    public ArrayList toDto(Object data) {
        ArrayList arrayList = new ArrayList();
        if (data != null) {
            data = data.toString().replaceAll("[,][,]", "");
            data = data.toString().replaceAll("[,]$", "");
            StringTokenizer st = new StringTokenizer(data.toString(), "%*%");
            while (st.hasMoreTokens()) {
                AddressGroupDTO dto = new AddressGroupDTO();
                String otroToken = st.nextToken();
                if (otroToken != null && otroToken.length() > 0) {
                    StringTokenizer st2 = new StringTokenizer(otroToken, "&");
                    dto.put("telecomId", st2.nextToken());
                    dto.put("addressId", st2.nextToken());
                    dto.put("contactPersonAddressId", st2.nextToken());
                    arrayList.add(dto);
                }
            }
        }
        return (arrayList);
    }

    /**
     * Compares a collection of the selected addresses that the user want to add to the group and the addresses
     * that already exists in the group, return an array of two collections, the addresses to save and the name of the
     * addresses already saved in the group.
     *
     * @param selectedMails
     * @param mailsInTheGroup
     * @return Is an array of two collections, the addresses to save and the name of the addresses already saved in the group.
     */
    public Collection[] filterCollections(ArrayList selectedMails, ArrayList mailsInTheGroup) {
        Collection[] res = new Collection[2];
        ArrayList toSave = new ArrayList();
        ArrayList duplicated = new ArrayList();
        boolean hasFound = false;
        int i = 0;
        while (i < selectedMails.size()) {
            int j = 0;
            AddressGroupDTO dto_i = (AddressGroupDTO) selectedMails.get(i);
            while (j < mailsInTheGroup.size() && !hasFound) {
                AddressGroupDTO dto_j = (AddressGroupDTO) mailsInTheGroup.get(j);
                if (dto_i.get("contactPersonAddressId") != null && !dto_i.get("contactPersonAddressId").toString().equals("null")) {
                    if (dto_j.get("contactPersonId") != null) {
                        if (dto_i.get("addressId").toString().equals(dto_j.get("contactPersonId").toString()) &&
                                (dto_i.get("contactPersonAddressId").toString().equals(dto_j.get("addressId").toString()))) {
                            HashMap hm = new HashMap();
                            hm.put("addressId", dto_i.get("addressId"));
                            hm.put("ERRORTYPE", "CONTACTPERSON");
                            duplicated.add(hm);
                            hasFound = true;
                            mailsInTheGroup.remove(j);
                        }
                    }
                } else {
                    if (dto_j.get("contactPersonId") == null) {
                        if (dto_i.get("addressId").toString().equals(dto_j.get("addressId").toString())) {
                            HashMap hm = new HashMap();
                            hm.put("addressId", dto_i.get("addressId"));
                            hm.put("ERRORTYPE", "ADDRESS");
                            duplicated.add(hm);
                            hasFound = true;
                            mailsInTheGroup.remove(j);
                        }
                    }
                }
                j = j + 1;
            }
            if (!hasFound) {
                toSave.add(dto_i);
            }
            hasFound = false;
            i = i + 1;
        }
        res[0] = toSave;
        res[1] = duplicated;
        return (res);
    }

    /**
     * Verify the concurrence of the selected addresses
     *
     * @param addresses
     * @param request
     * @return
     * @throws Exception
     */
    public ArrayList verifyConcurrence(ArrayList addresses, HttpServletRequest request) throws Exception {

        DTO dto = new DTO();
        dto.put("addresses", addresses);
        dto.put("op", "concurrenceValidation");
        AddressGroupCmd addressGroupCmd = new AddressGroupCmd();
        addressGroupCmd.putParam(dto);
        ResultDTO resultDTO = new ResultDTO();
        resultDTO = BusinessDelegate.i.execute(addressGroupCmd, request);
        return ((ArrayList) resultDTO.get("errors"));
    }

    /**
     * Return the addressName of one address(if the address dont exist's, return a blank String)
     *
     * @param addressId
     * @param request
     * @return
     * @throws Exception
     */
    private String getAddressName(String addressId, HttpServletRequest request) throws Exception {

        LightlyAddressCmd lightlyAddressCmd = new LightlyAddressCmd();
        DTO commandDto = new DTO();
        commandDto.put("addressId", addressId);
        lightlyAddressCmd.putParam(commandDto);
        ResultDTO result = new ResultDTO();
        result = BusinessDelegate.i.execute(lightlyAddressCmd, request);

        Object addressName = result.get("addressName") != null ? result.get("addressName") : "";
        return (addressName.toString());
    }
}
