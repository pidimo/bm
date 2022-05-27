package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * LoggedCompany bean local interface
 *
 * @author Fernando Montaño
 * @version $Id: Company.java 12654 2017-03-24 23:46:39Z miguel $
 */

public interface Company extends EJBLocalObject {

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getMaxAttachSize();

    void setMaxAttachSize(Integer maxAttachSize);

    Integer getRowsPerPage();

    void setRowsPerPage(Integer rowsPerPage);

    String getStyle();

    void setStyle(String style);

    Integer getTimeout();

    void setTimeout(Integer timeout);

    Integer getVersion();

    void setVersion(Integer version);

    String getLogin();

    void setLogin(String login);

    String getMailDomain();

    void setMailDomain(String mailDomain);

    Integer getRoutePageId();

    void setRoutePageId(Integer routePageId);

    AdminFreeText getRoutePage();

    void setRoutePage(AdminFreeText routePage);

    Integer getStartLicenseDate();

    void setStartLicenseDate(Integer startLicenseDate);

    Integer getFinishLicenseDate();

    void setFinishLicenseDate(Integer finishLicenseDate);

    Integer getCompanyType();

    void setCompanyType(Integer companyType);

    Integer getUsersAllowed();

    void setUsersAllowed(Integer usersAllowed);

    Collection getCompanyModules();

    void setCompanyModules(Collection companyModules);

    Boolean getActive();

    void setActive(Boolean active);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);


    String getTelecomTypeStatus();

    void setTelecomTypeStatus(String telecomTypeStatus);

    Integer getLogoId();

    void setLogoId(Integer logoId);

    AdminFreeText getLogo();

    void setLogo(AdminFreeText logo);

    public void setRoutePage(EJBLocalObject descriptionText);

    Integer getCopyTemplate();

    void setCopyTemplate(Integer isTemplate);

    String getLanguage();

    void setLanguage(String language);

    Integer getInvoiceDaysSend();

    void setInvoiceDaysSend(Integer invoiceDaysSend);

    Integer getSequenceRuleIdForInvoice();

    void setSequenceRuleIdForInvoice(Integer sequenceRuleIdForInvoice);

    Integer getSequenceRuleIdForCreditNote();

    void setSequenceRuleIdForCreditNote(Integer sequenceRuleIdforCreditNote);

    Integer getNetGross();

    void setNetGross(Integer netGross);

    Integer getSalutationId();

    void setSalutationId(Integer salutationId);

    String getEmailContract();

    void setEmailContract(String emailContract);

    String getTimeZone();

    void setTimeZone(String timeZone);

    String getMediaType();

    void setMediaType(String mediaType);

    Integer getMaxMaxAttachSize();

    void setMaxMaxAttachSize(Integer maxMaxAttachSize);

    Integer getInvoiceMailTemplateId();

    void setInvoiceMailTemplateId(Integer invoiceMailTemplateId);

    Boolean getMobileActive();

    void setMobileActive(Boolean mobileActive);

    Integer getMobileEndLicense();

    void setMobileEndLicense(Integer mobileEndLicense);

    Integer getMobileStartLicense();

    void setMobileStartLicense(Integer mobileStartLicense);

    Integer getMobileUserAllowed();

    void setMobileUserAllowed(Integer mobileUserAllowed);
    
    String getVatId();
    
    void setVatId(String vatId);
    
    Integer getXinvoiceMailTemplateId();

    void setXinvoiceMailTemplateId(Integer xinvoiceMailTemplateId);
}
