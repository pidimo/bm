package com.piramide.elwis.domain.catalogmanager;

/**
 * This Class represents the Local interface of the Branch Entity Bean
 *
 * @author Ivan
 * @version $Id: Branch.java 1933 2004-07-21 16:35:51Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

import javax.ejb.EJBLocalObject;

public interface Branch extends EJBLocalObject {
    Integer getBranchId();

    void setBranchId(Integer branchId);

    Integer getGroup();

    void setGroup(Integer branchGroup);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer versionId);

    String getBranchName();

    void setBranchName(String nameId);

    /*void checkVersion(int version) throws ConcurrencyException;*/
}
