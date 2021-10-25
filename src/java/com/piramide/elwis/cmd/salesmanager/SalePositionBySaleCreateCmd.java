package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.util.SaleUtil;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;

import javax.ejb.SessionContext;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionBySaleCreateCmd extends SalePositionCreateCmd {

    @Override
    protected void create(SalePositionDTO salePositionDTO, String text, SessionContext ctx) {
        SaleDTO saleDTO = getSaleDTO((Integer) salePositionDTO.get("saleId"));
        if (null == saleDTO) {
            resultDTO.addResultMessage("Sale.NotFound");
            resultDTO.setForward("SaleList");
            return;
        }

        Integer customerId = (Integer) saleDTO.get("customerId");
        salePositionDTO.put("customerId", customerId);

        Integer freetextId = createSaleFreeText(text,
                (Integer) salePositionDTO.get("companyId"),
                FreeTextTypes.FREETEXT_SALEPOSITION);

        salePositionDTO.put("freetextId", freetextId);

        SalePosition salePosition =
                (SalePosition) ExtendedCRUDDirector.i.create(salePositionDTO, resultDTO, false);

        //calculate the total price and total gross price
        SaleUtil.i.calculateSalePositionTotalPrices(salePosition);

        //use in ui to show customer name
        resultDTO.put("customerName", readAddressName(salePosition.getCustomerId(), ctx));

        //use in ui to show productName
        resultDTO.put("productName", readProductName(salePosition.getProductId(), ctx));

        //create categories
        createSalePositionCategoryFieldValues(salePosition, ctx);
    }
}
