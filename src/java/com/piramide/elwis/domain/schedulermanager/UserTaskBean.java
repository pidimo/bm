package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: May 31, 2005
 * Time: 2:11:52 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class UserTaskBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
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

    public abstract Integer getScheduledUserId();

    public abstract void setScheduledUserId(Integer scheduledUserId);

    public abstract Integer getStatusId();

    public abstract void setStatusId(Integer statusId);

    public abstract Integer getNoteId();

    public abstract void setNoteId(Integer noteId);

    public abstract SchedulerFreeText getSchedulerFreeText();

    public abstract void setSchedulerFreeText(SchedulerFreeText schedulerFreeText);

    public abstract ScheduledUser getScheduledUser();

    public abstract void setScheduledUser(ScheduledUser scheduledUser);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer ejbSelectCountByStatus(Integer taskId, Integer status) throws FinderException;

    public Integer ejbHomeSelectCountByStatus(Integer taskId, Integer status) throws FinderException {
        return ejbSelectCountByStatus(taskId, status);
    }

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);
}
