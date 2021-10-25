package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of PayMorality Entity Bean
 *
 * @author Ivan
 * @version $Id: PayMoralityHome.java 841 2004-05-25 23:08:13Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface PayMoralityHome extends EJBLocalHome {
    public PayMorality create(ComponentDTO dto) throws CreateException;

    PayMorality findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
