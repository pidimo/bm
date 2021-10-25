package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.3.5
 */
public interface UserArticleAccess extends EJBLocalObject {
    Integer getArticleId();

    void setArticleId(Integer articleId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUserGroupId();

    void setUserGroupId(Integer userGroupId);

    Article getArticle();

    void setArticle(Article article);
}
