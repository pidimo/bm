package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.GeneralCmd;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.catalogmanager.LanguageHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the language
 *
 * @author Ivan
 * @version $Id: LanguageCmd.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class LanguageCmd extends GeneralCmd {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("operation = '" + this.getOp() + "'");
        log.debug("languageId...." + paramDTO.get("languageId"));
        String op = this.getOp();
        LanguageHome home = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);

        //check duplicate ISO entry
        if (ExtendedCRUDDirector.OP_CREATE.equals(op) || ExtendedCRUDDirector.OP_UPDATE.equals(op)) {
            boolean isDuplicadedIso = false;
            Integer companyId = new Integer(paramDTO.get("companyId").toString());
            Integer acualLanguageId = null;
            Language languageByDefault = null;

            try {
                acualLanguageId = new Integer((String) paramDTO.get("languageId"));
            } catch (NumberFormatException nfe) {
            }

            //find all iso languages associated

            try {
                languageByDefault = home.findByDefault(companyId);
            } catch (FinderException e) {
                log.debug(" ... language byDefault not found ...");
            }

/*  if language by default already exist formed for this company and other is created by default then
    this is the new language by default.
*/
            if (languageByDefault != null && "on".equals(paramDTO.getAsString("isDefault"))) {
                languageByDefault.setIsDefault(false);
            }
            paramDTO.put("isDefault", new Boolean("on".equals(paramDTO.getAsString("isDefault"))));

            String isoAssigned = (String) paramDTO.get("languageIso");
            try {
                Collection languagesWithIURelation = home.findByUILanguages(companyId);
                for (Iterator iterator = languagesWithIURelation.iterator(); iterator.hasNext();) {
                    Language language = (Language) iterator.next();

                    // if have selected iso value from IU and
                    // this iso exists in some iso languages already in data base and
                    // this language is not the same languaje from IU
                    // then iso selected in IU is duplicaded
                    if (null != isoAssigned &&
                            isoAssigned.equals(language.getLanguageIso()) &&
                            !language.getLanguageId().equals(acualLanguageId)) {
                        isDuplicadedIso = true;
                        break;
                    }
                }
            } catch (FinderException e) {
            }
            //setting up error msg if isDuplicaded iso have true value
            if (isDuplicadedIso) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage("Language.error.duplicated.iso");
                return;
            }
        }
        super.setOp(this.getOp());
        if (ExtendedCRUDDirector.OP_DELETE.equals(this.getOp()) || "true".equals(paramDTO.get("withReferences"))) {
            try {
                log.debug(" languageId     ->" + paramDTO.get("languageId"));
                Language language = home.findByPrimaryKey(new Integer(paramDTO.get("languageId").toString()));
                if (language.getIsDefault() != null) {
                    if (language.getIsDefault().booleanValue()) {
                        resultDTO.setResultAsFailure();
                        resultDTO.addResultMessage("Language.error.defaultNotRemove");
                        return;
                    }
                }
            } catch (FinderException e) {
                log.debug(" language not found ...");
            }
        }
        super.executeInStateless(ctx, paramDTO, LanguageDTO.class);
    }

    /**
     * Set language by default of the company.
     */
    public Integer getLanguageByDefault(Integer companyId) {
        LanguageHome home = (LanguageHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGUAGE);
        Language languageByDefault = null;
        Integer id = null;
        try {
            languageByDefault = home.findByDefault(companyId);
            id = languageByDefault.getLanguageId();
        } catch (FinderException e) {
            log.debug(" .. language by default for this cia. not found ...");
        }

        return id;
    }

    public Integer getLanguageContact(Integer companyId, Integer addressId) {

        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address address = null;
        Integer id = null;
        try {
            address = addressHome.findByPrimaryKey(addressId);
            id = address.getLanguageId();
        } catch (FinderException e) {
            log.debug(" .. language for this address not found ...");
        }

        if (id == null || "".equals(id)) {
            id = getLanguageByDefault(companyId);
        }
        return id;
    }


    public boolean isStateful() {
        return false;
    }
}
