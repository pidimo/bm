package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.dto.common.ReferentialSQL;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;


/**
 * Represents Address DTO.
 *
 * @author Ernesto
 * @version $Id: AddressDTO.java 10529 2015-03-26 21:23:21Z miguel $
 */
public class AddressDTO extends ComponentDTO implements IntegrityReferentialDTO {
    private Log log = LogFactory.getLog(this.getClass());
    public static final String KEY_ADDRESSID = "addressId";

    /**
     * Creates an instance.
     */
    public AddressDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public AddressDTO(DTO dto) {
        super.putAll(dto);
    }

    public AddressDTO(Integer key) {
        setPrimKey(key);
    }

    public AddressDTO(int id) {
        setPrimKey(new Integer(id));
    }

    public String getPrimKeyName() {
        return KEY_ADDRESSID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_ADDRESS;
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
        if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {
            if (get("name1") != null) {
                resultDTO.addResultMessage("msg.Duplicated", get("name1") + "" +
                        ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
            } else {
                resultDTO.addResultMessage("msg.Duplicated", "Contact");
            }
        } else {
            if (get("name1") != null) {
                resultDTO.addResultMessage("msg.Duplicated", get("name1") + " " +
                        ((get("name2") != null) ? get("name2") : "") + " " +
                        ((get("name3") != null) ? get("name3") : ""));
            } else {
                resultDTO.addResultMessage("msg.Duplicated", "Contact");
            }
        }
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        if (get("addressType") != null) { // if contain the address type  set not found message
            if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {
                if (get("name1") != null) {
                    resultDTO.addResultMessage("customMsg.NotFound", get("name1") + "" +
                            ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
                } else {
                    resultDTO.addResultMessage("Address.NotFound");
                }
            } else {
                if (get("name1") != null) {
                    resultDTO.addResultMessage("customMsg.NotFound", get("name1") + " " +
                            ((get("name2") != null) ? get("name2") : "") + " " +
                            ((get("name3") != null) ? get("name3") : ""));
                } else {
                    resultDTO.addResultMessage("Address.NotFound");
                }
            }
        } else { // is comming from favorites or recents listboxes. They cannot set address type, only addressId.
            resultDTO.addResultMessage("Address.NotFound");
        }
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        if (ContactConstants.ADDRESSTYPE_PERSON.equals(get("addressType").toString())) {
            if (get("name1") != null) {
                resultDTO.addResultMessage("customMsg.Referenced", get("name1") + "" +
                        ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
            } else {
                resultDTO.addResultMessage("msg.Referenced");
            }
        } else {
            if (get("name1") != null) {
                resultDTO.addResultMessage("customMsg.Referenced", get("name1") + " " +
                        ((get("name2") != null) ? get("name2") : "") + " " +
                        ((get("name3") != null) ? get("name3") : ""));
            } else {
                resultDTO.addResultMessage("msg.Referenced");
            }
        }
    }

    public ComponentDTO createDTO() {
        return new AddressDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_USER,
                ReferentialPK.create()
                        .addKey("addressId", "addressid")
                        .addKey("userTypeValue", "type"));

        tables.put(ContactConstants.TABLE_CONTACTPERSON, "contactpersonid");
        tables.put(ContactConstants.TABLE_EMPLOYEE, "healthfund");
        tables.put(ContactConstants.TABLE_CUSTOMER, "partnerid, customerid, invoiceaddressid");
        tables.put(ContactConstants.TABLE_SUPPLIER, "supplierid");
        tables.put(CampaignConstants.TABLE_CAMPAIGNCONTACT, "addressid");
        tables.put(ProductConstants.TABLE_COMPETITORPRODUCT, "competitorid");

        tables.put(WebMailConstants.TABLE_ADDRESSGROUP, "addressid");
        tables.put(SchedulerConstants.TABLE_APPOINTMENT, "addressid");
        tables.put(SchedulerConstants.TABLE_TASK, "addressid");

        /*for Support module*/
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "addressid");
        tables.put(SupportConstants.TABLE_SUPPORT_CONTACT,
                ReferentialSQL.create()
                        .setFromSQL("contact, supportcontact")
                        .setWhereSQL("contact.contactid=supportcontact.contactid and contact.addressid=?")
                        .addParamDTO("addressId"));

        tables.put(SalesConstants.TABLE_SALESPROCESS, "addressid");
        tables.put(FinanceConstants.TABLE_INVOICE, "addressid, sentaddressid");

        tables.put(ProjectConstants.TABLE_PROJECT_TIME, "assigneeid");
        tables.put(ProjectConstants.TABLE_PROJECT_ASSIGNEE, "addressid");
        tables.put(FinanceConstants.TABLE_SEQUENCERULE, "debitorid");

        tables.put(CampaignConstants.TABLE_SENTLOGCONTACT, "addressid");

        tables.put(ContactConstants.TABLE_ADDITIONALADDRESS, "addressid");
        tables.put(ContactConstants.TABLE_ADDRESSRELATION, "addressid, relatedaddressid");

        tables.put(ContactConstants.TABLE_IMPORTRECORD, "organizationid");
        tables.put(ContactConstants.TABLE_RECORDUPLICATE, "addressid");
        tables.put(ContactConstants.TABLE_DUPLICATEADDRESS, "addressid");

        tables.put(SalesConstants.TABLE_SALE, "sentaddressid");
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "sentaddressid");
        tables.put(ProjectConstants.TABLE_PROJECT_TIME_LIMIT, "assigneeid");

        return tables;
    }
}
