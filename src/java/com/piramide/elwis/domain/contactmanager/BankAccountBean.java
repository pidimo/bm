/**
 * @author Fernando Montaño   15:52:48
 * @version 2.0
 */
package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.domain.catalogmanager.Bank;
import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Represents BankAccountBean EntityBean
 *
 * @author Titus
 * @version BankAccountBean.java, v 2.0 Apr 10, 2004 11:24:32 AM
 */
public abstract class BankAccountBean implements EntityBean {
    Bank bank;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setBankAccountId(PKGenerator.i.nextKey(ContactConstants.TABLE_BANKACCOUNT));
        setVersion(new Integer(1));
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public BankAccountBean() {
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

    public void ejbStore() throws EJBException {
    }

    public abstract String getAccountNumber();

    public abstract void setAccountNumber(String accountNumber);

    public abstract String getAccountOwner();

    public abstract void setAccountOwner(String accountOwner);

    public abstract Integer getBankAccountId();

    public abstract void setBankAccountId(Integer bankAccountId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer company);

    public void ejbLoad() throws EJBException {
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        setBankId(bank.getBankId());
    }

    public abstract Integer getBankId();

    public abstract void setBankId(Integer bankId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract String getIban();

    public abstract void setIban(String iban);

    public abstract Integer getAccountId();

    public abstract void setAccountId(Integer accountId);

    public abstract String getDescription();

    public abstract void setDescription(String description);
}
