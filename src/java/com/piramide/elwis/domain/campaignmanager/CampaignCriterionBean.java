/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignCriterionBean.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 17-jun-2004 16:22:21 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;

import javax.ejb.*;

public abstract class CampaignCriterionBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        setCampaignCriterionId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGNCRITERION));
        setVersion(new Integer(1));
        DTOFactory.i.copyFromDTO(dto, this, false);
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


    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Integer getCategoryId();

    public abstract void setCategoryId(Integer categoryId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getCampaignCriterionId();

    public abstract void setCampaignCriterionId(Integer campaignCriterionId);

    public abstract Integer getNumberHits();

    public abstract void setNumberHits(Integer numberHits);

    public abstract String getOperator();

    public abstract void setOperator(String operator);

    public abstract Integer getValueId();

    public abstract void setValueId(Integer value);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getCampCriterionValueId();

    public abstract void setCampCriterionValueId(Integer campcriterionvalueid);

    public abstract CampaignFreeText getCampaignFreeText();

    public abstract void setCampaignFreeText(CampaignFreeText campaignFreeText);
}
