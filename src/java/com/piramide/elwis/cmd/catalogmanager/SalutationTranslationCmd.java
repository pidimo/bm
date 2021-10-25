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
 * @author Ivan Alban  10:20:17
 * @version 2.0
 */
public class SalutationTranslationCmd extends EJBCommand {
    int PAGESIZE = 10;
    private Log log = LogFactory.getLog(this.getClass());

    /*
    * the emplementation of this class creates LangTexts  such as represents the translations of
    * an salutation
    */
    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing SalutationTranslation command with \n" +
                "paramDTO = " + paramDTO);

        Integer salutationId = Integer.valueOf(paramDTO.get("salutationId").toString());

        SalutationHome salutationHome = (SalutationHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_SALUTATION);
        Salutation salutation = null;
        try {
            salutation = salutationHome.findByPrimaryKey(salutationId);
        } catch (FinderException e) {
            log.debug("Cannot find salutation Object with salutationId =  " + salutationId);
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("salutationLabel"));
            resultDTO.setForward("Fail");
            return;
        }


        if (null != salutation) {
            LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

            Collection myAddressTexts = new ArrayList();
            try {
                myAddressTexts = langTextHome.findByLangTextId(salutation.getAddressTextId());
            } catch (FinderException e) {

            }

            Collection myLetterTexts = new ArrayList();
            try {
                myLetterTexts = langTextHome.findByLangTextId(salutation.getLetterTextId());
            } catch (FinderException e) {

            }

            Collection allLangTexts = new ArrayList();
            allLangTexts.addAll(myAddressTexts);
            allLangTexts.addAll(myLetterTexts);
            List languagesIdsTranslated = extractsLanguageId(allLangTexts);

            List languagesNotTranlated = new ArrayList();
            try {
                languagesNotTranlated = getLanguagesNotTranlated(languagesIdsTranslated,
                        salutation.getCompanyId());
            } catch (FinderException e) {
            }

            List structure = constructStructure((List) myAddressTexts, (List) myLetterTexts, languagesNotTranlated);

            List sortedStructure = SortUtils.orderByPropertyMap((ArrayList) structure, "languageName");
            resultDTO.put("translationStructure", sortedStructure);
            resultDTO.put("addressTextId", salutation.getAddressTextId());
            resultDTO.put("letterTextId", salutation.getLetterTextId());
            resultDTO.put("salutationId", salutation.getSalutationId());
            resultDTO.put("salutationLabel", salutation.getSalutationLabel());
            resultDTO.put("translationStructureSize", String.valueOf(sortedStructure.size()));
        }

        if ("create".equals(getOp())) {

            LangTextHome home = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
            Integer translationStructureSize = Integer.valueOf(paramDTO.get("translationStructureSize").toString());
            Integer addressTextId =
                    (null != paramDTO.get("addressTextId") && !"".equals(paramDTO.get("addressTextId").toString().trim()) ?
                            Integer.valueOf(paramDTO.get("addressTextId").toString()) : null);

            Integer letterTextId =
                    (null != paramDTO.get("letterTextId") && !"".equals(paramDTO.get("letterTextId").toString().trim()) ?
                            Integer.valueOf(paramDTO.get("letterTextId").toString()) : null);

            for (int i = 1; i <= translationStructureSize.intValue(); i++) {
                Integer languageId = Integer.valueOf(paramDTO.get("languageId" + i).toString());

                String addressText = paramDTO.get("addressText" + i).toString();
                String letterText = paramDTO.get("letterText" + i).toString();

                LangTextPK addressTextPK = new LangTextPK(addressTextId, languageId);
                LangTextPK letterTextPK = new LangTextPK(letterTextId, languageId);

                try {
                    LangText addressTextOBJ = home.findByPrimaryKey(addressTextPK);
                    addressTextOBJ.setText(addressText);
                } catch (FinderException e) {
                    log.debug("Cannot find addressText with langTextPK = " + addressTextPK);
                    LangTextDTO addressTextDTO = new LangTextDTO();
                    addressTextDTO.put("companyId", salutation.getCompanyId());
                    addressTextDTO.put("langTextId", addressTextId);
                    addressTextDTO.put("languageId", languageId);
                    addressTextDTO.put("text", addressText);

                    try {
                        LangText addresslangText = home.create(addressTextDTO);
                        if (null == addressTextId) {
                            addressTextId = addresslangText.getLangTextId();
                            salutation.setAddressTextId(addressTextId);
                        }
                    } catch (CreateException e1) {
                        log.error("Cannot Create LangText Object ...", e1);
                    }
                }

                try {
                    LangText letterTextOBJ = home.findByPrimaryKey(letterTextPK);
                    letterTextOBJ.setText(letterText);
                } catch (FinderException e) {
                    log.debug("Cannot find letterText with langTextPK = " + letterTextPK);
                    LangTextDTO letterTextDTO = new LangTextDTO();
                    letterTextDTO.put("companyId", salutation.getCompanyId());
                    letterTextDTO.put("langTextId", letterTextId);
                    letterTextDTO.put("languageId", languageId);
                    letterTextDTO.put("text", letterText);
                    try {
                        LangText letterLangText = home.create(letterTextDTO);
                        if (null == letterTextId) {
                            letterTextId = letterLangText.getLangTextId();
                            salutation.setLetterTextId(letterTextId);
                        }
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

    private List constructStructure(List addressTexts, List letterTexts, List notTranslatedLanguages) {
        log.debug("Executing constructStructure method with \n" +
                "addressTexts           = " + addressTexts + "\n" +
                "List letterTexts       = " + letterTexts + "\n" +
                "notTranslatedLanguages = " + notTranslatedLanguages);

        LanguageHome home = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        List structure = new ArrayList();

        for (int i = 0; i < addressTexts.size(); i++) {
            LangText addressText = (LangText) addressTexts.get(i);
            Map map = new HashMap();
            map.put("addressText", addressText.getText());
            LangText letterText = getLetterTextAssociated(letterTexts, addressText.getLanguageId());
            if (null != letterText) {
                map.put("letterText", letterText.getText());
            } else {
                map.put("letterText", "");
            }
            try {
                Language language = home.findByPrimaryKey(addressText.getLanguageId());
                map.put("languageName", language.getLanguageName());
                map.put("languageId", language.getLanguageId());
            } catch (FinderException e) {
            }
            structure.add(map);
        }

        for (int i = 0; i < notTranslatedLanguages.size(); i++) {
            Language language = (Language) notTranslatedLanguages.get(i);
            Map map = new HashMap();
            map.put("addressText", "");
            map.put("letterText", "");
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
