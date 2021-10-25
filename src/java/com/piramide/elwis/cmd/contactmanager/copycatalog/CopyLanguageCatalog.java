package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun s.r.l
 * <p/>
 * This class execute logic to copy the language catalog from source company to target company.
 *
 * @author: ivan
 */
public class CopyLanguageCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * This method copy all Languages from source company to target company.
     * <p/>
     * First search all Languages from source company and copy all languages were not assign iso value
     * (languageIso = null), after update the languageName of all Languages with isoLanguage in target
     * company. Is necesary update the name of language with iso value assigned, because it (the languageName)
     * is used to search the Language object when other catalos are copied.
     *
     * @param source         Source company where select the Languages to copy.
     * @param target         Company where the languages are copied.
     * @param sessionContext SessionContext Object to execute the command.
     */
    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {

        LanguageHome languageHome = (LanguageHome)
                EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        List sourceLanguages;
        try {
            sourceLanguages = (List) languageHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.warn("Cannot copy Languages from sourceCompanyId=" +
                    source.getCompanyId() +
                    " to targetCompanyId=" + target.getCompanyId());
            return;
        }

        List<Language> sourceLanguagesWithIso = new ArrayList<Language>();
        for (int i = 0; i < sourceLanguages.size(); i++) {
            Language sourceLanguage = (Language) sourceLanguages.get(i);
            if (null != sourceLanguage.getLanguageIso()) {
                sourceLanguagesWithIso.add(sourceLanguage);
                continue;
            }

            LanguageDTO dto = new LanguageDTO();
            DTOFactory.i.copyToDTO(sourceLanguage, dto);
            dto.put("companyId", target.getCompanyId());
            dto.put("isDefault", Boolean.valueOf("false"));
            ExtendedCRUDDirector.i.create(dto, new ResultDTO(), false);
        }

        //update the target languageNames with iso
        for (int i = 0; i < sourceLanguagesWithIso.size(); i++) {
            Language sourceLanguage = sourceLanguagesWithIso.get(i);
            Language targetLanguage =
                    getTargetLanguageWithIso(sourceLanguage.getLanguageIso(), target.getCompanyId());

            if (null != targetLanguage) {
                targetLanguage.setLanguageName(sourceLanguage.getLanguageName());
            }
        }
    }

    private Language getTargetLanguageWithIso(String iso, Integer companyId) {
        LanguageHome languageHome =
                (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        try {
            return languageHome.findByLanguageIso(iso, companyId);
        } catch (FinderException e) {
            log.debug("Cannot find Language with iso=" + iso + " in companyId=" + companyId);
        }

        return null;
    }
}
