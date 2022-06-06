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

public abstract class ProductContractBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setContractId(PKGenerator.i.nextKey(SalesConstants.TABLE_PRODUCTCONTRACT));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public ProductContractBean() {
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

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getAmounType();

    public abstract void setAmounType(Integer amounType);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContractEndDate();

    public abstract void setContractEndDate(Integer contractEndDate);

    public abstract Integer getContractTypeId();

    public abstract void setContractTypeId(Integer contractTypeId);

    public abstract Integer getContractId();

    public abstract void setContractId(Integer contractId);

    public abstract Integer getCurrencyId();

    public abstract void setCurrencyId(Integer currencyId);

    public abstract java.math.BigDecimal getDiscount();

    public abstract void setDiscount(java.math.BigDecimal discount);

    public abstract Integer getSellerId();

    public abstract void setSellerId(Integer sellerId);

    public abstract Integer getInstallment();

    public abstract void setInstallment(Integer installment);

    public abstract Integer getMatchCalendarPeriod();

    public abstract void setMatchCalendarPeriod(Integer invoiceRemainOn);

    public abstract Integer getInvoicedUntil();

    public abstract void setInvoicedUntil(Integer invoicedUntil);

    public abstract java.math.BigDecimal getOpenAmount();

    public abstract void setOpenAmount(java.math.BigDecimal openAmount);

    public abstract Integer getOrderDate();

    public abstract void setOrderDate(Integer orderDate);

    public abstract Integer getPayPeriod();

    public abstract void setPayPeriod(Integer payPeriod);

    public abstract Integer getPayMethod();

    public abstract void setPayMethod(Integer payMethod);

    public abstract Integer getPayStartDate();

    public abstract void setPayStartDate(Integer payStartDate);

    public abstract java.math.BigDecimal getPrice();

    public abstract void setPrice(java.math.BigDecimal price);

    public abstract Integer getSalePositionId();

    public abstract void setSalePositionId(Integer salePositionId);

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getPayConditionId();

    public abstract void setPayConditionId(Integer payConditionId);

    public abstract java.util.Collection getPaymentSteps();

    public abstract void setPaymentSteps(java.util.Collection paymentStep);

    public abstract String getContractNumber();

    public abstract void setContractNumber(String contractNumber);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getNetGross();

    public abstract void setNetGross(Integer netGross);

    public abstract Integer getNoteId();

    public abstract void setNoteId(Integer noteId);

    public abstract SalesFreeText getSalesFreeText();

    public abstract void setSalesFreeText(SalesFreeText salesFreeText);

    public abstract String getGrouping();

    public abstract void setGrouping(String grouping);

    public abstract Integer getDaysToRemind();

    public abstract void setDaysToRemind(Integer daysToRemind);

    public abstract Long getReminderTime();

    public abstract void setReminderTime(Long reminderTime);

    public abstract Integer getPricePeriod();

    public abstract void setPricePeriod(Integer pricePeriod);

    public abstract BigDecimal getPricePerMonth();

    public abstract void setPricePerMonth(BigDecimal pricePerMonth);

    public abstract Integer getSentAddressId();

    public abstract void setSentAddressId(Integer sentAddressId);

    public abstract Integer getSentContactPersonId();

    public abstract void setSentContactPersonId(Integer sentContactPersonId);

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);

    public abstract Integer getInvoiceDelay();

    public abstract void setInvoiceDelay(Integer invoiceDelay);

    public abstract Integer getNextInvoiceDate();

    public abstract void setNextInvoiceDate(Integer nextInvoiceDate);

    public abstract Boolean getCancelledContract();

    public abstract void setCancelledContract(Boolean cancelledContract);
    
    public abstract String getReferenceId();

    public abstract void setReferenceId(String referenceId);
    
    public abstract String getXrechmailaddress();

    public abstract void setXrechmailaddress(String Xrechmailaddress);
}
