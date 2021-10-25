package com.piramide.elwis.domain.supportmanager;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:26:10 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class SupportContactBean implements EntityBean {
    EntityContext entityContext;

    public SupportContactPK ejbCreate(Integer activityId, Integer caseId, Integer companyId, Integer contactId) throws CreateException {
        setActivityId(activityId);
        setCaseId(caseId);
        setCompanyId(companyId);
        setContactId(contactId);
        return null;
    }

    public void ejbPostCreate(Integer activityId, Integer caseId, Integer companyId, Integer contactId) throws CreateException {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getCaseId();

    public abstract void setCaseId(Integer caseId);
}
