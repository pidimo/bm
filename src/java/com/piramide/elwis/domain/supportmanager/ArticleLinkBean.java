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
 * Time: 11:35:10 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class ArticleLinkBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setLinkId(PKGenerator.i.nextKey(SupportConstants.TABLE_ARTICLELINK));
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

    public abstract String getComment();

    public abstract void setComment(String comment);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Long getCreateDateTime();

    public abstract void setCreateDateTime(Long createDateTime);

    public abstract Integer getCreateUserId();

    public abstract void setCreateUserId(Integer createUserId);

    public abstract Integer getLinkId();

    public abstract void setLinkId(Integer linkId);

    public abstract String getUrl();

    public abstract void setUrl(String url);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);
}
