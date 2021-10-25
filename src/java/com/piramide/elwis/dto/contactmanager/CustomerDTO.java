package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.ProjectConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;


/**
 * Customer DTO value class.
 *
 * @author Yumi
 * @version $Id: CustomerDTO.java 10182 2011-10-01 19:16:50Z miguel $
 */
public class CustomerDTO extends ComponentDTO implements IntegrityReferentialDTO {
    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_CUSTOMERID = "customerId";

    /**
     * Creates an instance.
     */
    public CustomerDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CustomerDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_CUSTOMERID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_CUSTOMER;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", "Customer");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("addressType") != null) { // if contain the address type  set not found message
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {

                resultDTO.addResultMessage("customMsg.Customer.NotFound", get("name1") + "" +
                        ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
            } else {
                resultDTO.addResultMessage("customMsg.Customer.NotFound", get("name1") + " " +
                        ((get("name2") != null) ? get("name2") : "") + " " +
                        ((get("name3") != null) ? get("name3") : ""));
            }

        } else { // when a customer was deleted by other user
            resultDTO.addResultMessage("Customer.NotFound");
        }
    }

    public ComponentDTO createDTO() {
        return new CustomerDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
        /*if (get("expectedTurnOver") != null) {
            this.put("expectedTurnOver", (new java.math.BigDecimal(get("expectedTurnOver").toString())));
        }*/
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_SALESPROCESS, "addressid");
        tables.put(FinanceConstants.TABLE_INVOICE, "addressid");
        tables.put(SalesConstants.TABLE_SALE, "customerid");
        tables.put(SalesConstants.TABLE_SALEPOSITION, "customerId");
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "addressId");
        tables.put(ProjectConstants.TABLE_PROJECT, "customerid");
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (get("addressType") != null) { // if contain the address type  set not found message
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {

                resultDTO.addResultMessage("customMsg.Referenced", get("name1") + "" +
                        ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
            } else {
                resultDTO.addResultMessage("customMsg.Referenced", get("name1") + " " +
                        ((get("name2") != null) ? get("name2") : "") + " " +
                        ((get("name3") != null) ? get("name3") : ""));
            }

        } else { // references by other functionalities
            resultDTO.addResultMessage("msg.Referenced");
        }
    }
}
