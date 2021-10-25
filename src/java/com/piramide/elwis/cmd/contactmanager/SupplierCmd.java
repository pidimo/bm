package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Supplier;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.SupplierDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.HashMapCleaner;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Manages supplier bussines logic.
 *
 * @author Yumi
 * @version $Id: SupplierCmd.java 9994 2010-09-24 21:32:23Z ivan $
 */

public class SupplierCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {


        HashMapCleaner.clean(paramDTO); // clean
        SupplierDTO supplierDTO = new SupplierDTO(paramDTO);

        if (!paramDTO.hasOp()) { // read
            log.debug("read supplier information for address");

            //recovering address to set the taxNumber
            AddressDTO addressDTO = new AddressDTO(paramDTO);
            try {
                Address address = (Address) EJBFactory.i.findEJB(addressDTO);
                resultDTO.put("taxNumber", address.getTaxNumber());

            } catch (EJBFactoryException e) { // address not found
                addressDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }

            supplierDTO.put("supplierId", paramDTO.get("addressId"));
            CRUDDirector.i.read(supplierDTO, resultDTO); //read supplier detail
            if (resultDTO.isFailure()) { //if cannot read the supplier, was deleted by other user
                if (ContactConstants.ADDRESSTYPE_PERSON.equals(addressDTO.getAsString("addressType"))) {
                    resultDTO.setForward("PersonDetail");
                } else {
                    resultDTO.setForward("OrganizationDetail");
                }
                return;
            }
        }
        // updating supplier info
        if (CRUDDirector.OP_UPDATE.equals(paramDTO.getOp())) {

            if (paramDTO.get("delete") == null) { //save changes
                //version control
                VersionControlChecker.i.check(supplierDTO, resultDTO, paramDTO);
                if (resultDTO.get("EntityBeanNotFound") != null) { // bean not found in version control
                    resultDTO.put("addressId", supplierDTO.get("supplierId"));
                    supplierDTO.addNotFoundMsgTo(resultDTO);
                    if (ContactConstants.ADDRESSTYPE_PERSON.equals(supplierDTO.getAsString("addressType"))) {
                        resultDTO.setForward("PersonDetail");
                    } else {
                        resultDTO.setForward("OrganizationDetail");
                    }
                    return;
                }
                if (resultDTO.isFailure()) { //version control detect changes of other user
                    resultDTO.setForward("Success"); //invalidating failure
                    CRUDDirector.i.read(supplierDTO, resultDTO);
                    //reading the address to get the taxnumber
                    AddressDTO addressDTO = new AddressDTO(paramDTO);
                    try {
                        Address address = (Address) EJBFactory.i.findEJB(addressDTO);
                        resultDTO.put("taxNumber", address.getTaxNumber());
                    } catch (EJBFactoryException e) { // address not found
                        addressDTO.addNotFoundMsgTo(resultDTO);
                        resultDTO.setForward("Fail");
                        return;
                    }
                    return;
                }
                supplierDTO.put("version", paramDTO.get("version"));

                Supplier supplier = (Supplier) CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, supplierDTO, resultDTO);

                if (resultDTO.isFailure()) { // if supplier bean was removed
                    if (ContactConstants.ADDRESSTYPE_PERSON.equals(supplierDTO.getAsString("addressType"))) {
                        resultDTO.setForward("PersonDetail");
                    } else {
                        resultDTO.setForward("OrganizationDetail");
                    }
                    return;
                }

                if (supplier != null) { //update changed values Id's
                    String customerNumber = "";
                    if (null != paramDTO.get("customerNumber") &&
                            !"".equals(paramDTO.get("customerNumber").toString().trim())) {
                        customerNumber = (String) paramDTO.get("customerNumber");
                    }
                    supplier.setCustomerNumber(customerNumber);

                    if (supplier.getBranchId() != null && paramDTO.get("branchId") == null) {
                        supplier.setBranchId(null);
                    }
                    if (supplier.getSupplierTypeId() != null && paramDTO.get("supplierTypeId") == null) {
                        supplier.setSupplierTypeId(null);
                    }
                    if (supplier.getPriorityId() != null && paramDTO.get("priorityId") == null) {
                        supplier.setPriorityId(null);
                    }
                    if (supplier.getCategoryId() != null && paramDTO.get("categoryId") == null) {
                        supplier.setCategoryId(null);
                    }

                    //recovering address to set the taxNumber
                    AddressDTO addressDTO = new AddressDTO(paramDTO);
                    addressDTO.setPrimKey(paramDTO.get("supplierId"));
                    try {
                        Address address = (Address) EJBFactory.i.findEJB(addressDTO);
                        if (paramDTO.get("taxNumber") != null) {
                            address.setTaxNumber(paramDTO.get("taxNumber").toString());
                            resultDTO.put("taxNumber", address.getTaxNumber());
                        } else {
                            address.setTaxNumber(null);
                        }

                    } catch (EJBFactoryException e) { // address not found
                        addressDTO.addNotFoundMsgTo(resultDTO);
                        resultDTO.put("addressId", supplierDTO.get("supplierId"));
                        resultDTO.setForward("Fail");
                        return;
                    }

                    CRUDDirector.i.read(supplierDTO, resultDTO); // read updated values
                }
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
