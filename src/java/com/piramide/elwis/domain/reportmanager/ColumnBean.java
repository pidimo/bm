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

public abstract class ColumnBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setColumnId(PKGenerator.i.nextKey(ReportConstants.TABLE_COLUMN));
        setVersion(new Integer(1));
        //set path
        setPath(dto.getAsString("tempPath").getBytes());

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ColumnBean() {
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

    public abstract String getColumnReference();

    public abstract void setColumnReference(String columnReference);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract byte[] getPath();

    public abstract void setPath(byte[] path);

    public abstract Integer getColumnId();

    public abstract void setColumnId(Integer columnId);

    public abstract Integer getReportId();

    public abstract void setReportId(Integer reportId);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer squence);

    public abstract String getTableReference();

    public abstract void setTableReference(String tableReference);

    public abstract Integer getColumnType();

    public abstract void setColumnType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getReports();

    public abstract void setReports(Collection reports);

    public abstract ColumnGroup getColumnGroup();

    public abstract void setColumnGroup(ColumnGroup column);

    public abstract Collection getColumnTotalizers();

    public abstract void setColumnTotalizers(Collection columnTotalizers);

    public abstract Boolean getIsTotalizer();

    public abstract void setIsTotalizer(Boolean isTotalizer);

    public abstract Boolean getColumnOrder();

    public abstract void setColumnOrder(Boolean columnOrder);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);
}
