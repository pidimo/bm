package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.util.SaleUtil;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.SalesConstants;

import javax.ejb.SessionContext;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionBySaleUpdateCmd extends SalePositionUpdateCmd {
    protected void update(SalePositionDTO salePositionDTO, String text, SessionContext ctx) {
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

        //use the customerId stored in sale
        SaleDTO saleDTO = getSaleDTO(salePosition.getSaleId());
        Integer customerId = (Integer) saleDTO.get("customerId");
        salePosition.setCustomerId(customerId);

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
}
