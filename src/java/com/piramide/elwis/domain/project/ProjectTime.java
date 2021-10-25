/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:19:40 $
 */
package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;

public interface ProjectTime extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getConfirmedById();

    void setConfirmedById(Integer confirmedById);

    Integer getDate();

    void setDate(Integer date);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getProjectId();

    void setProjectId(Integer projectId);

    Integer getStatus();

    void setStatus(Integer status);

    Integer getSubProjectId();

    void setSubProjectId(Integer subProjectId);

    Integer getTimeId();

    void setTimeId(Integer timeId);

    java.math.BigDecimal getTime();

    void setTime(java.math.BigDecimal time);

    Boolean getToBeInvoiced();

    void setToBeInvoiced(Boolean toBeInvoiced);

    Integer getUserId();

    void setUserId(Integer userId);

    Integer getVersion();

    void setVersion(Integer version);

    Project getProject();

    void setProject(Project project);

    ProjectFreeText getDescriptionText();

    void setDescriptionText(ProjectFreeText descriptionText);

    Integer getAssigneeId();

    void setAssigneeId(Integer assigneeId);

    Integer getCreatedDate();

    void setCreatedDate(Integer createdDate);

    Integer getReleasedDate();

    void setReleasedDate(Integer releasedDate);

    Integer getReleasedUserId();

    void setReleasedUserId(Integer releasedUserId);

    Long getFromDateTime();

    void setFromDateTime(Long fromDateTime);

    Long getToDateTime();

    void setToDateTime(Long toDateTime);
}
