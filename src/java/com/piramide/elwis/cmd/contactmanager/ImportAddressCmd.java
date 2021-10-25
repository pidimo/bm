package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class ImportAddressCmd extends AddressCreateCmd {

    @Override
    protected void createCustomer(Address address, SessionContext ctx) {
        String customerNumber = (String) paramDTO.get("number");

        // isn't necessary add the customer code, because it was added in the Address object at creation time.
        paramDTO.put(CustomerDTO.KEY_CUSTOMERID, address.getAddressId());
        Customer customer = (Customer) EJBFactory.i.createEJB(new CustomerDTO(paramDTO));

        if (null != customerNumber && !"".equals(customerNumber.trim())) {
            customer.setNumber(customerNumber);
        } else {
            String newCustomerNumber = InvoiceUtil.i.getCustomerNumber(customer.getCompanyId());
            if (null != newCustomerNumber) {
                customer.setNumber(newCustomerNumber);
            }
        }
    }
}
