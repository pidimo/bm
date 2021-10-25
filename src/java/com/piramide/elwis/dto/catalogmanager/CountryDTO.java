package com.piramide.elwis.dto.catalogmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Conuntry DTO
 *
 * @author Ivan
 * @version $Id: CountryDTO.java 10354 2013-06-17 22:44:21Z miguel $
 */

public class CountryDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_COUNTRYLIST = "countryList";
    public static final String KEY_COUNTRYID = "countryId";

    /**
     * Creates an instance.
     */
    public CountryDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CountryDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty CountryDTO with specified languageId
     */
    public CountryDTO(Integer countryId) {
        setPrimKey(countryId);
    }

    public String getPrimKeyName() {
        return KEY_COUNTRYID;
    }

    public String getDTOListName() {
        return KEY_COUNTRYLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_COUNTRY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("countryName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("countryName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("countryName"));
    }

    public ComponentDTO createDTO() {
        return new CountryDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(CatalogConstants.TABLE_ADDRESS, "countryid");
        tables.put(CatalogConstants.TABLE_CITY, "countryid");
        tables.put(SchedulerConstants.TABLE_HOLIDAY, "countryid");
        tables.put(AdminConstants.TABLE_USER, "holidaycountryid");
        tables.put(ContactConstants.TABLE_ADDITIONALADDRESS, "countryid");

        return tables;
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("countryName", "countryname");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_COUNTRYID, "countryid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_COUNTRY;
    }
}