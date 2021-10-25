package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;
import java.math.BigDecimal;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public interface ProjectTimeLimit extends EJBLocalObject {
    Integer getAssigneeId();

    void setAssigneeId(Integer assigneeId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Boolean getHasTimeLimit();

    void setHasTimeLimit(Boolean hasTimeLimit);

    BigDecimal getInvoiceLimit();

    void setInvoiceLimit(BigDecimal invoiceLimit);

    BigDecimal getNoInvoiceLimit();

    void setNoInvoiceLimit(BigDecimal noInvoiceLimit);

    Integer getProjectId();

    void setProjectId(Integer projectId);

    Integer getSubProjectId();

    void setSubProjectId(Integer subProjectId);

    Integer getTimeLimitId();

    void setTimeLimitId(Integer timeLimitId);

    Integer getVersion();

    void setVersion(Integer version);

    Project getProject();

    void setProject(Project project);
}
