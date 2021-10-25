package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of Language Entity Bean
 *
 * @author Ivan
 * @version $Id: LanguageHome.java 9661 2009-09-01 15:18:52Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface LanguageHome extends EJBLocalHome {

    public Language create(ComponentDTO dto) throws CreateException;

    Language findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findByTraslatedLanguages(Integer companyId, Integer langTextId) throws FinderException;

    public Collection findByUILanguages(Integer companyId) throws FinderException;

    public Language findByLanguageByName(Integer companyId, String languageName) throws FinderException;

    public Language findByDefault(Integer companyId) throws FinderException;

    public Language findByLanguageIso(String languageIso, Integer companyId) throws FinderException;

}
