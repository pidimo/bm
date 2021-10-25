package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;

/**
 * This Class represents the Local interface of the Bank Entity Bean
 *
 * @author Ivan
 * @version $Id: Bank.java 1922 2004-07-19 21:20:07Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface Bank extends EJBLocalObject {
    String getBankCode();

    void setBankCode(String bankCode);

    Integer getBankId();

    void setBankId(Integer bankId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    String getBankInternationalCode();

    void setBankInternationalCode(String bic);

    String getBankName();

    void setBankName(String nameId);

    String getBankLabel();

    void setBankLabel(String label);

    AddressBank getAddress();

    void setAddress(AddressBank address);

    public String getName1();

    public void setName1(String name);

    public Boolean getIsAddress();

    public void setIsAddress(Boolean test);
}
