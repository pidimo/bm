package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * This Class represents the  CostCenter Entity Bean
 *
 * @author Ivan
 * @version $Id: CostCenterBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public abstract class CostCenterBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCostCenterId(PKGenerator.i.nextKey(CatalogConstants.TABLE_COSTCENTER));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public CostCenterBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCostCenterId();

    public abstract void setCostCenterId(Integer costCenterId);

    public abstract String getCostCenterName();

    public abstract void setCostCenterName(String name);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getCostCencers();

    public abstract void setCostCencers(Collection costCencers);

    public abstract CostCenter getCostCencer();

    public abstract void setCostCencer(CostCenter costCencer);

    public abstract Integer getParentCostCenterId();

    public abstract void setParentCostCenterId(Integer parentCostCenterId);
}
