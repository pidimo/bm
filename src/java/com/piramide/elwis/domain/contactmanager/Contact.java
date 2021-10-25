package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;
import java.util.Date;

/**
 * @author Yumi
 * @version $Id: Contact.java 11332 2015-10-27 23:44:46Z miguel ${NAME}.java, v 2.0 13-may-2004 18:25:50 Yumi Exp $
 */

public interface Contact extends EJBLocalObject {
    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getDateFinish();

    void setDateFinish(Integer dateFinish);

    Integer getDateStart();

    void setDateStart(Integer dateStart);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getContactPersonId();

    void setContactPersonId(Integer contactPersonId);

    Integer getEmployeeId();

    void setEmployeeId(Integer employeeId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    String getNote();

    void setNote(String note);

    Integer getProcessId();

    void setProcessId(Integer processId);

    Integer getTemplateId();

    void setTemplateId(Integer templateId);

    Integer getVersion();

    void setVersion(Integer version);

    String getContactNumber();

    void setContactNumber(String contactNumber);

    String getStatus();

    void setStatus(String status);

    String getType();

    void setType(String type);

    ContactFreeText getContactFreeText();

    void setContactFreeText(ContactFreeText contactFreeText);

    void setDateFinishField(Date value);

    Date getDateFinishField();

    void setDateStartField(Date value);

    Date getDateStartField();

    ContactPerson getContactPerson();

    void setContactPerson(ContactPerson contactPerson);

    Integer getInOut();

    void setInOut(Integer inout);

    void setEmployee(Employee employee);

    Employee getEmployee();

    Boolean getIsAction();

    void setIsAction(Boolean isAction);

    void setContactFreeText(EJBLocalObject contactFreeText);

    Integer getProbability();

    void setProbability(Integer probability);

    String getDocumentFileName();

    void setDocumentFileName(String documentFileName);

    Integer getAdditionalAddressId();

    void setAdditionalAddressId(Integer additionalAddressId);

    Integer getWebDocumentId();

    void setWebDocumentId(Integer webDocumentId);

    String getWebGenerateUUID();

    void setWebGenerateUUID(String webGenerateUUID);
}
