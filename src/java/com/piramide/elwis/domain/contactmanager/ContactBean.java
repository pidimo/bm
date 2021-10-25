/**
 * @author Yumi
 * @version $Id: ContactBean.java 11332 2015-10-27 23:44:46Z miguel ${NAME}.java, v 2.0 13-may-2004 18:25:50 Yumi Exp $
 */
package com.piramide.elwis.domain.contactmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Date;

public abstract class ContactBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setContactId(PKGenerator.i.nextKey(ContactConstants.TABLE_CONTACT));
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

    public abstract Integer getDateFinish();

    public abstract void setDateFinish(Integer dateFinish);

    public abstract Integer getDateStart();

    public abstract void setDateStart(Integer dateStart);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getContactId();

    public abstract void setContactId(Integer contactId);

    public abstract Integer getContactPersonId();

    public abstract void setContactPersonId(Integer contactPersonId);

    public abstract Integer getEmployeeId();

    public abstract void setEmployeeId(Integer employeeId);

    public abstract Integer getFreeTextId();

    public abstract void setFreeTextId(Integer freeTextId);

    public abstract String getNote();

    public abstract void setNote(String note);

    public abstract Integer getProcessId();

    public abstract void setProcessId(Integer processId);

    public abstract Integer getTemplateId();

    public abstract void setTemplateId(Integer templateId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getContactNumber();

    public abstract void setContactNumber(String contactNumber);

    public abstract String getStatus();

    public abstract void setStatus(String status);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract ContactFreeText getContactFreeText();

    public abstract void setContactFreeText(ContactFreeText contactFreeText);


    public void setDateFinishField(Date value) {
        setDateFinish(DateUtils.dateToInteger(value));
    }

    public Date getDateFinishField() {
        return DateUtils.integerToDate(getDateFinish());
    }

    public void setDateStartField(Date value) {
        setDateStart(DateUtils.dateToInteger(value));
    }

    public Date getDateStartField() {
        return DateUtils.integerToDate(getDateStart());
    }

    public abstract ContactPerson getContactPerson();

    public abstract void setContactPerson(ContactPerson contactPerson);

    public abstract Integer getInOut();

    public abstract void setInOut(Integer inout);

    public abstract void setEmployee(Employee employee);

    public abstract Employee getEmployee();

    public abstract Boolean getIsAction();

    public abstract void setIsAction(Boolean isAction);

    public void setContactFreeText(EJBLocalObject contactFreeText) {
        setContactFreeText((ContactFreeText) contactFreeText);
    }

    public abstract Integer getProbability();

    public abstract void setProbability(Integer probability);

    public abstract Integer ejbSelectMaxStartDate(Integer processId, Integer addressId) throws FinderException;

    public Integer ejbHomeSelectMaxStartDate(Integer processId, Integer addressId) throws FinderException {
        return ejbSelectMaxStartDate(processId, addressId);
    }

    public abstract Integer ejbSelectMaxContactIdByStartDate(Integer processId, Integer addressId, Integer dateStart) throws FinderException;

    public Integer ejbHomeSelectMaxContactIdByStartDate(Integer processId, Integer addressId, Integer dateStart) throws FinderException {
        return ejbSelectMaxContactIdByStartDate(processId, addressId, dateStart);
    }

    public abstract Integer ejbSelectCountContactIdByAction(Integer contactId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectCountContactIdByAction(Integer contactId, Integer companyId) throws FinderException {
        return ejbSelectCountContactIdByAction(contactId, companyId);
    }

    public abstract String getDocumentFileName();

    public abstract void setDocumentFileName(String documentFileName);

    public abstract Integer getAdditionalAddressId();

    public abstract void setAdditionalAddressId(Integer additionalAddressId);

    public abstract Integer getWebDocumentId();

    public abstract void setWebDocumentId(Integer webDocumentId);

    public abstract String getWebGenerateUUID();

    public abstract void setWebGenerateUUID(String webGenerateUUID);
}
