package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:46:30 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleRating extends EJBLocalObject {

    Integer getArticleId();

    void setArticleId(Integer articleId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getUserId();

    void setUserId(Integer userId);
}
