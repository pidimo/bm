package com.piramide.elwis.domain.catalogmanager;

/**
 * This Class represents the Home interface of Bank Entity Bean
 *
 * @author Ivan
 * @version $Id: BankHome.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface BankHome extends EJBLocalHome {

    public Bank create(ComponentDTO dto) throws CreateException;

    Bank findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
