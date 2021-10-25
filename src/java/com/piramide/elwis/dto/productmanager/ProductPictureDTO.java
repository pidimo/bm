package com.piramide.elwis.dto.productmanager;

import com.piramide.elwis.dto.common.DuplicatedEntryDTO;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents a Data Transfer Object (DTO)
 *
 * @author Ivan
 * @version id: ProductPictureDTO.java,v 1.33 2004/07/19 21:18:48 ivan Exp $
 */
public class ProductPictureDTO extends ComponentDTO implements DuplicatedEntryDTO {

    public static final String KEY_PRODUCTPICTURELIST = "productPictureList";
    public static final String KEY_PRODUCTPICTUREID = "productId";

    /**
     * Creates an instance.
     */
    public ProductPictureDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ProductPictureDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProductPictureDTO(Integer key) {
        setPrimKey(key);
    }

    public String getPrimKeyName() {
        return KEY_PRODUCTPICTUREID;
    }

    public String getDTOListName() {
        return KEY_PRODUCTPICTURELIST;
    }

    public String getJNDIName() {
        return ProductConstants.JNDI_PRODUCTPICTURE;
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
        resultDTO.addResultMessage("msg.Duplicated", "...");
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.NotFound", get("productPictureName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", "...");
    }

    public ComponentDTO createDTO() {
        return new ProductPictureDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public String getTableName() {
        return ProductConstants.TABLE_PRODUCTPICTURE;
    }

    public HashMap getDuplicatedValues() {
        HashMap values = new HashMap();
        //put the duplicated values
        return values;
    }

    public HashMap getPrimaryKey() {
        HashMap values = new HashMap();
        values.put(KEY_PRODUCTPICTUREID, "productId");
        return values;
    }


}
