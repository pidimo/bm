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
 * Time: 10:07:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class SupportCaseSeverityDTO extends ComponentDTO implements DuplicatedEntryDTO, IntegrityReferentialDTO, Translate {
    public static final String KEY_SEVERITYID = "severityId";

    /**
     * Creates an instance.
     */
    public SupportCaseSeverityDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportCaseSeverityDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public SupportCaseSeverityDTO(Integer severityId) {
        setPrimKey(severityId);
    }

    public String getPrimKeyName() {
        return KEY_SEVERITYID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_CASE_SEVERTITY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("severityName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("severityName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("severityName"));
    }

    public ComponentDTO createDTO() {
        return new SupportCaseSeverityDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("severityName", "severityname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_SEVERITYID, "severityid");
        return values;
    }

    public String getTableName() {
        return SupportConstants.TABLE_SUPPORT_SEVERITY;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "severityid");

        return tables;
    }

    public String fieldToTranslate() {
        return "severityName";
    }

    public String fieldOfLanguaje() {
        return "languageId";
    }

    public String fieldRelatedWithObject() {
        return "langTextId";
    }
}