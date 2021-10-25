package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.catalogmanager.TranslateCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.*;
import com.piramide.elwis.dto.productmanager.PricingDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Execute product update business logic.
 *
 * @author Fernando Montaño
 * @version $Id: ProductUpdateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class ProductUpdateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing update product command...");
        log.debug("Operation = " + getOp());

        ProductDTO productDTO = new ProductDTO(paramDTO);
        //deleting from update mode
        if (paramDTO.get("delete") != null) {
            log.debug("Deleting product");
            ProductDeleteCmd deleteCmd = new ProductDeleteCmd();
            deleteCmd.putParam(productDTO);
            deleteCmd.executeInStateless(ctx); // executing product delete
            ResultDTO resultDTO1 = deleteCmd.getResultDTO();
            if (resultDTO1.isFailure()) { //if is referenced
                resultDTO.putAll(resultDTO1);
                if (resultDTO1.hasResultMessage()) {
                    if (resultDTO1.isFailure()) {
                        for (Iterator iterator = resultDTO1.getResultMessages(); iterator.hasNext();) {
                            ResultMessage message = (ResultMessage) iterator.next();
                            resultDTO.addResultMessage(message);
                        }
                        resultDTO.setResultAsFailure();
                    }
                }
                resultDTO.setResultAsFailure();
            } else if (resultDTO1.getForward() != null && "Fail".equals(resultDTO1.getForward())) { //if it was deleted by other user
                productDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            } else {
                resultDTO.setForward("SuccessDelete");
            }
        } else { //updating
            //Update the product
            Product product = (Product) ExtendedCRUDDirector.i.update(productDTO, resultDTO, false, true, false, "Fail");


            if (resultDTO.isFailure() && product != null) { //control version failed, then read again additionaly data

                ProductReadCmd productReadCmd = new ProductReadCmd();
                productReadCmd.putParam(paramDTO);
                productReadCmd.executeInStateless(ctx);
                resultDTO.putAll(productReadCmd.getResultDTO());

            } else if (product != null) { //ready to update additionaly data
                if (product.getDescriptionText() != null) {
                    product.getDescriptionText().setValue(productDTO.getAsString("description").getBytes());
                    resultDTO.put("description", new String(product.getDescriptionText().getValue()));

                } else { //does not have freetext, then create it.
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
                //updating default base price in pricing
                PricingPK pricingPK = new PricingPK(product.getProductId(), new Integer(1));
                PricingHome pricingHome = (PricingHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRICING);
                try {
                    Pricing pricing = pricingHome.findByPrimaryKey(pricingPK);
                    pricing.setPrice(product.getPrice());
                } catch (FinderException e) {
                    if (product.getPrice() != null) {
                        PricingDTO pricingDTO = new PricingDTO();
                        pricingDTO.put("productId", product.getProductId());
                        pricingDTO.put("quantity", new Integer(1));
                        pricingDTO.put("companyId", product.getCompanyId());
                        pricingDTO.put("price", product.getPrice());
                        try {
                            pricingHome.create(pricingDTO);
                        } catch (CreateException createException) {
                            log.error("Cannot create Pricing for this product");
                        }
                    }
                }


                //synchronize productName to default LangText
                TranslateCmd translateCmd = new TranslateCmd();
                translateCmd.putParam(paramDTO);
                translateCmd.putParam("dtoClassName", ProductDTO.class.getName());
                translateCmd.putParam("synchronizedFieldName", "productName");
                translateCmd.setOp("synchronizedDefaultTranslation");
                translateCmd.executeInStateless(ctx);

                String finderName = "findByProductId";
                Object[] params = new Object[]{product.getProductId(), product.getCompanyId()};
                List paramsAsList = Arrays.asList(params);

                List<Map> sourceValues = new ArrayList<Map>();
                Map productMap = new HashMap();
                productMap.put("identifier", "productId");
                productMap.put("value", product.getProductId());
                sourceValues.add(productMap);

                CategoryUtilCmd cmd = new CategoryUtilCmd();
                cmd.putParam("sourceValues", sourceValues);
                cmd.putParam("companyId", product.getCompanyId());
                cmd.putParam(paramDTO);
                cmd.putParam("finderName", finderName);
                cmd.putParam("params", paramsAsList);
                cmd.setOp("updateValues");

                cmd.executeInStateless(ctx);
                resultDTO.put("productId", product.getProductId());


            }
        }

    }

    public boolean isStateful() {
        return false;
    }
}
