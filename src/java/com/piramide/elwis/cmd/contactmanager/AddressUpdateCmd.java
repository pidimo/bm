package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.AccessRightDataLevelCmdUtil;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Execute address update business logic in service bean updating information for address.
 *
 * @author Fernando Montaño
 * @version $Id: AddressUpdateCmd.java 10479 2014-08-08 22:50:20Z miguel $
 */
public class AddressUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {


        log.debug("Executing update command");
        log.debug("Operation = " + getOp());

        log.debug("Address Id to update = " + paramDTO.get("addressId"));

        Byte changeCode = null;
        if (paramDTO.get("code") != null) {
            changeCode = new Byte(paramDTO.get("code").toString());
            log.debug("Code value from web tier = " + changeCode);
            paramDTO.remove("code"); // remove from param to not modified the address current code.
        }

        AddressDTO addressDTO = new AddressDTO(paramDTO);

        Address address = (Address) ExtendedCRUDDirector.i.update(addressDTO, resultDTO,
                false, true, false, "Fail");


        if (resultDTO.isFailure() && address != null) { //concurrency exception
            log.debug("A concurrency exception was hit");
            AddressCmd readCmd = new AddressCmd();
            readCmd.putParam(addressDTO);
            readCmd.executeInStateless(ctx);
            resultDTO.putAll(readCmd.getResultDTO());
            if (readCmd.getResultDTO().isFailure()) {  //address not found
                addressDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            }
        } else if (address != null) { // if found then update!

            /*** Telecoms management **/
            Map telecomMap = (Map) paramDTO.get("telecomMap");
            TelecomHome telecomHome = (TelecomHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_TELECOM);
            if (telecomMap != null) {
                /** updating telecoms **/
                Map updatedOrCreatedTelecomIds = new HashMap();
                for (Iterator iterator = telecomMap.values().iterator(); iterator.hasNext();) {
                    TelecomWrapperDTO wrapperDTO = (TelecomWrapperDTO) iterator.next();
                    for (Iterator iterator1 = wrapperDTO.getTelecoms().iterator(); iterator1.hasNext();) {
                        TelecomDTO telecomDTO = (TelecomDTO) iterator1.next();
                        if (telecomDTO.getTelecomId() != null && !"".equals(telecomDTO.getTelecomId())) {
                            try {
                                log.debug("Updating telecom data for telecomId =  " + telecomDTO.getTelecomId());
                                Telecom telecom = telecomHome.findByPrimaryKey(new Integer(telecomDTO.getTelecomId()));
                                telecom.setData(telecomDTO.getData());
                                telecom.setDescription(telecomDTO.getDescription());
                                telecom.setPredetermined(new Boolean(telecomDTO.getPredetermined()));
                                updatedOrCreatedTelecomIds.put(telecom.getTelecomId(), telecom.getTelecomId());
                            } catch (FinderException e) {
                                log.error("Error finding the telecom", e);
                            }
                        } else if (telecomDTO.getData() != null && !"".equals(telecomDTO.getData())) { //create the telecom
                            Telecom telecom = null;
                            try {
                                log.debug("Creating a new telecom");
                                telecom = telecomHome.create(address.getAddressId(), null, telecomDTO.getData(),
                                        telecomDTO.getDescription(), new Boolean(telecomDTO.getPredetermined()),
                                        new Integer(wrapperDTO.getTelecomTypeId()), address.getCompanyId());
                                log.debug("Before adding the telecom to colection of telecoms CMR");
                                address.getTelecoms().add(telecom);
                                updatedOrCreatedTelecomIds.put(telecom.getTelecomId(), telecom.getTelecomId());
                                // adding the created Telecom to telecomDTO reference
                                telecomDTO.setTelecomId(telecom.getTelecomId().toString()); //updating the creating Id

                            } catch (CreateException e) {
                                log.error("Error adding the telecom", e);
                            }
                        }
                    }

                }

                /*** removing telecoms which can be deleted **/

                TelecomDTO newTelecomDTO = null;
                TelecomTypeSelectCmd cmd = null;
                String telecomTypeId = null;
                Iterator iteratorTelecoms = address.getTelecoms().iterator();
                while (iteratorTelecoms.hasNext()) {
                    Telecom telecom = (Telecom) iteratorTelecoms.next();
                    if (telecom.getContactPersonId() == null) {
                        //telecom was removed, then remove it
                        if (!updatedOrCreatedTelecomIds.containsKey(telecom.getTelecomId())) {
                            telecomTypeId = telecom.getTelecomTypeId().toString();
                            newTelecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(),
                                    telecom.getDescription(), telecom.getPredetermined().booleanValue());
                            try {
                                telecom.remove();
                                iteratorTelecoms = address.getTelecoms().iterator();
                            } catch (RemoveException ex) {
                                log.warn("Telecom cannot be removed because is referenced, telecom it'll be restored to the user");
                                cmd = new TelecomTypeSelectCmd();
                                cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
                                cmd.putParam("companyId", address.getCompanyId());
                                cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, paramDTO.get("locale"));
                                cmd.putParam("telecomTypeId", telecomTypeId);
                                cmd.executeInStateless(ctx);
                                TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) cmd.getResultDTO().get(TelecomTypeSelectCmd.RESULT);
                                TelecomWrapperDTO.addToMapTelecomDTO(telecomMap, newTelecomDTO, telecomTypeDTO);

                                iteratorTelecoms = address.getTelecoms().iterator();
                            }
                        }
                    }
                }

            }


            String noteText = paramDTO.getAsString("note");
            String wayDescriptionText = paramDTO.getAsString("wayDescription");
            FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
            ContactFreeTextHome contactFreeTextHome = (ContactFreeTextHome)
                    EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTFREETEXT);
            if (noteText != null) {
                if (address.getFreeText() != null) {
                    address.getFreeText().setValue(noteText.getBytes());
                    resultDTO.put("note", new String(address.getFreeText().getValue()));
                } else {
                    FreeText freeText;
                    try {
                        freeText = freeTextHome.create(noteText.getBytes(),
                                new Integer((String) paramDTO.get("companyId")), new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                        address.setFreeText(freeText);
                        resultDTO.put("note", new String(address.getFreeText().getValue()));
                    } catch (CreateException e) {
                        log.error("Error creating note", e);
                    }
                }
            } else if (address.getFreeText() != null) {
                address.getFreeText().setValue(null);
            }

            if (wayDescriptionText != null) {
                if (address.getWayDescription() != null) {
                    address.getWayDescription().setValue(wayDescriptionText.getBytes());
                    resultDTO.put("wayDescription", new String(address.getWayDescription().getValue()));
                } else {
                    FreeText wayfreeText;
                    try {
                        wayfreeText = freeTextHome.create(wayDescriptionText.getBytes(), new Integer((String)
                                paramDTO.get("companyId")), new Integer(FreeTextTypes.FREETEXT_ADDRESS));
                        address.setWayDescription(wayfreeText);
                        resultDTO.put("wayDescription", new String(address.getWayDescription().getValue()));
                    } catch (CreateException e) {
                        log.error("Error creating waydescription", e);
                    }
                }
            } else if (address.getWayDescription() != null) {
                address.getWayDescription().setValue(null);
            }

            //updating the image
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
                resultDTO.put("imageRemovedFlag", "false");
            } else if ("true".equals(paramDTO.get("imageRemovedFlag"))) {
                if (address.getImageFreeText() != null) {
                    try {
                        ContactFreeText imageFreeText = address.getImageFreeText();
                        address.setImageFreeText(null);
                        imageFreeText.remove();
                        resultDTO.put("imageId", null);
                    } catch (RemoveException e) {
                        log.error("unexpected error removing the image freetext", e);
                    }
                }
                resultDTO.put("imageRemovedFlag", "false");
            }

            //current address code
            byte currentCode = address.getCode();
            log.debug("Current address code = " + currentCode);

            //Creating a Customer, if address code allows it
            if (null != changeCode && changeCode != currentCode) { // only update code if is change it
                if (CodeUtil.isSupplier(currentCode)) {
                    if (!CodeUtil.isSupplier(changeCode)) {
                        deleteSupplier(address.getAddressId(), ctx);
                    }
                } else {
                    if (CodeUtil.isSupplier(changeCode)) {
                        createSupplier(address.getAddressId(), address.getCompanyId(), ctx);
                    }
                }

                if (CodeUtil.isCustomer(currentCode)) {
                    if (!CodeUtil.isCustomer(changeCode)) {
                        deleteCustomer(address.getAddressId(), ctx);
                    }
                } else {
                    if (CodeUtil.isCustomer(changeCode)) {
                        createCustomer(address.getAddressId(), address.getCompanyId(), ctx);
                    }
                }
            }

            //If date do not have year
            if (address.getBirthday() != null) {
                if (address.getBirthday().toString().length() <= 5) {
                    resultDTO.put("dateWithoutYear", "true");
                }
            }

            resultDTO.put("isCustomer", new Boolean(CodeUtil.isCustomer(address.getCode().byteValue())));
            resultDTO.put("isSupplier", new Boolean(CodeUtil.isSupplier(address.getCode().byteValue())));
            resultDTO.put("isActive", address.getActive());

            if (!isUpdatedFromContactPerson()) {

                //update categoryfieldvalues for contact or organization
                String finderName = "findByAddressId";
                Object[] params = new Object[]{address.getAddressId(), address.getCompanyId()};
                List paramsAsList = Arrays.asList(params);

                List<Map> sourceValues = new ArrayList<Map>();
                Map productMap = new HashMap();
                productMap.put("identifier", "addressId");
                productMap.put("value", address.getAddressId());
                sourceValues.add(productMap);

                CategoryUtilCmd myCmd = new CategoryUtilCmd();
                myCmd.putParam("sourceValues", sourceValues);
                myCmd.putParam("companyId", address.getCompanyId());
                myCmd.putParam(paramDTO);
                myCmd.putParam("finderName", finderName);
                myCmd.putParam("params", paramsAsList);
                myCmd.setOp("updateValues");
                myCmd.executeInStateless(ctx);

                CategoryUtilCmd rmyCmd = new CategoryUtilCmd();
                rmyCmd.putParam("finderName", finderName);
                rmyCmd.putParam("params", paramsAsList);
                rmyCmd.setOp("readCAtegoryFieldValues");
                rmyCmd.executeInStateless(ctx);
                ResultDTO myResultDTO = rmyCmd.getResultDTO();
                resultDTO.putAll(myResultDTO);


                //update main address in additional address
                updateMainAddressInAdditionalAddress(address);
            }

            //user data level access right
            if (updateUserAddressAccessRight()) {
                userAddressAccessRight(address, ctx);
            }

        }
    }

    public boolean isStateful() {
        return false;
    }

    private boolean isUpdatedFromContactPerson() {
        return (paramDTO.get("updatedFromContactPerson") != null);
    }

    private void deleteCustomer(Integer customerId, SessionContext ctx) {
        CustomerUtilCmd customerUtilCmd = new CustomerUtilCmd();
        customerUtilCmd.setOp("deleteCustomer");
        customerUtilCmd.putParam("customerId", customerId);

        customerUtilCmd.executeInStateless(ctx);
    }

    private void createCustomer(Integer addressId, Integer companyId, SessionContext ctx) {
        CustomerUtilCmd customerUtilCmd = new CustomerUtilCmd();
        customerUtilCmd.setOp("createCustomer");
        customerUtilCmd.putParam("addressId", addressId);
        customerUtilCmd.putParam("companyId", companyId);

        customerUtilCmd.executeInStateless(ctx);
    }

    private void deleteSupplier(Integer supplierId, SessionContext ctx) {
        SupplierUtilCmd supplierUtilCmd = new SupplierUtilCmd();
        supplierUtilCmd.setOp("deleteSupplier");
        supplierUtilCmd.putParam("supplierId", supplierId);

        supplierUtilCmd.executeInStateless(ctx);
    }

    private void createSupplier(Integer addressId, Integer companyId, SessionContext ctx) {
        SupplierUtilCmd supplierUtilCmd = new SupplierUtilCmd();
        supplierUtilCmd.setOp("createSupplier");
        supplierUtilCmd.putParam("addressId", addressId);
        supplierUtilCmd.putParam("companyId", companyId);

        supplierUtilCmd.executeInStateless(ctx);
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

    private boolean updateUserAddressAccessRight() {
        return !isUpdatedFromContactPerson();
    }

    private void updateMainAddressInAdditionalAddress(Address address) {
        AdditionalAddress additionalAddress = null;
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);
        try {
            Collection additionalAddresses = additionalAddressHome.findByAdditionalAddressType(address.getAddressId(), ContactConstants.AdditionalAddressType.MAIN.getConstant());
            if (!additionalAddresses.isEmpty()) {
                additionalAddress = (AdditionalAddress) additionalAddresses.iterator().next();
            }
        } catch (FinderException e) {
            log.debug("error in found additional address..", e);
        }

        if (additionalAddress != null) {

            additionalAddress.setCityId(address.getCityId());
            additionalAddress.setCountryId(address.getCountryId());
            additionalAddress.setStreet(address.getStreet());
            additionalAddress.setHouseNumber(address.getHouseNumber());
            additionalAddress.setAdditionalAddressLine(address.getAdditionalAddressLine());
        }
    }

}
