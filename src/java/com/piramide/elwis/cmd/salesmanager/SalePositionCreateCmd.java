package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.salesmanager.util.SaleUtil;
import com.piramide.elwis.domain.contactmanager.Customer;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.ResultDTO;

import javax.ejb.SessionContext;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SalePositionCreateCmd extends SalePositionCmd {
    public void executeInStateless(SessionContext ctx) {
        SalePositionDTO salePositionDTO = getSalePositionDTO();
        String text = (String) paramDTO.get("text");
        create(salePositionDTO, text, ctx);
    }

    protected void create(SalePositionDTO salePositionDTO, String text, SessionContext ctx) {
        Integer saleId = (Integer) salePositionDTO.get("saleId");
        if (null != saleId) {
            SaleDTO saleDTO = getSaleDTO(saleId);
            Integer customerId = (Integer) saleDTO.get("customerId");
            Integer contactPersonId = (Integer) saleDTO.get("contactPersonId");

            salePositionDTO.put("customerId", customerId);
            salePositionDTO.put("contactPersonId", contactPersonId);
        } else {
            Customer customer = getCustomer((Integer) salePositionDTO.get("customerId"));
            salePositionDTO.put("customerId", customer.getCustomerId());
        }

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

    protected void createSalePositionCategoryFieldValues(SalePosition salePosition, SessionContext ctx) {
        SalePositionCategoryManagerCmd cmd = new SalePositionCategoryManagerCmd();
        cmd.setOp("create");
        cmd.putParam("salePositionId", salePosition.getSalePositionId());
        cmd.putParam("companyId", salePosition.getCompanyId());
        cmd.putParam(paramDTO);
        cmd.executeInStateless(ctx);

        ResultDTO myResultDTO = cmd.getResultDTO();
        //resultDTO.putAll(myResultDTO);
    }

    public boolean isStateful() {
        return false;
    }
}
