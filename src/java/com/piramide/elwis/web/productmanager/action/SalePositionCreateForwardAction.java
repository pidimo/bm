package com.piramide.elwis.web.productmanager.action;

import com.piramide.elwis.cmd.productmanager.ProductReadLightCmd;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import net.java.dev.strutsejb.web.DefaultForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Setting up in salePositionForm default values for quantity, unitPrice, unitId and
 * versionNumber
 * quantity = 1
 * unitPrice = product sell price
 * unitId = product unitId
 * versionNumber = product currentVersion
 *
 * @author Ivan
 *         <p/>
 *         Jatun S.R.L.
 */
public class SalePositionCreateForwardAction extends ProductManagerForwardAction {
    private Log log = LogFactory.getLog(SalePositionCreateForwardAction.class);

    @Override
    protected void setDTOValues(DefaultForm defaultForm, HttpServletRequest request) {
        setProductInformation(defaultForm, request);
    }

    private void setProductInformation(DefaultForm salePositionProductForm,
                                       HttpServletRequest request) {
        ProductReadLightCmd productReadLightCmd = new ProductReadLightCmd();
        productReadLightCmd.putParam("productId", request.getParameter("productId"));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productReadLightCmd, request);
            if (!resultDTO.isFailure()) {
                salePositionProductForm.setDto("quantity", "1");
                salePositionProductForm.setDto("unitPrice", resultDTO.get("price"));
                salePositionProductForm.setDto("unitId", resultDTO.get("unitId"));
                salePositionProductForm.setDto("versionNumber", resultDTO.get("currentVersion"));
                salePositionProductForm.setDto("active", "true");
                salePositionProductForm.setDto("vatId", resultDTO.get("vatId"));
                salePositionProductForm.setDto("payMethod", SalesConstants.PayMethod.SingleWithoutContract.getConstant());
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + ProductReadLightCmd.class.getName() + " FAIL", e);
        }
    }
}
