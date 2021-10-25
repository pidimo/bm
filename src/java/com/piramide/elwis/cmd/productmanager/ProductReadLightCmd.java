package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;

/**
 * Execute product read operation.
 *
 * @author Fernando Montaño
 * @version $Id: ProductReadLightCmd.java 9630 2009-08-26 21:29:57Z ivan $
 */
public class ProductReadLightCmd extends EJBCommand {

    private Log log = LogFactory.getLog(ProductReadLightCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read product command ... ");
        log.debug("productId to read = " + paramDTO.get("productId"));
        //Read the product
        ProductDTO productDTO = new ProductDTO(paramDTO);
        Product product = null;
        try {
            product = (Product) EJBFactory.i.findEJB(productDTO);
            resultDTO.put("productName", product.getProductName());
            resultDTO.put("accountId", product.getAccountId());
            resultDTO.put("unitId", product.getUnitId());
            resultDTO.put("currentVersion", product.getCurrentVersion());
            resultDTO.put("priceGross", product.getPriceGross());
            resultDTO.put("price", product.getPrice());
            resultDTO.put("vatId", product.getVatId());
        } catch (EJBFactoryException ex) {
            productDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setResultAsFailure();
        }
    }

    public boolean isStateful() {
        return false;
    }
}
