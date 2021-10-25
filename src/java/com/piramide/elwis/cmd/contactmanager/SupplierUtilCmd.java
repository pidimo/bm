package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Supplier;
import com.piramide.elwis.domain.contactmanager.SupplierHome;
import com.piramide.elwis.dto.contactmanager.SupplierDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class SupplierUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void executeInStateless(SessionContext ctx) {
        if ("deleteSupplier".equals(getOp())) {
            Integer supplierId = EJBCommandUtil.i.getValueAsInteger(this, "supplierId");
            deleteSupplier(supplierId);
        }
        if ("createSupplier".equals(getOp())) {
            Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            createSupplier(addressId, companyId);
        }
    }

    private void createSupplier(Integer addressId, Integer companyId) {
        //use addressId  as supplierId because it is a foreign primary key with supplier table
        if (null == getSupplier(addressId)) {
            log.debug("Creating a new Supplier Object for addressId: " + addressId + ", companyId: " + companyId);

            SupplierDTO dto = new SupplierDTO();
            dto.setPrimKey(addressId);
            dto.put("companyId", companyId);

            ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            addSupplierCode(addressId);
        }
    }

    private void deleteSupplier(Integer supplierId) {
        log.debug("Deleting Supplier Object supplierId: " + supplierId);

        Supplier supplier = getSupplier(supplierId);
        if (null != supplier) {
            if (!checkRelationShips(supplier)) {
                try {
                    supplier.remove();

                    //use supplierId  as addressId because it is a foreign primary key with address table
                    removeSupplierCode(supplierId);
                } catch (RemoveException e) {
                    log.error("The Supplier Object supplierId: " + supplierId + " cannot be deleted.", e);
                    resultDTO.setResultAsFailure();
                }
            }
        }
    }

    private void addSupplierCode(Integer addressId) {
        Address address = getAddress(addressId);
        if (!CodeUtil.isSupplier(address.getCode())) {
            byte newCode = (byte) (address.getCode() + CodeUtil.supplier);
            address.setCode(newCode);
        }
    }

    private void removeSupplierCode(Integer addressId) {
        Address address = getAddress(addressId);
        if (null != address) {
            address.setCode(CodeUtil.removeCode(address.getCode(), CodeUtil.supplier));
            address.setTaxNumber(null);
        }
    }

    private boolean checkRelationShips(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
        DTOFactory.i.copyToDTO(supplier, dto);
        IntegrityReferentialChecker.i.check(dto, resultDTO);

        return resultDTO.isFailure();
    }

    private Supplier getSupplier(Integer supplierId) {
        SupplierHome supplierHome =
                (SupplierHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_SUPPLIER);
        try {
            return supplierHome.findByPrimaryKey(supplierId);
        } catch (FinderException e) {
            log.debug("The Supplier with id: " + supplierId
                    + " was deleted by other user, or never been existed in the database.");
            return null;
        }
    }

    private Address getAddress(Integer addressId) {
        AddressHome addressHome =
                (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);

        try {
            return addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.debug("The Address with id: " + addressId + " was deleted by other user.");
            return null;
        }
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
