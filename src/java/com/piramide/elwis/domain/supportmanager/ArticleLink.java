package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:35:14 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleLink extends EJBLocalObject {
    Integer getArticleId();

    void setArticleId(Integer articleId);

    String getComment();

    void setComment(String comment);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Long getCreateDateTime();

    void setCreateDateTime(Long createDateTime);

    Integer getCreateUserId();

    void setCreateUserId(Integer createUserId);

    Integer getLinkId();

    void setLinkId(Integer linkId);

    String getUrl();

    void setUrl(String url);

    Integer getVersion();

    void setVersion(Integer version);
}
