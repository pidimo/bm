package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author: ivan
 * Date: 13-12-2006: 03:25:10 PM
 */
public class LanguageUtilCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        if ("findSystemLanguageByIso".equals(getOp())) {
            findSystemLanguageByIso();
        }
        if ("getCompanyLanguages".equals(getOp())) {
            Integer companyId = (Integer) paramDTO.get("companyId");
            getCompanyLanguages(companyId);
        }
        if ("getLanguage".equals(getOp())) {
            Integer languageId = (Integer) paramDTO.get("languageId");
            getLanguage(languageId);
        }
    }

    private List<LanguageDTO> getCompanyLanguages(Integer companyId) {

        List<LanguageDTO> languages = new ArrayList<LanguageDTO>();
        LanguageHome languageHome = getLanguageHome();
        try {
            Collection companyLanguages = languageHome.findByCompanyId(companyId);
            if (null != companyLanguages) {
                for (Object object : companyLanguages) {
                    Language language = (Language) object;
                    LanguageDTO dto = new LanguageDTO();
                    DTOFactory.i.copyToDTO(language, dto);
                    languages.add(dto);
                }
            }
        } catch (FinderException e) {
            log.debug("->Read Languages companyId=" + companyId + " FAIL");
        }

        resultDTO.put("getCompanyLanguages", languages);
        return languages;
    }

    private LanguageDTO getLanguage(Integer languageId) {
        LanguageHome languageHome = getLanguageHome();
        try {
            Language language = languageHome.findByPrimaryKey(languageId);
            LanguageDTO languageDTO = new LanguageDTO();
            DTOFactory.i.copyToDTO(language, languageDTO);

            resultDTO.put("getLanguage", languageDTO);
            return languageDTO;
        } catch (FinderException e) {
            log.debug("-> Read Language languageId=" + languageId + " FAIL");
            resultDTO.put("getLanguage", null);
        }
        return null;
    }

    private void findSystemLanguageByIso() {
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        String iso = paramDTO.get("iso").toString();
        LanguageHome lH = getLanguageHome();
        Collection systemLanguages = new ArrayList();
        try {
            systemLanguages = lH.findByUILanguages(companyId);
        } catch (FinderException fe) {
            log.debug("Cannot find languages system ", fe);
        }
        for (Iterator it = systemLanguages.iterator(); it.hasNext();) {
            Language l = (Language) it.next();
            if (iso.equals(l.getLanguageIso())) {
                resultDTO.put("languageId", l.getLanguageId());
                break;
            }
        }
    }

    private LanguageHome getLanguageHome() {
        return (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
    }

    public boolean isStateful() {
        return false;
    }
}
