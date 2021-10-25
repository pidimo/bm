package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.catalogmanager.TranslateCmd;
import com.piramide.elwis.domain.productmanager.CompetitorProduct;
import com.piramide.elwis.domain.productmanager.Pricing;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductPicture;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Execute product delete bussines logic.
 *
 * @author Fernando Montaño
 * @version $Id: ProductDeleteCmd.java 9120 2009-04-17 00:27:45Z fernando $
 */

public class ProductDeleteCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing delete command");

        ProductDTO productDTO = new ProductDTO(paramDTO);
        IntegrityReferentialChecker.i.check(productDTO, resultDTO);
        if (resultDTO.isFailure()) { //is referenced
            return;
        }

        Product product = null;
        try {
            product = (Product) EJBFactory.i.findEJB(productDTO);

            try {
                //remove pricing
                Iterator pricings = product.getPricingList().iterator();
                log.debug("remove pricing ... " + product.getPricingList().size());
                while (pricings.hasNext()) {
                    Pricing pricing = (Pricing) pricings.next();
                    product.getPricingList().remove(pricing);
                    pricing.remove();
                    pricings = product.getPricingList().iterator();
                }

                //remove product cmpetitor
                log.debug("remove competitor ... " + product.getCompetitorProduct().size());
                Collection competitors = product.getCompetitorProduct();
                for (Iterator iterator = competitors.iterator(); iterator.hasNext();) {
                    ((CompetitorProduct) iterator.next()).remove();
                    iterator = competitors.iterator();
                }
                //remove picture asociated with this product
                log.debug("remove picture ... " + product.getProductPictureList().size());
                Iterator pictures = product.getProductPictureList().iterator();
                while (pictures.hasNext()) {
                    ProductPicture picture = (ProductPicture) pictures.next();
                    product.getProductPictureList().remove(picture);
                    picture.remove();
                    pictures = product.getProductPictureList().iterator();
                }

                //remove categoryfieldValues from product
                String finderName = "findByProductId";
                Object[] params = new Object[]{product.getProductId(), product.getCompanyId()};
                List paramsAsList = Arrays.asList(params);

                CategoryUtilCmd cmd = new CategoryUtilCmd();
                cmd.putParam("finderName", finderName);
                cmd.putParam("params", paramsAsList);
                cmd.setOp("deleteValues");
                cmd.executeInStateless(ctx);

                //delete productText objects
                ProductTextCmd productTextCmd = new ProductTextCmd();
                productTextCmd.putParam("productId", product.getProductId());
                productTextCmd.putParam("companyId", product.getCompanyId());
                productTextCmd.setOp("delete");
                productTextCmd.executeInStateless(ctx);

                log.debug("remove this product ... " + product);
                Integer langTextId = product.getLangTextId();
                product.remove();  //remove below the additional not CMR relationships

                TranslateCmd translateCmd = new TranslateCmd();
                translateCmd.setOp("deleteTranslations");
                translateCmd.putParam("langTextId", langTextId);
                translateCmd.executeInStateless(ctx);
                log.debug("success");
            } catch (RemoveException e) {
                ctx.setRollbackOnly();
                log.error("Error removing product", e);
                productDTO.addNotFoundMsgTo(resultDTO);
                resultDTO.setForward("Fail");
            }
        } catch (EJBFactoryException e) {
            log.debug("Product to delete cannot be found...");
            // if not found has been deleted by other user
            ctx.setRollbackOnly();//invalid the transaction
            productDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
