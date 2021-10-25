package com.piramide.elwis.cmd.supportmanager;

import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.utils.CatalogConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class build DTO' list that contain translations for Support catalogs:
 * Priorities, Case Types, Case Severities, Work levels, States
 * <p/>
 * param properties:
 * <p/>
 * companyId = company identifier.
 * componentDTO = children of <code>ComponentDTO</code> object, is used to read and fill database values.
 * isoLanguage = System iso language identifier.
 * ejbIdentifierFieldName = field name of EJB primary key identifier.
 * langTextIdentifierName = field name of EJB translation key identifier.
 * ejbTextName = field name of EJB text.
 * primaryKey = value of primary key EJB.
 * op = operation can be executed ( readAll, readElement ).
 *
 * @author: ivan
 */
public class SupportCatalogSelectCmd extends EJBCommand {
    private Log log = LogFactory.getLog(SupportCatalogSelectCmd.class);

    public static final String METHOD_FINDER_NAME = "findSupportCatalogByCompanyId";
    public static final String TEXT_FIELD_NAME = "translation";
    public static final String TRANSLATIONS = "translations";

    public void executeInStateless(SessionContext sessionContext) {
        Integer companyId = (Integer) paramDTO.get("companyId");
        ComponentDTO dto = (ComponentDTO) paramDTO.get("componentDTO");

        String isoLanguage = (String) paramDTO.get("isoLanguage");
        String ejbIdentifierFieldName = (String) paramDTO.get("ejbIdentifierFieldName");
        String langTextIdentifierName = (String) paramDTO.get("langTextIdentifierName");
        String ejbTextName = (String) paramDTO.get("ejbTextName");
        Integer primaryKey = (Integer) paramDTO.get("primaryKey");

        String operation = (String) paramDTO.get("op");
        if ("readAll".equals(operation)) {
            readAllElementsTranslations(companyId, dto, isoLanguage, ejbIdentifierFieldName, ejbTextName, langTextIdentifierName);
        }

        if ("readElement".equals(operation)) {
            readElementTranslation(primaryKey, dto, isoLanguage, ejbIdentifierFieldName, ejbTextName, langTextIdentifierName);
        }
    }


    /**
     * This method reads the translations of catalog element.
     *
     * @param key                    primary identifier element to read.
     * @param dto                    children of <code>ComponentDTO</code>.
     * @param isoLanguage            language to read translations.
     * @param ejbIdentifierName      catalog identifier field name
     * @param ejbTextName            catalog text field name
     * @param langTextIdentifierName catalog translation identifier field name
     */
    private void readElementTranslation(Integer key,
                                        ComponentDTO dto,
                                        String isoLanguage,
                                        String ejbIdentifierName,
                                        String ejbTextName,
                                        String langTextIdentifierName) {
        List dtoObjects = new ArrayList();
        ComponentDTO newDto = readEJBObjectByPrimaryKey(key, dto);
        dtoObjects.add(newDto);

        readEJBObjectsTranslations(dtoObjects, isoLanguage, ejbIdentifierName, ejbTextName, langTextIdentifierName);
    }


    /**
     * This method reads the translations of each catalog element.
     *
     * @param companyId              Company identifier to read catalog elements
     * @param dto                    children of <code>ComponentDTO</code>.
     * @param isoLanguage            language to read translations.
     * @param ejbIdentifierName      catalog identifier field name
     * @param ejbTextName            catalog text field name
     * @param langTextIdentifierName catalog translation identifier field name
     */
    private void readAllElementsTranslations(Integer companyId,
                                             ComponentDTO dto,
                                             String isoLanguage,
                                             String ejbIdentifierName,
                                             String ejbTextName,
                                             String langTextIdentifierName) {
        List dtoObjects = readEJBObjectsByCompany(companyId, dto);

        readEJBObjectsTranslations(dtoObjects, isoLanguage, ejbIdentifierName, ejbTextName, langTextIdentifierName);
    }

    /**
     * This method read element by primary key
     *
     * @param key primary key to read element
     * @param dto children of <code>ComponentDTO</code>
     * @return <code>ComponentDTO</code> object can contain catalog information.
     */
    private ComponentDTO readEJBObjectByPrimaryKey(Integer key, ComponentDTO dto) {
        EJBLocalObject ejbObj = (EJBLocalObject) EJBFactory.i.callFinder(dto, "findByPrimaryKey", new Integer[]{key});

        DTOFactory.i.copyToDTO(ejbObj, dto);

        return dto;
    }

    /**
     * This method read EJB catalog objects by company identifier.
     *
     * @param companyId Company identifier.
     * @param dto       children of <code>ComponentDTO</code>
     * @return List <code>List</code> of <code>ComponentDTO</code> can contain all elements by company.
     */
    private List readEJBObjectsByCompany(Integer companyId, ComponentDTO dto) {

        List ejbs = (List) EJBFactory.i.callFinder(dto, METHOD_FINDER_NAME, new Integer[]{companyId});

        return DTOFactory.i.createDTOs(ejbs, dto.getClass());
    }

    /**
     * This method reads the translations of each DTO.
     *
     * @param dtoObjects             <code>List</code> of dto objects.
     * @param isoLanguage            language to read translations.
     * @param ejbIdentifierName      catalog identifier field name
     * @param ejbTextName            catalog text field name
     * @param langTextIdentifierName catalog translation identifier field name
     */
    private void readEJBObjectsTranslations(
            List dtoObjects,
            String isoLanguage,
            String ejbIdentifierName,
            String ejbTextName,
            String langTextIdentifierName) {
        LangTextHome langTextHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

        List result = new ArrayList();

        for (int i = 0; i < dtoObjects.size(); i++) {
            ComponentDTO dto = (ComponentDTO) dtoObjects.get(i);
            Integer ejbIdentifier = (Integer) dto.get(ejbIdentifierName);
            Integer langTextIdentifier = (Integer) dto.get(langTextIdentifierName);
            String ejbText = (String) dto.get(ejbTextName);

            try {
                LangText langText = langTextHome.findByLangTextIdAndLanguageRelatedToUI(langTextIdentifier, isoLanguage);
                Map element = new HashMap();
                if (null != langText.getText() && !"".equals(langText.getText())) {
                    element.put(ejbIdentifierName, ejbIdentifier);
                    element.put(TEXT_FIELD_NAME, langText.getText());
                } else {
                    element.put(ejbIdentifierName, ejbIdentifier);
                    element.put(TEXT_FIELD_NAME, ejbText);
                }
                result.add(element);
            } catch (FinderException e) {
                log.debug("Cannot find langText isoLanguage = " + isoLanguage + " langtextId = " + langTextIdentifier);
                Map element = new HashMap();
                element.put(ejbIdentifierName, ejbIdentifier);
                element.put(TEXT_FIELD_NAME, ejbText);
                result.add(element);
            }
        }

        resultDTO.put(TRANSLATIONS, result);
    }

    public boolean isStateful() {
        return false;
    }
}
