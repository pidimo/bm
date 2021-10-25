/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;

public abstract class SalePositionBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setSalePositionId(PKGenerator.i.nextKey(SalesConstants.TABLE_SALEPOSITION));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public SalePositionBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
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

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCustomerId();

    public abstract void setCustomerId(Integer customerId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getDeliveryDate();

    public abstract void setDeliveryDate(Integer deliveryDate);

    public abstract Integer getFreetextId();

    public abstract void setFreetextId(Integer freetextId);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getSalePositionId();

    public abstract void setSalePositionId(Integer salePositionId);

    public abstract BigDecimal getQuantity();

    public abstract void setQuantity(BigDecimal quantity);

    public abstract Integer getSaleId();

    public abstract void setSaleId(Integer saleId);

    public abstract String getSerial();

    public abstract void setSerial(String serial);

    public abstract java.math.BigDecimal getTotalPrice();

    public abstract void setTotalPrice(java.math.BigDecimal totalPrice);

    public abstract Integer getUnitId();

    public abstract void setUnitId(Integer unitId);

    public abstract java.math.BigDecimal getUnitPrice();

    public abstract void setUnitPrice(java.math.BigDecimal unitPrice);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getVersionNumber();

    public abstract void setVersionNumber(String versionNumber);

    public abstract SalesFreeText getSalesFreeText();

    public abstract void setSalesFreeText(SalesFreeText salesFreeText);

    public abstract java.util.Collection getProductContracts();

    public abstract void setProductContracts(java.util.Collection productContract);

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract Integer getPayMethod();

    public abstract void setPayMethod(Integer payMethod);

    public abstract java.math.BigDecimal getDiscount();

    public abstract void setDiscount(java.math.BigDecimal discount);

    public abstract java.math.BigDecimal getUnitPriceGross();

    public abstract void setUnitPriceGross(java.math.BigDecimal unitPriceGross);

    public abstract java.math.BigDecimal getTotalPriceGross();

    public abstract void setTotalPriceGross(java.math.BigDecimal totalPriceGross);

    public abstract Integer ejbSelectMaxDeliveryDate(Integer saleId) throws FinderException;

    public Integer ejbHomeSelectMaxDeliveryDate(Integer saleId) throws FinderException {
        return ejbSelectMaxDeliveryDate(saleId);
    }

    public abstract Integer ejbSelectCountByProductId(Integer productId) throws FinderException;

    public Integer ejbHomeSelectCountByProductId(Integer productId) throws FinderException {
        return ejbSelectCountByProductId(productId);
    }

}
