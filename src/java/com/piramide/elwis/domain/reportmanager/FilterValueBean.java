package com.piramide.elwis.domain.reportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: FilterValueBean.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 03-abr-2006 17:33:58 Miky Exp $
 */

public abstract class FilterValueBean implements EntityBean {

    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setFilterValueId(PKGenerator.i.nextKey(ReportConstants.TABLE_FILTERVALUE));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public FilterValueBean() {
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

    public abstract Integer getPkSequence();

    public abstract void setPkSequence(Integer pkSequence);

    public abstract Integer getFilterId();

    public abstract void setFilterId(Integer reportFilterId);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract Integer getFilterValueId();

    public abstract void setFilterValueId(Integer filterValueId);

    public abstract String getFilterValue();

    public abstract void setFilterValue(String filterValue);

    public abstract Filter getFilter();

    public abstract void setFilter(Filter filter);
}
