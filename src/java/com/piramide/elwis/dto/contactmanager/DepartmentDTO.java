package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents data from Department Bean for been used in Aplication
 *
 * @author Titus
 * @version DepartmentDTO.java, v 2.0  May 7, 2004 11:08:38 AM
 */
public class DepartmentDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_DEPARTMENTID = "departmentId";

    /**
     * Creates an instance.
     */
    public DepartmentDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public DepartmentDTO(DTO dto) {
        super.putAll(dto);
    }

    public DepartmentDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_DEPARTMENTID;
    }

    public String getJNDIName() {
        return ContactConstants.JNDI_DEPARTMENT;
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
        resultDTO.addResultMessage("Department.Duplicated", get("name"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("name"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("name"));
    }

    public ComponentDTO createDTO() {
        return new DepartmentDTO();
    }

    /**
     * parse values to original type in BD before execute any transaction, especialy primaryKey
     */
    public void parseValues() {
        super.convertPrimKeyToInteger();
        if ("".equals(get("parentId"))) {
            put("parentId", null);
        }

    }

    /**
     * Values referenced in other tables in BD
     */
    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put("employee", "departmentid");
        tables.put("contactperson", "departmentid");
        tables.put(ContactConstants.TABLE_DEPARTMENT, "parentdepartmentid");

        return tables;
    }

}
