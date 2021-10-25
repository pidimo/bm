package com.piramide.elwis.domain.productmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Product local interface
 *
 * @author Fernando Montaño
 * @version $Id: Product.java 12479 2016-02-15 20:29:29Z miguel $
 */


public interface Product extends EJBLocalObject {
    Integer getProductId();

    void setProductId(Integer productId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getCurrentVersion();

    void setCurrentVersion(String currentVersion);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    java.math.BigDecimal getPrice();

    void setPrice(java.math.BigDecimal price);

    Integer getProductGroupId();

    void setProductGroupId(Integer productGroupId);

    String getProductName();

    void setProductName(String productName);

    String getProductNumber();

    void setProductNumber(String productNumber);

    Integer getProductTypeId();

    void setProductTypeId(Integer productTypeId);

    Integer getUnitId();

    void setUnitId(Integer unitId);

    Integer getVersion();

    void setVersion(Integer version);

    ProductFreeText getDescriptionText();

    void setDescriptionText(ProductFreeText descriptionText);

    Collection getProductCategoryList();

    void setProductCategoryList(Collection productCategoryList);

    Collection getCompetitorProduct();

    void setCompetitorProduct(Collection competitorProduct);

    Collection getPricingList();

    void setPricingList(Collection pricingList);

    Collection getProductPictureList();

    void setProductPictureList(Collection productPictureList);

    Integer getAccountId();

    void setAccountId(Integer actionId);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);

    java.math.BigDecimal getPriceGross();

    void setPriceGross(java.math.BigDecimal priceGross);

    Integer getVatId();

    void setVatId(Integer vatId);

    Long getInitDateTime();

    void setInitDateTime(Long initDateTime);

    Long getEndDateTime();

    void setEndDateTime(Long endDateTime);

    String getWebSiteLink();

    void setWebSiteLink(String webSiteLink);

    String getEventAddress();

    void setEventAddress(String eventAddress);

    Integer getEventMaxParticipant();

    void setEventMaxParticipant(Integer eventMaxParticipant);

    Long getClosingDateTime();

    void setClosingDateTime(Long closingDateTime);
}
