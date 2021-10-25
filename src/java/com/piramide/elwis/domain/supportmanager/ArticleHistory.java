package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:43:06 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleHistory extends EJBLocalObject {
    Integer getAction();

    void setAction(Integer action);

    Integer getArticleId();

    void setArticleId(Integer articleId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getHistoryId();

    void setHistoryId(Integer historyId);

    Long getLogDateTime();

    void setLogDateTime(Long logDateTime);

    Integer getUserId();

    void setUserId(Integer userId);
}
