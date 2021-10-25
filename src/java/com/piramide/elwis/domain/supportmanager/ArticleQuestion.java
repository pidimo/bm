package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:19:47 AM
 * To change this template use File | Settings | File Templates.
 */

public interface ArticleQuestion extends EJBLocalObject {
    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Long getCreateDateTime();

    void setCreateDateTime(Long createDateTime);

    Integer getCreateUserId();

    void setCreateUserId(Integer createUserId);

    Integer getDetailId();

    void setDetailId(Integer detailId);

    Integer getPublished();

    void setPublished(Integer published);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getQuestionId();

    void setQuestionId(Integer questionId);

    String getSummary();

    void setSummary(String summary);

    Integer getVersion();

    void setVersion(Integer version);

    SupportFreeText getSupportFreeText();

    void setSupportFreeText(SupportFreeText supportFreeText);
}
