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

public abstract class FilterBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setFilterId(PKGenerator.i.nextKey(ReportConstants.TABLE_FILTER));
        setVersion(new Integer(1));
        //set path
        setPath(dto.getAsString("tempPath").getBytes());

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public FilterBean() {
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

    public abstract String getAliasCondition();

    public abstract void setAliasCondition(String aliasCondition);

    public abstract String getColumnReference();

    public abstract void setColumnReference(String columnReference);

    public abstract Integer getColumnType();

    public abstract void setColumnType(Integer columnType);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getOperator();

    public abstract void setOperator(Integer operator);

    public abstract Integer getFilterId();

    public abstract void setFilterId(Integer filterId);

    public abstract Integer getReportId();

    public abstract void setReportId(Integer reportId);

    public abstract String getTableReference();

    public abstract void setTableReference(String tableReference);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getSequence();

    public abstract void setSequence(Integer sequence);

    public abstract byte[] getPath();

    public abstract void setPath(byte[] path);

    public abstract Integer ejbSelectMaxSequence(Integer reportId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectMaxSequence(Integer reportId, Integer companyId) throws FinderException {
        return ejbSelectMaxSequence(reportId, companyId);
    }

    public abstract Integer getFilterType();

    public abstract void setFilterType(Integer filterType);

    public abstract Collection getFilterValues();

    public abstract void setFilterValues(Collection filterValues);

    public abstract Report getReport();

    public abstract void setReport(Report report);

    public abstract String getLabel();

    public abstract void setLabel(String label);

    public abstract Boolean getIsParameter();

    public abstract void setIsParameter(Boolean isParameter);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract ReportQueryParam getReportQueryParam();

    public abstract void setReportQueryParam(ReportQueryParam reportQueryParam);
}
