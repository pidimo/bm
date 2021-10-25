package com.piramide.elwis.domain.project;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public abstract class ProjectTimeLimitBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTimeLimitId(PKGenerator.i.nextKey(ProjectConstants.TABLE_PROJECT_TIME_LIMIT));
        setVersion(1);
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public ProjectTimeLimitBean() {
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

    public abstract Integer getAssigneeId();

    public abstract void setAssigneeId(Integer assigneeId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Boolean getHasTimeLimit();

    public abstract void setHasTimeLimit(Boolean hasTimeLimit);

    public abstract BigDecimal getInvoiceLimit();

    public abstract void setInvoiceLimit(BigDecimal invoiceLimit);

    public abstract BigDecimal getNoInvoiceLimit();

    public abstract void setNoInvoiceLimit(BigDecimal noInvoiceLimit);

    public abstract Integer getProjectId();

    public abstract void setProjectId(Integer projectId);

    public abstract Integer getSubProjectId();

    public abstract void setSubProjectId(Integer subProjectId);

    public abstract Integer getTimeLimitId();

    public abstract void setTimeLimitId(Integer timeLimitId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Project getProject();

    public abstract void setProject(Project project);
}
