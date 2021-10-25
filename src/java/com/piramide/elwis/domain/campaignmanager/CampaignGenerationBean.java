package com.piramide.elwis.domain.campaignmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenerationBean.java 10517 2015-02-26 01:45:04Z miguel ${NAME}.java, v1.0 30-may-2008 11:03:24 Miky Exp $
 */

public abstract class CampaignGenerationBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setGenerationId(PKGenerator.i.nextKey(CampaignConstants.TABLE_CAMPAIGNGENERATION));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public CampaignGenerationBean() {
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

    public abstract Integer getGenerationId();

    public abstract void setGenerationId(Integer generationId);

    public abstract Long getGenerationTime();

    public abstract void setGenerationTime(Long generationTime);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getUserId();

    public abstract void setUserId(Integer userId);

    public abstract Collection getCampaignActivityContacts();

    public abstract void setCampaignActivityContacts(Collection campaignActivityContacts);

    public abstract Collection getCampaignGenTexts();

    public abstract void setCampaignGenTexts(Collection campaignGenTexts);

    public abstract Collection getGenerationAttachs();

    public abstract void setGenerationAttachs(Collection generationAttachs);

    public abstract Integer getCampaignId();

    public abstract void setCampaignId(Integer campaignId);

    public abstract Campaign getCampaign();

    public abstract void setCampaign(Campaign campaign);

    public abstract Boolean getCreateComm();

    public abstract void setCreateComm(Boolean createComm);

    public abstract Integer getDocumentType();

    public abstract void setDocumentType(Integer documentType);

    public abstract String getEmployeeMail();

    public abstract void setEmployeeMail(String employeeMail);

    public abstract Integer getSenderEmployeeId();

    public abstract void setSenderEmployeeId(Integer senderEmployeeId);

    public abstract String getSenderPrefix();

    public abstract void setSenderPrefix(String senderPrefix);

    public abstract Integer getSenderPrefixType();

    public abstract void setSenderPrefixType(Integer senderPrefixType);

    public abstract String getSubject();

    public abstract void setSubject(String subject);

    public abstract Integer getTelecomTypeId();

    public abstract void setTelecomTypeId(Integer telecomTypeId);

    public abstract CampaignSentLog getCampaignSentLog();

    public abstract void setCampaignSentLog(CampaignSentLog campaignSentLog);

    public abstract String getRequestLocale();

    public abstract void setRequestLocale(String requestLocale);

    public abstract String getNotificationMail();

    public abstract void setNotificationMail(String notificationMail);
}
