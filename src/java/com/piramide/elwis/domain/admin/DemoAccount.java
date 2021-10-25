package com.piramide.elwis.domain.admin;

import javax.ejb.EJBLocalObject;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public interface DemoAccount extends EJBLocalObject {
    String getCompanyLogin();

    void setCompanyLogin(String companyLogin);

    String getCompanyName();

    void setCompanyName(String companyName);

    Integer getCreationDate();

    void setCreationDate(Integer creationDate);

    Integer getDemoAccountId();

    void setDemoAccountId(Integer demoAccountId);

    String getEmail();

    void setEmail(String email);

    String getFirstName();

    void setFirstName(String firstName);

    Boolean getIsAlreadyCreated();

    void setIsAlreadyCreated(Boolean isAlreadyCreated);

    String getLastName();

    void setLastName(String lastName);

    String getPassword();

    void setPassword(String password);

    String getPhoneNumber();

    void setPhoneNumber(String phoneNumber);

    Integer getRegistrationDate();

    void setRegistrationDate(Integer registrationDate);

    String getRegistrationKey();

    void setRegistrationKey(String registrationKey);

    Integer getVersion();

    void setVersion(Integer version);

    String getUserLogin();

    void setUserLogin(String userLogin);
}
