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
 * Time: 10:10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class SupportCaseWorkLevelDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO, Translate {

    public static final String KEY_WORKLEVELID = "workLevelId";

    /**
     * Creates an instance.
     */
    public SupportCaseWorkLevelDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportCaseWorkLevelDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public SupportCaseWorkLevelDTO(Integer workLevelId) {
        setPrimKey(workLevelId);
    }

    public String getPrimKeyName() {
        return KEY_WORKLEVELID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_CASE_WORKLEVEL;
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
        resultDTO.addResultMessage("msg.Duplicated", get("workLevelName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("workLevelName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("workLevelName"));
    }

    public ComponentDTO createDTO() {
        return new SupportCaseWorkLevelDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("workLevelName", "worklevelname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_WORKLEVELID, "worklevelid");
        return values;
    }

    public String getTableName() {
        return SupportConstants.TABLE_SUPPORTCASE_WORKLEVEL;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_CASE_ACTIVITY, "worklevelid");

        return tables;
    }

    public String fieldToTranslate() {
        return "workLevelName";
    }

    public String fieldOfLanguaje() {
        return "languageId";
    }

    public String fieldRelatedWithObject() {
        return "langTextId";
    }
}