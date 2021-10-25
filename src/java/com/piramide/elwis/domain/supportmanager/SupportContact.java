package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:26:11 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportContact extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getCaseId();

    void setCaseId(Integer caseId);
}
