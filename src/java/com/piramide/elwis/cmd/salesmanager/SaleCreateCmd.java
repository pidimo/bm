package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleCreateCmd extends SaleCmd {
    private Log log = LogFactory.getLog(SaleCreateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        SaleDTO saleDTO = getSaleDTO();
        String text = (String) paramDTO.get("text");

        create(saleDTO, text, ctx);
    }

    private void create(SaleDTO saleDTO, String text, SessionContext ctx) {
        Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");

        Customer customer = getCustomer(addressId);

        if (null == customer) {
            log.error("-> Unable to find or create customer addressId=" + addressId + " Create Sale FAIL");
            ctx.setRollbackOnly();
            return;
        }

        saleDTO.put("customerId", customer.getCustomerId());

        Integer freetextId = createSaleFreeText(text, (Integer) saleDTO.get("companyId"), FreeTextTypes.FREETEXT_SALES);
        saleDTO.put("freetextId", freetextId);

        Sale sale = (Sale) ExtendedCRUDDirector.i.create(saleDTO, resultDTO, false);

        //create salepositions when sale has created from action
        if (null != sale.getProcessId() && null != sale.getContactId()) {
            createSalePositions(sale.getProcessId(), sale.getContactId(), sale, ctx);
        }
    }

    private void createSalePositions(Integer processId,
                                     Integer contactId,
                                     Sale sale,
                                     SessionContext ctx) {
        ActionHome actionHome =
                (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);

        ActionPK actionPK = new ActionPK();
        actionPK.contactId = contactId;
        actionPK.processId = processId;

        try {
            Action action = actionHome.findByPrimaryKey(actionPK);
            Collection actionPositions = action.getActionPositions();
            SalePositionCreateCmd salePositionCreateCmd;
            if (null != actionPositions) {
                for (Object object : actionPositions) {
                    ActionPosition actionPosition = (ActionPosition) object;
                    salePositionCreateCmd = new SalePositionCreateCmd();
                    salePositionCreateCmd.putParam("saleId", sale.getSaleId());
                    salePositionCreateCmd.putParam("companyId", actionPosition.getCompanyId());
                    salePositionCreateCmd.putParam("productId", actionPosition.getProductId());
                    salePositionCreateCmd.putParam("totalPrice", actionPosition.getTotalPrice());
                    salePositionCreateCmd.putParam("quantity", actionPosition.getAmount());
                    salePositionCreateCmd.putParam("unitId", actionPosition.getUnit());
                    salePositionCreateCmd.putParam("unitPrice", actionPosition.getPrice());
                    salePositionCreateCmd.putParam("unitPriceGross", actionPosition.getUnitPriceGross());
                    salePositionCreateCmd.putParam("active", true);
                    salePositionCreateCmd.putParam("deliveryDate", sale.getSaleDate());
                    salePositionCreateCmd.putParam("discount", actionPosition.getDiscount());
                    salePositionCreateCmd.putParam("payMethod", SalesConstants.PayMethod.SingleWithoutContract.getConstant());
                    if (null != actionPosition.getDescriptionId()) {
                        salePositionCreateCmd.putParam("text", new String(actionPosition.getDescriptionText().getValue()));
                    }

                    //by default the vatid in saleposition is the same vatid of actionposition product
                    ProductDTO productDTO = getProduct(actionPosition.getProductId());
                    salePositionCreateCmd.putParam("vatId", productDTO.get("vatId"));

                    //customerId and contactPersonId are added in salePositionCreateCmd
                    salePositionCreateCmd.executeInStateless(ctx);
                }
            }
        } catch (FinderException e) {
            log.debug("-> Cannot create SalePositions because cannot find Action [processId=" +
                    processId + ", contactId=" + contactId + "] ");
        }
    }

    public boolean isStateful() {
        return false;
    }
}
