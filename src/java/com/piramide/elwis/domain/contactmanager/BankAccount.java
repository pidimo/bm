/**
 * @author Fernando Montaño   15:52:48
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.domain.catalogmanager.Bank;

import javax.ejb.EJBLocalObject;

public interface BankAccount extends EJBLocalObject {
    String getAccountNumber();

    void setAccountNumber(String accountNumber);

    String getAccountOwner();

    void setAccountOwner(String accountOwner);

    Integer getBankAccountId();

    void setBankAccountId(Integer bankAccountId);

    Integer getCompanyId();

    void setCompanyId(Integer company);

    Bank getBank();

    void setBank(Bank bank);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getBankId();

    void setBankId(Integer bankId);

    String getIban();

    void setIban(String iban);

    Integer getAccountId();

    void setAccountId(Integer accountId);

    String getDescription();

    void setDescription(String description);
}
