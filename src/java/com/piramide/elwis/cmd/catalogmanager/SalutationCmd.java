package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.LangText;
import com.piramide.elwis.domain.catalogmanager.LangTextHome;
import com.piramide.elwis.domain.catalogmanager.LangTextPK;
import com.piramide.elwis.domain.catalogmanager.Salutation;
import com.piramide.elwis.dto.catalogmanager.LangTextDTO;
import com.piramide.elwis.dto.catalogmanager.SalutationDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.DuplicatedEntryChecker;
import com.piramide.elwis.utils.IntegrityReferentialChecker;
import com.piramide.elwis.utils.VersionControlChecker;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * This Class suppervice the operations that arrives to web-browser,
 * such operations be (read, create, update, delete, control concurrency,
 * referencial integriry and entry duplicated); all relatinated with the salutation
 *
 * @author Ivan
 * @version $Id: SalutationCmd.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class SalutationCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("executeInStateless(javax.ejb.SessionContext)");
        log.debug("paramDTO ..: " + paramDTO);
        boolean isRead = true;
        if ("create".equals(getOp())) {
            isRead = false;
            create();
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update();
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete();
        }
        if (isRead == true) {
            read();
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void create() {
        SalutationDTO salutationDTO = new SalutationDTO(paramDTO);
        DuplicatedEntryChecker.i.check(salutationDTO, resultDTO, this);
        if (resultDTO.isFailure()) {
            return;
        }


        LangTextDTO letterPartDTO = new LangTextDTO();
        letterPartDTO.put("companyId", salutationDTO.get("companyId"));
        letterPartDTO.put("languageId", salutationDTO.get("languageId"));
        letterPartDTO.put("text", salutationDTO.get("letterText"));

        LangTextDTO addressPartDTO = new LangTextDTO();
        addressPartDTO.put("companyId", salutationDTO.get("companyId"));
        addressPartDTO.put("languageId", salutationDTO.get("languageId"));
        addressPartDTO.put("text", salutationDTO.get("addressText"));

        LangText letterPart = (LangText) EJBFactory.i.createEJB(letterPartDTO);

        LangText addressPart = (LangText) EJBFactory.i.createEJB(addressPartDTO);
        salutationDTO.put("addressTextId", addressPart.getLangTextId());
        salutationDTO.put("letterTextId", letterPart.getLangTextId());


        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE, salutationDTO, resultDTO);

    }

    private void update() {
        //readLanguage();
        SalutationDTO salutationDTO;
        VersionControlChecker.i.check(new SalutationDTO(paramDTO), resultDTO, paramDTO);
        salutationDTO = new SalutationDTO(paramDTO);
        if (resultDTO.get("EntityBeanNotFound") != null) {
            new SalutationDTO(paramDTO).addNotFoundMsgTo(resultDTO);
            resultDTO.setForward("Fail");
            return;
        }
        if (resultDTO.isFailure()) {
            CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, salutationDTO, resultDTO);
            return;
        } else {
            DuplicatedEntryChecker.i.check(salutationDTO, resultDTO, this);
            if (resultDTO.isFailure()) {
                read();
                return;
            }

            salutationDTO.remove("addressTextId");
            salutationDTO.remove("letterTextId");
            Salutation salutation = (Salutation) CRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE, salutationDTO, resultDTO);

            Integer languageId = Integer.valueOf(paramDTO.get("languageId").toString());
            String addressText = (String) paramDTO.get("addressText");
            String letterText = (String) paramDTO.get("letterText");

            LangText addressTextObject = saveLangText(salutation.getCompanyId(),
                    salutation.getAddressTextId(),
                    languageId,
                    addressText);
            LangText letterTextObject = saveLangText(salutation.getCompanyId(),
                    salutation.getLetterTextId(),
                    languageId,
                    letterText);
            salutation.setAddressTextId(addressTextObject.getLangTextId());
            salutation.setLetterTextId(letterTextObject.getLangTextId());
        }
    }

    private void delete() {
        log.debug("Executing delete method on  SalutationCmd ... ");
        //readLanguage();
        SalutationDTO salutationDTO = new SalutationDTO(paramDTO);
        CRUDDirector.i.doCRUD(CRUDDirector.OP_DELETE, salutationDTO, resultDTO);
        Collection ltxCollection;
        LangTextHome ltxHome = (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        try {
            if (null != paramDTO.get("letterTextId") && !"".equals(paramDTO.get("letterTextId").toString().trim())) {
                ltxCollection = ltxHome.findByLangTextId(new Integer(paramDTO.getAsInt("letterTextId")));
                for (Iterator iterator = ltxCollection.iterator(); iterator.hasNext();) {
                    LangText o = (LangText) iterator.next();
                    o.remove();
                }
            }

            if (null != paramDTO.get("addressTextId") && !"".equals(paramDTO.get("addressTextId").toString().trim())) {
                ltxCollection = ltxHome.findByLangTextId(new Integer(paramDTO.getAsInt("addressTextId")));
                for (Iterator iterator = ltxCollection.iterator(); iterator.hasNext();) {
                    LangText o = (LangText) iterator.next();
                    o.remove();
                }
            }
        } catch (FinderException e) {
            log.error("Cannont find LangText on delete method...");
        } catch (RemoveException e) {
            log.error("Cannont remove LanText on delete method...");
        }
        if (resultDTO.isFailure()) {
            resultDTO.setForward("Fail");
            return;
        }
    }

    private void read() {
        if (paramDTO.getAsBool("withReferences")) {
            IntegrityReferentialChecker.i.check(new SalutationDTO(paramDTO), resultDTO);
            if (resultDTO.isFailure()) {
                return;
            }
        }
        Salutation salutation = (Salutation) CRUDDirector.i.doCRUD(CRUDDirector.OP_READ, new SalutationDTO(paramDTO), resultDTO);
        if (null != salutation) {
            Integer languageId = readFirstLanguage(salutation);

            if (null != salutation.getAddressTextId() && null != languageId) {
                LangText addressText = readLangText(salutation.getAddressTextId(), languageId);
                if (null != addressText) {
                    resultDTO.put("addressText", addressText.getText());
                }

                resultDTO.put("languageId", languageId);
            }

            if (null != salutation.getLetterTextId() && null != languageId) {
                LangText letterText = readLangText(salutation.getLetterTextId(), languageId);
                if (null != letterText) {
                    resultDTO.put("letterText", letterText.getText());
                }

                resultDTO.put("languageId", languageId);
            }
        }

        if (resultDTO.isFailure()) {
            return;
        }
    }

    private LangText readLangText(Integer langTextId) {
        LangTextHome langTextHome =
                (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
        LangText firstLangText = null;

        try {
            Collection elements = langTextHome.findByLangTextId(langTextId);
            if (!elements.isEmpty()) {
                firstLangText = (LangText) elements.toArray()[0];
            }
        } catch (FinderException e) {
            log.debug("-> Execute findByLangTextId FAIL", e);
        }

        return firstLangText;
    }

    private Integer readFirstLanguage(Salutation salutation) {
        if (null != salutation.getAddressTextId()) {
            LangText firstLangText = readLangText(salutation.getAddressTextId());
            return firstLangText.getLanguageId();
        } else if (null != salutation.getLetterTextId()) {
            LangText firstLangText = readLangText(salutation.getLetterTextId());
            return firstLangText.getLanguageId();
        }
        return null;
    }

    private LangText readLangText(Integer langTextId, Integer languageId) {
        LangTextHome langTextHome = getLangTextHome();

        LangText langText = null;
        LangTextPK pk = new LangTextPK();
        pk.langTextId = langTextId;
        pk.languageId = languageId;

        try {
            langText = langTextHome.findByPrimaryKey(pk);
        } catch (FinderException e) {
            log.debug("-> Read LangText langTextId=" + langTextId + " languageId=" + languageId + " FAIL");
        }

        return langText;
    }


    private LangText saveLangText(Integer companyId,
                                  Integer langTextId,
                                  Integer languageId,
                                  String text) {
        LangTextHome langTextHome = getLangTextHome();

        LangText langText = null;
        LangTextPK pk = new LangTextPK();
        pk.langTextId = langTextId;
        pk.languageId = languageId;

        try {
            langText = langTextHome.findByPrimaryKey(pk);
            langText.setText(text);
        } catch (FinderException e) {
            log.debug("-> Read LangText langTextId=" + langTextId + " languageId=" + languageId + " FAIL");
            LangTextDTO dto = new LangTextDTO();
            dto.put("languageId", languageId);
            dto.put("companyId", companyId);
            dto.put("text", text);
            try {
                langText = langTextHome.create(dto);
            } catch (CreateException e1) {
                log.debug("-> Create LangText Object FAIL", e1);
            }
        }
        return langText;
    }


    private LangTextHome getLangTextHome() {
        return (LangTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_LANGTEXT);
    }
}