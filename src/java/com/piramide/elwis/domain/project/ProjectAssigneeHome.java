/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.project;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProjectAssigneeHome extends EJBLocalHome {
    ProjectAssignee create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.project.ProjectAssignee findByPrimaryKey(ProjectAssigneePK key) throws FinderException;

    ProjectAssignee findByAddressId(Integer addressId, Integer projectId, Integer companyId) throws FinderException;

    Collection findByProjectId(Integer projectId, Integer companyId) throws FinderException;

    Collection findAssigneedProjects(Integer addressId, Integer companyId) throws FinderException;

    Collection findByAddressId(Integer addressId) throws FinderException;
}
