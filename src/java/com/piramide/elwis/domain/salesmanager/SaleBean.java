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

public abstract class SaleBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setSaleId(PKGenerator.i.nextKey(SalesConstants.TABLE_SALE));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public SaleBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getCustomerId();

    public abstract void setCustomerId(Integer customerId);

    public abstract Integer getSellerId();

    public abstract void setSellerId(Integer sellerId);

    public abstract Integer getFreetextId();

    public abstract void setFreetextId(Integer freetextId);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract Integer getSaleDate();

    public abstract void setSaleDate(Integer saleDate);

    public abstract Integer getSaleId();

    public abstract void setSaleId(Integer saleId);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract SalesFreeText getSalesFreeText();

    public abstract void setSalesFreeText(SalesFreeText salesFreeText);

    public abstract SalesProcess getSalesProcess();

    public abstract void setSalesProcess(SalesProcess salesProcess);

    public abstract Integer getNetGross();

    public abstract void setNetGross(Integer netGross);

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getCurrencyId();

    public abstract void setCurrencyId(Integer currencyId);

    public abstract Integer getSentAddressId();

    public abstract void setSentAddressId(Integer sentAddressId);

    public abstract Integer getSentContactPersonId();

    public abstract void setSentContactPersonId(Integer sentContactPersonId);

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);
}
