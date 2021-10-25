package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Salutation Entity Bean
 *
 * @author Ivan
 * @version $Id: SalutationHome.java 8136 2008-04-03 00:58:45Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface SalutationHome extends EJBLocalHome {
    public Salutation create(ComponentDTO dto) throws CreateException;

    Salutation findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Salutation findByLabel(String label, Integer companyId) throws FinderException;
}
