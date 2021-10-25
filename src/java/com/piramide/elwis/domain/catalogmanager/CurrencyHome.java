package com.piramide.elwis.domain.catalogmanager;


import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Currency Entity Bean
 *
 * @author Ivan
 * @version $Id: CurrencyHome.java 1900 2004-07-15 19:29:18Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface CurrencyHome extends EJBLocalHome {
    public Currency create(ComponentDTO dto) throws CreateException;

    Currency findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Currency getIsBasicCurrency(Integer companyId) throws FinderException;
}
