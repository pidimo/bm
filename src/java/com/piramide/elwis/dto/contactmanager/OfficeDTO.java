package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Titus
 * @version OfficeDTO.java, v 2.0 May 10, 2004 10:29:44 AM Titus
 */
public class OfficeDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_OFFICELIST = "officeList";
    public static final String KEY_OFFICEID = "officeId";

    public OfficeDTO() {

    }

    public OfficeDTO(DTO dto) {
        super.putAll(dto);
    }

    public ComponentDTO createDTO() {
        return new OfficeDTO();
    }

    public String getPrimKeyName() {
        return KEY_OFFICEID;
    }

    public String getDTOListName() {
        return KEY_OFFICELIST;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_OFFICE;
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
        resultDTO.addResultMessage("Office.Duplicated", get("name"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
        if ("".equals(get("supervisorId"))) {
            put("supervisorId", null);
        }
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_EMPLOYEE, "officeid");
        return tables;
    }

}
