/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:19:40 $
 */
package com.piramide.elwis.domain.project;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.ProjectConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.math.BigDecimal;
import java.util.Date;

public abstract class ProjectTimeBean implements EntityBean {
    public ProjectTimeBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getConfirmedById();

    public abstract void setConfirmedById(Integer confirmedById);

    public abstract Integer getDate();

    public abstract void setDate(Integer date);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Integer getProjectId();

    public abstract void setProjectId(Integer projectId);

    public abstract Integer getStatus();

    public abstract void setStatus(Integer status);

    public abstract Integer getSubProjectId();

    public abstract void setSubProjectId(Integer subProjectId);

    public abstract Integer getTimeId();

    public abstract void setTimeId(Integer timeId);

    public abstract java.math.BigDecimal getTime();

    public abstract void setTime(java.math.BigDecimal time);

    public abstract Boolean getToBeInvoiced();

    public abstract void setToBeInvoiced(Boolean toBeInvoiced);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Project getProject();

    public abstract void setProject(Project project);

    public abstract ProjectFreeText getDescriptionText();

    public abstract void setDescriptionText(ProjectFreeText descriptionText);

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setTimeId(PKGenerator.i.nextKey(ProjectConstants.TABLE_PROJECT_TIME));
        setVersion(1);
        setCreatedDate(DateUtils.dateToInteger(new Date()));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public abstract BigDecimal ejbSelectSumTimes(Integer projectId, Integer assigneeId, Integer companyId, Integer date) throws FinderException;

    public BigDecimal ejbHomeSelectSumTimes(Integer projectId, Integer assigneeId, Integer companyId, Integer date) throws FinderException {
        return ejbSelectSumTimes(projectId, assigneeId, companyId, date);
    }

    public abstract BigDecimal ejbSelectSumTimesByProject(Integer projectId, Integer companyId, Boolean toBeInvoiced) throws FinderException;

    public BigDecimal ejbHomeSelectSumTimesByProject(Integer projectId, Integer companyId, Boolean toBeInvoiced) throws FinderException {
        return ejbSelectSumTimesByProject(projectId, companyId, toBeInvoiced);
    }

    public abstract BigDecimal ejbSelectSumTimesByProjectAssigneeSubProject(Integer projectId, Integer assigneeId, Integer subProjectId, Boolean toBeInvoiced, Integer timeId) throws FinderException;

    public BigDecimal ejbHomeSelectSumTimesByProjectAssigneeSubProject(Integer projectId, Integer assigneeId, Integer subProjectId, Boolean toBeInvoiced, Integer timeId) throws FinderException {
        return ejbSelectSumTimesByProjectAssigneeSubProject(projectId, assigneeId, subProjectId, toBeInvoiced, timeId);
    }

    public abstract BigDecimal ejbSelectSumTimesByProjectSubProject(Integer projectId, Integer subProjectId, Boolean toBeInvoiced) throws FinderException;

    public BigDecimal ejbHomeSelectSumTimesByProjectSubProject(Integer projectId, Integer subProjectId, Boolean toBeInvoiced) throws FinderException {
        return ejbSelectSumTimesByProjectSubProject(projectId, subProjectId, toBeInvoiced);
    }

    public abstract Integer getAssigneeId();

    public abstract void setAssigneeId(Integer assigneeId);

    public abstract Integer getCreatedDate();

    public abstract void setCreatedDate(Integer createdDate);

    public abstract Integer getReleasedDate();

    public abstract void setReleasedDate(Integer releasedDate);

    public abstract Integer getReleasedUserId();

    public abstract void setReleasedUserId(Integer releasedUserId);

    public abstract Long getFromDateTime();

    public abstract void setFromDateTime(Long fromDateTime);

    public abstract Long getToDateTime();

    public abstract void setToDateTime(Long toDateTime);
}
