/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignCriterionValueBean.java 10314 2013-02-06 19:04:04Z miguel ${NAME}.java, v 2.0 28-jul-2004 16:55:42 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import java.util.Collection;

public abstract class CampaignCriterionValueBean implements EntityBean {
    public CampaignCriterionValueBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
    }

    public void unsetEntityContext() throws EJBException {
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

    public abstract Integer getCampCriterionValueId();

    public abstract void setCampCriterionValueId(Integer campCriterionValueId);

    public abstract Integer getTableId();

    public abstract void setTableId(Integer tableId);

    public abstract String getDescriptionKey();

    public abstract void setDescriptionKey(String descriptionKey);

    public abstract String getField();

    public abstract void setField(String field);

    public abstract Integer getFieldType();

    public abstract void setFieldType(Integer fieldType);

    public abstract String getFieldName();

    public abstract void setFieldName(String fieldName);

    public abstract String getTableName();

    public abstract void setTableName(String tableName);

    public abstract Collection getCampaignCriterion();

    public abstract void setCampaignCriterion(Collection campaignCriterion);

    public abstract String getRelationField();

    public abstract void setRelationField(String relationField);

    public abstract String getContactPersonField();

    public abstract void setContactPersonField(String contactPersonField);
}
