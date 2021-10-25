package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.ProductUnit;
import com.piramide.elwis.domain.catalogmanager.ProductUnitHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.dto.productmanager.PricingDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Execute product creation business logic.
 *
 * @author Fernando Montaño
 * @version $Id: PricingCreateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class PricingCreateCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing create command");
        log.debug("Operation = " + getOp());

        ProductUnitHome unitHome = (ProductUnitHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRODUCTUNIT);
        try {
            Product product = (Product) EJBFactory.i.findEJB(new ProductDTO(paramDTO));
            ProductUnit unit = null;
            if (product.getUnitId() != null) {
                unit = (ProductUnit) unitHome.findByPrimaryKey(product.getUnitId());
            }
            if (unit != null) {
                resultDTO.put("productId", product.getProductId());
                resultDTO.put("unitName", unit.getUnitName());
            }

        } catch (EJBFactoryException e) {
            //product mot found
        } catch (FinderException e) {
            //unit not found
        }


        PricingDTO pricingDTO = new PricingDTO(paramDTO);
        log.debug("DTO created");
        //Create the product
        if ("create".equals(getOp())) {
            log.debug("calling CRUD");
            ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_CREATE,
                    pricingDTO, resultDTO, false, false, false, false);

        }


    }

    public boolean isStateful() {
        return false;
    }
}
