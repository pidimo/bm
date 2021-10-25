package com.piramide.elwis.domain.reportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public abstract class TotalizeBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTotalizeId(PKGenerator.i.nextKey(ReportConstants.TABLE_TOTALIZE));
        setVersion(Integer.valueOf("1"));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public TotalizeBean() {
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

    public abstract String getFormula();

    public abstract void setFormula(String formula);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getReportId();

    public abstract void setReportId(Integer reportId);

    public abstract Integer getTotalizeId();

    public abstract void setTotalizeId(Integer totalizeId);

    public abstract Integer getTotalizeType();

    public abstract void setTotalizeType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getColumnTotalizers();

    public abstract void setColumnTotalizers(Collection columnTotalizers);

    public abstract Chart getChartXValue();

    public abstract void setChartXValue(Chart chartXValue);

    public abstract Chart getChartYValue();

    public abstract void setChartYValue(Chart chartYValue);

    public abstract Chart getChartZValue();

    public abstract void setChartZValue(Chart c);
}
