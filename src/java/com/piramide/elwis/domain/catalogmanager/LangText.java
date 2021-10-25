package com.piramide.elwis.domain.catalogmanager;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;

/**
 * This Class represents the Local interface of the LangText Entity Bean
 *
 * @author yumi
 * @version $Id: LangText.java 8456 2008-09-02 14:19:25Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public interface LangText extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getLangTextId();

    void setLangTextId(Integer langTextId);

    Integer getLanguageId();

    void setLanguageId(Integer languageId);

    String getText();

    void setText(String text);

    public String getLanguageName() throws FinderException;

    String getType();

    void setType(String type);

    Boolean getIsDefault();

    void setIsDefault(Boolean isDefault);

    Language getLanguage();

    void setLanguage(Language language);
}
