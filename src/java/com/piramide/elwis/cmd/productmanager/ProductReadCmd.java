package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.Arrays;
import java.util.List;

/**
 * Execute product read operation.
 *
 * @author Fernando Montaño
 * @version $Id: ProductReadCmd.java 11758 2015-12-09 00:16:33Z miguel $
 */

public class ProductReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(ProductReadCmd.class);

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read product command ... ");
        log.debug("Operation = " + getOp());

//Read the product
        Product product = (Product) ExtendedCRUDDirector.i.read(new ProductDTO(paramDTO), resultDTO, true);

        if (product != null) {
            if (product.getDescriptionText() != null) {
                resultDTO.put("description", new String(product.getDescriptionText().getValue()));

            }
            //readProductCategories(product);

            String finderName = "findByProductId";
            Object[] params = new Object[]{product.getProductId(), product.getCompanyId()};
            List paramsAsList = Arrays.asList(params);

            CategoryUtilCmd cmd = new CategoryUtilCmd();
            cmd.putParam("finderName", finderName);
            cmd.putParam("params", paramsAsList);
            cmd.setOp("readCAtegoryFieldValues");
            cmd.executeInStateless(ctx);
            ResultDTO myResultDTO = cmd.getResultDTO();
            resultDTO.putAll(myResultDTO);


        }
    }

    public boolean isStateful() {
        return false;
    }
}
