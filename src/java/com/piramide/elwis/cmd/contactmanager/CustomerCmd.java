package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.InvoiceUtil;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.CustomerDTO;
import com.piramide.elwis.utils.CategoryUtil;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.*;

/**
 * This class manages customer business logic for Customer functionality.
 *
 * @author Fernando Montaño
 * @version $Id: CustomerCmd.java 10359 2013-07-04 00:29:08Z miguel $
 */

public class CustomerCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        Map myParamDTO = new HashMap();
        //myParamDTO is used for CategoryUtilCmd
        myParamDTO.putAll(paramDTO);

        CustomerDTO customerDTO = new CustomerDTO(paramDTO);
        log.debug("paramDTO" + paramDTO);
        if (!paramDTO.hasOp()) { // read
            log.debug("read customer information for address");

            //recovering address to set the taxNumber
            AddressDTO addressDTO = new AddressDTO(paramDTO);
            Address address = null;
            try {
                address = (Address) EJBFactory.i.findEJB(addressDTO);
                resultDTO.put("taxNumber", address.getTaxNumber());

            } catch (EJBFactoryException e) { // address not found
                addressDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
                return;
            }

            customerDTO.put("customerId", paramDTO.get("addressId"));
            Customer customer = (Customer) CRUDDirector.i.read(customerDTO, resultDTO); //if read from customer detail.

            if (resultDTO.isFailure()) { //if cannot read the customer, was deleted by other user
                if (ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType())) {
                    resultDTO.setForward("PersonDetail");
                } else {
                    resultDTO.setForward("OrganizationDetail");
                }
                return;
            }

            String customerNumber =
                    InvoiceUtil.i.applyCustomerNumber(customer.getNumber(), customer.getCompanyId());
            if (null != customerNumber) {
                resultDTO.put("number", customerNumber);
            }

            if (customer != null && customer.getPartner() != null) {
                log.debug("reading partner address name...");
                StringBuffer partnerName = new StringBuffer("");
                if (ContactConstants.ADDRESSTYPE_PERSON.equals(customer.getPartner().getAddressType())) {
                    partnerName.append(customer.getPartner().getName1() +
                            ((customer.getPartner().getName2() != null) ? ", " + customer.getPartner().getName2() : ""));
                } else {
                    partnerName.append(customer.getPartner().getName1() + " " +
                            ((customer.getPartner().getName2() != null) ? customer.getPartner().getName2() : "") + " " +
                            ((customer.getPartner().getName3() != null) ? customer.getPartner().getName3() : ""));
                }

                resultDTO.put("partnerName", partnerName.toString());
            }

            //read categoryFieldValues object
            String finderName = "findByCustomerId";
            Object[] params = new Object[]{customer.getCustomerId(), customer.getCompanyId()};
            List paramsAsList = Arrays.asList(params);

            CategoryUtilCmd cmd = new CategoryUtilCmd();
            cmd.putParam("finderName", finderName);
            cmd.putParam("params", paramsAsList);
            cmd.setOp("readCAtegoryFieldValues");
            cmd.executeInStateless(ctx);
            ResultDTO myResultDTO = cmd.getResultDTO();
            resultDTO.putAll(myResultDTO);

            return;
        }
        // if is updating customer info
        if (CRUDDirector.OP_UPDATE.equals(paramDTO.getOp())) {

            if (paramDTO.get("save") != null) { //update customer information
                //version control
                VersionControlChecker.i.check(customerDTO, resultDTO, paramDTO);
                if (resultDTO.get("EntityBeanNotFound") != null) { // bean not found in version control
                    resultDTO.put("addressId", customerDTO.get("customerId"));
                    customerDTO.addNotFoundMsgTo(resultDTO);
                    if (ContactConstants.ADDRESSTYPE_PERSON.equals(customerDTO.getAsString("addressType"))) {
                        resultDTO.setForward("PersonDetail");
                    } else {
                        resultDTO.setForward("OrganizationDetail");
                    }
                    return;
                }
                if (resultDTO.isFailure()) { //version control detect changes of other user
                    resultDTO.setForward("Success"); //invalidating failure

                    //CRUDDirector.i.read(customerDTO, resultDTO);
                    Customer customer = (Customer) CRUDDirector.i.read(customerDTO, resultDTO); //if read from customer detail.
                    // read partner
                    if (customer != null && customer.getPartner() != null) {
                        log.debug("reading partner address name...");
                        StringBuffer partnerName = new StringBuffer("");
                        if (ContactConstants.ADDRESSTYPE_PERSON.equals(customer.getPartner().getAddressType())) {
                            partnerName.append(customer.getPartner().getName1() +
                                    ((customer.getPartner().getName2() != null) ? ", " + customer.getPartner().getName2() : ""));
                        } else {
                            partnerName.append(customer.getPartner().getName1() + " " +
                                    ((customer.getPartner().getName2() != null) ? customer.getPartner().getName2() : "") + " " +
                                    ((customer.getPartner().getName3() != null) ? customer.getPartner().getName3() : ""));
                        }

                        resultDTO.put("partnerName", partnerName.toString());
                    }

                    //removes all keys related with category because this are invalid values
                    CategoryUtil.removeKeyAndValueForMap(paramDTO, CategoryUtilCmd.categoryDtoConstant);

                    //read categoryFieldValues object
                    String finderName = "findByCustomerId";
                    Object[] params = new Object[]{customer.getCustomerId(), customer.getCompanyId()};
                    List paramsAsList = Arrays.asList(params);

                    CategoryUtilCmd cmd = new CategoryUtilCmd();
                    cmd.putParam("finderName", finderName);
                    cmd.putParam("params", paramsAsList);
                    cmd.setOp("readCAtegoryFieldValues");
                    cmd.executeInStateless(ctx);
                    ResultDTO myResultDTO = cmd.getResultDTO();
                    resultDTO.putAll(myResultDTO);

                    //recovering address to set the taxNumber
                    AddressDTO addressDTO = new AddressDTO(paramDTO);
                    try {
                        Address address = (Address) EJBFactory.i.findEJB(addressDTO);
                        resultDTO.put("taxNumber", address.getTaxNumber());
                        log.debug("TaxNumber after read the address = " + resultDTO.get("taxNumber"));

                    } catch (EJBFactoryException e) { // address not found
                        resultDTO.put("addressId", customerDTO.get("customerId"));
                        addressDTO.addNotFoundMsgTo(resultDTO);
                        resultDTO.setForward("Fail");
                        return;
                    }
                    return;
                }

                customerDTO.put("version", paramDTO.get("version"));
                Customer customer = (Customer) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, customerDTO, resultDTO);

                if (resultDTO.isFailure()) { // if  customer bean was removed
                    if (ContactConstants.ADDRESSTYPE_PERSON.equals(customerDTO.getAsString("addressType"))) {
                        resultDTO.setForward("PersonDetail");
                    } else {
                        resultDTO.setForward("OrganizationDetail");
                    }
                    return;
                }

                if (customer != null) { //update changed values Id's
                    if (customer.getBranchId() != null && paramDTO.get("branchId") == null) {
                        customer.setBranchId(null);
                    }
                    if (customer.getCustomerTypeId() != null && paramDTO.get("customerTypeId") == null) {
                        customer.setCustomerTypeId(null);
                    }
                    if (customer.getSourceId() != null && paramDTO.get("sourceId") == null) {
                        customer.setSourceId(null);
                    }
                    if (customer.getPayMoralityId() != null && paramDTO.get("payMoralityId") == null) {
                        customer.setPayMoralityId(null);
                    }
                    if (customer.getPayConditionId() != null && paramDTO.get("payConditionId") == null) {
                        customer.setPayConditionId(null);
                    }
                    if (customer.getPriorityId() != null && paramDTO.get("priorityId") == null) {
                        customer.setPriorityId(null);
                    }
                    if (customer.getExpectedTurnOver() != null && paramDTO.get("expectedTurnOver") == null) {
                        customer.setExpectedTurnOver(null);
                    }
                    if (customer.getNumber() != null && paramDTO.get("number") == null) {
                        customer.setNumber(null);
                    }
                    if (customer.getNumberOfEmployees() != null && paramDTO.get("numberOfEmployees") == null) {
                        customer.setNumberOfEmployees(null);
                    }
                    if (customer.getPartnerId() != null && paramDTO.get("partnerId") == null) {
                        customer.setPartnerId(null);
                    }

                    //recovering address to set the taxNumber
                    AddressDTO addressDTO = new AddressDTO(paramDTO);
                    addressDTO.setPrimKey(paramDTO.get("customerId"));
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
                        resultDTO.put("addressId", customerDTO.get("customerId"));
                        resultDTO.setForward("Fail");
                        return;
                    }

                    CRUDDirector.i.read(customerDTO, resultDTO); // read updated values
                    //read partner
                    if (resultDTO.get("partnerId") != null) { //put partner name if exists
                        try {
                            Address address = (Address) EJBFactory.i.findEJB(new AddressDTO((Integer) resultDTO.get("partnerId")));
                            StringBuffer partnerName = new StringBuffer("");
                            if (ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType())) {
                                partnerName.append(address.getName1() +
                                        ((address.getName2() != null) ? ", " + address.getName2() : ""));
                            } else {
                                partnerName.append(address.getName1() + " " +
                                        ((address.getName2() != null) ? address.getName2() : "") + " " +
                                        ((address.getName3() != null) ? address.getName3() : ""));
                            }

                            resultDTO.put("partnerName", partnerName.toString());

                        } catch (EJBFactoryException ex) {
                            // bean not found in Operation whit Address
                            new AddressDTO().addNotFoundMsgTo(resultDTO);
                            resultDTO.put("addressId", customerDTO.get("customerId"));
                            resultDTO.setForward("Fail");
                            return;
                        }
                    } else {
                        resultDTO.put("partnerName", "");

                    }

                    //update categoryfieldvalues object
                    String finderName = "findByCustomerId";
                    Object[] params = new Object[]{customer.getCustomerId(), customer.getCompanyId()};
                    List paramsAsList = Arrays.asList(params);

                    List<Map> sourceValues = new ArrayList<Map>();
                    Map productMap = new HashMap();
                    productMap.put("identifier", "customerId");
                    productMap.put("value", customer.getCustomerId());
                    sourceValues.add(productMap);

                    CategoryUtilCmd cmd = new CategoryUtilCmd();
                    cmd.putParam("sourceValues", sourceValues);
                    cmd.putParam("companyId", customer.getCompanyId());
                    cmd.putParam(myParamDTO);
                    cmd.putParam("finderName", finderName);
                    cmd.putParam("params", paramsAsList);
                    cmd.setOp("updateValues");
                    cmd.executeInStateless(ctx);

                    CategoryUtilCmd cmd2 = new CategoryUtilCmd();
                    cmd2.putParam("finderName", finderName);
                    cmd2.putParam("params", paramsAsList);
                    cmd2.setOp("readCAtegoryFieldValues");
                    cmd2.executeInStateless(ctx);
                    ResultDTO myResultDTO = cmd2.getResultDTO();
                    resultDTO.putAll(myResultDTO);
                }
            }
        }
    }

    public void clean(HashMap map) {
        Iterator it = map.values().iterator();
        while (it.hasNext()) {
            Object value = it.next();
            if (value == null) {
                it.remove();
                it = map.values().iterator();
            } else if (String.class.equals(value.getClass())) {
                String val = (String) value;
                if (value == null || val.equals("")) {
                    it.remove();
                    it = map.values().iterator();
                }
            }

        }
    }

    public boolean isStateful() {
        return false;
    }

}
