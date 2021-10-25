package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * LoggedCompany local home interface.
 *
 * @author Fernando Montaño
 * @version $Id: CompanyHome.java 9121 2009-04-17 00:28:59Z fernando $
 */


public interface CompanyHome extends EJBLocalHome {

    public Company create(ComponentDTO dto) throws CreateException;

    Company findByPrimaryKey(Integer key) throws FinderException;

    Company findByName(String login) throws FinderException;

    public Collection findAll() throws FinderException;

    public Company findByCopyTemplate(Integer copyTemplate) throws FinderException;

    public Company findByCopyTemplateLanguage(Integer copyTemplate, String language) throws FinderException;
}
