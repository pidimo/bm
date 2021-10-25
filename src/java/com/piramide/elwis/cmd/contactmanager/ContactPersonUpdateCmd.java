package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.catalogmanager.TelecomTypeSelectCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.ContactPersonDTO;
import com.piramide.elwis.dto.contactmanager.TelecomDTO;
import com.piramide.elwis.dto.contactmanager.TelecomWrapperDTO;
import com.piramide.elwis.utils.ContactConstants;
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
 * Execute update ContactPerson business logic.
 *
 * @author Fernando Montaño
 * @version $Id: ContactPersonUpdateCmd.java 12717 2017-11-21 23:55:58Z miguel $
 */
public class ContactPersonUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update command");
        log.debug("Operation = " + getOp());

        log.debug("contactPersonId to read = " + paramDTO.get("contactPersonId"));
        log.debug("addressId container of before contactpersonid= " + paramDTO.get("addressId"));

        //for update private information
        AddressDTO privateDTO = new AddressDTO(paramDTO);
        ContactPersonDTO personDTO = new ContactPersonDTO(paramDTO);
        privateDTO.put("addressId", paramDTO.get("contactPersonId"));
        privateDTO.put("version", paramDTO.get("privateVersion"));
        privateDTO.remove("active");

        //update the contact person data
        ContactPersonPK pK = new ContactPersonPK(new Integer(paramDTO.get("addressId").toString()),
                new Integer(paramDTO.get("contactPersonId").toString()));

        paramDTO.remove("addressId"); //this invalid copy of primary key into Bean in update operation
        paramDTO.remove("contactPersonId"); //this invalid copy of primary key into Bean in update operation
        ContactPersonDTO contactPersonDTO = new ContactPersonDTO(paramDTO);
        contactPersonDTO.setPrimKey(pK);

        //process the organization department
        contactPersonDTO.put("departmentId", processOrganizationDepartment(pK.addressId, ctx));

        ContactPerson contactPerson = (ContactPerson) ExtendedCRUDDirector.i.update(contactPersonDTO, resultDTO,
                false, true, true, "Fail");

        if (resultDTO.isFailure() && contactPerson != null) {//concurrency error, reading additional information

            ContactPersonCmd readCmd = new ContactPersonCmd();
            contactPersonDTO.put("contactPersonId", contactPerson.getContactPersonId());
            contactPersonDTO.put("addressId", contactPerson.getAddressId());
            readCmd.putParam(contactPersonDTO);
            readCmd.executeInStateless(ctx);
            resultDTO.putAll(readCmd.getResultDTO());
            if (readCmd.getResultDTO().isFailure()) //fail to read
            {
                resultDTO.setForward("Fail");
            }


        } else if (contactPerson != null) {

            //update private address information of contact person
            log.debug("private address version = " + paramDTO.get("privateVersion"));

            AddressUpdateCmd privateUpdateCmd = new AddressUpdateCmd();
            privateUpdateCmd.putParam(privateDTO);
            privateUpdateCmd.putParam("updatedFromContactPerson", Boolean.valueOf(true));
            privateUpdateCmd.executeInStateless(ctx);
            if (privateUpdateCmd.getResultDTO().isFailure() &&
                    "Fail".equals(privateUpdateCmd.getResultDTO().getForward())) {//address was deleted by other user
                resultDTO.setResultAsFailure();
                personDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            } else if (privateUpdateCmd.getResultDTO().isFailure()) { //concurrency exception
                resultDTO.setResultAsFailure();
                ContactPersonCmd readCmd = new ContactPersonCmd();
                readCmd.putParam(personDTO);
                readCmd.executeInStateless(ctx);
                resultDTO.putAll(readCmd.getResultDTO());
                if (readCmd.getResultDTO().isFailure()) { //contact person not found
                    personDTO.addNotFoundMsgTo(resultDTO);
                    resultDTO.setForward("Fail");
                } else {
                    resultDTO.addResultMessage("Common.error.concurrency");
                }

                return;
            }

            /*** Telecoms management **/
            Map telecomMap = (Map) paramDTO.get("contactPersonTelecomMap");
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
                                telecom = telecomHome.create(contactPerson.getAddressId(),
                                        contactPerson.getContactPersonId(), telecomDTO.getData(),
                                        telecomDTO.getDescription(), new Boolean(telecomDTO.getPredetermined()),
                                        new Integer(wrapperDTO.getTelecomTypeId()), contactPerson.getCompanyId());
                                log.debug("Before adding the telecom to colection of telecoms CMR");
                                contactPerson.getTelecoms().add(telecom);
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
                Iterator iteratorTelecoms = contactPerson.getTelecoms().iterator();
                while (iteratorTelecoms.hasNext()) {
                    Telecom telecom = (Telecom) iteratorTelecoms.next();
                    //telecom was removed, then remove it
                    if (!updatedOrCreatedTelecomIds.containsKey(telecom.getTelecomId())) {
                        telecomTypeId = telecom.getTelecomTypeId().toString();
                        newTelecomDTO = new TelecomDTO(telecom.getTelecomId().toString(), telecom.getData(),
                                telecom.getDescription(), telecom.getPredetermined().booleanValue());
                        try {
                            telecom.remove();
                            iteratorTelecoms = contactPerson.getTelecoms().iterator();
                        } catch (RemoveException ex) {
                            log.warn("Telecom cannot be removed because is referenced, telecom it'll be restored to the user");
                            cmd = new TelecomTypeSelectCmd();
                            cmd.putParam(TelecomTypeSelectCmd.SELECT_TYPE, TelecomTypeSelectCmd.TYPE_SINGLE);
                            cmd.putParam("companyId", contactPerson.getCompanyId());
                            cmd.putParam(TelecomTypeSelectCmd.ISO_LANGUAGE, paramDTO.get("locale"));
                            cmd.putParam("telecomTypeId", telecomTypeId);
                            cmd.executeInStateless(ctx);
                            TelecomTypeDTO telecomTypeDTO = (TelecomTypeDTO) cmd.getResultDTO().get(TelecomTypeSelectCmd.RESULT);
                            TelecomWrapperDTO.addToMapTelecomDTO(telecomMap, newTelecomDTO, telecomTypeDTO);

                            iteratorTelecoms = contactPerson.getTelecoms().iterator();
                        }
                    }
                }

            } //end telecoms update

            //update categoryfieldvalues for contact or organization
            String finderName = "findByAddressIdAndContactPersonId";
            Object[] params = new Object[]{contactPerson.getAddressId(),
                    contactPerson.getContactPersonId(),
                    contactPerson.getCompanyId()};
            List paramsAsList = Arrays.asList(params);

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
            myCmd.putParam("companyId", contactPerson.getCompanyId());
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

            resultDTO.put("companyId", contactPerson.getCompanyId());
            resultDTO.put("addressId", contactPerson.getAddressId());
            resultDTO.put("contactPersonId", contactPerson.getContactPersonId());
            resultDTO.putAll(myResultDTO);
        }

    }

    public boolean isStateful() {
        return false;
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
}
