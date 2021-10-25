package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.catalogmanager.LangTextPK;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.dto.catalogmanager.TelecomTypeDTO;
import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.dto.common.Translate;
import com.piramide.elwis.dto.common.ValidateCatalog;
import com.piramide.elwis.dto.contactmanager.CompanyDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.SystemLanguage;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.SysLevelException;
import net.java.dev.strutsejb.dto.ComponentDTO;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBLocalObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AlfaCentauro Team
 *
 * @author Ivan
 * @version $Id: CatalogCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CatalogCmd extends EJBCommand {
    private Log log = LogFactory.getLog(CatalogCmd.class);
    /**
     * Define if check references. By default is true.
     */
    protected boolean checkReferences;
    /**
     * Define if is clearing Form. By default is true.
     */
    protected boolean isClearingForm;

    private String dtoClassName;

    private String notFoundForward;
    private boolean validate;

    public CatalogCmd() {
        notFoundForward = "Fail";
        isClearingForm = true;
        checkReferences = false;
        validate = false;
    }

    /**
     * This method, execute the logic related catalog operations an catalog controls
     * eg:
     * read, update, delete operations
     * <p/>
     * control of concurrence, referencial integrity controls
     * <p/>
     * and it assigns translations to catalog, as long as  the DTO of this catalog implements of Translate interface
     *
     * @param sessionContext
     * @throws AppLevelException
     */
    public void executeInStateless(SessionContext sessionContext) throws AppLevelException {
        final ComponentDTO dto = instantiateDTO(dtoClassName);
        dto.putAll(paramDTO);
        log.debug("Validate:" + validate);
        if (validate && ((ValidateCatalog) dto).evaluation()) {
            IntegrityReferentialChecker.i.check(dto, resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        //update, create, delete, read catalog
        EJBLocalObject localObject = ExtendedCRUDDirector.i.doCRUD(getOp(), dto, resultDTO, checkReferences, isClearingForm, notFoundForward);

        //if telecomtypes has been modificated
        if (dto instanceof TelecomTypeDTO && !resultDTO.isFailure()) {
            UpdateTelecomTypeStatus();
        }

        //if catalog have translations associated
        if (dto instanceof Translate && null != localObject) {

            Translate translateDTO = (Translate) dto;
            //read field Name of field can be translated
            // eg: telecomTypeName
            String fieldToTranslate = translateDTO.fieldToTranslate();

            //read field name of languaje that translation can be realized
            // eg:languageId
            String languageToTranslate = translateDTO.fieldOfLanguaje();

            //read field name of relation catalog
            // eg:langtextId
            String relationField = translateDTO.fieldRelatedWithObject();


            String actualTranslation = null;

            //if catalog have translation associated
            if (null != dto.get(relationField) && !"".equals((dto.get(relationField)).toString().trim())) {
                actualTranslation = dto.get(relationField).toString();
            }


            log.debug("fieldToTranslate " + fieldToTranslate);
            log.debug("languageToTranslate " + languageToTranslate);
            log.debug("relationField " + relationField);

            log.debug("dto.get(fieldToTranslate)" + dto.get(fieldToTranslate));
            log.debug("dto.get(languageToTranslate)" + dto.get(languageToTranslate));
            log.debug("dto.get(relationField)" + dto.get(relationField));


            LangTextHome langTextHome = (LangTextHome)
                    EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);

            if ((ExtendedCRUDDirector.OP_CREATE.equals(this.getOp()) ||
                    ExtendedCRUDDirector.OP_UPDATE.equals(this.getOp())) && !resultDTO.isFailure())

            {
                if (null != actualTranslation) {
                    try {
                        LangText langText = langTextHome.findByIsDefault(new Integer(actualTranslation));
                        langText.setText((String) dto.get(fieldToTranslate));
                    } catch (FinderException fe) {
                        log.debug("Cannot find Default translation for Catalog");
                        LangTextPK pk = new LangTextPK(new Integer(actualTranslation),
                                new Integer((String) dto.get(languageToTranslate)));

                        try {
                            LangText lt = langTextHome.findByPrimaryKey(pk);
                            lt.setIsDefault(Boolean.valueOf(true));
                        } catch (FinderException e) {
                            log.error("Cannot find LangText with pk = " + pk, e);
                        }
                    }
                } else {

                    LangTextDTO langTextDTO = new LangTextDTO();
                    langTextDTO.put("languageId", dto.get(languageToTranslate));
                    langTextDTO.put("companyId", dto.get("companyId"));
                    LangText langText = (LangText) ExtendedCRUDDirector.i.create(langTextDTO, resultDTO, false);
                    langText.setIsDefault(Boolean.valueOf(true));
                    langText.setText((String) dto.get(fieldToTranslate));
                    langText.setType(SystemLanguage.SYSTEM_TRANSLATION);

                    ComponentDTO myDto = instantiateDTO(dtoClassName);
                    myDto.put(relationField, langText.getLangTextId());


                    ExtendedDTOFactory.i.copyFromDTO(myDto, localObject, false);
                }
            }

            if (ExtendedCRUDDirector.OP_DELETE.equals(this.getOp()) && !resultDTO.isFailure()) {
                if (null != actualTranslation) {
                    try {
                        Collection allTranslationsAssociated =
                                langTextHome.findByLangTextId(new Integer(actualTranslation));
                        List elementsToRemove = new ArrayList(allTranslationsAssociated);

                        for (int i = 0; i < elementsToRemove.size(); i++) {
                            LangText lt = (LangText) elementsToRemove.get(i);
                            try {
                                lt.remove();
                            } catch (RemoveException e) {
                                log.error("Cannor remove object ", e);
                            }
                        }
                    } catch (FinderException e) {
                        log.error("Cannot find translations associated to catalog ", e);
                    }
                }
            }

            if (ExtendedCRUDDirector.OP_READ.equals(this.getOp()) ||
                    "".equals(this.getOp().trim()) || resultDTO.isFailure()) {
                if (null != actualTranslation) {
                    try {
                        LangText lt = langTextHome.findByIsDefault(new Integer(actualTranslation));
                        resultDTO.put(languageToTranslate, lt.getLanguageId());
                        resultDTO.put("haveFirstTranslation", Boolean.valueOf(true));
                    } catch (FinderException e) {
                        log.error("Cannot find default LangText ", e);
                        resultDTO.put("haveFirstTranslation", Boolean.valueOf(false));
                    }
                } else {
                    resultDTO.put("haveFirstTranslation", Boolean.valueOf(false));
                }
            }
        }
    }

    private ComponentDTO instantiateDTO(String dtoClassName) {
        try {
            return (ComponentDTO) Class.forName(dtoClassName).newInstance();
        } catch (ClassNotFoundException e) {
            throw new SysLevelException(
                    "DTO class not found: " + dtoClassName);
        } catch (InstantiationException e) {
            throw new SysLevelException(e);
        } catch (IllegalAccessException e) {
            throw new SysLevelException(e);
        }
    }

    public boolean isStateful() {
        return false;
    }

    public void setCheckReferences(boolean checkReferences) {
        this.checkReferences = checkReferences;
    }

    public void setClearingForm(boolean clearingForm) {
        isClearingForm = clearingForm;
    }

    public String getNotFoundForward() {
        return notFoundForward;
    }

    public void setNotFoundForward(String notFoundForward) {
        this.notFoundForward = notFoundForward;
    }

    public void setDtoClassName(String dtoClassName) {
        this.dtoClassName = dtoClassName;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    private void createOrUpdateTranslation() {

    }

    private void UpdateTelecomTypeStatus() {
        log.debug("Update company TelecomTypeStatus field...");

        Long actualTime = new Long(System.currentTimeMillis());

        Company company = (Company) EJBFactory.i.callFinder(new CompanyDTO(),
                "findByPrimaryKey",
                new Object[]{Integer.valueOf(paramDTO.get("companyId").toString())});

        company.setTelecomTypeStatus(actualTime.toString());
    }

}
