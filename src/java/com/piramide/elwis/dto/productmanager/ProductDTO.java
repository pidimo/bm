package com.piramide.elwis.dto.productmanager;

import com.piramide.elwis.dto.common.IntegrityReferentialDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SalesConstants;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTO;
import net.java.dev.strutsejb.dto.ResultDTO;

import java.util.HashMap;

/**
 * Represents Product DTO.
 *
 * @author Fernando Montaño
 * @version $Id: ProductDTO.java 9898 2009-11-27 15:24:04Z fernando $
 */
public class ProductDTO extends ComponentDTO implements IntegrityReferentialDTO {
    public static final String PRODUCT_PRIMARY_KEY = "productId";

    public ProductDTO() {

    }

    public ProductDTO(DTO dto) {
        super.putAll(dto);
    }

    public ProductDTO(Integer productId) {
        put(PRODUCT_PRIMARY_KEY, productId);
    }

    public String getPrimKeyName() {
        return PRODUCT_PRIMARY_KEY;
    }

    public String getJNDIName() {
        return ProductConstants.JNDI_PRODUCT;
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
        if (get("productName") != null) {
            resultDTO.addResultMessage("customMsg.NotFound", get("productName"));
        } else {
            resultDTO.addResultMessage("Product.NotFound");
        }
    }

    public void addReferencedMsgTo(ResultDTO resultDTO) {
        resultDTO.addResultMessage("customMsg.Referenced", get("productName"));
    }

    public ComponentDTO createDTO() {
        return new ProductDTO();
    }

    public void parseValues() {
        super.convertPrimKeyToInteger();
    }

    public HashMap referencedValues() {
        HashMap tables = new HashMap();
        tables.put(ProductConstants.TABLE_PRODUCTSUPPLIER, "productid");

        tables.put(SupportConstants.TABLE_ARTICLEQUESTION, "productid");
        tables.put(SupportConstants.TABLE_ARTICLE, "productid");
        tables.put(SupportConstants.TABLE_SUPPORT_CASE, "productid");
        tables.put(SupportConstants.TABLE_SUPPORT_USER, "productid");

        tables.put(FinanceConstants.TABLE_INVOICEPOSITION, "productid");
        tables.put(SalesConstants.TABLE_ACTIONPOSITION, "productid");
        tables.put(SalesConstants.TABLE_SALEPOSITION, "productid");
        return tables;
    }
}
