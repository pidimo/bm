package com.piramide.elwis.domain.supportmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SupportConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:31:57 PM
 * To change this template use File | Settings | File Templates.
 */

public abstract class SupportCaseBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setCaseId(PKGenerator.i.nextKey(SupportConstants.TABLE_SUPPORT_CASE));
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

    public abstract Integer getAddressId();

    public abstract void setAddressId(Integer addressId);

    public abstract Integer getCaseId();

    public abstract void setCaseId(Integer caseId);

    public abstract String getCaseTitle();

    public abstract void setCaseTitle(String title);

    public abstract Integer getCaseTypeId();

    public abstract void setCaseTypeId(Integer caseTypeId);

    public abstract Integer getCloseDate();

    public abstract void setCloseDate(Integer closeDate);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getExpireDate();

    public abstract void setExpireDate(Integer expireDate);

    public abstract Integer getDescriptionId();

    public abstract void setDescriptionId(Integer descriptionId);

    public abstract String getKeywords();

    public abstract void setKeywords(String keywords);

    public abstract Integer getFromUserId();

    public abstract void setFromUserId(Integer fromUserId);

    public abstract String getNumber();

    public abstract void setNumber(String number);

    public abstract Integer getOpenByUserId();

    public abstract void setOpenByUserId(Integer openByUserId);

    public abstract Integer getOpenDate();

    public abstract void setOpenDate(Integer openDate);

    public abstract Integer getPriorityId();

    public abstract void setPriorityId(Integer priorityId);

    public abstract Integer getProductId();

    public abstract void setProductId(Integer productId);

    public abstract Integer getSeverityId();

    public abstract void setSeverityId(Integer severityId);

    public abstract Integer getStateId();

    public abstract void setStateId(Integer stateId);

    public abstract Integer getTotalHours();

    public abstract void setTotalHours(Integer totalHours);

    public abstract Integer getToUserId();

    public abstract void setToUserId(Integer toUserId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getWorkLevelId();

    public abstract void setWorkLevelId(Integer workLevelId);

    public abstract SupportCaseType getSupportCaseType();

    public abstract void setSupportCaseType(SupportCaseType supportCaseType);

    public abstract SupportCaseSeverity getSupportCaseSeverity();

    public abstract void setSupportCaseSeverity(SupportCaseSeverity supportCaseSeverity);

    public abstract Collection getSupportAttachs();

    public abstract void setSupportAttachs(Collection attachs);

    public abstract SupportCaseWorkLevel getSupportCaseWorkLevel();

    public abstract void setSupportCaseWorkLevel(SupportCaseWorkLevel supportCaseWorkLevel);

    public abstract State getState();

    public abstract void setState(State state);

    public abstract Collection getActivities();

    public abstract void setActivities(Collection activities);

    public void setDescriptionText(EJBLocalObject descriptionText) {
        setDescriptionText((SupportFreeText) descriptionText);
    }

    public abstract SupportFreeText getDescriptionText();

    public abstract void setDescriptionText(SupportFreeText descriptionText);

    public abstract String ejbSelectMaxSupportCaseNumber(Integer companyId) throws FinderException;

    public String ejbHomeSelectMaxSupportCaseNumber(Integer companyId) throws FinderException {
        return ejbSelectMaxSupportCaseNumber(companyId);
    }
}
