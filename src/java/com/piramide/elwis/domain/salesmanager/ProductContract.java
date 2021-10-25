/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

public interface ProductContract extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getAmounType();

    void setAmounType(Integer amounType);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContractEndDate();

    void setContractEndDate(Integer contractEndDate);

    Integer getContractTypeId();

    void setContractTypeId(Integer contractTypeId);

    Integer getContractId();

    void setContractId(Integer contractId);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    java.math.BigDecimal getDiscount();

    void setDiscount(java.math.BigDecimal discount);

    Integer getSellerId();

    void setSellerId(Integer sellerId);

    Integer getInstallment();

    void setInstallment(Integer installment);

    Integer getMatchCalendarPeriod();

    void setMatchCalendarPeriod(Integer invoiceRemainOn);

    Integer getInvoicedUntil();

    void setInvoicedUntil(Integer invoicedUntil);

    java.math.BigDecimal getOpenAmount();

    void setOpenAmount(java.math.BigDecimal openAmount);

    Integer getOrderDate();

    void setOrderDate(Integer orderDate);

    Integer getPayPeriod();

    void setPayPeriod(Integer payPeriod);

    Integer getPayMethod();

    void setPayMethod(Integer payMethod);

    Integer getPayStartDate();

    void setPayStartDate(Integer payStartDate);

    java.math.BigDecimal getPrice();

    void setPrice(java.math.BigDecimal price);

    Integer getSalePositionId();

    void setSalePositionId(Integer salePositionId);

    Integer getVatId();

    void setVatId(Integer vatId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getPayConditionId();

    void setPayConditionId(Integer payConditionId);

    java.util.Collection getPaymentSteps();

    void setPaymentSteps(java.util.Collection paymentStep);

    String getContractNumber();

    void setContractNumber(String contractNumber);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getNetGross();

    void setNetGross(Integer netGross);

    Integer getNoteId();

    void setNoteId(Integer noteId);

    SalesFreeText getSalesFreeText();

    void setSalesFreeText(SalesFreeText salesFreeText);

    String getGrouping();

    void setGrouping(String grouping);

    Integer getDaysToRemind();

    void setDaysToRemind(Integer daysToRemind);

    Long getReminderTime();

    void setReminderTime(Long reminderTime);

    Integer getPricePeriod();

    void setPricePeriod(Integer pricePeriod);

    BigDecimal getPricePerMonth();

    void setPricePerMonth(BigDecimal pricePerMonth);

    Integer getSentAddressId();

    void setSentAddressId(Integer sentAddressId);

    Integer getSentContactPersonId();

    void setSentContactPersonId(Integer sentContactPersonId);

    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);

    Integer getInvoiceDelay();

    void setInvoiceDelay(Integer invoiceDelay);

    Integer getNextInvoiceDate();

    void setNextInvoiceDate(Integer nextInvoiceDate);

    Boolean getCancelledContract();

    void setCancelledContract(Boolean cancelledContract);
}
