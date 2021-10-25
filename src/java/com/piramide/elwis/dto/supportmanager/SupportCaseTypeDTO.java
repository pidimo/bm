package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.dto.common.Translate;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 12, 2005
 * Time: 9:59:13 AM
 * To change this template use File | Settings | File Templates.
 */

public class SupportCaseTypeDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO, Translate {

    public static final String KEY_CASETYPEID = "caseTypeId";

    /**
     * Creates an instance.
     */
    public SupportCaseTypeDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportCaseTypeDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public SupportCaseTypeDTO(Integer caseTypeId) {
        setPrimKey(caseTypeId);
    }

    public String getPrimKeyName() {
        return KEY_CASETYPEID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_CASE_TYPE;
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
        resultDTO.addResultMessage("msg.Duplicated", get("caseTypeName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("caseTypeName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("caseTypeName"));
    }

    public ComponentDTO createDTO() {
        return new SupportCaseTypeDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("caseTypeName", "casetypename");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CASETYPEID, "casetypeid");
        return values;
    }

    public String getTableName() {
        return SupportConstants.TABLE_SUPPORT_CASETYPE;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "casetypeid");
        return tables;
    }

    public String fieldToTranslate() {
        return "caseTypeName";
    }

    public String fieldOfLanguaje() {
        return "languageId";
    }

    public String fieldRelatedWithObject() {
        return "langTextId";
    }
}