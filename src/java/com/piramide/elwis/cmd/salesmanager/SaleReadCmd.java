package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.Sale;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.dto.salesmanager.SaleDTO;
import com.piramide.elwis.utils.FinanceConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SaleReadCmd extends SaleCmd {
    private Log log = LogFactory.getLog(SaleReadCmd.class);

    public void executeInStateless(SessionContext ctx) {

        boolean isRead = true;
        if ("hasAssociatedSale".equals(getOp())) {
            isRead = false;
            Integer processId = EJBCommandUtil.i.getValueAsInteger(this, "processId");
            Integer companyId = (Integer) paramDTO.get("companyId");

            hasAssociatedSale(processId, companyId);
        }

        if ("hasChangeCustomer".equals(getOp())) {
            isRead = false;
            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            Integer customerId = EJBCommandUtil.i.getValueAsInteger(this, "customerId");
            hasChangeCustomer(saleId, customerId);
        }

        if ("getSaleDTO".equals(getOp())) {
            isRead = false;
            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            getSaleDTO(saleId, ctx);
        }

        if ("changeNetGross".equals(getOp())) {
            isRead = false;
            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            Integer pageNetGross = EJBCommandUtil.i.getValueAsInteger(this, "pageNetGross");
            changeNetGross(saleId, companyId, pageNetGross);
        }

        if ("checkNetGrossChange".equals(getOp())) {
            isRead = false;
            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            checkNetGrossChange(saleId, companyId);
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            SaleDTO saleDTO = getSaleDTO();
            read(saleDTO, withReferences, ctx);
        }
    }

    private void checkNetGrossChange(Integer saleId, Integer companyId) {
        Sale sale = getSale(saleId);
        if (null == sale) {
            return;
        }

        List salePositions = getSalePositions(sale.getSaleId(), companyId);
        boolean salePositionsRelatedWithInvoicePositions =
                isSalePositionRelatedWithInvoicePositions(salePositions);

        boolean salePositionsRelatedWithProductContracts = false;
        boolean productContractRelatedWithInvoicePositions = false;
        for (int i = 0; i < salePositions.size(); i++) {
            SalePosition salePosition = (SalePosition) salePositions.get(i);
            List productContracts = new ArrayList(salePosition.getProductContracts());
            if (!productContracts.isEmpty()) {
                salePositionsRelatedWithProductContracts = true;
            }

            productContractRelatedWithInvoicePositions =
                    isProductContractRelatedWithInvoicePositions(productContracts);
            if (productContractRelatedWithInvoicePositions) {
                productContractRelatedWithInvoicePositions = true;
                break;
            }
        }
        resultDTO.put("salePositionsRelatedWithInvoicePositions", salePositionsRelatedWithInvoicePositions);
        resultDTO.put("salePositionsRelatedWithProductContracts", salePositionsRelatedWithProductContracts);
        resultDTO.put("productContractRelatedWithInvoicePositions", productContractRelatedWithInvoicePositions);
    }

    private void changeNetGross(Integer saleId, Integer companyId, Integer pageNetGross) {
        Sale sale = getSale(saleId);
        if (null == sale) {
            resultDTO.put("changeNetGross", 0);
            return;
        }

        List salePositions = getSalePositions(saleId, companyId);
        if (salePositions.isEmpty()) {
            resultDTO.put("changeNetGross", 1);
            return;
        }
        resultDTO.put("changeNetGross", 1);
        for (Object object : salePositions) {
            SalePosition salePosition = (SalePosition) object;
            if (FinanceConstants.NetGrossFLag.NET.equal(pageNetGross) && null == salePosition.getUnitPrice()) {
                resultDTO.put("changeNetGross", -1);
            }

            if (FinanceConstants.NetGrossFLag.GROSS.equal(pageNetGross) && null == salePosition.getUnitPriceGross()) {
                resultDTO.put("changeNetGross", -1);
            }
        }
    }

    private void hasChangeCustomer(Integer saleId, Integer customerId) {
        resultDTO.put("hasChangeCustomer", changeCustomer(saleId, customerId));
    }

    private void read(SaleDTO saleDTO, boolean withReferences, SessionContext ctx) {
        Sale sale = (Sale) ExtendedCRUDDirector.i.read(saleDTO, resultDTO, withReferences);

        //sale was deleted by another user or referential integrity checker fails
        if (null == sale) {
            return;
        }

        //any saleposition of sale has assigned payment 
        if (withReferences && !canDeleteSale(sale)) {
            resultDTO.addResultMessage("Sale.error.ProductContractsHasPayments");
            resultDTO.setResultAsFailure();
            return;
        }

        if (null != sale.getFreetextId()) {
            resultDTO.put("text", readSaleFreeText(sale.getFreetextId()));
        }

        //setting up in resultDTO the name of the customer to display it in the ui
        resultDTO.put("addressName", readAddressName(sale.getCustomerId(), ctx));

        //in ui customerId is managed as addressId
        resultDTO.put("addressId", sale.getCustomerId());

        // setting up salesProcess Name if sale has assigned 
        if (null != sale.getProcessId()) {
            resultDTO.put("processName", sale.getSalesProcess().getProcessName());
        }
    }

    protected SaleDTO getSaleDTO(Integer saleId, SessionContext ctx) {
        SaleDTO saleDTO = super.getSaleDTO(saleId);
        String addressName = readAddressName((Integer) saleDTO.get("customerId"), ctx);
        saleDTO.put("addressName", addressName);
        resultDTO.put("getSaleDTO", saleDTO);
        return saleDTO;
    }

    public boolean isStateful() {
        return false;
    }
}
