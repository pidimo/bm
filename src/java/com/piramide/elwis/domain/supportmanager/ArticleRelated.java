package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:32:03 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleRelated extends EJBLocalObject {

    Integer getArticleId();

    void setArticleId(Integer articleId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getRelatedArticleId();

    void setRelatedArticleId(Integer relatedArticleId);

    /*Article getArticle();

    void setArticle(Article article);*/
}
