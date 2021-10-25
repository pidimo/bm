package com.piramide.elwis.dto.productmanager;

import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Represents Product DTO.
 *
 * @author Fernando Montaño
 * @version $Id: PricingDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */
public class PricingDTO extends ComponentDTO {
    public static final String KEY_PRICINGID = "pricingPK";
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * Creates an instance.
     */
    public PricingDTO() {

    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public PricingDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_PRICINGID;
    }

    public String getJNDIName() {
        return ProductConstants.JNDI_PRICING;
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
        resultDTO.addResultMessage("msg.Duplicated", get("quantity"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("quantity"));
    }


    public void addReferencedMsgTo(ResultDTO resultDTO) {

        resultDTO.addResultMessage("customMsg.Referenced", get("quantity"));

    }

    public ComponentDTO createDTO() {
        return new PricingDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }


}
