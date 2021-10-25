package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.SortUtils;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.*;

/**
 * @author Ivan
 * @version $Id: PayConditionTranslationCmd.java 9703 2009-09-12 15:46:08Z fernando $
 * @deprecated
 */
//import com.piramide.elwis.dto.catalogmanager.PayConditionTranslationCmdDTO;

public class PayConditionTranslationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing PayConditionTranslation command with \n" +
                "paramDTO = " + paramDTO);

        Integer payConditionId = Integer.valueOf(paramDTO.get("payConditionId").toString());

        PayConditionHome payConditionHome =
                (PayConditionHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITION);

        PayCondition payCondition = null;
        try {
            payCondition = payConditionHome.findByPrimaryKey(payConditionId);
        } catch (FinderException e) {
            log.debug("Cannot find salutation Object with payConditionId =  " + payConditionId);
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("payConditionName"));
            resultDTO.setForward("Fail");
            return;
        }


        if (null != payCondition) {
            LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

            Collection myFirstConditions = new ArrayList();
            try {
                myFirstConditions = langTextHome.findByLangTextId(payCondition.getFirstConditionId());
            } catch (FinderException e) {

            }

            Collection mySecondConditions = new ArrayList();
            try {
                mySecondConditions = langTextHome.findByLangTextId(payCondition.getSecondConditionId());
            } catch (FinderException e) {

            }

            Collection allLangTexts = new ArrayList();
            allLangTexts.addAll(myFirstConditions);
            allLangTexts.addAll(mySecondConditions);
            List languagesIdsTranslated = extractsLanguageId(allLangTexts);

            List languagesNotTranlated = new ArrayList();
            try {
                languagesNotTranlated = getLanguagesNotTranlated(languagesIdsTranslated,
                        payCondition.getCompanyId());
            } catch (FinderException e) {
            }

            List structure = constructStructure((List) myFirstConditions, (List) mySecondConditions, languagesNotTranlated);

            List sortedStructure = SortUtils.orderByPropertyMap((ArrayList) structure, "languageName");
            resultDTO.put("translationStructure", sortedStructure);
            resultDTO.put("firstConditionId", payCondition.getFirstConditionId());
            resultDTO.put("secondConditionId", payCondition.getSecondConditionId());
            resultDTO.put("payConditionId", payCondition.getPayConditionId());
            resultDTO.put("payConditionName", payCondition.getPayConditionName());
            resultDTO.put("translationStructureSize", String.valueOf(sortedStructure.size()));
        }

        if ("create".equals(getOp())) {

            LangTextHome home = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
            Integer translationStructureSize = Integer.valueOf(paramDTO.get("translationStructureSize").toString());
            Integer firstConditionId = Integer.valueOf(paramDTO.get("firstConditionId").toString());
            Integer secondConditionId = Integer.valueOf(paramDTO.get("secondConditionId").toString());
            for (int i = 1; i <= translationStructureSize.intValue(); i++) {
                Integer languageId = Integer.valueOf(paramDTO.get("languageId" + i).toString());

                String firstConditionText = paramDTO.get("firstCondition" + i).toString();
                String secondConditionText = paramDTO.get("secondCondition" + i).toString();

                LangTextPK firstConditionPK = new LangTextPK(firstConditionId, languageId);
                LangTextPK secondConditionPK = new LangTextPK(secondConditionId, languageId);

                try {
                    LangText firstTextOBJ = home.findByPrimaryKey(firstConditionPK);
                    firstTextOBJ.setText(firstConditionText);
                } catch (FinderException e) {
                    log.debug("Cannot find addressText with langTextPK = " + firstConditionPK);
                    LangTextDTO firstConditionDTO = new LangTextDTO();
                    firstConditionDTO.put("companyId", payCondition.getCompanyId());
                    firstConditionDTO.put("langTextId", firstConditionId);
                    firstConditionDTO.put("languageId", languageId);
                    firstConditionDTO.put("text", firstConditionText);

                    try {
                        home.create(firstConditionDTO);
                    } catch (CreateException e1) {
                        log.error("Cannot Create LangText Object ...", e1);
                    }
                }

                try {
                    LangText secondTextOBJ = home.findByPrimaryKey(secondConditionPK);
                    secondTextOBJ.setText(secondConditionText);
                } catch (FinderException e) {
                    log.debug("Cannot find letterText with langTextPK = " + secondConditionPK);
                    LangTextDTO secondConditionDTO = new LangTextDTO();
                    secondConditionDTO.put("companyId", payCondition.getCompanyId());
                    secondConditionDTO.put("langTextId", secondConditionId);
                    secondConditionDTO.put("languageId", languageId);
                    secondConditionDTO.put("text", secondConditionText);
                    try {
                        home.create(secondConditionDTO);
                    } catch (CreateException e1) {
                        log.error("Cannot Create LangText Object ...", e1);
                    }
                }
            }
        }
    }


    private List getLanguagesNotTranlated(List languagesIdsTranslated, Integer companId) throws FinderException {
        log.debug("Executing checkLanguagesTranslated method with \n" +
                "languagesIdsTranslated = " + languagesIdsTranslated + "\n" +
                "companyId              = " + companId);

        List languagesNotTranslated = new ArrayList();
        LanguageHome home = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        Collection allLanguages = home.findByCompanyId(companId);
        for (Iterator iterator = allLanguages.iterator(); iterator.hasNext();) {
            Language language = (Language) iterator.next();
            if (!languagesIdsTranslated.contains(language.getLanguageId())) {
                languagesNotTranslated.add(language);
            }
        }

        return languagesNotTranslated;
    }

    private List extractsLanguageId(Collection langTexts) {
        log.debug("Executing extractsLanguageId method with \n" +
                "langTexts = ????");

        List languageIds = new ArrayList();
        for (Iterator iterator = langTexts.iterator(); iterator.hasNext();) {
            LangText langText = (LangText) iterator.next();
            languageIds.add(langText.getLanguageId());
        }

        return languageIds;
    }

    private List constructStructure(List firstConditions, List seconConditions, List notTranslatedLanguages) {
        log.debug("Executing constructStructure method with \n" +
                "firstConditions        = " + firstConditions + "\n" +
                "seconConditions        = " + seconConditions + "\n" +
                "notTranslatedLanguages = " + notTranslatedLanguages);

        LanguageHome home = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        List structure = new ArrayList();

        for (int i = 0; i < firstConditions.size(); i++) {
            LangText firstCondition = (LangText) firstConditions.get(i);
            Map map = new HashMap();
            map.put("firstCondition", firstCondition.getText());
            LangText secondCondition = getLetterTextAssociated(seconConditions, firstCondition.getLanguageId());
            if (null != secondCondition) {
                map.put("secondCondition", secondCondition.getText());
            } else {
                map.put("secondCondition", "");
            }
            try {
                Language language = home.findByPrimaryKey(firstCondition.getLanguageId());
                map.put("languageName", language.getLanguageName());
                map.put("languageId", language.getLanguageId());
            } catch (FinderException e) {
            }
            structure.add(map);
        }

        for (int i = 0; i < notTranslatedLanguages.size(); i++) {
            Language language = (Language) notTranslatedLanguages.get(i);
            Map map = new HashMap();
            map.put("firstCondition", "");
            map.put("secondCondition", "");
            map.put("languageName", language.getLanguageName());
            map.put("languageId", language.getLanguageId());
            structure.add(map);
        }
        return structure;
    }

    private LangText getLetterTextAssociated(List letterTexts, Integer languageId) {
        for (int i = 0; i < letterTexts.size(); i++) {
            LangText letteText = (LangText) letterTexts.get(i);
            if (letteText.getLanguageId().equals(languageId)) {
                return letteText;
            }
        }
        return null;
    }


    public boolean isStateful() {
        return false;
    }
}