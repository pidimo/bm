/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;

public abstract class InvoicePositionBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setPositionId(PKGenerator.i.nextKey(FinanceConstants.TABLE_INVOICEPOSITION));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public InvoicePositionBean() {
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

    public abstract Integer getPositionId();

    public abstract void setPositionId(Integer positionId);

    public abstract Integer getAccountId();

    public abstract void setAccountId(Integer accountId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContractId();

    public abstract void setContractId(Integer contractId);

    public abstract Integer getFreetextId();

    public abstract void setFreetextId(Integer freetextId);

    public abstract Integer getInvoiceId();

    public abstract void setInvoiceId(Integer invoiceId);

    public abstract Integer getPayStepId();

    public abstract void setPayStepId(Integer payStepId);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getNumber();

    public abstract void setNumber(Integer number);

    public abstract BigDecimal getQuantity();

    public abstract void setQuantity(BigDecimal quantity);

    public abstract java.math.BigDecimal getTotalPrice();

    public abstract void setTotalPrice(java.math.BigDecimal totalPrice);

    public abstract java.math.BigDecimal getUnitPrice();

    public abstract void setUnitPrice(java.math.BigDecimal unitPrice);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract java.math.BigDecimal getVatRate();

    public abstract void setVatRate(java.math.BigDecimal vatRate);

    public abstract FinanceFreeText getFinanceFreeText();

    public abstract void setFinanceFreeText(FinanceFreeText financeFreeText);

    public abstract Integer ejbSelectMaxPositionNumber(Integer invoiceId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectMaxPositionNumber(Integer invoiceId, Integer companyId) throws FinderException {
        return ejbSelectMaxPositionNumber(invoiceId, companyId);
    }

    public abstract java.math.BigDecimal getDiscount();

    public abstract void setDiscount(java.math.BigDecimal discount);

    public abstract String getUnit();

    public abstract void setUnit(String unit);

    public abstract BigDecimal getUnitPriceGross();

    public abstract void setUnitPriceGross(BigDecimal unitPriceGross);

    public abstract BigDecimal getTotalPriceGross();

    public abstract void setTotalPriceGross(BigDecimal totalPriceGross);

    public abstract Invoice getInvoice();

    public abstract void setInvoice(Invoice invoice);

    public abstract Integer getSalePositionId();

    public abstract void setSalePositionId(Integer salePositionId);

    public abstract BigDecimal getDiscountValue();

    public abstract void setDiscountValue(BigDecimal discountValue);

    public abstract Boolean getExported();

    public abstract void setExported(Boolean exported);
}
