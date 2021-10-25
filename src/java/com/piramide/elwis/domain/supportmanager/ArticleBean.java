package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:08:50 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ArticleBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setArticleId(PKGenerator.i.nextKey(SupportConstants.TABLE_ARTICLE));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getArticleId();

    public abstract void setArticleId(Integer articleId);

    public abstract String getArticleTitle();

    public abstract void setArticleTitle(String articleTitle);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContentId();

    public abstract void setContentId(Integer contentId);

    public abstract Long getCreateDateTime();

    public abstract void setCreateDateTime(Long createDateTime);

    public abstract Integer getCreateUserId();

    public abstract void setCreateUserId(Integer createUserId);

    public abstract String getKeywords();

    public abstract void setKeywords(String keywords);

    public abstract String getNumber();

    public abstract void setNumber(String number);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getPublishedTo();

    public abstract void setPublishedTo(Integer publishedTo);

    public abstract Integer getRootQuestionId();

    public abstract void setRootQuestionId(Integer rootQuestionId);

    public abstract Integer getUpdateUserId();

    public abstract void setUpdateUserId(Integer updateUserId);

    public abstract Long getUpdateDateTime();

    public abstract void setUpdateDateTime(Long updateDateTime);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getViewTimes();

    public abstract void setViewTimes(Integer viewTimes);

    public abstract Long getVisitDateTime();

    public abstract void setVisitDateTime(Long visitDateTime);

    public abstract Integer getVote1();

    public abstract void setVote1(Integer vote1);

    public abstract Integer getVote2();

    public abstract void setVote2(Integer vote2);

    public abstract Integer getVote3();

    public abstract void setVote3(Integer vote3);

    public abstract Integer getVote4();

    public abstract void setVote4(Integer vote4);

    public abstract Integer getVote5();

    public abstract void setVote5(Integer vote5);

    public abstract Collection getArticleComment();

    public abstract void setArticleComment(Collection articleComment);

    public abstract Collection getArticleRelated();

    public abstract void setArticleRelated(Collection articleRelated);

    public abstract Collection getArticleLink();

    public abstract void setArticleLink(Collection articleLink);

    public abstract Collection getArticleHistory();

    public abstract void setArticleHistory(Collection articleHistory);

    public abstract Collection getArticleRating();

    public abstract void setArticleRating(Collection articleRating);

    public abstract Collection getSupportAttach();

    public abstract void setSupportAttach(Collection supportAttach);

    public abstract SupportFreeText getSupportFreeText();

    public abstract void setSupportFreeText(SupportFreeText supportFreeText);

    public abstract String ejbSelectMaxArticleNumber(Integer companyId) throws FinderException;

    public String ejbHomeSelectMaxArticleNumber(Integer companyId) throws FinderException {
        return ejbSelectMaxArticleNumber(companyId);
    }
}
