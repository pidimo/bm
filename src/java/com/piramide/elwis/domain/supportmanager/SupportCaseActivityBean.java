package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:52:17 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class SupportCaseActivityBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setActivityId(PKGenerator.i.nextKey(SupportConstants.TABLE_CASE_ACTIVITY));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
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

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Integer getCloseDate();

    public abstract void setCloseDate(Integer closeDate);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract Integer getFromUserId();

    public abstract void setFromUserId(Integer fromUserId);

    public abstract Integer getOpenDate();

    public abstract void setOpenDate(Integer openDate);

    public abstract Integer getParentActivityId();

    public abstract void setParentActivityId(Integer parentActivityId);

    public abstract Integer getStateId();

    public abstract void setStateId(Integer stateId);

    public abstract Integer getToUserId();

    public abstract void setToUserId(Integer toUserId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer versionId);

    public abstract Integer getWorkLevelId();

    public abstract void setWorkLevelId(Integer workLevelId);

    public abstract SupportCaseActivity getParentActivity();

    public abstract void setParentActivity(SupportCaseActivity parentActivity);

    public abstract Integer getIsOpen();

    public abstract void setIsOpen(Integer isOpen);

    public abstract SupportFreeText getDescriptionText();

    public abstract void setDescriptionText(SupportFreeText value);

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((SupportFreeText) descriptionText);
    }

    public abstract SupportCase getSupportCase();

    public abstract void setSupportCase(SupportCase supportCase);

    public abstract State getState();

    public abstract void setState(State state);

    public abstract Integer getCaseId();

    public abstract void setCaseId(Integer caseId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
