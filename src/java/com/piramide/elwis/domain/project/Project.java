/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:12:44 $
 */
package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;

public interface Project extends EJBLocalObject {
    Integer getAccountId();

    void setAccountId(Integer accountId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getCustomerId();

    void setCustomerId(Integer customerId);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    Integer getEndDate();

    void setEndDate(Integer endDate);

    Boolean getHasTimeLimit();

    void setHasTimeLimit(Boolean hasTimeLimit);

    java.math.BigDecimal getPlannedInvoice();

    void setPlannedInvoice(java.math.BigDecimal plannedInvoice);

    java.math.BigDecimal getPlannedNoInvoice();

    void setPlannedNoInvoice(java.math.BigDecimal plannedNoInvoice);

    Integer getProjectId();

    void setProjectId(Integer projectId);

    String getName();

    void setName(String name);

    Integer getResponsibleId();

    void setResponsibleId(Integer responsibleId);

    Integer getStartDate();

    void setStartDate(Integer startDate);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getToBeInvoiced();

    void setToBeInvoiced(Integer toBeInvoiced);

    java.math.BigDecimal getTotalInvoice();

    void setTotalInvoice(java.math.BigDecimal totalInvoice);

    java.math.BigDecimal getTotalNoInvoice();

    void setTotalNoInvoice(java.math.BigDecimal totalNoInvoice);

    Integer getVersion();

    void setVersion(Integer version);

    java.util.Collection getTimes();

    void setTimes(java.util.Collection times);

    java.util.Collection getSubProjects();

    void setSubProjects(java.util.Collection subProjects);

    ProjectFreeText getDescriptionText();

    void setDescriptionText(ProjectFreeText descriptionText);

    void setDescriptionText(EJBLocalObject descriptionText);
}
