/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:12:43 $
 */
package com.piramide.elwis.domain.project;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class ProjectBean implements EntityBean {
    public ProjectBean() {
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

    public abstract Integer getAccountId();

    public abstract void setAccountId(Integer accountId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getCustomerId();

    public abstract void setCustomerId(Integer customerId);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract Integer getEndDate();

    public abstract void setEndDate(Integer endDate);

    public abstract Boolean getHasTimeLimit();

    public abstract void setHasTimeLimit(Boolean hasTimeLimit);

    public abstract java.math.BigDecimal getPlannedInvoice();

    public abstract void setPlannedInvoice(java.math.BigDecimal plannedInvoice);

    public abstract java.math.BigDecimal getPlannedNoInvoice();

    public abstract void setPlannedNoInvoice(java.math.BigDecimal plannedNoInvoice);

    public abstract Integer getProjectId();

    public abstract void setProjectId(Integer projectId);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Integer getResponsibleId();

    public abstract void setResponsibleId(Integer responsibleId);

    public abstract Integer getStartDate();

    public abstract void setStartDate(Integer startDate);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getToBeInvoiced();

    public abstract void setToBeInvoiced(Integer toBeInvoiced);

    public abstract java.math.BigDecimal getTotalInvoice();

    public abstract void setTotalInvoice(java.math.BigDecimal totalInvoice);

    public abstract java.math.BigDecimal getTotalNoInvoice();

    public abstract void setTotalNoInvoice(java.math.BigDecimal totalNoInvoice);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract java.util.Collection getTimes();

    public abstract void setTimes(java.util.Collection times);

    public abstract java.util.Collection getSubProjects();

    public abstract void setSubProjects(java.util.Collection subProjects);

    public abstract ProjectFreeText getDescriptionText();

    public abstract void setDescriptionText(ProjectFreeText descriptionText);

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setProjectId(PKGenerator.i.nextKey(ProjectConstants.TABLE_PROJECT));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((ProjectFreeText) descriptionText);
    }
}
