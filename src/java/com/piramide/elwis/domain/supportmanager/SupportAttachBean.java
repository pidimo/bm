package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:29:07 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class SupportAttachBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setAttachId(PKGenerator.i.nextKey(SupportConstants.TABLE_SUPPORT_ATTACH));
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

    public abstract Integer getActivityId();

    public abstract void setActivityId(Integer activityId);

    public abstract Integer getArticleId();

    public abstract void setArticleId(Integer articleId);

    public abstract Integer getAttachId();

    public abstract void setAttachId(Integer attachId);

    public abstract Integer getCaseId();

    public abstract void setCaseId(Integer caseId);

    public abstract String getComment();

    public abstract void setComment(String comment);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract String getSupportAttachName();

    public abstract void setSupportAttachName(String filename);

    public abstract Integer getSize();

    public abstract void setSize(Integer size);

    public abstract Long getUploadDateTime();

    public abstract void setUploadDateTime(Long uploadDateTime);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getFreetextId();

    public abstract void setFreetextId(Integer freetextId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);
}
