package com.piramide.elwis.domain.reportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ReportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public abstract class ColumnGroupBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setColumnGroupId(PKGenerator.i.nextKey(ReportConstants.TABLE_COLUMNGROUP));
        setVersion(Integer.valueOf("1"));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ColumnGroupBean() {
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

    public abstract Integer getAxis();

    public abstract void setAxis(Integer axis);

    public abstract Integer getColumnGroupId();

    public abstract void setColumnGroupId(Integer columnGroupId);

    public abstract Boolean getColumnOrder();

    public abstract void setColumnOrder(Boolean columnOrder);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getGroupByDate();

    public abstract void setGroupByDate(Integer groupByDate);

    public abstract Integer getColumnId();

    public abstract void setColumnId(Integer reportColumnId);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer squence);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Column getColumn();

    public abstract void setColumn(Column group);

    public abstract Chart getChartSerie();

    public abstract void setChartSerie(Chart chartSerie);

    public abstract Chart getChartCategory();

    public abstract void setChartCategory(Chart chartCategory);
}
