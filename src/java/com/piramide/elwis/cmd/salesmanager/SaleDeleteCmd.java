package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.Sale;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;


/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleDeleteCmd extends SaleCmd {
    private Log log = LogFactory.getLog(SaleDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        SaleDTO saleDTO = getSaleDTO();

        delete(saleDTO, ctx);
    }

    public void delete(SaleDTO saleDTO, SessionContext ctx) {
        Sale sale = (Sale) ExtendedCRUDDirector.i.read(saleDTO, resultDTO, true);

        //Sale was deleted by another user
        if (null == sale) {
            resultDTO.setForward("Fail");
            return;
        }

        //any saleposition of sale has assigned payment
        if (!canDeleteSale(sale)) {
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("Sale.error.ProductContractsHasPayments");
            return;
        }
        //delete sale relations
        deleteSalePositions(sale.getSaleId(), sale.getCompanyId(), ctx);

        try {
            sale.remove();
        } catch (RemoveException e) {
            log.error("-> Delete sale [saleId=" + sale.getSaleId() + "] FAIL");
            ctx.setRollbackOnly();
        }
    }

    private void deleteSalePositions(Integer saleId, Integer companyId, SessionContext ctx) {
        SalePositionDeleteCmd salePositionDeleteCmd = new SalePositionDeleteCmd();
        salePositionDeleteCmd.setOp("deleteAllSalePositionsOfSale");
        salePositionDeleteCmd.putParam("saleId", saleId);
        salePositionDeleteCmd.putParam("companyId", companyId);
        salePositionDeleteCmd.executeInStateless(ctx);
    }

    public boolean isStateful() {
        return false;
    }
}
