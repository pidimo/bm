package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.PricingHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductFreeText;
import com.piramide.elwis.domain.productmanager.ProductFreeTextHome;
import com.piramide.elwis.dto.productmanager.PricingDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Execute product creation business logic.
 *
 * @author Fernando Montaño
 * @version $Id: ProductCreateCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */
public class ProductCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing create command");
        log.debug("Operation = " + getOp());


        ProductDTO productDTO = new ProductDTO(paramDTO);
        //Create the product bean
        Product product = (Product) ExtendedCRUDDirector.i.create(productDTO, resultDTO, false);
        log.debug("After crud create");
        if (product != null) {
            if (productDTO.get("description") != null) {
                try {
                    ProductFreeTextHome freeTextHome = (ProductFreeTextHome)
                            EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT_FREETEXT);
                    ProductFreeText descriptionFreeText = freeTextHome.create(productDTO.getAsString("description").getBytes(),
                            new Integer((String) productDTO.get("companyId")), new Integer(FreeTextTypes.FREETEXT_PRODUCT));
                    product.setDescriptionText(descriptionFreeText);
                    resultDTO.put("description", new String(product.getDescriptionText().getValue()));

                } catch (CreateException e) {
                    log.error("Error creating the description", e);
                }
            }

            //if pricing is setting up
            if (productDTO.get("price") != null && !"".equals(productDTO.get("price"))) {
                PricingDTO pricingDTO = new PricingDTO();
                pricingDTO.put("productId", product.getProductId());
                pricingDTO.put("quantity", new Integer(1));
                pricingDTO.put("companyId", product.getCompanyId());
                pricingDTO.put("price", product.getPrice());
                PricingHome pricingHome = (PricingHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRICING);
                try {
                    pricingHome.create(pricingDTO);
                } catch (CreateException e) {
                    log.error("Cannot create Pricing for this product", e);
                }
            }

            List<Map> sourceValues = new ArrayList<Map>();
            Map productMap = new HashMap();
            productMap.put("identifier", "productId");
            productMap.put("value", product.getProductId());
            sourceValues.add(productMap);
            CategoryUtilCmd cmd = new CategoryUtilCmd();
            cmd.putParam("sourceValues", sourceValues);
            cmd.putParam(paramDTO);
            cmd.putParam("companyId", product.getCompanyId());
            cmd.setOp("createValues");
            cmd.executeInStateless(ctx);

        }


    }

    public boolean isStateful() {
        return false;
    }
}
