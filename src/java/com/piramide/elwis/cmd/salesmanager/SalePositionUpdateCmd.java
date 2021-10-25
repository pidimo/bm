package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.util.SaleUtil;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.domain.salesmanager.SalePositionHome;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
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
public class SalePositionUpdateCmd extends SalePositionCmd {
    private Log log = LogFactory.getLog(SalePositionUpdateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isUpdate = true;
        if ("updateCustomerAndContactPerson".equals(getOp())) {
            isUpdate = false;
            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            Integer customerId = EJBCommandUtil.i.getValueAsInteger(this, "customerId");
            Integer contactPersonId = EJBCommandUtil.i.getValueAsInteger(this, "contactPersonId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            updateCustomerAndContactPerson(saleId, customerId, contactPersonId, companyId);
        }

        if (isUpdate) {
            SalePositionDTO salePositionDTO = getSalePositionDTO();
            String text = (String) paramDTO.get("text");
            update(salePositionDTO, text, ctx);
        }
    }

    private void updateCustomerAndContactPerson(Integer saleId,
                                                Integer customerId,
                                                Integer contactPersonId,
                                                Integer companyId) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);

        try {
            Collection salePositions = salePositionHome.findBySaleId(saleId, companyId);
            if (null != salePositions) {
                for (Object object : salePositions) {
                    SalePosition salePosition = (SalePosition) object;
                    salePosition.setCustomerId(customerId);
                    salePosition.setContactPersonId(contactPersonId);
                }
            }

            log.debug("-> Update customerId and contactPersonId for Sale [saleId=" + saleId + "] OK");
        } catch (FinderException e) {
            log.debug("-> Read SalePositions [saleId=" + saleId + ", companyId=" + companyId + "] FAIL");
        }
    }

    protected void update(SalePositionDTO salePositionDTO, String text, SessionContext ctx) {

        if (!canChangeProduct(salePositionDTO, ctx)) {
            SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
            salePositionReadCmd.putParam("salePositionId", salePositionDTO.get("salePositionId"));
            salePositionReadCmd.executeInStateless(ctx);
            resultDTO.addResultMessage("SalePosition.productChange.error");
            resultDTO.putAll(salePositionReadCmd.getResultDTO());
            resultDTO.setResultAsFailure();
            return;
        }

        SalePosition salePosition =
                (SalePosition) ExtendedCRUDDirector.i.update(salePositionDTO, resultDTO, false, true, true, "Fail");

        //SalePosition was deleted by another user
        if (null == salePosition) {
            return;
        }

        //version error
        if (resultDTO.isFailure()) {
            SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
            salePositionReadCmd.putParam("salePositionId", salePosition.getSalePositionId());
            salePositionReadCmd.executeInStateless(ctx);
            resultDTO.putAll(salePositionReadCmd.getResultDTO());
            return;
        }


        if (null != salePosition.getSaleId()) {
            SaleDTO saleDTO = getSaleDTO(salePosition.getSaleId());
            Integer customerId = (Integer) saleDTO.get("customerId");
            Integer contactPersonId = (Integer) saleDTO.get("contactPersonId");
            salePosition.setCustomerId(customerId);
            salePosition.setContactPersonId(contactPersonId);
        } else {
            Customer customer = getCustomer((Integer) salePositionDTO.get("customerId"));
            salePosition.setCustomerId(customer.getCustomerId());
        }

        //update saleFreeText
        if (null == salePosition.getFreetextId()) {
            Integer freetextId = createSaleFreeText(text,
                    salePosition.getCompanyId(),
                    FreeTextTypes.FREETEXT_SALEPOSITION);
            salePosition.setFreetextId(freetextId);
        } else if (null != text) {
            salePosition.getSalesFreeText().setValue(text.getBytes());
        }

        //update category values
        updateSalePositionCategoryFieldValues(ctx);

        //update total price and total gross price
        SaleUtil.i.calculateSalePositionTotalPrices(salePosition);

        //delete productContracts only when payMethod = SingleWithoutContract and his productContracts does not have payments.
        if (null != salePosition.getPayMethod() &&
                SalesConstants.PayMethod.SingleWithoutContract.equal(salePosition.getPayMethod()) &&
                canDeleteSaleposition(salePosition)) {
            deleteProductContracts(salePosition.getSalePositionId(), salePosition.getCompanyId(), ctx);
        }

        //put in resultDTO payMethod explicitly, because is used to define action forward logic.
        resultDTO.put("payMethod", salePosition.getPayMethod());
    }

    public boolean isStateful() {
        return false;
    }

    protected boolean canChangeProduct(SalePositionDTO salePositionDTO, SessionContext ctx) {
        Integer salePositionId = (Integer) salePositionDTO.get("salePositionId");
        Integer companyId = (Integer) salePositionDTO.get("companyId");

        SalePositionDTO oldSalePositionDTO = getSalePositionDTO(salePositionId, ctx);
        if (null == oldSalePositionDTO) {
            return true;
        }

        Integer productId = (Integer) salePositionDTO.get("productId");
        Integer oldProductId = (Integer) oldSalePositionDTO.get("productId");

        return !(!oldProductId.equals(productId) && salePositionHasInvoiced(salePositionId, companyId));
    }

    protected void updateSalePositionCategoryFieldValues(SessionContext ctx) {
        SalePositionCategoryManagerCmd cmd = new SalePositionCategoryManagerCmd();
        cmd.setOp("update");
        cmd.putParam(paramDTO);
        cmd.executeInStateless(ctx);

        ResultDTO myResultDTO = cmd.getResultDTO();
        resultDTO.putAll(myResultDTO);
    }

}
