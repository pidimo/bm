/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.salesmanager;

import javax.ejb.EJBLocalObject;

public interface Sale extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getCustomerId();

    void setCustomerId(Integer customerId);

    Integer getSellerId();

    void setSellerId(Integer sellerId);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getProcessId();

    void setProcessId(Integer processId);

    Integer getSaleDate();

    void setSaleDate(Integer saleDate);

    Integer getSaleId();

    void setSaleId(Integer saleId);

    String getTitle();

    void setTitle(String title);

    Integer getVersion();

    void setVersion(Integer version);

    SalesFreeText getSalesFreeText();

    void setSalesFreeText(SalesFreeText salesFreeText);

    SalesProcess getSalesProcess();

    void setSalesProcess(SalesProcess salesProcess);

    Integer getNetGross();

    void setNetGross(Integer netGross);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getCurrencyId();

    void setCurrencyId(Integer currencyId);

    Integer getSentAddressId();

    void setSentAddressId(Integer sentAddressId);

    Integer getSentContactPersonId();

    void setSentContactPersonId(Integer sentContactPersonId);

    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);
}
