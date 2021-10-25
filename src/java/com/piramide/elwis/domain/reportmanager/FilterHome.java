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

public interface FilterHome extends EJBLocalHome {
    public Filter create(ComponentDTO dto) throws CreateException;

    Filter findByPrimaryKey(Integer key) throws FinderException;

    public Integer selectMaxSequence(Integer reportId, Integer companyId) throws FinderException;

    Collection findByReportId(Integer reporrId, Integer companyId) throws FinderException;

    Collection findByReportIdAndIsParameter(Integer reporrId, Integer companyId) throws FinderException;

    Collection findByTableRefAndColumnRef(String tableReference, String columnReference) throws FinderException;
}
