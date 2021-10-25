package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author : ivan
 * @version : $Id ${NAME} ${time}
 */

public interface ColumnHome extends EJBLocalHome {
    public Column create(ComponentDTO dto) throws CreateException;

    Column findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByReportId(Integer reportId, Integer companyId) throws FinderException;

    public Collection findByReportIdTableReference(Integer reportId,
                                                   String talbleRefernece,
                                                   Integer companyId) throws FinderException;

    public Collection findByReportIdOnlyGroupColumns(Integer reportId,
                                                     Integer companyId) throws FinderException;

    public Collection findByTableAndColumnReference(String tableReference, String columnReference) throws FinderException;
}
