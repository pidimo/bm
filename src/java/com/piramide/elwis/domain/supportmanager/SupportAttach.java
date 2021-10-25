package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:29:08 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportAttach extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    Integer getArticleId();

    void setArticleId(Integer articleId);

    Integer getAttachId();

    void setAttachId(Integer attachId);

    Integer getCaseId();

    void setCaseId(Integer caseId);

    String getComment();

    void setComment(String comment);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getSupportAttachName();

    void setSupportAttachName(String filename);

    Integer getSize();

    void setSize(Integer size);

    Long getUploadDateTime();

    void setUploadDateTime(Long uploadDateTime);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getFreetextId();

    void setFreetextId(Integer freetextId);

    Integer getUserId();

    void setUserId(Integer userId);
}
