package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.domain.financemanager.InvoicePosition;
import com.piramide.elwis.domain.financemanager.InvoicePositionHome;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.List;

/**
 * @author Ivan Alban
 * @version 4.2.2
 */
public abstract class SalePositionCmd extends SaleManagerCmd {
    private static Log log = LogFactory.getLog(SalePositionCmd.class);

    /**
     * Build SalePositionDTO Object, however the total prices does'n setting up in the object
     * because this always are recalculate.
     *
     * @return
     */
    protected SalePositionDTO getSalePositionDTO() {
        SalePositionDTO salePositionDTO = new SalePositionDTO();

        salePositionDTO.put("active", EJBCommandUtil.i.getValueAsBoolean(this, "active"));
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "companyId");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "contactPersonId");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "customerId");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "deliveryDate");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "productId");

        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "saleId");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "salePositionId");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "payMethod");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "unitId");
        EJBCommandUtil.i.setValueAsInteger(this, salePositionDTO, "vatId");

        salePositionDTO.put("discount", paramDTO.get("discount"));
        salePositionDTO.put("serial", paramDTO.get("serial"));

        EJBCommandUtil.i.setValueAsBigDecimal(this, salePositionDTO, "quantity");
        EJBCommandUtil.i.setValueAsBigDecimal(this, salePositionDTO, "unitPrice");
        EJBCommandUtil.i.setValueAsBigDecimal(this, salePositionDTO, "unitPriceGross");

        salePositionDTO.put("version", paramDTO.get("version"));
        salePositionDTO.put("versionNumber", paramDTO.get("versionNumber"));

        //use addNotFoundMsgTo(ResultDTO resultDTO) in SalePositionDTO 
        salePositionDTO.put("productName", paramDTO.get("productName"));

        //use addNotFoundMsgTo(ResultDTO resultDTO) in SalePositionDTO
        salePositionDTO.put("customerName", paramDTO.get("customerName"));

        //use in CRUD utility
        salePositionDTO.put("withReferences", paramDTO.get("withReferences"));
        return salePositionDTO;
    }

    protected void deleteProductContracts(Integer salePositionId, Integer companyId, SessionContext ctx) {
        ProductContractDeleteCmd productContractDeleteCmd = new ProductContractDeleteCmd();
        productContractDeleteCmd.putParam("salePositionId", salePositionId);
        productContractDeleteCmd.putParam("companyId", companyId);
        productContractDeleteCmd.setOp("deleteProductContractFromSalePosition");
        productContractDeleteCmd.executeInStateless(ctx);
    }

    /**
     * Verifies if the saleposition associated to <code>salePositionId</code> has invoiced or  if some
     * <code>ProductContract</code> associated to <code>SalePosition</code> has invoiced.
     *
     * @param salePositionId <code>Integer</code> that is the saleposition identifier.
     * @param companyId      <code>Integer</code> that is the company identifier.
     * @return true if the <code>SalePosition</code> or <code>ProductContract</code>  objects are invoiced false
     *         in other case.
     */
    protected boolean salePositionHasInvoiced(Integer salePositionId, Integer companyId) {
        InvoicePositionHome invoicePositionHome =
                (InvoicePositionHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICEPOSITION);

        try {
            InvoicePosition invoicePosition = invoicePositionHome.findBySalePositionId(salePositionId, companyId);
            if (null != invoicePosition) {
                return true;
            }
        } catch (FinderException e) {
            //The saleposition is not invoiced.
        }

        List productContracts = getProductContracts(salePositionId, companyId);
        return isProductContractRelatedWithInvoicePositions(productContracts);
    }
}
