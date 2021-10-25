package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.AccessRightDataLevelCmdUtil;
import com.piramide.elwis.domain.catalogmanager.City;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.catalogmanager.CityDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
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
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Execute address creation business logic.
 *
 * @author Fernando Montaño
 * @version $Id: AddressCreateCmd.java 10377 2013-09-27 15:59:03Z miguel $
 */

public class AddressCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {


        log.debug("Executing create command");
        log.debug("Operation = " + getOp());

        //create Address
        Address address = (Address) ExtendedCRUDDirector.i.create(new AddressDTO(paramDTO),
                resultDTO, false);
        log.debug("addressId after create = " + resultDTO.get("addressId"));

        if (address != null) {

            String noteText = paramDTO.getAsString("note");
            String wayDescriptionText = paramDTO.getAsString("wayDescription");
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
            ContactFreeTextHome contactFreeTextHome = (ContactFreeTextHome)
                    EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTFREETEXT);
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
            if (wayDescriptionText != null) {
                FreeText wayfreeText;
                try {
                    wayfreeText = freeTextHome.create(wayDescriptionText.getBytes(),
                            new Integer((String) paramDTO.get("companyId")), new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                    address.setWayDescription(wayfreeText);
                    resultDTO.put("wayDescription", new String(address.getWayDescription().getValue()));
                } catch (CreateException e) {
                    log.error("Error creating the waydescription", e);
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

            // creating telecoms
            Map telecoms = (Map) paramDTO.get("telecomMap");
            String companyId = (String) paramDTO.get("companyId");
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            if (telecoms != null && companyId != null && telecoms.size() > 0) {
                for (Iterator iterator = telecoms.values().iterator(); iterator.hasNext();) {
                    TelecomWrapperDTO telecomWrapperDTO = (TelecomWrapperDTO) iterator.next();
                    for (Iterator iterator1 = telecomWrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                        TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                        try {
                            Telecom telecom = telecomHome.create(address.getAddressId(), null, telecomDTO.getData(),
                                    telecomDTO.getDescription(), new Boolean(telecomDTO.getPredetermined()),
                                    new Integer(telecomWrapperDTO.getTelecomTypeId()),
                                    address.getCompanyId());
                            address.getTelecoms().add(telecom);
                            telecomDTO.setTelecomId(telecom.getTelecomId().toString());
                        } catch (CreateException e) {
                            log.error("Unexpected error creating telecoms", e);
                        }
                    }

                }
            }
            //to add a contact group
            resultDTO.put("telecomMap", telecoms);

            //creating a Customer if address code allows it
            byte createdCode = address.getCode();
            if (CodeUtil.isCustomer(createdCode)) {
                createCustomer(address, ctx);
            }

            // creating a supplier if address code allows it
            if (CodeUtil.isSupplier(createdCode)) {
                createSupplier(address.getAddressId(), address.getCompanyId(), ctx);
            }

            if (address.getCityId() != null) {
                City city = (City) EJBFactory.i.findEJB(new CityDTO(address.getCityId()));
                if (city != null) {
                    resultDTO.put("city", city.getCityName());
                    resultDTO.put("zip", city.getCityZip());
                    resultDTO.put("beforeZip", city.getCityZip());
                    resultDTO.put("cityId", city.getCityId());
                }
            } else {
                resultDTO.put("city", null);
                resultDTO.put("zip", null);
                resultDTO.put("cityId", null);
                resultDTO.put("beforeZip", null);
            }
            //If date do not have year
            if (address.getBirthday() != null) {
                if (address.getBirthday().toString().length() <= 5) {
                    resultDTO.put("dateWithoutYear", "true");
                }
            }


            resultDTO.put("isCustomer", new Boolean(CodeUtil.isCustomer(address.getCode().toString())));
            resultDTO.put("isSupplier", new Boolean(CodeUtil.isSupplier(address.getCode().toString())));
            resultDTO.put("isActive", address.getActive());

            resultDTO.put("addressName", address.getName());//putting the name formatted.

            // update recent list, with addressId created.
            RecentCmd recentCmd = new RecentCmd();
            recentCmd.putParam(resultDTO);
            recentCmd.putParam("userId", paramDTO.get("recordUserId"));
            recentCmd.setOp(CRUDDirector.OP_UPDATE);
            recentCmd.executeInStateless(ctx);

            //create categoryfieldvalues
            List<Map> sourceValues = new ArrayList<Map>();
            Map productMap = new HashMap();
            productMap.put("identifier", "addressId");
            productMap.put("value", address.getAddressId());
            sourceValues.add(productMap);
            CategoryUtilCmd myCmd = new CategoryUtilCmd();
            myCmd.putParam("sourceValues", sourceValues);
            myCmd.putParam(paramDTO);
            myCmd.putParam("companyId", address.getCompanyId());
            myCmd.setOp("createValues");
            myCmd.executeInStateless(ctx);

            String finderName = "findByAddressId";
            Object[] params = new Object[]{address.getAddressId(), address.getCompanyId()};
            List paramsAsList = Arrays.asList(params);
            CategoryUtilCmd rmyCmd = new CategoryUtilCmd();
            rmyCmd.putParam("finderName", finderName);
            rmyCmd.putParam("params", paramsAsList);
            rmyCmd.setOp("readCAtegoryFieldValues");
            rmyCmd.executeInStateless(ctx);
            ResultDTO myResultDTO = rmyCmd.getResultDTO();
            resultDTO.putAll(myResultDTO);

            //user data level access right
            userAddressAccessRight(address, ctx);
        }
    }

    private void createSupplier(Integer addressId, Integer companyId, SessionContext ctx) {
        SupplierUtilCmd supplierUtilCmd = new SupplierUtilCmd();
        supplierUtilCmd.setOp("createSupplier");
        supplierUtilCmd.putParam("addressId", addressId);
        supplierUtilCmd.putParam("companyId", companyId);

        supplierUtilCmd.executeInStateless(ctx);
    }

    protected void createCustomer(Address address, SessionContext ctx) {
        CustomerUtilCmd customerUtilCmd = new CustomerUtilCmd();
        customerUtilCmd.setOp("createCustomer");
        customerUtilCmd.putParam("addressId", address.getAddressId());
        customerUtilCmd.putParam("companyId", address.getCompanyId());

        customerUtilCmd.executeInStateless(ctx);
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

    public boolean isStateful() {
        return false;
    }
}
