package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.domain.salesmanager.SalePositionHome;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SalePositionDeleteCmd extends SalePositionCmd {
    private Log log = LogFactory.getLog(SalePositionDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isDelete = true;
        if ("deleteAllSalePositionsOfSale".equals(getOp())) {
            isDelete = false;

            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            deleteAllSalePositionsOfSale(saleId, companyId, ctx);
        }

        if (isDelete) {
            SalePositionDTO salePositionDTO = getSalePositionDTO();
            delete(salePositionDTO, ctx);
        }
    }

    private void deleteAllSalePositionsOfSale(Integer saleId, Integer companyId, SessionContext ctx) {
        SalePositionHome salePositionHome =
                (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);
        try {
            Collection salePositions = salePositionHome.findBySaleId(saleId, companyId);
            for (Object object : salePositions) {
                SalePosition salePosition = (SalePosition) object;

                //delete all product contracts associated to this saleposition
                deleteProductContracts(salePosition.getSalePositionId(), companyId, ctx);

                //delete categories
                deleteSalePositionCategoryFieldValues(salePosition, ctx);

                try {
                    salePosition.remove();
                } catch (RemoveException e) {
                    log.error("-> Delete salePosition  [salePositionId" +
                            salePosition.getSalePositionId() +
                            "] FAIL", e);
                    break;
                }
            }
        } catch (FinderException e) {
            log.debug("-> Find SalePositions [saleId=" + saleId + ", companyId=" + companyId + "] FAIL");
        }
    }

    private void delete(SalePositionDTO salePositionDTO, SessionContext ctx) {
        SalePosition salePosition =
                (SalePosition) ExtendedCRUDDirector.i.read(salePositionDTO, resultDTO, true);

        //saleposition was deleted by another user
        if (null == salePosition) {
            resultDTO.setForward("Fail");
            return;
        }

        //saleposition has assigned productcontracts with payments
        if (!canDeleteSaleposition(salePosition)) {
            resultDTO.setForward("Fail");
            resultDTO.addResultMessage("SalePosition.error.ProductContractsHasPayments");
            resultDTO.put("withReferences", "false");
            return;
        }

        //delete all product contracts associated to this saleposition
        deleteProductContracts(salePosition.getSalePositionId(), salePosition.getCompanyId(), ctx);

        //delete categories
        deleteSalePositionCategoryFieldValues(salePosition, ctx);

        //delete saleposition
        ExtendedCRUDDirector.i.delete(salePositionDTO, resultDTO, true, "Fail");
    }

    private void deleteSalePositionCategoryFieldValues(SalePosition salePosition, SessionContext ctx) {
        SalePositionCategoryManagerCmd cmd = new SalePositionCategoryManagerCmd();
        cmd.setOp("delete");
        cmd.putParam("salePositionId", salePosition.getSalePositionId());
        cmd.putParam("companyId", salePosition.getCompanyId());
        cmd.executeInStateless(ctx);
    }

    public boolean isStateful() {
        return false;
    }
}
