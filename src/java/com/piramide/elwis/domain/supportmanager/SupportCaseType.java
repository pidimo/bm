package com.piramide.elwis.domain.supportmanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:41:42 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseType extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getCaseTypeId();

    void setCaseTypeId(Integer caseTypeId);

    String getCaseTypeName();

    void setCaseTypeName(String name);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);
}
