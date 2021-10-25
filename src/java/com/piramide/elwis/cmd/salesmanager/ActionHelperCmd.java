package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.domain.salesmanager.ActionPosition;
import com.piramide.elwis.domain.salesmanager.ActionPositionHome;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class ActionHelperCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ActionHelperCmd.class);

    @Override
    public void executeInStateless(SessionContext sessionContext) {
        if ("getActionPositionProductsWithoutVat".equals(getOp())) {
            Integer contactId = EJBCommandUtil.i.getValueAsInteger(this, "contactId");
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            getActionPositionProductsWithoutVat(contactId, processId);
        }

    }


    /**
     * Return all actionposition products when vatid is null
     *
     * @param contactId action contactId
     * @param processId sales processId
     */
    private void getActionPositionProductsWithoutVat(Integer contactId, Integer processId) {
        ActionPositionHome actionPositionHome =
                (ActionPositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONPOSITION);

        List actionPositions = new ArrayList();
        try {
            actionPositions = (List) actionPositionHome.findByProcessAndContactId(processId, contactId);
        } catch (FinderException e) {
            log.debug("-> No exists ActionPositions registred for contactId=" + contactId + ", processId=" + processId);
        }

        List<ProductDTO> badProducts = new ArrayList<ProductDTO>();
        for (int i = 0; i < actionPositions.size(); i++) {
            ActionPosition actionPosition = (ActionPosition) actionPositions.get(i);
            Product product = getProduct(actionPosition.getProductId());
            if (null == product) {
                continue;
            }

            if (null != product.getVatId()) {
                continue;
            }

            ProductDTO productDTO = new ProductDTO();
            DTOFactory.i.copyToDTO(product, productDTO);
            badProducts.add(productDTO);
        }

        resultDTO.put("getActionPositionProductsWithoutVat", badProducts);
    }

    private Product getProduct(Integer productId) {
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        try {
            return productHome.findByPrimaryKey(productId);
        } catch (FinderException e) {
            log.debug("-> Read Product with productId=" + productId + " FAIL");
        }

        return null;
    }

    public boolean isStateful() {
        return false;
    }
}
