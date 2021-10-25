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
 * Represents a Branch DTO
 *
 * @author Ivan
 * @version $Id: BranchDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class BranchDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_BRANCHLIST = "branchList";
    public static final String KEY_BRANCHID = "branchId";

    /**
     * Creates an instance.
     */
    public BranchDTO() {
    }

    public BranchDTO(Integer key) {
        setPrimKey(key);
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public BranchDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_BRANCHID;
    }

    public String getDTOListName() {
        return KEY_BRANCHLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_BRANCH;
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
        resultDTO.addResultMessage("msg.Duplicated", get("branchName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("branchName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("branchName"));
    }

    public ComponentDTO createDTO() {
        return new BranchDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("branchName", "branchname");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_BRANCHID, "branchid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_BRANCH;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_BRANCH, "branchgroup");
        tables.put(ContactConstants.TABLE_SUPPLIER, "branchid");
        tables.put(ContactConstants.TABLE_CUSTOMER, "branchid");
        return tables;
    }
}
