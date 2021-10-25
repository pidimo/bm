package com.piramide.elwis.domain.project;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.4
 */
public interface ProjectTimeLimitHome extends EJBLocalHome {
    ProjectTimeLimit findByPrimaryKey(Integer key) throws FinderException;

    ProjectTimeLimit create(ComponentDTO dto) throws CreateException;

    Collection findByProjectId(Integer projectId, Integer companyId) throws FinderException;

    Collection findByProjectIdAssigneeIdSubProjectId(Integer projectId, Integer assigneeId, Integer subProjectId) throws FinderException;

}
