package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.ProductType;
import com.piramide.elwis.domain.catalogmanager.ProductTypeHome;
import com.piramide.elwis.dto.catalogmanager.ProductTypeDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class ProductTypeLightCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ProductTypeLightCmd......" + paramDTO);

        if ("findEventType".equals(getOp())) {
            findByEventType();
        }
    }

    private void findByEventType() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        ProductTypeHome productTypeHome = (ProductTypeHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PRODUCTTYPE);

        Collection collection = null;
        try {
            collection = productTypeHome.findByProductTypeType(ProductConstants.ProductTypeType.EVENT.getConstant(), companyId);
        } catch (FinderException e) {
            collection = new ArrayList();
        }

        if (!collection.isEmpty()) {
            ProductType productType = (ProductType) collection.iterator().next();

            ProductTypeDTO productTypeDTO = new ProductTypeDTO();
            DTOFactory.i.copyToDTO(productType, productTypeDTO);

            resultDTO.put("dtoProductType", productTypeDTO);
        }
    }

    public boolean isStateful() {
        return false;
    }
}
