/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;

public interface ProjectAssignee extends EJBLocalObject {
    Integer getProjectId();

    void setProjectId(Integer projectId);

    Integer getAddressId();

    void setAddressId(Integer addressId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Byte getPermission();

    void setPermission(Byte permission);

    Integer getVersion();

    void setVersion(Integer version);

    Project getProject();

    void setProject(Project project);
}
