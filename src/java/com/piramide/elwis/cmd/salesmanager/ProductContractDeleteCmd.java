package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.domain.salesmanager.ProductContractHome;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author : ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class ProductContractDeleteCmd extends ProductContractCmd {
    private Log log = LogFactory.getLog(ProductContractDeleteCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isDelete = true;
        if ("deleteProductContractFromSalePosition".equals(getOp())) {
            isDelete = false;
            Integer salePositionId = EJBCommandUtil.i.getValueAsInteger(this, "salePositionId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            deleteProductContractFromSalePosition(salePositionId, companyId, ctx);
        }

        if (isDelete) {
            ProductContractDTO productContracDTO = getProductContractDTO();
            delete(productContracDTO);
        }
    }

    private void deleteProductContractFromSalePosition(Integer salePositionId,
                                                       Integer companyId,
                                                       SessionContext ctx) {
        ProductContractHome productContractHome =
                (ProductContractHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_PRODUCTCONTRACT);
        try {
            Collection productContracts = productContractHome.findBySalePositionId(salePositionId, companyId);
            for (Object object : productContracts) {
                ProductContract productContract = (ProductContract) object;

                if (SalesConstants.PayMethod.PartialFixed.equal(productContract.getPayMethod())) {
                    try {
                        removePaymentSteps(productContract.getContractId(), productContract.getCompanyId());
                    } catch (RemoveException e) {
                        log.error("-> Execute removePaymentSteps[contractId=" +
                                productContract.getContractId() +
                                ", companyId" + productContract.getCompanyId() + "] Fail", e);

                        ctx.setRollbackOnly();
                        resultDTO.setResultAsFailure();
                        return;
                    }
                }

                try {
                    productContract.remove();
                } catch (RemoveException e) {
                    log.error("-> Remove ProductContract Fail", e);
                    resultDTO.setResultAsFailure();
                    ctx.setRollbackOnly();
                }
            }
        } catch (FinderException e) {
            log.debug("-> Execute ProductContractHome.findBySalePositionId [salePositionId=" +
                    salePositionId + ", companyId=" + companyId + "] FAIL");
        }
    }

    private void delete(ProductContractDTO productContractDTO) {
        ProductContract productContract =
                (ProductContract) ExtendedCRUDDirector.i.read(productContractDTO, resultDTO, true);

        //productContract was deleted by another user
        if (null == productContract) {
            resultDTO.setForward("Fail");
            return;
        }

        if (SalesConstants.PayMethod.PartialFixed.equal(productContract.getPayMethod())) {
            try {
                removePaymentSteps(productContract.getContractId(), productContract.getCompanyId());
            } catch (RemoveException e) {
                log.debug("-> Execute removePaymentSteps[contractId=" +
                        productContract.getContractId() +
                        ", companyId" + productContract.getCompanyId() + "] Fail", e);
            }
        }

        ExtendedCRUDDirector.i.delete(productContractDTO, resultDTO, true, "Fail");
    }

    public boolean isStateful() {
        return false;
    }
}
