package com.piramide.elwis.domain.productmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProductConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * This class represents the Product Entity
 *
 * @author Fernando Montaño
 * @version $Id: ProductBean.java 12479 2016-02-15 20:29:29Z miguel $
 */


public abstract class ProductBean implements EntityBean {

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setProductId(PKGenerator.i.nextKey(ProductConstants.TABLE_PRODUCT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getCurrentVersion();

    public abstract void setCurrentVersion(String currentVersion);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract java.math.BigDecimal getPrice();

    public abstract void setPrice(java.math.BigDecimal price);

    public abstract Integer getProductGroupId();

    public abstract void setProductGroupId(Integer productGroupId);

    public abstract String getProductName();

    public abstract void setProductName(String productName);

    public abstract String getProductNumber();

    public abstract void setProductNumber(String productNumber);

    public abstract Integer getProductTypeId();

    public abstract void setProductTypeId(Integer productTypeId);

    public abstract Integer getUnitId();

    public abstract void setUnitId(Integer unitId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract ProductFreeText getDescriptionText();

    public abstract void setDescriptionText(ProductFreeText descriptionText);

    public abstract Collection getProductCategoryList();

    public abstract void setProductCategoryList(Collection productCategoryList);

    public abstract Collection getCompetitorProduct();

    public abstract void setCompetitorProduct(Collection competitorProduct);

    public abstract Collection getPricingList();

    public abstract void setPricingList(Collection pricingList);

    public abstract Collection getProductPictureList();

    public abstract void setProductPictureList(Collection productPictureList);

    public abstract Integer getAccountId();

    public abstract void setAccountId(Integer actionId);

    public abstract Integer getLangTextId();

    public abstract void setLangTextId(Integer langTextId);

    public abstract java.math.BigDecimal getPriceGross();

    public abstract void setPriceGross(java.math.BigDecimal priceGross);

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract Long getInitDateTime();

    public abstract void setInitDateTime(Long initDateTime);

    public abstract Long getEndDateTime();

    public abstract void setEndDateTime(Long endDateTime);

    public abstract String getWebSiteLink();

    public abstract void setWebSiteLink(String webSiteLink);

    public abstract String getEventAddress();

    public abstract void setEventAddress(String eventAddress);

    public abstract Integer getEventMaxParticipant();

    public abstract void setEventMaxParticipant(Integer eventMaxParticipant);

    public abstract Long getClosingDateTime();

    public abstract void setClosingDateTime(Long closingDateTime);
}
