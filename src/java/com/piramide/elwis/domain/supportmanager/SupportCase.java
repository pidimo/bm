package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:31:58 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCase extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCaseId();

    void setCaseId(Integer caseId);

    String getCaseTitle();

    void setCaseTitle(String title);

    Integer getCaseTypeId();

    void setCaseTypeId(Integer caseTypeId);

    Integer getCloseDate();

    void setCloseDate(Integer closeDate);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getExpireDate();

    void setExpireDate(Integer expireDate);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    String getKeywords();

    void setKeywords(String keywords);

    Integer getFromUserId();

    void setFromUserId(Integer fromUserId);

    String getNumber();

    void setNumber(String number);

    Integer getOpenByUserId();

    void setOpenByUserId(Integer openByUserId);

    Integer getOpenDate();

    void setOpenDate(Integer openDate);

    Integer getPriorityId();

    void setPriorityId(Integer priorityId);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getSeverityId();

    void setSeverityId(Integer severityId);

    Integer getStateId();

    void setStateId(Integer stateId);

    Integer getTotalHours();

    void setTotalHours(Integer totalHours);

    Integer getToUserId();

    void setToUserId(Integer toUserId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getWorkLevelId();

    void setWorkLevelId(Integer workLevelId);

    SupportCaseType getSupportCaseType();

    void setSupportCaseType(SupportCaseType supportCaseType);

    SupportCaseSeverity getSupportCaseSeverity();

    void setSupportCaseSeverity(SupportCaseSeverity supportCaseSeverity);

    Collection getSupportAttachs();

    void setSupportAttachs(Collection attachs);

    SupportCaseWorkLevel getSupportCaseWorkLevel();

    void setSupportCaseWorkLevel(SupportCaseWorkLevel supportCaseWorkLevel);

    State getState();

    void setState(State state);

    Collection getActivities();

    void setActivities(Collection activities);

    void setDescriptionText(EJBLocalObject descriptionText);

    SupportFreeText getDescriptionText();

    void setDescriptionText(SupportFreeText descriptionText);
}
