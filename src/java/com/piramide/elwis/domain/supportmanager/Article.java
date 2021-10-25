package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:08:54 AM
 * To change this template use File | Settings | File Templates.
 */

public interface Article extends EJBLocalObject {
    Integer getArticleId();

    void setArticleId(Integer articleId);

    String getArticleTitle();

    void setArticleTitle(String articleTitle);

    Integer getCategoryId();

    void setCategoryId(Integer categoryId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContentId();

    void setContentId(Integer contentId);

    Long getCreateDateTime();

    void setCreateDateTime(Long createDateTime);

    Integer getCreateUserId();

    void setCreateUserId(Integer createUserId);

    String getKeywords();

    void setKeywords(String keywords);

    String getNumber();

    void setNumber(String number);

    Integer getProductId();

    void setProductId(Integer productId);

    Integer getPublishedTo();

    void setPublishedTo(Integer publishedTo);

    Integer getRootQuestionId();

    void setRootQuestionId(Integer rootQuestionId);

    Integer getUpdateUserId();

    void setUpdateUserId(Integer updateUserId);

    Long getUpdateDateTime();

    void setUpdateDateTime(Long updateDateTime);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getViewTimes();

    void setViewTimes(Integer viewTimes);

    Long getVisitDateTime();

    void setVisitDateTime(Long visitDateTime);

    Integer getVote1();

    void setVote1(Integer vote1);

    Integer getVote2();

    void setVote2(Integer vote2);

    Integer getVote3();

    void setVote3(Integer vote3);

    Integer getVote4();

    void setVote4(Integer vote4);

    Integer getVote5();

    void setVote5(Integer vote5);

    Collection getArticleComment();

    void setArticleComment(Collection articleComment);

    Collection getArticleRelated();

    void setArticleRelated(Collection articleRelated);

    Collection getArticleLink();

    void setArticleLink(Collection articleLink);

    Collection getArticleHistory();

    void setArticleHistory(Collection articleHistory);

    Collection getArticleRating();

    void setArticleRating(Collection articleRating);

    Collection getSupportAttach();

    void setSupportAttach(Collection supportAttach);

    SupportFreeText getSupportFreeText();

    void setSupportFreeText(SupportFreeText supportFreeText);
}
