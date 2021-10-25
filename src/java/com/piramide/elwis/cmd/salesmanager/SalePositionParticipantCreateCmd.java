package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.productmanager.Product;
import com.piramide.elwis.domain.productmanager.ProductHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Date;

/**
 * Cmd to create sale position participants related to product
 * @author Miguel A. Rojas Cardenas
 * @version 5.5.0.3
 */
public class SalePositionParticipantCreateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SalePositionParticipantCreateCmd................" + paramDTO);

        Integer productId = new Integer(paramDTO.get("productId").toString());
        Integer userId = new Integer(paramDTO.get("userId").toString());

        User user = findUser(userId);
        Product product = findProduct(productId);

        if (product != null && user != null) {

            if (user.getMobileOrganizationId() == null) {
                resultDTO.addResultMessage("User.mobile.error.organization");
                resultDTO.setForward("Fail");
                return;
            }

            SalePositionCreateCmd salePositionCreateCmd = new SalePositionCreateCmd();
            salePositionCreateCmd.putParam("productId", product.getProductId());
            salePositionCreateCmd.putParam("companyId", product.getCompanyId());
            salePositionCreateCmd.putParam("customerId", user.getMobileOrganizationId());
            salePositionCreateCmd.putParam("contactPersonId", user.getAddressId());
            salePositionCreateCmd.putParam("quantity",1);
            salePositionCreateCmd.putParam("unitPrice", product.getPrice());
            salePositionCreateCmd.putParam("unitPriceGross", product.getPriceGross());
            salePositionCreateCmd.putParam("unitId", product.getUnitId());
            salePositionCreateCmd.putParam("versionNumber", product.getCurrentVersion());
            salePositionCreateCmd.putParam("active", Boolean.TRUE);
            salePositionCreateCmd.putParam("vatId", product.getVatId());
            salePositionCreateCmd.putParam("payMethod", SalesConstants.PayMethod.SingleWithoutContract.getConstant());
            salePositionCreateCmd.putParam("deliveryDate", DateUtils.dateToInteger(new Date()));

            salePositionCreateCmd.executeInStateless(ctx);
        }
    }

    private User findUser(Integer userId) {
        User user = null;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        if (userId != null) {
            try {
                user = userHome.findByPrimaryKey(userId);
            } catch (FinderException e) {
                log.debug("Not found user.. " + userId);
            }
        }
        return user;
    }

    private Product findProduct(Integer productId) {
        Product product = null;
        ProductHome productHome = (ProductHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT);
        if (productId != null) {
            try {
                product = productHome.findByPrimaryKey(productId);
            } catch (FinderException e) {
                log.debug("Not found Product:" + productId);
            }
        }
        return product;
    }

    public boolean isStateful() {
        return false;
    }
}