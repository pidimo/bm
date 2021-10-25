package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.salesmanager.Sale;
import com.piramide.elwis.domain.salesmanager.SaleHome;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleUpdateCmd extends SaleCmd {
    private Log log = LogFactory.getLog(SaleUpdateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        SaleDTO saleDTO = getSaleDTO();
        String text = (String) paramDTO.get("text");

        update(saleDTO, text, ctx);
    }

    private void update(SaleDTO saleDTO, String text, SessionContext ctx) {
        //get before update the old contact person id
        Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
        Integer oldContactPersonId = getOldContactPersonId(saleId);

        Sale sale = (Sale) ExtendedCRUDDirector.i.update(saleDTO, resultDTO, false, true, true, "Fail");

        //Sale was deleted by another user
        if (null == sale) {
            return;
        }

        //version error
        if (resultDTO.isFailure()) {
            SaleReadCmd saleReadCmd = new SaleReadCmd();
            saleReadCmd.putParam("saleId", sale.getSaleId());
            saleReadCmd.putParam("title", sale.getTitle());
            saleReadCmd.executeInStateless(ctx);
            resultDTO.putAll(saleReadCmd.getResultDTO());
            return;
        }

        Integer addressId = EJBCommandUtil.i.getValueAsInteger(this, "addressId");
        if (!sale.getCustomerId().equals(addressId) || contactPersonIsChanged(oldContactPersonId, sale)) {
            Customer customer = getCustomer(addressId);
            if (null == customer) {
                log.error("-> Update Sale FAIL, Unable to find or create customer [addressId=" + addressId + "]");
                ctx.setRollbackOnly();
                return;
            }
            //update customerId
            sale.setCustomerId(customer.getCustomerId());

            //update customer and contacPerson
            updateCustomerAndContacPersonInSalePositions(sale.getSaleId(),
                    sale.getCustomerId(),
                    sale.getContactPersonId(),
                    sale.getCompanyId(), ctx);
        }

        //update saleFreeText
        if (null == sale.getFreetextId()) {
            Integer freetextId = createSaleFreeText(text,
                    (Integer) saleDTO.get("companyId"),
                    FreeTextTypes.FREETEXT_SALES);
            sale.setFreetextId(freetextId);
        } else if (null != text) {
            sale.getSalesFreeText().setValue(text.getBytes());
        }
    }

    private void updateCustomerAndContacPersonInSalePositions(Integer saleId,
                                                              Integer customerId,
                                                              Integer contactPersonId,
                                                              Integer companyId,
                                                              SessionContext ctx) {
        SalePositionUpdateCmd salePositionUpdateCmd = new SalePositionUpdateCmd();
        salePositionUpdateCmd.setOp("updateCustomerAndContactPerson");
        salePositionUpdateCmd.putParam("saleId", saleId);
        salePositionUpdateCmd.putParam("customerId", customerId);
        salePositionUpdateCmd.putParam("contactPersonId", contactPersonId);
        salePositionUpdateCmd.putParam("companyId", companyId);
        salePositionUpdateCmd.executeInStateless(ctx);
    }

    private Integer getOldContactPersonId(Integer saleId) {
        Integer oldContactPersonId = null;
        SaleHome saleHome = (SaleHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALE);

        if (saleId != null) {
            try {
                Sale sale = saleHome.findByPrimaryKey(saleId);
                oldContactPersonId = sale.getContactPersonId();
            } catch (FinderException e) {
                log.debug("Error in find sale... " + saleId);
            }
        }
        return oldContactPersonId;
    }

    /**
     * Verify change of contact person. Only for SALE MODULE
     * @param oldContactPersonId
     * @param updatedSale
     * @return
     */
    private boolean contactPersonIsChanged(Integer oldContactPersonId, Sale updatedSale) {
        boolean isChanged = false;
        if (!"true".equals(paramDTO.get("isSaleFromContact"))) {
            if ((updatedSale.getContactPersonId() != null && !updatedSale.getContactPersonId().equals(oldContactPersonId)) ||
                    (updatedSale.getContactPersonId() == null && oldContactPersonId != null)) {
                isChanged = true;
            }
        }
        return isChanged;
    }

    public boolean isStateful() {
        return false;
    }
}
