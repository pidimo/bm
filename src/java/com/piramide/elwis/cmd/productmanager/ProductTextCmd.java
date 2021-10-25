package com.piramide.elwis.cmd.productmanager;

import com.piramide.elwis.cmd.catalogmanager.LanguageUtilCmd;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.productmanager.*;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.dto.productmanager.ProductTextDTO;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ProductTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ProductTextCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;

        if ("update".equals(getOp())) {
            isRead = false;
            Integer productId = getProductId();
            Integer companyId = (Integer) paramDTO.get("companyId");
            update(productId, companyId, ctx);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            Integer productId = getProductId();
            Integer companyId = (Integer) paramDTO.get("companyId");
            delete(productId, companyId);
        }
        if (isRead) {
            Integer productId = getProductId();
            Integer companyId = (Integer) paramDTO.get("companyId");
            read(productId, companyId, ctx);
        }
    }

    private void delete(Integer productId, Integer companyId) {
        ProductTextHome productTextHome = getProductTextHome();
        try {
            Collection productTexts = productTextHome.findProductTextByProductId(productId, companyId);
            for (Object object : productTexts) {
                ProductText productText = (ProductText) object;
                productText.remove();
            }
        } catch (FinderException e) {
            log.debug("-> Read ProductTexts productId=" + productId + " FAIL");
        } catch (RemoveException e) {
            log.debug("-> Detele ProductText productId=" + productId + " FAIL");
        }
    }

    private void update(Integer productId, Integer companyId, SessionContext ctx) {
        Integer uiVersion = Integer.valueOf(paramDTO.get("version").toString());

        //version control
        if (!getActualVersion(productId, companyId).equals(uiVersion)) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.isClearingForm = true;
            resultDTO.setResultAsFailure();
            read(productId, companyId, ctx);
            return;
        }

        ProductText defaultProductText;
        try {
            defaultProductText = getDefaultProductText(productId, companyId);
            defaultProductText.setIsDefault(false);
        } catch (FinderException e) {
            log.debug("-> Default productText productId=" +
                    productId +
                    " not defined default ProductText object");
        }

        //update, create and delete productText by language
        List<String> uiLanguages = (List<String>) paramDTO.get("uiLanguages");
        int versionIncrement = 1;
        for (String uiLanguage : uiLanguages) {
            Integer languageId = Integer.valueOf(uiLanguage);
            String text = (String) paramDTO.get(buildTextKey(languageId));
            boolean isDefault = uiLanguage.equals(paramDTO.get("isDefault"));

            try {
                ProductText productText = getProductText(productId, languageId);
                if (null == text ||
                        "".equals(text.trim())) {
                    try {
                        productText.remove();
                        versionIncrement++;
                        continue;
                    } catch (RemoveException e) {
                        log.debug("-> Delete ProductText productId=" +
                                productId +
                                " languageId=" +
                                languageId + " FAIL");
                        continue;
                    }
                }
                productText.getProductFreeText().setValue(text.getBytes());
                productText.setVersion(productText.getVersion() + versionIncrement);
                productText.setIsDefault(isDefault);
            } catch (FinderException e) {
                if (null != text &&
                        !"".equals(text.trim())) {
                    createProductText(text, productId, languageId, companyId, isDefault);
                }
            }
        }
    }

    private void createProductText(String text,
                                   Integer productId,
                                   Integer languageId,
                                   Integer companyId,
                                   boolean isDefault) {

        ProductFreeTextHome freeTextHome = (ProductFreeTextHome)
                EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCT_FREETEXT);
        ProductFreeText productFreeText;
        try {
            productFreeText =
                    freeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_PRODUCT);
        } catch (CreateException e) {
            log.error("-> Cannot create ProductText description");
            return;
        }

        ProductTextDTO productTextDTO = new ProductTextDTO();
        productTextDTO.put("productId", productId);
        productTextDTO.put("languageId", languageId);
        productTextDTO.put("freetextId", productFreeText.getFreeTextId());
        productTextDTO.put("companyId", companyId);
        productTextDTO.put("isDefault", isDefault);

        ExtendedCRUDDirector.i.create(productTextDTO, resultDTO, false);
    }

    private void read(Integer productId,
                      Integer companyId,
                      SessionContext ctx) {
        LanguageUtilCmd languageUtilCmd = new LanguageUtilCmd();
        languageUtilCmd.putParam("companyId", companyId);
        languageUtilCmd.setOp("getCompanyLanguages");
        languageUtilCmd.executeInStateless(ctx);
        ResultDTO customResultDTO = languageUtilCmd.getResultDTO();
        List<LanguageDTO> languages = (List<LanguageDTO>) customResultDTO.get("getCompanyLanguages");

        int version = 0;
        for (LanguageDTO languageDTO : languages) {
            Integer languageId = (Integer) languageDTO.get("languageId");


            ProductText productText;
            try {
                productText = getProductText(productId, languageId);
            } catch (FinderException e) {
                log.debug("-> Read ProductText productId=" +
                        productId + " languageId=" +
                        languageId + " NOT DEFINED");
                continue;
            }

            String text = new String(productText.getProductFreeText().getValue());

            resultDTO.put(buildLanguageKey(languageId), languageDTO.get("languageName"));
            resultDTO.put(buildTextKey(languageId), text);
            if (productText.getIsDefault()) {
                resultDTO.put("isDefault", languageDTO.get("languageId"));
            }

            version = version + productText.getVersion();
        }

        resultDTO.put("productId", productId);
        resultDTO.put("version", version);
    }

    private ProductText getProductText(Integer productId, Integer languageId) throws FinderException {
        ProductTextHome productTextHome = getProductTextHome();
        ProductTextPK productTextPK = new ProductTextPK();
        productTextPK.productId = productId;
        productTextPK.languageId = languageId;

        return productTextHome.findByPrimaryKey(productTextPK);
    }

    private ProductText getDefaultProductText(Integer productId, Integer companyId) throws FinderException {
        ProductTextHome productTextHome = getProductTextHome();
        return productTextHome.findDefaultProductText(productId, companyId);
    }

    private Integer getActualVersion(Integer productId, Integer companyId) {
        ProductTextHome productTextHome = getProductTextHome();

        Collection actualProductTexts;
        try {
            actualProductTexts = productTextHome.findProductTextByProductId(productId, companyId);
        } catch (FinderException e) {
            log.debug("-> Read ProductText productId=" +
                    productId + " FAIL");
            return 0;
        }

        int version = 0;
        for (Object object : actualProductTexts) {
            ProductText productText = (ProductText) object;
            version += productText.getVersion();
        }

        return version;
    }

    private ProductTextHome getProductTextHome() {
        return (ProductTextHome) EJBFactory.i.getEJBLocalHome(ProductConstants.JNDI_PRODUCTTEXT);
    }

    private String buildTextKey(Integer languageId) {
        return "text_" + languageId;
    }

    private String buildLanguageKey(Integer languageId) {
        return "language_" + languageId;
    }

    private Integer getProductId() {
        Integer productId = null;
        if (null != paramDTO.get("productId") &&
                !"".equals(paramDTO.get("productId").toString())) {
            try {
                productId = Integer.valueOf(paramDTO.get("productId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse productId" + paramDTO.get("productId") + " FAIL");
            }
        }

        return productId;
    }

    public boolean isStateful() {
        return false;
    }
}
