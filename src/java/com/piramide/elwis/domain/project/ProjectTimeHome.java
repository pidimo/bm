/**
 *
 * @author Fernando Montao
 * @version $Id: ${NAME}.java 2009-02-20 11:19:40 $
 */
package com.piramide.elwis.domain.project;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.math.BigDecimal;
import java.util.Collection;

public interface ProjectTimeHome extends EJBLocalHome {
    com.piramide.elwis.domain.project.ProjectTime findByPrimaryKey(Integer key) throws FinderException;

    ProjectTime create(ComponentDTO dto) throws CreateException;

    BigDecimal selectSumTimes(Integer projectId, Integer assigneeId, Integer companyId, Integer date) throws FinderException;

    BigDecimal selectSumTimesByProject(Integer projectId, Integer companyId, Boolean toBeInvoiced) throws FinderException;

    BigDecimal selectSumTimesByProjectAssigneeSubProject(Integer projectId, Integer assigneeId, Integer subProjectId, Boolean toBeInvoiced, Integer timeId) throws FinderException;

    BigDecimal selectSumTimesByProjectSubProject(Integer projectId, Integer subProjectId, Boolean toBeInvoiced) throws FinderException;

    Collection findByProjectId(Integer projectId, Integer companyId) throws FinderException;

    Collection findByAssigneeId(Integer addressId) throws FinderException;
}
