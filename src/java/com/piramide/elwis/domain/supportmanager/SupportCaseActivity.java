package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:52:17 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseActivity extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getCloseDate();

    void setCloseDate(Integer closeDate);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    SupportFreeText getDescriptionText();

    void setDescriptionText(SupportFreeText descriptionText);

    void setDescriptionText(EJBLocalObject descriptionText);

    Integer getFromUserId();

    void setFromUserId(Integer fromUserId);

    Integer getOpenDate();

    void setOpenDate(Integer openDate);

    Integer getParentActivityId();

    void setParentActivityId(Integer parentActivityId);

    Integer getStateId();

    void setStateId(Integer stateId);

    Integer getToUserId();

    void setToUserId(Integer toUserId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getWorkLevelId();

    void setWorkLevelId(Integer workLevelId);

    SupportCaseActivity getParentActivity();

    void setParentActivity(SupportCaseActivity parentActivity);

    Integer getIsOpen();

    void setIsOpen(Integer isOpen);

    SupportCase getSupportCase();

    void setSupportCase(SupportCase supportCase);

    State getState();

    void setState(State state);

    Integer getCaseId();

    void setCaseId(Integer caseId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
