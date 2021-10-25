package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:29:14 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleComment extends EJBLocalObject {
    Integer getArticleId();

    void setArticleId(Integer articleId);

    Integer getCommentId();

    void setCommentId(Integer commentId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Long getCreateDateTime();

    void setCreateDateTime(Long createDateTime);

    Integer getCreateUserId();

    void setCreateUserId(Integer createUserId);

    Integer getDescriptionId();

    void setDescriptionId(Integer descriptionId);

    SupportFreeText getSupportFreeText();

    void setSupportFreeText(SupportFreeText supportFreeText);
}
