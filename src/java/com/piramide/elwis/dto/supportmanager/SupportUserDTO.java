package com.piramide.elwis.dto.supportmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 29, 2005
 * Time: 10:26:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class SupportUserDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_SUPPORTUSER_ID = "supportUserPK";

    /**
     * Creates an instance.
     */
    public SupportUserDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupportUserDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty SalutationDTO with specified languageId
     */
    public SupportUserDTO(Integer severityId) {
        setPrimKey(severityId);
    }

    public String getPrimKeyName() {
        return KEY_SUPPORTUSER_ID;
    }


    public String getJNDIName() {
        return SupportConstants.JNDI_SUPPORT_USER;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {

    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {

    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {

    }

    //public void addDuplicatedMsgTo(ResultDTO resultDTO) {
    //  resultDTO.addResultMessage("SupportUser.duplicate", get("productName"));
    //}

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("productName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
    }

    public ComponentDTO createDTO() {
        return new SupportCaseSeverityDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("productId", "productid");
        map.put("userId", "userid");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_SUPPORTUSER_ID, "productid");
        return values;
    }

    public String getTableName() {
        return SupportConstants.TABLE_SUPPORT_USER;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        /*tables.put(SupportConstants.TABLE_SUPPORT_CASE, "severityid");*/

        return tables;
    }
}