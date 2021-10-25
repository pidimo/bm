package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a PayCondition DTO
 *
 * @author Ivan
 * @version $Id: PayConditionDTO.java 9695 2009-09-10 21:34:43Z fernando ${NAME}DTO.java,v 1.13 2004/05/25 18:43:26 Ivan Exp $
 */
public class PayConditionDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_PAYCONDITIONLIST = "payConditionList";
    public static final String KEY_PAYCONDITIONID = "payConditionId";

    /**
     * Creates an instance.
     */
    public PayConditionDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public PayConditionDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty PayConditionDTO with specified PayConditionId
     */
    public PayConditionDTO(Integer payConditionId) {
        setPrimKey(payConditionId);
    }

    public String getPrimKeyName() {
        return KEY_PAYCONDITIONID;
    }

    public String getDTOListName() {
        return KEY_PAYCONDITIONLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_PAYCONDITION;
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
        resultDTO.addResultMessage("msg.Duplicated", get("payConditionName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("payConditionName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("payConditionName"));
    }

    public ComponentDTO createDTO() {
        return new PayConditionDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("payConditionName", "conditionname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PAYCONDITIONID, "payconditionid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_PAYCONDITION;
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(FinanceConstants.TABLE_INVOICE, "payconditionid");
        tables.put(SalesConstants.TABLE_PRODUCTCONTRACT, "payconditionid");
        tables.put(ContactConstants.TABLE_CUSTOMER, "payconditionid");


        return tables;
    }

}