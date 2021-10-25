package com.piramide.elwis.cmd.contactmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.productmanager.ProductSupplierPK;
import com.piramide.elwis.dto.contactmanager.SupplierProductDTO;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: SupplierProductReadCmd.java 7936 2007-10-27 16:08:39Z fernando $
 */
public class SupplierProductReadCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        log.debug("Executing read  supplierproduct command productid ");
        log.debug("Operation = " + getOp());
        ProductSupplierPK pk = new ProductSupplierPK(new Integer(paramDTO.getAsInt("supplierId")),
                new Integer(paramDTO.getAsInt("productId")));
        SupplierProductDTO dto = new SupplierProductDTO(paramDTO);
        dto.setPrimKey(pk);
        resultDTO.put("productName", paramDTO.get("productName"));
        resultDTO.put("supplierName", paramDTO.get("supplierName"));
        ExtendedCRUDDirector.i.read(dto, resultDTO, true);
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        try {
            Product product = productHome.findByPrimaryKey(new Integer(paramDTO.get("productId").toString()));
            resultDTO.put("productName", product.getProductName());

        } catch (FinderException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public boolean isStateful() {
        return false;
    }
}
