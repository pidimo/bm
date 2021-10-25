package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.AccessRightDataLevelCmdUtil;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Execute create busines logic for contact person.
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonCreateCmd.java 12717 2017-11-21 23:55:58Z miguel $
 */
public class ContactPersonCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing command of contact person");
        log.debug("Executing create command");
        log.debug("Operation = " + getOp());

        log.debug("addressId  = " + paramDTO.get("addressId"));

        String addressIdToAddContactPerson = paramDTO.getAsString("addressId"); //the address to add contact person
        Address address = null;
        ContactFreeTextHome contactFreeTextHome = (ContactFreeTextHome)
                EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTFREETEXT);
        if (paramDTO.get("newAddress") != null) { // if is creating a new address to use as contact person

            log.debug("Creating new Person Address...");
            paramDTO.remove("addressId"); // remove addressId, it invalid pass primary key to new address creation
            byte code = CodeUtil.default_;
            paramDTO.put("code", new Byte(code));
            paramDTO.put("personal", new Boolean(false)); // set the address to plublic
            paramDTO.put("isPublic", Boolean.TRUE);
            address = (Address) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, new AddressDTO(paramDTO), resultDTO);

            resultDTO.put("contactPersonAddressId", address.getAddressId());
            String noteText = paramDTO.getAsString("note");
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

            if (noteText != null) {
                FreeText freeText;
                try {
                    freeText = freeTextHome.create(noteText.getBytes(), new Integer((String) paramDTO.get("companyId")),
                            new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                    address.setFreeText(freeText);
                    resultDTO.put("note", new String(address.getFreeText().getValue()));
                } catch (CreateException e) {
                    log.error("Error creating the note", e);
                }
            }

            //creating the image
            ArrayByteWrapper image = (ArrayByteWrapper) paramDTO.get("image");
            if (image != null) {
                ContactFreeText imageFreeText;
                try {
                    imageFreeText = contactFreeTextHome.create(image.getFileData(),
                            address.getCompanyId(), new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                    address.setImageFreeText(imageFreeText);
                    resultDTO.put("imageId", address.getImageFreeText().getFreeTextId());
                } catch (CreateException e) {
                    log.error("unexpected error creating the image", e);
                }
            }

            //user data level access right, only when is create
            userAddressAccessRight(address, ctx);

        } else if (paramDTO.get("importAddress") != null) { //import the address to set as contactperson
            log.debug("Importing new Person Address...");
            log.debug("addressIdToImport  = " + paramDTO.get("addressIdToImport"));
            paramDTO.put("addressId", paramDTO.get("addressIdToImport"));
            address = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(paramDTO), resultDTO, false); //read it
            if (resultDTO.isFailure()) {
                resultDTO.setForward("Fail");
                return;
            }
            //creating  the image if the imported contact has no a image before or updating the existing one.
            ArrayByteWrapper image = (ArrayByteWrapper) paramDTO.get("image");
            if (image != null) {
                if (address.getImageFreeText() != null) {
                    address.getImageFreeText().setValue(image.getFileData());
                } else {
                    ContactFreeText imageFreeText;
                    try {
                        imageFreeText = contactFreeTextHome.create(image.getFileData(),
                                address.getCompanyId(), new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                        address.setImageFreeText(imageFreeText);
                        resultDTO.put("imageId", address.getImageFreeText().getFreeTextId());
                    } catch (CreateException e) {
                        log.error("unexpected error creating the image", e);
                    }
                }
                address.setVersion(new Integer(address.getVersion().intValue() + 1)); //ensure version control checking, because a change was performed.
            }

        }

        //after create or read person address to continue with contactperson creation.
        paramDTO.put("addressId", new Integer(addressIdToAddContactPerson));
        paramDTO.put("contactPersonId", address.getAddressId());

        //process organization departments
        paramDTO.put("departmentId", processOrganizationDepartment(new Integer(addressIdToAddContactPerson), ctx));

        // creating the contact person
        ContactPerson contactPerson = (ContactPerson) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE,
                new ContactPersonDTO(paramDTO), resultDTO);

        if (contactPerson != null) {

            // creating telecoms for contact person
            Map telecoms = (Map) paramDTO.get("contactPersonTelecomMap");
            String companyId = (String) paramDTO.get("companyId");
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            if (telecoms != null && companyId != null && telecoms.size() > 0) {
                for (Iterator iterator = telecoms.values().iterator(); iterator.hasNext();) {
                    TelecomWrapperDTO telecomWrapperDTO = (TelecomWrapperDTO) iterator.next();
                    for (Iterator iterator1 = telecomWrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                        TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                        try {
                            Telecom telecom = telecomHome.create(contactPerson.getAddressId(),
                                    contactPerson.getContactPersonId(), telecomDTO.getData(), telecomDTO.getDescription(),
                                    new Boolean(telecomDTO.getPredetermined()), new Integer(telecomWrapperDTO.getTelecomTypeId()), contactPerson.getCompanyId());
                            contactPerson.getTelecoms().add(telecom);
                            telecomDTO.setTelecomId(telecom.getTelecomId().toString());
                        } catch (CreateException e) {
                            log.error("Unexpected error creating telecoms", e);
                        }
                    }


                }
            }

            //this keys are using in DataImportServiceBean, to create contactPerson categories.
            resultDTO.put("addresIdToImport", contactPerson.getAddressId());
            resultDTO.put("contactPersonIdToImport", contactPerson.getContactPersonId());

            resultDTO.put("isActive", contactPerson.getActive());

            //create categoryfieldvalues
            List<Map> sourceValues = new ArrayList<Map>();
            Map m1 = new HashMap();
            m1.put("identifier", "addressId");
            m1.put("value", contactPerson.getAddressId());
            sourceValues.add(m1);
            Map m2 = new HashMap();
            m2.put("identifier", "contactPersonId");
            m2.put("value", contactPerson.getContactPersonId());
            sourceValues.add(m2);

            CategoryUtilCmd myCmd = new CategoryUtilCmd();
            myCmd.putParam("sourceValues", sourceValues);
            myCmd.putParam(paramDTO);
            myCmd.putParam("companyId", contactPerson.getCompanyId());
            myCmd.setOp("createValues");
            myCmd.executeInStateless(ctx);

            String finderName = "findByAddressIdAndContactPersonId";
            Object[] params = new Object[]{contactPerson.getAddressId(),
                    contactPerson.getContactPersonId(),
                    contactPerson.getCompanyId()};
            List paramsAsList = Arrays.asList(params);
            CategoryUtilCmd rmyCmd = new CategoryUtilCmd();
            rmyCmd.putParam("finderName", finderName);
            rmyCmd.putParam("params", paramsAsList);
            rmyCmd.setOp("readCAtegoryFieldValues");
            rmyCmd.executeInStateless(ctx);
            ResultDTO myResultDTO = rmyCmd.getResultDTO();
            resultDTO.putAll(myResultDTO);

        }

    }

    private void userAddressAccessRight(Address address, SessionContext ctx) {
        String userGroupIds = (String) paramDTO.get("accessUserGroupIds");
        List<Integer> userGroupIdList = AccessRightDataLevelCmdUtil.getUserGroupIdList(userGroupIds);

        UserAddressAccessCmd userAddressAccessCmd = new UserAddressAccessCmd();
        userAddressAccessCmd.setOp("assignUserGroups");
        userAddressAccessCmd.putParam("addressId", address.getAddressId());
        userAddressAccessCmd.putParam("companyId", address.getCompanyId());
        userAddressAccessCmd.putParam("accessUserGroupIdList", userGroupIdList);
        userAddressAccessCmd.executeInStateless(ctx);
        ResultDTO myResultDTO = userAddressAccessCmd.getResultDTO();

        List<Integer> resultUserGroupIdList = (List<Integer>) myResultDTO.get("accessUserGroupIdList");

        //update address access right status
        boolean isAccessCreatorUser = AccessRightDataLevelCmdUtil.existCreatorUserItem(userGroupIds);
        if (isAccessCreatorUser) {
            address.setPersonal(true);
            resultUserGroupIdList.add(ContactConstants.CREATORUSER_ACCESSRIGHT);
        } else {
            address.setPersonal(false);
        }

        if (resultUserGroupIdList.isEmpty()) {
            address.setIsPublic(true);
        } else {
            address.setIsPublic(false);
        }

        resultDTO.put("accessUserGroupIds", AccessRightDataLevelCmdUtil.composeIdListAsStringValue(resultUserGroupIdList));
    }

    /**
     * Process the department of organization, if no exist create this
     * @param organizationId organization id
     * @param ctx session context
     * @return Integer
     */
    private Integer processOrganizationDepartment(Integer organizationId, SessionContext ctx) {
        Integer departmentId = null;

        if (paramDTO.get("departmentId") != null && !"".equals(paramDTO.get("departmentId").toString())) {
            departmentId = Integer.valueOf(paramDTO.get("departmentId").toString());

        } else if (paramDTO.get("departmentName") != null && !"".equals(paramDTO.get("departmentName").toString())) {

            String departmentName = paramDTO.get("departmentName").toString();
            Integer companyId = Integer.valueOf(paramDTO.get("companyId").toString());

            //search if this already registered
            DepartmentHome departmentHome = (DepartmentHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_DEPARTMENT);
            try {
                Collection collection = departmentHome.findByCompanyId(companyId, organizationId);
                for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
                    Department department = (Department) iterator.next();

                    if (departmentName.toLowerCase().trim().equals(department.getName().toLowerCase())) {
                        departmentId = department.getDepartmentId();
                        break;
                    }
                }
            } catch (FinderException ignore) {
            }

            if (departmentId == null) {
                //create department
                DepartmentCmd departmentCmd = new DepartmentCmd();
                departmentCmd.setOp("create");
                departmentCmd.putParam("companyId", paramDTO.get("companyId"));
                departmentCmd.putParam("name", paramDTO.get("departmentName"));
                departmentCmd.putParam("organizationId", organizationId);

                departmentCmd.executeInStateless(ctx);
                ResultDTO departmentResultDTO = departmentCmd.getResultDTO();

                departmentId = (Integer) departmentResultDTO.get("departmentId");
            }
        }

        return departmentId;
    }


    public boolean isStateful() {
        return false;
    }
}
