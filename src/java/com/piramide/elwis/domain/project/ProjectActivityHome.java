/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:18:24 $
 */
package com.piramide.elwis.domain.project;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ProjectActivityHome extends EJBLocalHome {
    com.piramide.elwis.domain.project.ProjectActivity findByPrimaryKey(Integer key) throws FinderException;

    ProjectActivity create(ComponentDTO dto) throws CreateException;

    Collection findByProjectId(Integer projectId, Integer companyId) throws FinderException;
}
