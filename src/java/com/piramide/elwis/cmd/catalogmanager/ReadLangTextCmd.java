package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.SystemLanguage;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: ReadLangTextCmd.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 18-03-2005 10:54:49 AM ivan Exp $
 */
public class ReadLangTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {

        Integer languageId = Integer.valueOf((String) paramDTO.get("languageId"));
        String type = "";
        if ("systemTranslation".equals(paramDTO.get("type"))) {
            type = SystemLanguage.SYSTEM_TRANSLATION;
        }
        if ("userTranslation".equals(paramDTO.get("type"))) {
            type = SystemLanguage.USER_TRANSLATIONS;
        }


        Collection translations = new ArrayList();
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        LanguageHome languageHome =
                (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        Language language = null;

        try {
            language = languageHome.findByPrimaryKey(languageId);
        } catch (FinderException e) {
            log.debug("->Read Language languageId=" + languageId + " FAIL", e);
        }

        Collection translationsDTOs = new ArrayList();
        String iso = null;

        if (null != language) {
            iso = language.getLanguageIso();

            try {
                translations = langTextHome.findByLanguageIdAndType(languageId, type);
            } catch (FinderException e) {
                e.printStackTrace();
            }


            for (Iterator iterator = translations.iterator(); iterator.hasNext();) {
                LangText langText = (LangText) iterator.next();
                LangTextDTO dto = new LangTextDTO();
                dto.put("langtextId", langText.getLangTextId());
                dto.put("languageId", langText.getLanguageId());
                dto.put("type", langText.getType());
                translationsDTOs.add(dto);
            }
        }

        resultDTO.put("translations", translationsDTOs);
        resultDTO.put("actualIso", iso);
    }

    public boolean isStateful() {
        return false;
    }
}
