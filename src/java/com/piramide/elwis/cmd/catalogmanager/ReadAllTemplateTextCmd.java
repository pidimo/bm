package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.catalogmanager.Template;
import com.piramide.elwis.domain.catalogmanager.TemplateText;
import com.piramide.elwis.dto.catalogmanager.TemplateDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * Read all template texts relate to an template
 *
 * @author Miky
 * @version $Id: ReadAllTemplateText.java 2009-05-18 12:10:08 PM $
 */
public class ReadAllTemplateTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ReadAllTemplateTextCmd......" + paramDTO);

        Integer templateId = new Integer(paramDTO.get("templateId").toString());

        List<Map> allTemplateTextList = new ArrayList<Map>();
        Template template = (Template) ExtendedCRUDDirector.i.read(new TemplateDTO(templateId), resultDTO, false);
        if (template != null) {
            Collection templateTexts = template.getTemplateText();
            for (Iterator iterator = templateTexts.iterator(); iterator.hasNext();) {
                TemplateText templateText = (TemplateText) iterator.next();
                Map map = new HashMap();
                map.put("templateId", templateText.getTemplateId());
                map.put("languageId", templateText.getLanguageId());
                map.put("languageName", readLanguageName(templateText.getLanguageId()));

                allTemplateTextList.add(map);
            }
        }

        resultDTO.put("allTemplateTexts", allTemplateTextList);
    }

    private String readLanguageName(Integer languageId) {
        String laguageName = "";
        LanguageHome languageHome = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        Language language = null;
        try {
            language = languageHome.findByPrimaryKey(languageId);
            laguageName = language.getLanguageName();
        } catch (FinderException e) {
            log.debug("->Read Language languageId=" + languageId + " FAIL", e);
        }
        return laguageName;
    }

    public boolean isStateful() {
        return false;
    }
}
