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
 * @author Fernando Montaño?
 * @version $Id: ProductSupplierDTO.java 9122 2009-04-17 00:31:07Z fernando $
 */
public class ProductSupplierDTO extends ComponentDTO {
    public static final String KEY_PRODUCTSUPPLIERID = "productSupplierId";
    public static final String KEY_PRODUCTSUPPLIERLIST = "supplierList";
    public static final String KEY_SEARCHSUPPLIERLIST = "searchSupplierList";
    private Log log = LogFactory.getLog(this.getClass());


    /**
     * Creates an instance.
     */
    public ProductSupplierDTO() {

    }

    /**
     * Creates an instance with values copied from specified DTO.
     */
    public ProductSupplierDTO(DTO dto) {
        super.putAll(dto);
    }

    public String getPrimKeyName() {
        return KEY_PRODUCTSUPPLIERID;
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

    public void addDuplicatedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("msg.Duplicated", get("supplierName"));
    }

    public void addNotFoundMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.NotFound", get("supplierName"));
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
    }


    public ComponentDTO createDTO() {
        return new ProductSupplierDTO();
    }


    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

}
