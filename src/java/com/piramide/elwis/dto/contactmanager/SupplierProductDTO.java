package com.piramide.elwis.dto.contactmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * @author Yumi
 * @version SupplierProductDTO.java, v 2.0 Aug 26, 2004 1:54:26 PM
 */

public class SupplierProductDTO extends ComponentDTO implements IntegrityReferentialDTO {

    public static final String KEY_SUPPLIERPRODUCTID = "productSupplierId";

    /**
     * Creates an instance.
     */
    public SupplierProductDTO() {
    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public SupplierProductDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_SUPPLIERPRODUCTID;
    }

    public String getJNDIName() {
        return ProductConstants.JNDI_PRODUCTSUPPLIER;
    }

    public void addCreatedMsgTo(ResultDTO resultDTO) {
    }

    public void addReadMsgTo(ResultDTO resultDTO) {
    }

    public void addUpdatedMsgTo(ResultDTO resultDTO) {
    }

    public void addDeletedMsgTo(ResultDTO resultDTO) {
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        // when a customer was deleted by other user
        if (resultDTO.get("supplierName") != null) {
            resultDTO.addResultMessage("customMsg.NotFound", resultDTO.get("supplierName"));
        } else {
            resultDTO.addResultMessage("customMsg.NotFound", resultDTO.get("productName"));
        }
    }

    public ComponentDTO createDTO() {
        return new SupplierProductDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        return tables;
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", resultDTO.get("supplierName"));
    }

}