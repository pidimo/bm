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

public interface ChartHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    Chart create(ComponentDTO dto) throws CreateException;

    Chart findByPrimaryKey(Integer key) throws FinderException;

    Chart findByReportKey(Integer key) throws FinderException;

    Chart findByTotalizeId(Integer key, Integer companyId) throws FinderException;
}
