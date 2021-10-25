package com.piramide.elwis.domain.admin;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.DateUtils;
import com.piramide.elwis.utils.PKGenerator;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Date;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public abstract class DemoAccountBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setDemoAccountId(PKGenerator.i.nextKey(AdminConstants.TABLE_DEMOACCOUNT));

        setRegistrationDate(DateUtils.dateToInteger(new Date()));
        setRegistrationKey(String.valueOf(System.currentTimeMillis()));
        setVersion(new Integer(1));

        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public DemoAccountBean() {
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

    public abstract String getCompanyLogin();

    public abstract void setCompanyLogin(String companyLogin);

    public abstract String getCompanyName();

    public abstract void setCompanyName(String companyName);

    public abstract Integer getCreationDate();

    public abstract void setCreationDate(Integer creationDate);

    public abstract Integer getDemoAccountId();

    public abstract void setDemoAccountId(Integer demoAccountId);

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract String getFirstName();

    public abstract void setFirstName(String firstName);

    public abstract Boolean getIsAlreadyCreated();

    public abstract void setIsAlreadyCreated(Boolean isAlreadyCreated);

    public abstract String getLastName();

    public abstract void setLastName(String lastName);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getPhoneNumber();

    public abstract void setPhoneNumber(String phoneNumber);

    public abstract Integer getRegistrationDate();

    public abstract void setRegistrationDate(Integer registrationDate);

    public abstract String getRegistrationKey();

    public abstract void setRegistrationKey(String registrationKey);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract String getUserLogin();

    public abstract void setUserLogin(String userLogin);
}
