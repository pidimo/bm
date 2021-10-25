package com.piramide.elwis.cmd.admin.copycatalog.util;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author: ivan
 */
public class CopyCatalogUtil {
    private Log log = LogFactory.getLog(this.getClass());
    public static CopyCatalogUtil i = new CopyCatalogUtil();

    private CopyCatalogUtil() {
    }

    public void copyCatalog(Company source, Company target, ComponentDTO dto) {
        Collection sourceObjects;

        sourceObjects = (Collection)
                EJBFactory.i.callFinder(dto, "findByCompanyId", new Object[]{source.getCompanyId()});

        if (null != sourceObjects && !sourceObjects.isEmpty()) {
            log.debug("number of elements to copy ." + sourceObjects.size());
            for (Object obj : sourceObjects) {
                EJBLocalObject sourceEJB = (EJBLocalObject) obj;
                ComponentDTO targetDto = creteNewDTOInstance(dto.getClass());
                if (null != targetDto) {
                    DTOFactory.i.copyToDTO(sourceEJB, targetDto);
                    targetDto.put("companyId", target.getCompanyId());
                    targetDto.put(targetDto.getPrimKey(), null);
                    ExtendedCRUDDirector.i.create(targetDto, new ResultDTO(), false);
                }
            }
        }
    }

    public Integer buildLangText(Integer sourceLangTextId, Integer targetCompanyId) {
        LangText targetLangText = buildTargetLangText(sourceLangTextId, targetCompanyId);
        if (null != targetLangText) {
            return targetLangText.getLangTextId();
        }
        return null;
    }

    public LangText buildTargetLangText(Integer sourceLangTextId, Integer targetCompanyId) {

        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        LangText targetLangText = null;
        LangTextHome langTextHome = (LangTextHome)
                EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        Collection targetLanguages = new ArrayList();
        try {
            targetLanguages = languageHome.findByCompanyId(targetCompanyId);
        } catch (FinderException e) {
            log.warn("Cannot read languages of target company ", e);
        }

        Collection sourceLangTexts = null;
        try {
            sourceLangTexts = langTextHome.findByLangTextId(sourceLangTextId);
        } catch (FinderException e) {
            log.warn("Cannot read langtext of source company.");
        }

        if (null != sourceLangTexts && !sourceLangTexts.isEmpty()) {
            int i = 0;
            for (Object obj : sourceLangTexts) {
                LangText sourceLangText = (LangText) obj;
                Integer targetLanguageId = null;
                Boolean isTargetDefaultTranslation = false;
                try {
                    Language sourceLanguage = languageHome.findByPrimaryKey(sourceLangText.getLanguageId());
                    Language targetLanguage = searchTargetLanguage(sourceLanguage, targetLanguages);
                    targetLanguageId = targetLanguage.getLanguageId();
                    isTargetDefaultTranslation = targetLanguage.getIsDefault();
                } catch (FinderException e) {
                    log.debug("Cannot read language for source langtext");
                }


                LangTextDTO dto = new LangTextDTO();
                DTOFactory.i.copyToDTO(sourceLangText, dto);
                dto.put("companyId", targetCompanyId);
                dto.put("languageId", targetLanguageId);
                dto.put("isDefault", isTargetDefaultTranslation);
                if (i == 0) {
                    dto.remove("langTextId");
                    try {
                        targetLangText = langTextHome.create(dto);
                    } catch (CreateException e) {
                        log.error("Cannot create target langtext.", e);
                    }
                } else {
                    if (null != targetLangText) {
                        dto.put("langTextId", targetLangText.getLangTextId());
                        try {
                            langTextHome.create(dto);
                        } catch (CreateException e) {
                            log.error("Cannot create target langtext.", e);
                        }
                    }
                }
                i++;
            }
        }

        if (null != targetLangText) {
            return targetLangText;
        } else {
            return null;
        }
    }

    public Language searchTargetLanguage(Language sourceLanguage, Collection targetLanguages) {
        for (Object obj : targetLanguages) {
            Language targetLanguage = (Language) obj;
            if (null != sourceLanguage.getLanguageIso() &&
                    sourceLanguage.getLanguageIso().equals(targetLanguage.getLanguageIso())) {
                return targetLanguage;
            }
            if (null == sourceLanguage.getLanguageIso() &&
                    sourceLanguage.getLanguageName().equals(targetLanguage.getLanguageName())) {
                return targetLanguage;
            }
        }
        return null;
    }

    public Collection getLanguages(Integer companyId) {
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        try {
            return languageHome.findByCompanyId(companyId);
        } catch (FinderException e) {
            log.debug("-> Read Languages companyId=" + companyId + " FAIL");
        }

        return new ArrayList();
    }

    public Language getLanguage(Integer languageId) {
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        try {
            return languageHome.findByPrimaryKey(languageId);
        } catch (FinderException e) {
            log.debug("-> Read Language languageId=" + languageId + " FAIL");
        }

        return null;
    }

    private ComponentDTO creteNewDTOInstance(Class dtoClass) {
        try {
            return (ComponentDTO) dtoClass.newInstance();
        } catch (InstantiationException e) {
            log.error("cannot create new instance of " + dtoClass.getName(), e);
        } catch (IllegalAccessException e) {
            log.error("cannot access to " + dtoClass.getName(), e);
        }
        return null;
    }
}
