package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a PersonType DTO
 *
 * @author yumi
 * @version $Id: PersonTypeDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class PersonTypeDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    public static final String KEY_PERSONTYPELIST = "personTypeList";
    public static final String KEY_PERSONTYPEID = "personTypeId";

    public PersonTypeDTO() {
    }

    public PersonTypeDTO(Integer key) {
        setPrimKey(key);
    }

    public PersonTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_PERSONTYPEID;
    }

    public String getDTOListName() {
        return KEY_PERSONTYPELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PERSONTYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("personTypeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("personTypeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("personTypeName"));
    }

    public ComponentDTO createDTO() {
        return new PersonTypeDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PERSONTYPE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("personTypeName", "persontypename");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PERSONTYPEID, "persontypeid");
        return values;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_CONTACTPERSON, "persontypeid");

        return tables;
    }
}
