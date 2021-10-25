package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * LoggedCompany Entity Bean class
 *
 * @author Fernando Montaño
 * @version $Id: CompanyBean.java 12654 2017-03-24 23:46:39Z miguel $
 */


public abstract class CompanyBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        //setCompanyId(PKGenerator.i.nextKey(ContactConstants.TABLE_COMPANY));
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


    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getMaxAttachSize();

    public abstract void setMaxAttachSize(Integer maxAttachSize);

    public abstract Integer getRowsPerPage();

    public abstract void setRowsPerPage(Integer rowsPerPage);

    public abstract String getStyle();

    public abstract void setStyle(String style);

    public abstract Integer getTimeout();

    public abstract void setTimeout(Integer timeout);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getLogin();

    public abstract void setLogin(String login);

    public abstract String getMailDomain();

    public abstract void setMailDomain(String mailDomain);

    public abstract Integer getRoutePageId();

    public abstract void setRoutePageId(Integer routePageId);

    public abstract AdminFreeText getRoutePage();

    public abstract void setRoutePage(AdminFreeText routePage);

    public abstract Integer getStartLicenseDate();

    public abstract void setStartLicenseDate(Integer startLicenseDate);

    public abstract Integer getFinishLicenseDate();

    public abstract void setFinishLicenseDate(Integer finishLicenseDate);

    public abstract Integer getCompanyType();

    public abstract void setCompanyType(Integer companyType);

    public abstract Integer getUsersAllowed();

    public abstract void setUsersAllowed(Integer usersAllowed);

    public abstract Collection getCompanyModules();

    public abstract void setCompanyModules(Collection companyModules);

    public abstract Boolean getActive();

    public abstract void setActive(Boolean active);

    public abstract Boolean getIsDefault();

    public abstract void setIsDefault(Boolean isDefault);

    public abstract String getTelecomTypeStatus();

    public abstract void setTelecomTypeStatus(String telecomTypeStatus);

    public abstract Integer getLogoId();

    public abstract void setLogoId(Integer logoId);

    public abstract AdminFreeText getLogo();

    public abstract void setLogo(AdminFreeText logo);

    public void setRoutePage(EJBLocalObject descriptionText) {
        setRoutePage((AdminFreeText) descriptionText);
    }

    public abstract Integer getCopyTemplate();

    public abstract void setCopyTemplate(Integer isTemplate);

    public abstract String getLanguage();

    public abstract void setLanguage(String language);

    public abstract Integer getInvoiceDaysSend();

    public abstract void setInvoiceDaysSend(Integer invoiceDaysSend);

    public abstract Integer getSequenceRuleIdForInvoice();

    public abstract void setSequenceRuleIdForInvoice(Integer sequenceRuleIdForInvoice);

    public abstract Integer getSequenceRuleIdForCreditNote();

    public abstract void setSequenceRuleIdForCreditNote(Integer sequenceRuleIdforCreditNote);

    public abstract Integer getNetGross();

    public abstract void setNetGross(Integer netGross);

    public abstract Integer getSalutationId();

    public abstract void setSalutationId(Integer salutationId);

    public abstract String getEmailContract();

    public abstract void setEmailContract(String emailContract);

    public abstract String getTimeZone();

    public abstract void setTimeZone(String timeZone);

    public abstract String getMediaType();

    public abstract void setMediaType(String mediaType);

    public abstract Integer getMaxMaxAttachSize();

    public abstract void setMaxMaxAttachSize(Integer maxMaxAttachSize);

    public abstract Integer getInvoiceMailTemplateId();

    public abstract void setInvoiceMailTemplateId(Integer invoiceMailTemplateId);

    public abstract Boolean getMobileActive();

    public abstract void setMobileActive(Boolean mobileActive);

    public abstract Integer getMobileEndLicense();

    public abstract void setMobileEndLicense(Integer mobileEndLicense);

    public abstract Integer getMobileStartLicense();

    public abstract void setMobileStartLicense(Integer mobileStartLicense);

    public abstract Integer getMobileUserAllowed();

    public abstract void setMobileUserAllowed(Integer mobileUserAllowed);
}
