package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Priority Entity Bean
 *
 * @author Ivan
 * @version $Id: PriorityHome.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface PriorityHome extends EJBLocalHome {
    public Priority create(ComponentDTO dto) throws CreateException;

    Priority findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findSupportCatalogByCompanyId(Integer companyId) throws FinderException;

    public Collection findPriorityByType(Integer companyId, String type) throws FinderException;
}