package com.piramide.elwis.cmd.salesmanager;

import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.salesmanager.ProductContract;
import com.piramide.elwis.domain.salesmanager.SalePosition;
import com.piramide.elwis.domain.salesmanager.SalePositionHome;
import com.piramide.elwis.dto.salesmanager.ProductContractDTO;
import com.piramide.elwis.dto.salesmanager.SalePositionDTO;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SalePositionReadCmd extends SalePositionCmd {
    private Log log = LogFactory.getLog(SalePositionReadCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;

        if ("readSalePosition".equals(getOp())) {
            isRead = false;
            Integer salePositionId = EJBCommandUtil.i.getValueAsInteger(this, "salePositionId");
            readSalePosition(salePositionId, ctx);
        }

        if ("canChangePayMethod".equals(getOp())) {
            isRead = false;
            Integer salePositionId = EJBCommandUtil.i.getValueAsInteger(this, "salePositionId");
            canChangePayMethod(salePositionId);
        }

        if ("getProductContractDTOs".equals(getOp())) {
            isRead = false;
            Integer salePositionId = EJBCommandUtil.i.getValueAsInteger(this, "salePositionId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            getProductContractDTOs(salePositionId, companyId);
        }

        if ("salePositionHasInvoiced".equals(getOp())) {
            isRead = false;
            Integer salePositionId = EJBCommandUtil.i.getValueAsInteger(this, "salePositionId");
            Integer companyId = EJBCommandUtil.i.getValueAsInteger(this, "companyId");
            salePositionHasInvoiced(salePositionId, companyId);
        }

        if ("getMaxDeliveryDatePosition".equals(getOp())) {
            isRead = false;
            Integer saleId = EJBCommandUtil.i.getValueAsInteger(this, "saleId");
            getMaxDeliveryDateSalePosition(saleId);
        }

        if ("countByProduct".equals(getOp())) {
            isRead = false;
            countSalePositionByProductId();
        }

        if (isRead) {
            boolean withReferences = null != paramDTO.get("withReferences")
                    && "true".equals(paramDTO.get("withReferences").toString());
            SalePositionDTO salePositionDTO = getSalePositionDTO();

            read(salePositionDTO, withReferences, ctx);
        }
    }

    @Override
    protected boolean salePositionHasInvoiced(Integer salePositionId, Integer companyId) {
        boolean salePositionHasInvoiced = super.salePositionHasInvoiced(salePositionId, companyId);
        resultDTO.put("salePositionHasInvoiced", salePositionHasInvoiced);
        return salePositionHasInvoiced;
    }

    private List<ProductContractDTO> getProductContractDTOs(Integer salePositionId, Integer companyId) {
        List productContracts = getProductContracts(salePositionId, companyId);

        List<ProductContractDTO> productContractDTOs = new ArrayList<ProductContractDTO>();
        for (int i = 0; i < productContracts.size(); i++) {
            ProductContract productContract = (ProductContract) productContracts.get(i);
            ProductContractDTO dto = new ProductContractDTO();
            DTOFactory.i.copyToDTO(productContract, dto);
            productContractDTOs.add(dto);
        }

        resultDTO.put("getProductContractDTOs", productContractDTOs);

        return productContractDTOs;
    }

    private void readSalePosition(Integer salePositionId, SessionContext ctx) {
        SalePositionDTO salePositionDTO = getSalePositionDTO(salePositionId, ctx);
        if (null == salePositionDTO) {
            salePositionDTO = new SalePositionDTO();
        }

        resultDTO.put("readSalePosition", salePositionDTO);
    }

    private void read(SalePositionDTO salePositionDTO, boolean withReferences, SessionContext ctx) {
        SalePosition salePosition =
                (SalePosition) ExtendedCRUDDirector.i.read(salePositionDTO, resultDTO, withReferences);

        //saleposition was deleted by another user or referential integritity checker fails
        if (null == salePosition) {
            return;
        }

        //saleposition has assigned productcontracts with payments
        if (withReferences && !canDeleteSaleposition(salePosition)) {
            resultDTO.addResultMessage("SalePosition.error.ProductContractsHasPayments");
            resultDTO.setResultAsFailure();
            return;
        }

        if (null != salePosition.getFreetextId()) {
            resultDTO.put("text", readSaleFreeText(salePosition.getFreetextId()));
        }

        //used in ui to show customer name
        resultDTO.put("customerName", readAddressName(salePosition.getCustomerId(), ctx));

        //used in ui to show productName
        resultDTO.put("productName", readProductName(salePosition.getProductId(), ctx));

        //used in ui to build sale link
        if (null != salePosition.getSaleId()) {
            resultDTO.put("saleName", readSaleName(salePosition.getSaleId()));
        }

        //check if the saleposition is invoiced
        salePositionHasInvoiced(salePosition.getSalePositionId(), salePosition.getCompanyId());

        //used in SalePositionForm to restore payMethod value in validation
        resultDTO.put("copyPayMethod", salePosition.getPayMethod());

        //Put in resultDTO the key 'canChangePayMethod' than used to enable or disable payMethod field in jsp page
        boolean isSalePositionRelatedWithInvoicePositions = isSalePositionRelatedWithInvoicePositions(salePosition);
        resultDTO.put("canChangePayMethod", !isSalePositionRelatedWithInvoicePositions);

        List relatedSingleContracts = getSingleProductContracts(
                salePosition.getSalePositionId(),
                salePosition.getCompanyId());

        // to enable or disable quantity field edition in user interface
        resultDTO.put("canUpdateQuantityField", relatedSingleContracts.isEmpty());

        //read category values
        readSalePositionCategoryFieldValues(salePosition, ctx);
    }

    private void readSalePositionCategoryFieldValues(SalePosition salePosition, SessionContext ctx) {
        SalePositionCategoryManagerCmd cmd = new SalePositionCategoryManagerCmd();
        cmd.setOp("readFieldValues");
        cmd.putParam("salePositionId", salePosition.getSalePositionId());
        cmd.putParam("companyId", salePosition.getCompanyId());
        cmd.executeInStateless(ctx);

        ResultDTO myResultDTO = cmd.getResultDTO();
        resultDTO.putAll(myResultDTO);
    }

    private void getMaxDeliveryDateSalePosition(Integer saleId) {
        Integer maxDeliveryDate = null;
        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);

        try {
            maxDeliveryDate = salePositionHome.selectMaxDeliveryDate(saleId);
        } catch (FinderException e) {
            log.debug("Error in execute finder...");
        }

        resultDTO.put("maxDeliveryDate", maxDeliveryDate);
    }

    private void countSalePositionByProductId() {
        Integer result = 0;
        Integer productId = new Integer(paramDTO.get("productId").toString());

        SalePositionHome salePositionHome = (SalePositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_SALEPOSITION);

        try {
            result = salePositionHome.selectCountByProductId(productId);
        } catch (FinderException e) {
            log.debug("Error in execute finder selectCountByProductId...", e);
        }

        resultDTO.put("countSalePosition", result);
    }


    public boolean isStateful() {
        return false;
    }
}
