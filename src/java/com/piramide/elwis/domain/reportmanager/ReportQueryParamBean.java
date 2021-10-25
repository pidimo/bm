package com.piramide.elwis.domain.reportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10
 */
public abstract class ReportQueryParamBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setReportQueryParamId(PKGenerator.i.nextKey(ReportConstants.TABLE_REPORTQUERYPARAM));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }


    public ReportQueryParamBean() {
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

    public abstract String getParameterName();

    public abstract void setParameterName(String parameterName);

    public abstract Integer getReportQueryParamId();

    public abstract void setReportQueryParamId(Integer reportQueryParamId);

    public abstract Integer getFilterId();

    public abstract void setFilterId(Integer filterId);

    public abstract Integer getReportId();

    public abstract void setReportId(Integer reportId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Filter getFilter();

    public abstract void setFilter(Filter filter);
}
