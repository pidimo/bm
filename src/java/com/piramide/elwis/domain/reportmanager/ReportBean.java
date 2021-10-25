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

public abstract class ReportBean implements EntityBean {

    EntityContext entityContext;

    public ReportBean() {
    }

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setReportId(PKGenerator.i.nextKey(ReportConstants.TABLE_REPORT));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
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

    public abstract String getFilterRule();

    public abstract void setFilterRule(String filterRule);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getEmployeeId();

    public abstract void setEmployeeId(Integer employeeId);

    public abstract String getInitialTableReference();

    public abstract void setInitialTableReference(String initialTableReference);

    public abstract String getModule();

    public abstract void setModule(String module);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getReportId();

    public abstract void setReportId(Integer reportId);

    public abstract Boolean getState();

    public abstract void setState(Boolean state);

    public abstract Integer getReportType();

    public abstract void setReportType(Integer type);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Collection getFilters();

    public abstract void setFilters(Collection filters);

    //public abstract Collection getReports();

    //public abstract void setReports(Collection reports);

    public abstract Collection getColumns();

    public abstract void setColumns(Collection columns);

    public abstract Collection getTotalizers();

    public abstract void setTotalizers(Collection totalizers);

    public abstract Chart getCharts();

    public abstract void setCharts(Chart charts);

    public abstract ReportFreeText getDescriptionText();

    public abstract void setDescriptionText(ReportFreeText descriptionText);

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((ReportFreeText) descriptionText);
    }

    public abstract Integer getPageSize();

    public abstract void setPageSize(Integer pageSize);

    public abstract Integer getPageOrientation();

    public abstract void setPageOrientation(Integer pageOrientation);

    public abstract String getReportFormat();

    public abstract void setReportFormat(String reportFormat);

    public abstract Integer getJrxmlFileId();

    public abstract void setJrxmlFileId(Integer jrxmlFileId);

    public abstract String getJrxmlFileName();

    public abstract void setJrxmlFileName(String jrxmlFileName);

    public abstract Integer getSourceType();

    public abstract void setSourceType(Integer sourceType);

    public abstract ReportFreeText getJrxmlFile();

    public abstract void setJrxmlFile(ReportFreeText jrxmlFile);

    public abstract Collection getReportQueryParams();

    public abstract void setReportQueryParams(Collection reportQueryParams);

    public abstract Collection getReportArtifacts();

    public abstract void setReportArtifacts(Collection reportArtifacts);
}
