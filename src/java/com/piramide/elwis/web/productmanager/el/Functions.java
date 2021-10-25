package com.piramide.elwis.web.productmanager.el;

import com.piramide.elwis.cmd.catalogmanager.ProductTypeCmd;
import com.piramide.elwis.cmd.catalogmanager.ProductTypeLightCmd;
import com.piramide.elwis.cmd.productmanager.ProductPictureCmd;
import com.piramide.elwis.cmd.productmanager.ProductReadLightCmd;
import com.piramide.elwis.cmd.salesmanager.SalePositionReadCmd;
import com.piramide.elwis.dto.catalogmanager.ProductTypeDTO;
import com.piramide.elwis.dto.productmanager.ProductDTO;
import com.piramide.elwis.dto.productmanager.ProductPictureDTO;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ProductConstants;
import com.piramide.elwis.web.admin.session.User;
import com.piramide.elwis.web.common.util.RequestUtils;
import com.piramide.elwis.web.common.validator.ForeignkeyValidator;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class Functions {
    private static Log log = LogFactory.getLog(Functions.class);

    public static boolean existsProduct(Object productId) {
        ActionErrors errors = new ActionErrors();
        ForeignkeyValidator.i.validate(ProductConstants.TABLE_PRODUCT, "productid",
                productId, errors, new ActionError("Product.NotFound"));
        return errors.isEmpty();
    }

    public static ProductDTO getProductDTO(String productId, HttpServletRequest request) {
        if (GenericValidator.isBlankOrNull(productId)) {
            return null;
        }

        ProductDTO productDTO = new ProductDTO();
        ProductReadLightCmd productReadLightCmd = new ProductReadLightCmd();
        productReadLightCmd.putParam("productId", Integer.valueOf(productId));
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(productReadLightCmd, request);
            if (resultDTO.isFailure()) {
                return null;
            }

            productDTO.putAll(resultDTO);

            return productDTO;
        } catch (AppLevelException e) {
            log.error("BusinessDelegate can't execute the command " + ProductReadLightCmd.class.getName(), e);
        }
        return null;
    }

    public static List<ProductPictureDTO> findProductPictureByProductIdDTOList(Object productId, HttpServletRequest request) {
        List<ProductPictureDTO> result = new ArrayList<ProductPictureDTO>();

        if (productId != null && !GenericValidator.isBlankOrNull(productId.toString())) {

            ProductPictureCmd productPictureCmd = new ProductPictureCmd();
            productPictureCmd.setOp("readByProductId");
            productPictureCmd.putParam("productId", productId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(productPictureCmd, request);
                if (!resultDTO.isFailure() && resultDTO.containsKey("productPictureDTOList")) {
                    result = (List<ProductPictureDTO>) resultDTO.get("productPictureDTOList");
                }
            } catch (AppLevelException e) {
                log.error("-> Execute cmd FAIL", e);
            }
        }
        return result;
    }

    public static Map findProductPictureByProductIdFirst(Object productId, HttpServletRequest request) {
        Map result = new HashMap();

        List<ProductPictureDTO> productPictureDTOs = findProductPictureByProductIdDTOList(productId, request);
        if (!productPictureDTOs.isEmpty()) {
            result.putAll(productPictureDTOs.get(0));
        }
        return result;
    }

    public static Integer findProductTypeIdOfEventType(HttpServletRequest request) {
        Integer productTypeEventId = null;

        User user = RequestUtils.getUser(request);
        Integer companyId = (Integer) user.getValue(Constants.COMPANYID);

        ProductTypeLightCmd cmd = new ProductTypeLightCmd();
        cmd.setOp("findEventType");
        cmd.putParam("companyId", companyId);

        ProductTypeDTO productTypeDTO = null;
        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, request);
            if (resultDTO.get("dtoProductType") != null) {
                productTypeDTO = (ProductTypeDTO) resultDTO.get("dtoProductType");
            }
        } catch (AppLevelException e) {
            log.error("Error in execute cmd " + ProductTypeLightCmd.class.getName(), e);
        }

        if (productTypeDTO != null) {
            productTypeEventId = (Integer) productTypeDTO.get("typeId");
        } else {
            //if no exists, create this
            ProductTypeCmd productTypeCmd = new ProductTypeCmd();
            productTypeCmd.setOp("create");
            productTypeCmd.putParam("companyId", companyId);
            productTypeCmd.putParam("typeName", ProductConstants.PRODUCTTYPE_TYPE_EVENT_NAME);
            productTypeCmd.putParam("productTypeType", ProductConstants.ProductTypeType.EVENT.getConstant());

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(productTypeCmd, request);
                if (resultDTO.get("typeId") != null) {
                    productTypeEventId = (Integer) resultDTO.get("typeId");
                }
            } catch (AppLevelException e) {
                log.error("Error in execute cmd " + ProductTypeCmd.class.getName(), e);
            }
        }

        return productTypeEventId;
    }

    public static Integer countSalePositionByProduct(Object productId) {
        Integer result = 0;
        if (productId != null && !GenericValidator.isBlankOrNull(productId.toString())) {

            SalePositionReadCmd salePositionReadCmd = new SalePositionReadCmd();
            salePositionReadCmd.setOp("countByProduct");
            salePositionReadCmd.putParam("productId", productId);

            try {
                ResultDTO resultDTO = BusinessDelegate.i.execute(salePositionReadCmd, null);
                if (!resultDTO.isFailure() && resultDTO.get("countSalePosition") != null) {
                    result = (Integer) resultDTO.get("countSalePosition");
                }
            } catch (AppLevelException e) {
                log.error("-> Execute cmd FAIL", e);
            }
        }
        return result;
    }

}
