package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.contactmanager.CustomerHome;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class LightlyCustomerCmd extends EJBCommand {
    private Log log = LogFactory.getLog(LightlyCustomerCmd.class);

    public void executeInStateless(SessionContext ctx) {
        if ("getCustomer".equals(getOp())) {
            Integer addressId = (Integer) paramDTO.get("addressId");
            getCustomer(addressId);
        }
    }

    private CustomerDTO getCustomer(Integer addressId) {
        CustomerDTO customerDTO = null;
        try {
            Customer customer = searchCustomer(addressId);
            if (null != customer) {
                customerDTO = new CustomerDTO();
                DTOFactory.i.copyToDTO(customer, customerDTO);
            }

        } catch (FinderException e) {
            log.debug("-> Read Customer addressId=" + addressId + " FAIL");
        }
        resultDTO.put("getCustomer", customerDTO);
        return customerDTO;
    }

    private Customer searchCustomer(Integer addressId) throws FinderException {
        CustomerHome customerHome =
                (CustomerHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CUSTOMER);
        return customerHome.findByPrimaryKey(addressId);
    }

    public boolean isStateful() {
        return false;
    }
}
