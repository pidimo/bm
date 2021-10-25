/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ReportRoleHome extends EJBLocalHome {
    public ReportRole create(ComponentDTO dto) throws CreateException;

    ReportRole findByPrimaryKey(ReportRolePK key) throws FinderException;

    Collection findByReportId(Integer reportId, Integer companyId) throws FinderException;
}
