/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:19:07 $
 */
package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;

public interface SubProject extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getProjectId();

    void setProjectId(Integer projectId);

    String getName();

    void setName(String name);

    Integer getSubProjectId();

    void setSubProjectId(Integer subProjectId);

    Integer getVersion();

    void setVersion(Integer version);

    Project getProject();

    void setProject(Project project);
}
