package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Category DTO
 *
 * @author Ivan
 * @version $Id: CategoryDTO.java 9695 2009-09-10 21:34:43Z fernando $
 */
public class CategoryDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_CATEGORYLIST = "categoryList";
    public static final String KEY_CATEGORYID = "categoryId";

    /**
     * Creates an instance.
     */
    public CategoryDTO() {
    }

    public CategoryDTO(Integer key) {
        setPrimKey(key);
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CategoryDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYID;
    }

    public String getDTOListName() {
        return KEY_CATEGORYLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CATEGORY;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {
    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("categoryName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("categoryName"));
    }

    public ComponentDTO createDTO() {
        return new CategoryDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_SUPPLIER, "categoryid");
        tables.put(CatalogConstants.TABLE_CATEGORY, "parentcategory");
        tables.put(CampaignConstants.TABLE_CAMPAIGNCRITERION, "categoryid");
        tables.put(ReportConstants.TABLE_COLUMN, "categoryid");
        tables.put(ReportConstants.TABLE_FILTER, "categoryid");
        return tables;
    }
}