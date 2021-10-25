package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Contact Person DTO
 *
 * @author Tayes
 * @version $Id: ContactPersonDTO.java 11784 2015-12-09 23:49:38Z miguel $
 */

public class ContactPersonDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_CONTACTPERSONID = "contactPersonPK";

    /**
     * Creates an instance.
     */
    public ContactPersonDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ContactPersonDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty ContactPersonDTO with specified contactPersonId
     */
    public ContactPersonDTO(Integer contactPersonId) {
        setPrimKey(contactPersonId);
    }

    public String getPrimKeyName() {
        return KEY_CONTACTPERSONID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_CONTACTPERSON;
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
        resultDTO.addResultMessage("ContactPerson.duplicated", get("name1") + "" +
                ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name1") + "" +
                ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name1") + "" +
                ((get("name2") != null && !"".equals(get("name2"))) ? ", " + get("name2") : ""));
    }

    public ComponentDTO createDTO() {
        return new ContactPersonDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }


    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CONTACT,
                ReferentialPK.create()
                        .addKey("addressId", "addressid")
                        .addKey("contactPersonId", "contactpersonid"));
        tables.put(ContactConstants.TABLE_DEPARTMENT,
                ReferentialPK.create()
                        .addKey("addressId", "addressid")
                        .addKey("contactPersonId", "managerid"));
        tables.put(CampaignConstants.TABLE_CAMPAIGNCONTACT,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(CampaignConstants.TABLE_SENTLOGCONTACT,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(ContactConstants.TABLE_EMPLOYEE,
                ReferentialPK.create()
                        .addKey("addressId", "companyid")
                        .addKey("contactPersonId", "employeeid"));
        tables.put(ProductConstants.TABLE_PRODUCTSUPPLIER,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "supplierid"));
        tables.put(WebMailConstants.TABLE_ADDRESSGROUP,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(SchedulerConstants.TABLE_APPOINTMENT,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(SchedulerConstants.TABLE_TASK,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(SupportConstants.TABLE_SUPPORT_CASE,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(FinanceConstants.TABLE_INVOICE,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(SalesConstants.TABLE_SALE,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "customerid"));
        tables.put(SalesConstants.TABLE_SALEPOSITION,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "customerid"));
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "addressid"));
        tables.put(ProjectConstants.TABLE_PROJECT,
                ReferentialPK.create()
                        .addKey("contactPersonId", "contactpersonid")
                        .addKey("addressId", "customerid"));

        tables.put(ContactConstants.TABLE_CUSTOMER,
                ReferentialPK.create()
                        .addKey("addressId", "invoiceaddressid")
                        .addKey("contactPersonId", "invoicecontactpersonid"));
        tables.put(SalesConstants.TABLE_SALE,
                ReferentialPK.create()
                        .addKey("addressId", "sentaddressid")
                        .addKey("contactPersonId", "sentcontactpersonid"));
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT,
                ReferentialPK.create()
                        .addKey("addressId", "sentaddressid")
                        .addKey("contactPersonId", "sentcontactpersonid"));
        tables.put(FinanceConstants.TABLE_INVOICE,
                ReferentialPK.create()
                        .addKey("addressId", "sentaddressid")
                        .addKey("contactPersonId", "sentcontactpersonid"));
        tables.put(AdminConstants.TABLE_USER,
                ReferentialPK.create()
                        .addKey("addressId", "moborganizationid")
                        .addKey("contactPersonId", "addressid"));

        return tables;
    }
}