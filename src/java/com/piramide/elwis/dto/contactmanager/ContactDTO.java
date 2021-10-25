package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.ReferentialPK;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Yumi
 * @version $Id: ContactDTO.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ContactDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String KEY_CONTACTID = "contactId";

    public ContactDTO() {
    }

    public ContactDTO(Integer key) {
        setPrimKey(key);
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ContactDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_CONTACTID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_CONTACT;
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
        resultDTO.addResultMessage("msg.Duplicated", "Contact.id");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("note"));
    }

    public ComponentDTO createDTO() {
        return new ContactDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }


    public boolean hasMultipleList() {
        return false;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SalesConstants.TABLE_ACTION, "contactid");
        tables.put(SalesConstants.TABLE_SALE, "contactid");
        tables.put(SupportConstants.TABLE_SUPPORT_CONTACT,
                ReferentialPK.create()
                        .addKey("contactId", "contactid")
                        .addKey("activityId", "activityid"));
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("note"));
    }
}