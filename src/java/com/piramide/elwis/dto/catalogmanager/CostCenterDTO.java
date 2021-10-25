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
 * Represents a CostCenter DTO
 *
 * @author Ivan
 * @version $Id: CostCenterDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */

public class CostCenterDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_COSTCENTERLIST = "costCenterList";
    public static final String KEY_COSTCENTERID = "costCenterId";

    /**
     * Creates an instance.
     */
    public CostCenterDTO() {
    }


    public CostCenterDTO(DTO dto) {
        super.putAll(dto);
    }


    public CostCenterDTO(Integer costCenterId) {
        setPrimKey(costCenterId);
    }

    public String getPrimKeyName() {
        return KEY_COSTCENTERID;
    }

    public String getDTOListName() {
        return KEY_COSTCENTERLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_COSTCENTER;
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
        resultDTO.addResultMessage("msg.Duplicated", get("costCenterName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("costCenterName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("costCenterName"));
    }

    public ComponentDTO createDTO() {
        return new CostCenterDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("costCenterName", "costcentername");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_COSTCENTERID, "costcenterid");
        return values;
    }

    public String getTableName() {

        return CatalogConstants.TABLE_COSTCENTER;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_COSTCENTER, "parentcostcenterid");
        tables.put(ContactConstants.TABLE_EMPLOYEE, "costcenterid");
        return tables;
    }
}