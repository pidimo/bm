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

public interface TotalizeHome extends EJBLocalHome {
    public Totalize create(ComponentDTO dto) throws CreateException;

    Totalize findByPrimaryKey(Integer key) throws FinderException;

    Collection findByReportIdNonCustomTotalize(Integer reportId, Integer companyId) throws FinderException;
}
