package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.CategoryTabManagerCmd;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.EJBFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * @author Ivan Alban
 * @version 4.4
 */
public class ProductCategoryTabCmd extends CategoryTabManagerCmd {
    @Override
    protected EJBLocalObject findEJBLocalObject(SessionContext ctx) {
        Integer productId = EJBCommandUtil.i.getValueAsInteger(this, "productId");

        ProductHome productHome =
                (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);

        try {
            return productHome.findByPrimaryKey(productId);
        } catch (FinderException e) {
            return null;
        }
    }

    @Override
    protected String getValuesFinderMethodName() {
        return "findValueByProductId";
    }

    @Override
    protected String getEntityFinderMethodName() {
        return "findByProductId";
    }

    @Override
    protected Integer getEntityId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((Product) ejbLocalObject).getProductId();
    }

    @Override
    protected Integer getCompanyId(EJBLocalObject ejbLocalObject, SessionContext ctx) {
        return ((Product) ejbLocalObject).getCompanyId();
    }

    @Override
    protected String getEntityIdFieldName() {
        return "productId";
    }

    @Override
    public boolean isStateful() {
        return false;
    }
}
