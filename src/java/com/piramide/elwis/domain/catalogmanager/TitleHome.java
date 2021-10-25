package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Title Entity Bean
 *
 * @author Ivan
 * @version $Id: TitleHome.java 8136 2008-04-03 00:58:45Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface TitleHome extends EJBLocalHome {
    public Title create(ComponentDTO dto) throws CreateException;

    Title findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Title findByTitleName(String titleName, Integer companyId) throws FinderException;
}