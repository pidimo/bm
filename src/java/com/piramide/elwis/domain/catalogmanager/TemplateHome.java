package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Template Entity Bean
 *
 * @author Ivan
 * @version $Id: TemplateHome.java 8032 2008-02-23 22:22:12Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface TemplateHome extends EJBLocalHome {

    public Template create(ComponentDTO dto) throws CreateException;

    Template findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}