package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.domain.catalogmanager.ProductUnit;
import com.piramide.elwis.domain.catalogmanager.ProductUnitHome;
import com.piramide.elwis.domain.productmanager.Product;
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
 * @version $Id: PricingReadCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class PricingReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing Pricing Read Command");
        log.debug("productId = " + paramDTO.get("productId"));

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

    }

    public boolean isStateful() {
        return false;
    }
}
