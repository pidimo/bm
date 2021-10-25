package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.utils.CodeUtil;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.3.6
 */
public class CustomerUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    @Override
    public void executeInStateless(SessionContext ctx) {
        if ("deleteCustomer".equals(getOp())) {
            Integer customerId = EJBCommandUtil.i.getValueAsInteger(this, "customerId");
            deleteCustomer(customerId, ctx);
        }

        if ("createCustomer".equals(getOp())) {
            Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            createCustomer(addressId, companyId);
        }

        if ("createCustomerWithNumber".equals(getOp())) {
            createCustomerWithNumber();
        }
    }

    private void createCustomer(Integer addressId, Integer companyId) {
        //use addressId as customerId because it is a foreign primary key with customer table
        if (null == getCustomer(addressId)) {
            log.debug("Creating a new Customer Object for addressId: " + addressId + ", companyId: " + companyId);

            CustomerDTO dto = new CustomerDTO();
            dto.setPrimKey(addressId);
            dto.put("companyId", companyId);

            Customer customer = (Customer) ExtendedCRUDDirector.i.create(dto, resultDTO, false);
            String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
            if (null != newCustomerNumber) {
                customer.setNumber(newCustomerNumber);
            }

            addCustomerCode(addressId);
        }
    }

    private void createCustomerWithNumber() {
        Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");
        Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
        String number = (String) paramDTO.get("number");

        if (null == getCustomer(addressId)) {
            log.debug("Creating a new Customer Object for addressId: " + addressId + ", companyId: " + companyId);

            CustomerDTO dto = new CustomerDTO();
            dto.setPrimKey(addressId);
            dto.put("companyId", companyId);
            dto.put("number", number);

            Customer customer = (Customer) ExtendedCRUDDirector.i.create(dto, resultDTO, false);

            if (customer != null) {
                if (customer.getNumber() == null) {
                    String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
                    if (null != newCustomerNumber) {
                        customer.setNumber(newCustomerNumber);
                    }
                }
                addCustomerCode(addressId);
            }
        }
    }

    private void deleteCustomer(Integer customerId, SessionContext ctx) {
        log.debug("Deleting the Customer Object customerId: " + customerId);
        Customer customer = getCustomer(customerId);
        if (null != customer) {
            if (checkRelationShips(customer)) {
                readCategoryFieldValues(customer, ctx);
            } else {
                deleteCategoryFieldValues(customer, ctx);

                try {
                    customer.remove();

                    //use customerId as addressId because it is a foreign primary key with address table
                    removeCustomerCode(customerId);
                } catch (RemoveException e) {
                    log.error("The Customer Object supplierId: " + customerId + " cannot be deleted.", e);
                    resultDTO.setResultAsFailure();
                }
            }
        }
    }

    private void addCustomerCode(Integer addressId) {
        Address address = getAddress(addressId);
        if (!CodeUtil.isCustomer(address.getCode())) {
            byte newCode = (byte) (address.getCode() + CodeUtil.customer);
            address.setCode(newCode);
        }
    }

    private void removeCustomerCode(Integer addressId) {
        Address address = getAddress(addressId);
        if (null != address) {
            address.setCode(CodeUtil.removeCode(address.getCode(), CodeUtil.customer));
            address.setTaxNumber(null);
        }
    }

    private void readCategoryFieldValues(Customer customer, SessionContext ctx) {
        List parameters = Arrays.asList(customer.getCustomerId(), customer.getCompanyId());

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", "findByCustomerId");
        cmd.putParam("params", parameters);
        cmd.setOp("readCAtegoryFieldValues");
        cmd.executeInStateless(ctx);

        ResultDTO localResultDTO = cmd.getResultDTO();
        resultDTO.putAll(localResultDTO);
    }

    private void deleteCategoryFieldValues(Customer customer, SessionContext ctx) {
        List parameters = Arrays.asList(customer.getCustomerId(), customer.getCompanyId());

        CategoryUtilCmd cmd = new CategoryUtilCmd();
        cmd.putParam("finderName", "findByCustomerId");
        cmd.putParam("params", parameters);
        cmd.setOp("deleteValues");
        cmd.executeInStateless(ctx);
    }

    private boolean checkRelationShips(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        DTOFactory.i.copyToDTO(customer, dto);

        IntegrityReferentialChecker.i.check(new CustomerDTO(paramDTO), resultDTO);

        return resultDTO.isFailure();
    }

    private Customer getCustomer(Integer customerId) {
        CustomerHome home =
                (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        try {
            return home.findByPrimaryKey(customerId);
        } catch (FinderException e) {
            log.debug("The Customer with id: " + customerId + " was deleted by other user, or never been existed in the database.");
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
