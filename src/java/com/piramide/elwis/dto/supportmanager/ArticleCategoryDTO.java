package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 2:14:43 PM
 * To change this template use File | Settings | File Templates.
 */

public class ArticleCategoryDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO {
    public static final String KEY_CATEGORYID = "categoryId";

    /**
     * Creates an instance.
     */
    public ArticleCategoryDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ArticleCategoryDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public ArticleCategoryDTO(Integer categoryId) {
        setPrimKey(categoryId);
    }

    public String getPrimKeyName() {
        return KEY_CATEGORYID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_ARTICLECATEGORY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("categoryName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("categoryName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("categoryName"));
    }

    public ComponentDTO createDTO() {
        return new ArticleCategoryDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("categoryName", "categoryname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CATEGORYID, "categoryid");
        return values;
    }

    public String getTableName() {
        return SupportConstants.TABLE_ARTICLECATEGORY;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_ARTICLE, "categoryid");
        tables.put(SupportConstants.TABLE_ARTICLEQUESTION, "categoryid");
        tables.put(SupportConstants.TABLE_ARTICLECATEGORY, "parentcategoryid");
        return tables;
    }
}