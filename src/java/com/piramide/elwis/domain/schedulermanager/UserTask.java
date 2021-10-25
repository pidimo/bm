package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: May 31, 2005
 * Time: 2:11:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserTask extends EJBLocalObject {
    Integer getScheduledUserId();

    void setScheduledUserId(Integer scheduledUserId);

    Integer getStatusId();

    void setStatusId(Integer statusId);

    Integer getNoteId();

    void setNoteId(Integer noteId);

    SchedulerFreeText getSchedulerFreeText();

    void setSchedulerFreeText(SchedulerFreeText schedulerFreeText);

    ScheduledUser getScheduledUser();

    void setScheduledUser(ScheduledUser scheduledUser);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
