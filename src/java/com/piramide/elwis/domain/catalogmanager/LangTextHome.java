package com.piramide.elwis.domain.catalogmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * This Class represents the Home interface of LangText Entity Bean
 *
 * @author yumi
 * @version $Id: LangTextHome.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface LangTextHome extends EJBLocalHome {
    LangText findByPrimaryKey(LangTextPK key) throws FinderException;

    public LangText create(ComponentDTO dto) throws CreateException;

    /**
     * @param langTextId
     * @return
     * @throws FinderException
     * @deprecated replaced by findLangTexts
     */
    public Collection findByLangTextId(Integer langTextId) throws FinderException;

    public Collection findLangTexts(Integer langTextId) throws FinderException;

    public LangText findByLangTextIdAndLanguageRelatedToUI(Integer langTextId, String isoLanguage) throws FinderException;

    public Collection findByLanguageIdAndType(Integer languageId, String type) throws FinderException;

    public LangText findByIsDefault(Integer langTextId) throws FinderException;
}
