/**
 * AlfaCentauro Team
 * @author Ivan
 * @version $Id: VatBean.java 10072 2011-07-04 20:33:26Z miguel ${NAME}.java, v 2.0 16-ago-2004 16:58:15 Ivan Exp $
 */
package com.piramide.elwis.domain.catalogmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class VatBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setVatId(PKGenerator.i.nextKey(CatalogConstants.TABLE_VAT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public VatBean() {
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

    public abstract String getVatDescription();

    public abstract void setVatDescription(String description);

    public abstract String getVatLabel();

    public abstract void setVatLabel(String label);

    public abstract Integer getVatId();

    public abstract void setVatId(Integer vatId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getTaxKey();

    public abstract void setTaxKey(Integer taxKey);
}
