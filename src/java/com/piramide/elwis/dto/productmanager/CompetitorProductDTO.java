package com.piramide.elwis.dto.productmanager;

import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CompetitorProductDTO.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class CompetitorProductDTO extends ComponentDTO {
    public static final String COMPETITORPRODUCT_PRIMARY_KEY = "competitorProductId";
    public static final String KEY_COMPETITORPRODUCTLIST = "competitorProductList";

    /**
     * Creates an instance.
     */
    public CompetitorProductDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public CompetitorProductDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return COMPETITORPRODUCT_PRIMARY_KEY;
    }

    public String getJNDIName() {
        return ProductConstants.JNDI_COMPETITORPRODUCT;
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
        resultDTO.addResultMessage("msg.Duplicated", get("productName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("productName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
    }

    public ComponentDTO createDTO() {
        return new CompetitorProductDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}
