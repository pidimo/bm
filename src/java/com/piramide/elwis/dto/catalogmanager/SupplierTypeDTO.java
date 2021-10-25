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
 * Represents a SupplierType DTO
 *
 * @author Ivan
 * @version $Id: SupplierTypeDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class SupplierTypeDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {

    public static final String KEY_SUPPLIERTYPELIST = "supplierTypeList";
    public static final String KEY_SUPPLIERTYPEID = "supplierTypeId";

    /**
     * Creates an instance.
     */
    public SupplierTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupplierTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SupplierTypeDTO with specified languageId
     */
    public SupplierTypeDTO(Integer SupplierTypeId) {
        setPrimKey(SupplierTypeId);
    }

    public String getPrimKeyName() {
        return KEY_SUPPLIERTYPEID;
    }

    public String getDTOListName() {
        return KEY_SUPPLIERTYPELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_SUPPLIERTYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("supplierTypeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("supplierTypeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("supplierTypeName"));
    }

    public ComponentDTO createDTO() {
        return new SupplierTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }


    public String getTableName() {
        return CatalogConstants.TABLE_SUPPLIERTYPE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("supplierTypeName", "suppliertypename");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_SUPPLIERTYPEID, "suppliertypeid");
        return values;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_SUPPLIER, "suppliertypeid");
        return tables;
    }
}