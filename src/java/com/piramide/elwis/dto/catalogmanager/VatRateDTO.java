package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.DateUtils;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.Date;
import java.util.HashMap;

/**
 * Represents a Data Transfer Object (DTO)
 *
 * @author Ivan
 * @version id: VatRateDTO.java,v 1.33 2004/07/19 21:18:48 ivan Exp $
 */
public class VatRateDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_VATRATELIST = "vatrateList";
    public static final String KEY_VATRATEID = "vatrateId";

    /**
     * Creates an instance.
     */
    public VatRateDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public VatRateDTO(DTO dto) {
        super.putAll(dto);
    }

    public VatRateDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_VATRATEID;
    }

    public String getDTOListName() {
        return KEY_VATRATELIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_VATRATE;
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
        Date formattedDate = DateUtils.integerToDate(new Integer(get("validFrom").toString()));
        String formattedDateString = DateUtils.parseDate(formattedDate, get("datePattern").toString());
        resultDTO.addResultMessage("msg.Duplicated", formattedDateString);
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {

        Date formattedDate = null;
        if (!"delete".equals(get("op"))) {
            formattedDate = DateUtils.integerToDate(new Integer(get("validFrom").toString()));
        }

        String formattedDateString = !"delete".equals(get("op")) ? DateUtils.parseDate(formattedDate, get("datePattern").toString())
                : get("validFrom").toString();
        resultDTO.addResultMessage("msg.NotFound", formattedDateString + ", " + get("vatLabel"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        Date formattedDate = null;
        if (!"delete".equals(get("op"))) {
            formattedDate = DateUtils.integerToDate(new Integer(get("validFrom").toString()));
        }

        String formattedDateString = !"delete".equals(get("op")) ? DateUtils.parseDate(formattedDate, get("datePattern").toString())
                : get("validFrom").toString();

        resultDTO.addResultMessage("customMsg.Referenced", formattedDateString + ", " + get("vatLabel"));
    }

    public ComponentDTO createDTO() {
        return new VatRateDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        //put the values of having referenced
        return tables;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_VATRATE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        values.put("vatId", "vatid");
        values.put("validFrom", "validfrom");
        //values.put("vatRate", "vatrate");
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_VATRATEID, "vatrateid");
        return values;
    }
}
