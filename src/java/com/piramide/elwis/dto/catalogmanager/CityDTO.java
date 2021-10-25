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
 * Represents a City DTO
 *
 * @author Ivan
 * @version $Id: CityDTO.java 10354 2013-06-17 22:44:21Z miguel $
 */
public class CityDTO extends ComponentDTO implements IntegrityReferentialDTO, DuplicatedEntryDTO {

    public static final String KEY_CITYLIST = "cityList";
    public static final String KEY_CITYID = "cityId";

    /**
     * Creates an instance.
     */
    public CityDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CityDTO(DTO dto) {
        super.putAll(dto);
    }

    /**
     * Creates an empty CityDTO with specified cityId
     */
    public CityDTO(Integer cityId) {
        setPrimKey(cityId);
    }

    public String getPrimKeyName() {
        return KEY_CITYID;
    }

    public String getDTOListName() {
        return KEY_CITYLIST;
    }

    public String getJNDIName() {
        return CatalogConstants.JNDI_CITY;
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
        resultDTO.addResultMessage("msg.Duplicated", get("cityName") + " " + get("cityZip"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("cityName") + " " + get("cityZip"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("cityName") + " " + get("cityZip"));
    }

    public ComponentDTO createDTO() {
        return new CityDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {

        HashMap tables = new HashMap();
        tables.put(ContactConstants.TABLE_ADDRESS, "cityid");
        tables.put(ContactConstants.TABLE_ADDITIONALADDRESS, "cityid");
        return tables;
    }

    public HashMap getDuplicatedValues() {
        HashMap map = new HashMap();
        map.put("cityName", "cityname");
        map.put("cityZip", "zip");
        map.put("countryId", "countryid");
        return map;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_CITYID, "cityid");
        return values;
    }

    public String getTableName() {
        return CatalogConstants.TABLE_CITY;
    }
}