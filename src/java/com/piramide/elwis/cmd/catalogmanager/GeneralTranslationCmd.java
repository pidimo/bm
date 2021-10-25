package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.catalogmanager.LangTextPK;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.SortUtils;
import com.piramide.elwis.utils.SystemLanguage;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ParamDTO;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * AlfaCentauro Team
 *
 * @author ivan
 * @version $Id: GeneralTranslationCmd.java 9703 2009-09-12 15:46:08Z fernando ${CLASS_NAME}.java,v 1.2 14-03-2005 03:15:21 PM ivan Exp $
 */
public class GeneralTranslationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public static final String NEW_LANGTEXT = "newLangText";

    public void executeInStateless(SessionContext ctx, String translationFieldName,
                                   String pkFieldName, String fieldName, ParamDTO paramDTO, Class dtoName) {
        log.debug("Executing GeneralTranslationCmd..." + paramDTO);
        log.debug("Execute method with \n" +
                "translationFieldName = " + translationFieldName + "\n" +
                "pkFieldName          = " + translationFieldName + "\n" +
                "fieldName            = " + translationFieldName + "\n" +
                "paramDTO             = " + paramDTO + "\n" +
                "dtoName              = " + dtoName + "\n");

        //if operation is equal to read
        if (ExtendedCRUDDirector.OP_READ.equals(this.getOp())) {
            ResultDTO myResultDTO = new ResultDTO();

            //Read all information about the catalog contains
            try {
                ComponentDTO dto = (ComponentDTO) Class.forName(dtoName.getName()).newInstance();
                dto.put(pkFieldName, paramDTO.get(pkFieldName));
                CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, dto, myResultDTO);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            //read the translation field ID (langtextid)
            Integer translationId = (Integer) myResultDTO.get(translationFieldName);

            //read the companyId
            Integer companyId = (Integer) myResultDTO.get("companyId");

            //read the first key of catalog
            Integer pk = (Integer) myResultDTO.get(pkFieldName);

            //if cannot read the catalog information then the catalog was deleted
            if (myResultDTO.isFailure()) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("msg.NotFound", paramDTO.get(fieldName));
                return;
            }

            //setting up the pkfield into translation page
            resultDTO.put(pkFieldName, pk);

            //setting up the translationfieldName into translation page
            resultDTO.put(translationFieldName, translationId);

            // setting up the fieldName into translation page
            resultDTO.put(fieldName, paramDTO.get(fieldName));

            //only if actual catalog contains value into translatedField read the translations
            if (translationId != null) {
                Collection translation = (Collection) EJBFactory.i.callFinder(new LangTextDTO(),
                        "findByLangTextId",
                        new Object[]{translationId});

                LangText firstLangText = (LangText) translation.toArray()[0];
                readSystemTranslations(firstLangText.getCompanyId(), firstLangText.getLangTextId());
            } else {
                readSystemTranslations(companyId, null);
            }
        }


        if (ExtendedCRUDDirector.OP_UPDATE.equals(this.getOp())) {
            ResultDTO readResultDTO = new ResultDTO();
            try {

                ComponentDTO dto = (ComponentDTO) Class.forName(dtoName.getName()).newInstance();
                dto.put(pkFieldName, paramDTO.get(pkFieldName));
                CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, dto, readResultDTO);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }

            if (readResultDTO.isFailure()) {
                resultDTO.addResultMessage("msg.NotFound", paramDTO.get(fieldName));
                resultDTO.setForward("Fail");
                return;
            }

            if (!readResultDTO.isFailure()) {


                Integer langTextId = saveSystemTranslations(translationFieldName, paramDTO);


                if (paramDTO.get(translationFieldName) == null || "".equals(paramDTO.get(translationFieldName))) {
                    ResultDTO myResultDTO = new ResultDTO();
                    try {
                        ComponentDTO dto = (ComponentDTO) Class.forName(dtoName.getName()).newInstance();
                        dto.put(translationFieldName, langTextId);
                        dto.put(pkFieldName, paramDTO.get(pkFieldName));
                        CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, dto, myResultDTO);


                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    }
                    Integer translationId = (Integer) myResultDTO.get(translationFieldName);
                    Integer companyId = (Integer) myResultDTO.get("companyId");
                    Integer pk = (Integer) myResultDTO.get(pkFieldName);

                    resultDTO.put(pkFieldName, pk);
                    resultDTO.put(translationFieldName, translationId);
                }
            }
        }
    }

    private void readSystemTranslations(Integer companyId, Integer catalogLangTextId) {
        log.debug("Executing readSystemTranslations ...");

        //read all system languages
        Collection SystemLanguages = (Collection) EJBFactory.i.callFinder(new LanguageDTO(),
                "findByUILanguages", new Object[]{companyId});

        //haveDefaultTranslation = true if  exists defaultTranslation by catalog item in other case is false
        Boolean haveDefaultTranslation = Boolean.valueOf(false);

        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        Collection translatedSystemLanguages = new ArrayList();

        //by each systemLanguages checks if exists translation
        for (Iterator systemLanguagesIterator = SystemLanguages.iterator(); systemLanguagesIterator.hasNext();) {
            Language systemLanguage = (Language) systemLanguagesIterator.next();
            LangTextDTO langTextDTO = new LangTextDTO();
            try {
                LangText langText = langTextHome.findByPrimaryKey(new LangTextPK(catalogLangTextId,
                        systemLanguage.getLanguageId()));

                langTextDTO.put("languageId", langText.getLanguageId());
                langTextDTO.put("text", langText.getText());
                langTextDTO.put("languageName", systemLanguage.getLanguageName());
                langTextDTO.put("languageId", systemLanguage.getLanguageId());
                if (null != langText.getIsDefault() && langText.getIsDefault().booleanValue()) {
                    langTextDTO.put("isDefault", langText.getIsDefault());
                    haveDefaultTranslation = Boolean.valueOf(true);
                } else {
                    langTextDTO.put("isDefault", Boolean.valueOf(false));
                }

                translatedSystemLanguages.add(langTextDTO);
            } catch (FinderException e) {
                langTextDTO.put("languageName", systemLanguage.getLanguageName());
                langTextDTO.put("languageId", systemLanguage.getLanguageId());
                langTextDTO.put("isDefault", Boolean.valueOf(false));
                translatedSystemLanguages.add(langTextDTO);
            }
        }

        List sortedStructure = SortUtils.orderByPropertyMap((ArrayList) translatedSystemLanguages, "languageName");
        resultDTO.put("translatedSystemLanguages", sortedStructure);

        resultDTO.put("haveDefaultTranslation", haveDefaultTranslation);
    }

    private Integer saveSystemTranslations(String translationFieldName, ParamDTO myParamDTO) {
        log.debug("Executing saveSystemTranslations...");
        Integer companyId = Integer.valueOf((String) myParamDTO.get("companyId"));

        Integer numberOfTranslations = Integer.valueOf("0");
        if (myParamDTO.get("numberOfTranslations") != null && !"".equals(myParamDTO.get("numberOfTranslations"))) {
            numberOfTranslations = new Integer((String) myParamDTO.get("numberOfTranslations"));
        }


        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        Integer actualLangText = null;
        Integer langTextId = null;
        if (myParamDTO.get(translationFieldName) != null && !"".equals(myParamDTO.get(translationFieldName).toString())) {
            langTextId = Integer.valueOf((String) myParamDTO.get(translationFieldName));
        }

        for (int i = 0; i < numberOfTranslations.intValue(); i++) {

            String text = (String) myParamDTO.get("text" + (i + 1));
            Integer languageId = Integer.valueOf((String) myParamDTO.get("languageId" + (i + 1)));


            try {
                LangText langTexts = langTextHome.findByPrimaryKey(new LangTextPK(langTextId, languageId));
                if (text != null && !"".equals(text.trim())) {
                    langTexts.setText(text);
                }
            } catch (FinderException e) {
                log.debug("Cannot find LangText...");
                log.debug("Create new LangText");

                if (!"".equals(text.trim())) {
                    if (langTextId == null) {
                        try {

                            LangTextDTO newLangTextDTO = new LangTextDTO();
                            newLangTextDTO.put("languageId", languageId);
                            newLangTextDTO.put("companyId", companyId);
                            LangText newLangText = langTextHome.create(newLangTextDTO);
                            newLangText.setText(text);
                            newLangText.setType(SystemLanguage.SYSTEM_TRANSLATION);
                            langTextId = newLangText.getLangTextId();
                            actualLangText = langTextId;

                            resultDTO.put(NEW_LANGTEXT, actualLangText);
                        } catch (CreateException e1) {
                            log.error("Error creating the langtext", e1);
                        }
                    } else {
                        LangTextDTO langTextDTO = new LangTextDTO();
                        langTextDTO.put("langTextId", langTextId);
                        langTextDTO.put("languageId", languageId);
                        langTextDTO.put("text", text);
                        langTextDTO.put("companyId", companyId);
                        langTextDTO.put("type", SystemLanguage.SYSTEM_TRANSLATION);

                        try {
                            langTextHome.create(langTextDTO);
                        } catch (CreateException e1) {
                            log.error("Error creating the langtext", e1);
                        }
                    }
                }
            }
        }
        if (null != langTextId) {
            LangText defaultLangText = null;
            try {
                defaultLangText = langTextHome.findByIsDefault(langTextId);
            } catch (FinderException e) {
                log.debug("not have definded default langtext");
            }
            if (null == defaultLangText) {
                try {


                    Integer defaultLanguageId = new Integer(myParamDTO.get("isDefault").toString());

                    Collection langTexts = langTextHome.findByLangTextId(langTextId);
                    for (Iterator iterator = langTexts.iterator(); iterator.hasNext();) {
                        LangText langText = (LangText) iterator.next();


                        if (!"".equals(langText.getText().trim())
                                && defaultLanguageId.equals(langText.getLanguageId())) {

                            langText.setIsDefault(Boolean.valueOf(true));
                            break;

                        }
                    }
                } catch (FinderException e) {
                    log.debug("Cannot find langtexts definded for langtextId = " + langTextId);
                }
            }
        }


        resultDTO.setForward("Success");
        return actualLangText;
    }


    /**
     * This method synchronizes the translation by default or first translation made
     * with the field "name" of catalog associate
     *
     * @param ctx           The context of Command
     * @param myParamDTO    ParamDTO object
     * @param fieldNameKey  field of catalog to synchronize with translation text
     * @param catalogPkKey  primary key field of catalog
     * @param langTextIdKey langtextid field
     * @param catalogDTO    Catalog DTO class
     */
    public void sincronizeFirstTranslation(SessionContext ctx,
                                           ParamDTO myParamDTO,
                                           String fieldNameKey,
                                           String catalogPkKey,
                                           String langTextIdKey,
                                           Class catalogDTO) {

        log.debug("sincronizeFirstTranslation method with \n" +
                "myParamDTO    = " + myParamDTO + "\n" +
                "fieldNameKey  = " + fieldNameKey + "\n" +
                "catalogPkKey  = " + catalogPkKey + "\n" +
                "langTextIdKey = " + langTextIdKey + "\n" +
                "catalogDTO    = " + catalogDTO);


        //creates new instance of the dto class
        ComponentDTO dto = null;
        try {
            dto = (ComponentDTO) Class.forName(catalogDTO.getName()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (null != dto) {

            LangTextHome home = (LangTextHome)
                    EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

            // langtextId
            Integer langTextId = Integer.valueOf(myParamDTO.get(langTextIdKey).toString());

            // primary key of catalog
            Integer pkCatalog = Integer.valueOf(myParamDTO.get(catalogPkKey).toString());

            try {
                // find the first langText
                LangText langText = home.findByIsDefault(langTextId);

                //creates DTO of a catalog
                dto.put(fieldNameKey, langText.getText());
                dto.put(catalogPkKey, pkCatalog);

                //update field name of catalog
                ExtendedCRUDDirector.i.update(dto, resultDTO, false, false, false, "Fail");

            } catch (FinderException e) {
                log.debug("Cannot find langtext with langtextId = " + langTextId);
            }
        }
    }

    /**
     * Setting up the first translation in langtext
     *
     * @param ctx        Command context
     * @param text       text for the first translation
     * @param languageId languageId for the translation
     * @param langTextId langtextId for the translation (only if op = update)
     * @param companyId  company
     * @param op         operation for create or update translations
     * @return langtextId (if op = create then have assign langtextId to catalog)
     */
    public Integer setFirstTranslation(SessionContext ctx,
                                       String text,
                                       Integer languageId,
                                       Integer langTextId,
                                       Integer companyId,
                                       String op) {

        log.debug("setFirstTranslation method with \n" +
                "text       = " + text + "\n" +
                "languageId = " + languageId + "\n" +
                "langtextId = " + langTextId + "\n" +
                "companyId  = " + companyId + "\n" +
                "op         = " + op + "\n");

        Integer actualLangTextId = null;

        LangTextHome langTextHome = (LangTextHome)
                EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        if ("create".equals(op)) {
            LangTextDTO newLangTextDTO = new LangTextDTO();
            newLangTextDTO.put("languageId", languageId);
            newLangTextDTO.put("companyId", companyId);
            LangText newLangText = null;
            try {
                newLangText = langTextHome.create(newLangTextDTO);
                newLangText.setText(text);
                newLangText.setType(SystemLanguage.SYSTEM_TRANSLATION);
                newLangText.setIsDefault(Boolean.valueOf(true));

                actualLangTextId = newLangText.getLangTextId();

            } catch (CreateException e) {
                log.debug("Cannot create LangText");
            }
        }
        if ("update".equals(op)) {
            try {
                LangText langText = langTextHome.findByIsDefault(langTextId);
                langText.setText(text);
                actualLangTextId = langText.getLangTextId();
            } catch (FinderException e) {
                log.debug("Cannot find default LangText with langtextId = " + langTextId);

                LangTextPK myLangTextPK = new LangTextPK(langTextId, languageId);

                try {
                    LangText actualLangText = langTextHome.findByPrimaryKey(myLangTextPK);
                    actualLangText.setText(text);
                    actualLangText.setIsDefault(Boolean.valueOf(true));
                    actualLangText.setType(SystemLanguage.SYSTEM_TRANSLATION);
                } catch (FinderException e1) {
                    log.debug("Cannot find  langText with langtextPK = " + myLangTextPK);

                    LangTextDTO newLangTextDTO = new LangTextDTO();
                    newLangTextDTO.put("languageId", languageId);
                    newLangTextDTO.put("companyId", companyId);
                    newLangTextDTO.put("langTextId", langTextId);
                    newLangTextDTO.put("text", text);
                    newLangTextDTO.put("isDefault", Boolean.valueOf(true));
                    newLangTextDTO.put("type", SystemLanguage.SYSTEM_TRANSLATION);
                    LangText newLangText = null;
                    try {
                        newLangText = langTextHome.create(newLangTextDTO);
                        actualLangTextId = newLangText.getLangTextId();

                    } catch (CreateException ec) {
                        log.debug("Cannot create LangText");
                    }
                }
            }
        }
        return actualLangTextId;
    }

    public boolean isStateful() {
        return false;
    }
}
