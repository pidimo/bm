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

public abstract class ContractTypeBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setContractTypeId(PKGenerator.i.nextKey(SalesConstants.TABLE_CONTRACTTYPE));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ContractTypeBean() {
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

    public abstract Integer getContractTypeId();

    public abstract void setContractTypeId(Integer contractTypeId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Boolean getTobeInvoiced();

    public abstract void setTobeInvoiced(Boolean tobeInvoiced);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
