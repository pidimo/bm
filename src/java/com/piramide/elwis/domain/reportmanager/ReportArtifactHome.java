package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.10.1
 */
public interface ReportArtifactHome extends EJBLocalHome {
    public ReportArtifact create(ComponentDTO dto) throws CreateException;

    ReportArtifact findByPrimaryKey(Integer key) throws FinderException;

    Collection findByReportId(Integer reportId) throws FinderException;
}
