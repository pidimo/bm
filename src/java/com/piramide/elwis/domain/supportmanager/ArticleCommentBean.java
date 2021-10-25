package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Aug 11, 2005
 * Time: 11:29:10 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ArticleCommentBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCommentId(PKGenerator.i.nextKey(SupportConstants.TABLE_ARTICLECOMMENT));
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

    public abstract Integer getCommentId();

    public abstract void setCommentId(Integer commentId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Long getCreateDateTime();

    public abstract void setCreateDateTime(Long createDateTime);

    public abstract Integer getCreateUserId();

    public abstract void setCreateUserId(Integer createUserId);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract SupportFreeText getSupportFreeText();

    public abstract void setSupportFreeText(SupportFreeText supportFreeText);
}
