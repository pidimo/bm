package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.List;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class TranslateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(TranslateCmd.class);

    private static enum TranslationElement {
        language("language"),
        text("text"),
        isDefault("isDefault"),
        separator("_"),
        hasDefinedDefaultValue("hasDefinedDefaultValue");

        private String constant;

        TranslationElement(String constant) {
            this.constant = constant;
        }

        public String getConstant() {
            return constant;
        }
    }

    public void executeInStateless(SessionContext sessionContext) {
        if ("readUserTranslations".equals(getOp())) {
            String dtoClassName = (String) paramDTO.get("dtoClassName");
            ComponentDTO dto = getInstance(dtoClassName);
            Integer pk = getPkObject(dto);
            readUserTranslations(dto, pk);
        }
        if ("updateUserTranslation".equals(getOp())) {
            String dtoClassName = (String) paramDTO.get("dtoClassName");
            ComponentDTO dto = getInstance(dtoClassName);
            Integer pk = getPkObject(dto);
            String synchronizedFieldName = (String) paramDTO.get("synchronizedFieldName");
            manageUserTranslations(dto, pk, synchronizedFieldName);
        }
        if ("synchronizedDefaultTranslation".equals(getOp())) {
            String dtoClassName = (String) paramDTO.get("dtoClassName");
            ComponentDTO dto = getInstance(dtoClassName);
            Integer pk = getPkObject(dto);
            String synchronizedFieldName = (String) paramDTO.get("synchronizedFieldName");
            synchronizedDefaultTranslation(dto, pk, synchronizedFieldName);
        }

        if ("deleteTranslations".equals(getOp())) {
            Integer langTextId = (Integer) paramDTO.get("langTextId");
            deleteTranslations(langTextId);
        }
    }

    private void deleteTranslations(Integer langTextId) {
        if (null == langTextId) {
            return;
        }

        LangTextHome langtextHome = getLangTextHome();
        try {
            Collection langTexts = langtextHome.findLangTexts(langTextId);

            for (Object object : langTexts) {
                LangText langText = (LangText) object;
                langText.remove();
            }
            log.debug("-> Delete all LangText langTextId=" + langTextId + " OK");
        } catch (FinderException e) {
            log.debug("-> Read LangText langTextId=" + langTextId + " FAIL");
        } catch (RemoveException e) {
            log.debug("-> Delete LangText langTextId=" +
                    langTextId +
                    " FAIL");
        }
    }

    private void synchronizedDefaultTranslation(ComponentDTO componentDTO,
                                                Integer pk,
                                                String synchronizedFieldName) {
        componentDTO.put(componentDTO.getPrimKeyName(), pk);

        ResultDTO customResultDTO = new ResultDTO();
        EJBLocalObject localObject =
                ExtendedCRUDDirector.i.read(componentDTO, customResultDTO, false);

        if (null == localObject) {
            componentDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        Integer langTextId = (Integer) customResultDTO.get("langTextId");
        if (null == langTextId) {
            log.debug("->Not LangText set for " +
                    componentDTO.getPrimKeyName() +
                    "=" + customResultDTO.get(componentDTO.getPrimKeyName()));
            return;
        }
        LangTextHome langTextHome = getLangTextHome();

        try {
            LangText langText = langTextHome.findByIsDefault(langTextId);
            String newText = (String) customResultDTO.get(synchronizedFieldName);
            langText.setText(newText);
            log.debug("-> Update default LangText to " + newText + " OK");
        } catch (FinderException e) {
            log.debug("-> Read DefaultLangText langTextId=" + langTextId + " FAIL");
        }
    }

    private void manageUserTranslations(ComponentDTO componentDTO,
                                        Integer pk,
                                        String synchronizedFieldName) {
        componentDTO.put(componentDTO.getPrimKeyName(), pk);

        ResultDTO customResultDTO = new ResultDTO();
        EJBLocalObject localObject =
                ExtendedCRUDDirector.i.read(componentDTO, customResultDTO, false);

        if (null == localObject) {
            componentDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        Integer langTextId = (Integer) customResultDTO.get("langTextId");
        Integer companyId = (Integer) customResultDTO.get("companyId");

        if (null != langTextId) {
            LangText defaulLangText = updateUserTranslations(langTextId, companyId);
            updateComponentDTOEntity(componentDTO, pk, defaulLangText, synchronizedFieldName);
        }
        if (null == langTextId) {
            LangText defaultLangText = createUserTranslations(companyId);
            updateComponentDTOEntity(componentDTO, pk, defaultLangText, synchronizedFieldName);
        }
    }

    private LangText createUserTranslations(Integer companyId) {
        List<String> uiLanguageIds = (List<String>) paramDTO.get("uiLanguages");

        String defaultLanguageId =
                (String) paramDTO.get(TranslationElement.isDefault.getConstant());
        String defaultText = (String) getText(defaultLanguageId);

        LangTextDTO defaultLangTextDTO = buildLangTextDTO(Integer.valueOf(defaultLanguageId),
                companyId, defaultText, true, null);


        LangText defaultLangText =
                (LangText) ExtendedCRUDDirector.i.create(defaultLangTextDTO, resultDTO, false);

        //Creating the remaining translations
        for (String uiLanguageId : uiLanguageIds) {

            //because already created
            if (uiLanguageId.equals(defaultLanguageId)) {
                continue;
            }

            //cannot create empty values
            String text = (String) getText(uiLanguageId);
            if (null == text ||
                    "".equals(text.trim())) {
                continue;
            }

            Language language = null;
            try {
                language = getLanguage(Integer.valueOf(uiLanguageId));
            } catch (FinderException e) {
                log.debug("-> Read language languageId=" + uiLanguageId + " FAIL");
            }

            LangTextDTO langTextDTO = buildLangTextDTO(language.getLanguageId(),
                    companyId, text, false, null);
            langTextDTO.put("langTextId", defaultLangText.getLangTextId());
            ExtendedCRUDDirector.i.create(langTextDTO, resultDTO, false);
        }

        return defaultLangText;
    }

    private LangText updateUserTranslations(Integer langTextId, Integer companyId) {
        List<String> uiLanguageIds = (List<String>) paramDTO.get("uiLanguages");

        LangTextHome langTextHome = getLangTextHome();

        String uiDefaultLanguageId =
                (String) paramDTO.get(TranslationElement.isDefault.getConstant());

        LangText defaultLangText = null;

        for (String uiLanguageId : uiLanguageIds) {
            String text = (String) getText(uiLanguageId);

            LangTextPK langTextPK = new LangTextPK();
            langTextPK.languageId = Integer.valueOf(uiLanguageId);
            langTextPK.langTextId = langTextId;

            try {
                LangText currentLangText = langTextHome.findByPrimaryKey(langTextPK);
                currentLangText.setText(text);
                if (uiLanguageId.equals(uiDefaultLanguageId)) {
                    currentLangText.setIsDefault(true);
                    defaultLangText = currentLangText;
                }
            } catch (FinderException e) {
                if (null != text && !"".equals(text.trim())) {
                    LangTextDTO langTextDTO = buildLangTextDTO(Integer.valueOf(uiLanguageId),
                            companyId, text, false, null);
                    langTextDTO.put("langTextId", langTextId);
                    LangText newLangText =
                            (LangText) ExtendedCRUDDirector.i.create(langTextDTO, resultDTO, false);
                    if (uiLanguageId.equals(uiDefaultLanguageId)) {
                        newLangText.setIsDefault(true);
                        defaultLangText = newLangText;
                    }
                }
            }
        }

        return defaultLangText;
    }

    private void readUserTranslations(ComponentDTO componentDTO, Integer pk) {
        componentDTO.put(componentDTO.getPrimKeyName(), pk);

        ResultDTO customResultDTO = new ResultDTO();
        EJBLocalObject localObject =
                ExtendedCRUDDirector.i.read(componentDTO, customResultDTO, false);

        if (null == localObject) {
            componentDTO.addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }

        Integer langTextId = (Integer) customResultDTO.get("langTextId");

        if (null != langTextId) {
            Collection langTexts = (Collection) EJBFactory.i.callFinder(new LangTextDTO(),
                    "findLangTexts",
                    new Object[]{langTextId});

            for (Object object : langTexts) {
                LangText langText = (LangText) object;
                Integer languageId = langText.getLanguageId();
                String text = langText.getText();
                boolean isDefault = langText.getIsDefault();
                String languageName;
                try {
                    languageName = getLanguage(languageId).getLanguageName();
                } catch (FinderException e) {
                    log.debug("-> Read languageName languageId=" + languageId + " FAIL");
                    continue;
                }

                putValueInResultDTO(languageId, languageName, text, isDefault);
            }

            resultDTO.put("langTextId", langTextId);
        }
        resultDTO.put(componentDTO.getPrimKeyName(), pk);
    }


    private void updateComponentDTOEntity(ComponentDTO componentDTO,
                                          Integer pk,
                                          LangText defaultLangText,
                                          String synchronizedFieldName) {
        ComponentDTO newInstanceOfComponentDTO = getInstance(componentDTO.getClass().getName());
        newInstanceOfComponentDTO.put(componentDTO.getPrimKeyName(), pk);
        newInstanceOfComponentDTO.put("langTextId", defaultLangText.getLangTextId());
        newInstanceOfComponentDTO.put(synchronizedFieldName, defaultLangText.getText());
        log.debug("-> synchronized " + newInstanceOfComponentDTO);
        ExtendedCRUDDirector.i.update(newInstanceOfComponentDTO, resultDTO, false, false, false, "Fail");
    }

    private void putValueInResultDTO(Integer languageId,
                                     String languageName,
                                     String text,
                                     boolean isDefault) {
        final String separatorKey = TranslationElement.separator.getConstant();
        final String languageKey = TranslationElement.language.getConstant();
        final String textKey = TranslationElement.text.getConstant();

        resultDTO.put(languageKey + separatorKey + languageId, languageName);
        resultDTO.put(textKey + separatorKey + languageId, text);
        if (isDefault) {
            resultDTO.put(TranslationElement.isDefault.getConstant(),
                    languageId);
            resultDTO.put(TranslationElement.hasDefinedDefaultValue.getConstant(), true);
        }

    }


    private Language getLanguage(Integer languageId) throws FinderException {
        LanguageHome languageHome =
                (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        return languageHome.findByPrimaryKey(languageId);
    }

    private LangTextDTO buildLangTextDTO(Integer languageId,
                                         Integer companyId,
                                         String text,
                                         boolean isDefault,
                                         String type) {
        LangTextDTO langTextDTO = new LangTextDTO();
        langTextDTO.put("languageId", languageId);
        langTextDTO.put("text", text);
        langTextDTO.put("companyId", companyId);
        langTextDTO.put("isDefault", isDefault);
        langTextDTO.put("type", type);

        return langTextDTO;
    }

    private Object getText(String languageId) {
        return paramDTO.get(
                TranslationElement.text.getConstant() +
                        TranslationElement.separator.getConstant() +
                        languageId);
    }

    private Integer getPkObject(ComponentDTO componentDTO) {
        Integer pk = null;
        if (null != paramDTO.get(componentDTO.getPrimKeyName()) &&
                !"".equals(paramDTO.get(componentDTO.getPrimKeyName()))) {
            try {
                pk = Integer.valueOf(
                        paramDTO.get(componentDTO.getPrimKeyName()).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + componentDTO.getPrimKeyName() + "=" +
                        paramDTO.get(componentDTO.getPrimKeyName()) + " FAIL");
            }
        }
        return pk;
    }

    private LangTextHome getLangTextHome() {
        return (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
    }

    private ComponentDTO getInstance(String dtoClassName) {
        ComponentDTO dto = null;
        try {
            dto = (ComponentDTO) Class.forName(dtoClassName).newInstance();
        } catch (InstantiationException e) {
            log.debug("-> Instanciate " + dtoClassName + " FAIL", e);
        } catch (IllegalAccessException e) {
            log.debug("-> Access " + dtoClassName + " FAIL", e);
        } catch (ClassNotFoundException e) {
            log.debug("-> Read " + dtoClassName + " FAIL", e);
        }
        log.debug("-> Work on " + dtoClassName + " OK");
        return dto;
    }

    public boolean isStateful() {
        return false;
    }
}
