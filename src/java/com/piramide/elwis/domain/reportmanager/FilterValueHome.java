package com.piramide.elwis.domain.reportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: FilterValueHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 03-abr-2006 17:34:00 Miky Exp $
 */

public interface FilterValueHome extends EJBLocalHome {
    FilterValue findByPrimaryKey(Integer key) throws FinderException;

    public FilterValue create(ComponentDTO dto) throws CreateException;

    /**
     * find by filterId, ordered by sequence and pkSequence
     *
     * @param filterId
     * @return
     * @throws FinderException
     */
    Collection findByFilterId(Integer filterId) throws FinderException;
}
