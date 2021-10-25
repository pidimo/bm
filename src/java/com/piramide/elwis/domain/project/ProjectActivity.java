/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:18:24 $
 */
package com.piramide.elwis.domain.project;

import javax.ejb.EJBLocalObject;

public interface ProjectActivity extends EJBLocalObject {
    Integer getActivityId();

    void setActivityId(Integer activityId);

    String getName();

    void setName(String name);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getProjectId();

    void setProjectId(Integer projectId);

    Integer getVersion();

    void setVersion(Integer version);

    Project getProject();

    void setProject(Project project);
}
