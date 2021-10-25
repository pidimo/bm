package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.LanguageDTO;
import com.piramide.elwis.dto.catalogmanager.PayConditionDTO;
import com.piramide.elwis.dto.catalogmanager.PayConditionTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
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
public class PayConditionTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(PayConditionTextCmd.class);


    public void executeInStateless(SessionContext sessionContext) {
        boolean isRead = true;
        PayConditionTextDTO payConditionTextDTO = getPayConditionTextDTO();
        Integer companyId = (Integer) paramDTO.get("companyId");
        if ("create".equals(getOp())) {
            isRead = false;
            String text = (String) paramDTO.get("payConditionText");
            create(text, payConditionTextDTO);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update((Integer) payConditionTextDTO.get("payConditionId"),
                    companyId,
                    sessionContext);
        }

        if (isRead) {
            read((Integer) payConditionTextDTO.get("payConditionId"),
                    companyId,
                    sessionContext);
        }
    }

    private void update(Integer payConditionId,
                        Integer companyId,
                        SessionContext ctx) {

        PayCondition payCondition = getPayCondition(payConditionId);
        if (null == payCondition) {
            resultDTO.setForward("Fail");
            return;
        }

        Integer uiVersion = Integer.valueOf(paramDTO.get("version").toString());
        if (!getActualVersion(payConditionId, companyId).equals(uiVersion)) {
            resultDTO.addResultMessage("Common.error.concurrency");
            resultDTO.isClearingForm = true;
            resultDTO.setResultAsFailure();
            read(payConditionId, companyId, ctx);
            return;
        }

        List<String> uiLanguages = (List<String>) paramDTO.get("uiLanguages");
        int versionIncrement = 1;
        for (String uiLanguage : uiLanguages) {
            Integer languageId = Integer.valueOf(uiLanguage);
            String text = (String) paramDTO.get(buildTextKey(languageId));

            try {
                PayConditionText payConditionText = getPayConditionText(payConditionId, languageId);
                if (null == text ||
                        "".equals(text.trim())) {
                    try {
                        payConditionText.remove();
                        versionIncrement++;
                        continue;
                    } catch (RemoveException e) {
                        log.debug("-> Delete PayConditionText payConditionId=" +
                                payConditionId +
                                " languageId=" +
                                languageId + " FAIL");
                        continue;
                    }
                }
                payConditionText.getFreeText().setValue(text.getBytes());
                payConditionText.setVersion(payConditionText.getVersion() + versionIncrement);
            } catch (FinderException e) {
                if (null != text &&
                        !"".equals(text.trim())) {
                    PayConditionTextDTO payConditionTextDTO = new PayConditionTextDTO();
                    payConditionTextDTO.put("payConditionId", payConditionId);
                    payConditionTextDTO.put("languageId", languageId);
                    payConditionTextDTO.put("companyId", companyId);
                    create(text, payConditionTextDTO);
                }
            }
        }
    }

    private void read(Integer payConditionId,
                      Integer companyId,
                      SessionContext ctx) {

        PayCondition payCondition = getPayCondition(payConditionId);
        if (null == payCondition) {
            resultDTO.setForward("Fail");
            return;
        }

        LanguageUtilCmd languageUtilCmd = new LanguageUtilCmd();
        languageUtilCmd.putParam("companyId", companyId);
        languageUtilCmd.setOp("getCompanyLanguages");
        languageUtilCmd.executeInStateless(ctx);
        ResultDTO customResultDTO = languageUtilCmd.getResultDTO();
        List<LanguageDTO> languages = (List<LanguageDTO>) customResultDTO.get("getCompanyLanguages");

        int version = 0;
        for (LanguageDTO languageDTO : languages) {
            Integer languageId = (Integer) languageDTO.get("languageId");

            PayConditionText payConditionText;
            try {
                payConditionText = getPayConditionText(payCondition.getPayConditionId(),
                        languageId);
            } catch (FinderException e) {
                log.debug("-> Read PayConditionText payConditionId=" + payCondition.getPayConditionId() +
                        " languageId=" + languageId + " FAIL");
                continue;
            }

            String text = new String(payConditionText.getFreeText().getValue());

            resultDTO.put(buildLanguageKey(languageId), languageDTO.get("languageName"));
            resultDTO.put(buildTextKey(languageId), text);

            version += payConditionText.getVersion();
        }

        resultDTO.put("payConditionId", payCondition.getPayConditionId());
        resultDTO.put("version", version);
    }

    private void create(String text,
                        PayConditionTextDTO payConditionTextDTO) {
        FreeText freeText = null;
        try {
            freeText = createFreeText(text, (Integer) payConditionTextDTO.get("companyId"));
        } catch (CreateException e) {
            log.error("-> Create PayConditionText payConditionId=" +
                    payConditionTextDTO.get("payConditionId") +
                    " FAIL", e);
            return;
        }

        payConditionTextDTO.put("freetextId", freeText.getFreeTextId());
        ExtendedCRUDDirector.i.create(payConditionTextDTO, resultDTO, false);
    }

    private PayConditionTextDTO getPayConditionTextDTO() {
        Integer payConditionId = null;
        if (null != paramDTO.get("payConditionId") &&
                !"".equals(paramDTO.get("payConditionId").toString().trim())) {
            try {
                payConditionId = Integer.valueOf(paramDTO.get("payConditionId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse payConditionId=" +
                        paramDTO.get("payConditionId") +
                        " FAIL");
            }
        }

        Integer languageId = null;
        if (null != paramDTO.get("languageId") &&
                !"".equals(paramDTO.get("languageId").toString().trim())) {
            try {
                languageId = Integer.valueOf(paramDTO.get("languageId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse payConditionId=" +
                        paramDTO.get("languageId") +
                        " FAIL");
            }
        }

        PayConditionTextDTO dto = new PayConditionTextDTO();
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("freetextId", paramDTO.get("freetextId"));
        dto.put("languageId", languageId);
        dto.put("payConditionId", payConditionId);
        dto.put("version", paramDTO.get("version"));

        log.debug("-> Work on PayConditionDTO=" + dto + " OK");
        return dto;
    }

    private PayCondition getPayCondition(Integer payConditionId) {
        PayConditionDTO payConditionDTO = new PayConditionDTO();
        payConditionDTO.put("payConditionId", payConditionId);
        payConditionDTO.put("payConditionName", paramDTO.get("payConditionName"));

        return (PayCondition) ExtendedCRUDDirector.i.read(payConditionDTO, resultDTO, false);
    }

    private FreeText createFreeText(String text, Integer companyId) throws CreateException {
        FreeTextHome freeTextHome =
                (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

        return freeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_CATALOG);
    }

    private PayConditionText getPayConditionText(Integer payConditionId,
                                                 Integer languageId) throws FinderException {
        PayConditionTextPK payConditionTextPK = new PayConditionTextPK();
        payConditionTextPK.languageId = languageId;
        payConditionTextPK.payConditionId = payConditionId;

        PayConditionTextHome payConditionTextHome = getPayConditionTextHome();

        return payConditionTextHome.findByPrimaryKey(payConditionTextPK);
    }

    private Integer getActualVersion(Integer payConditionId, Integer companyId) {
        PayConditionTextHome payConditionTextHome = getPayConditionTextHome();

        Collection payConditionTexts;
        try {
            payConditionTexts =
                    payConditionTextHome.findByPayConditionId(payConditionId, companyId);
        } catch (FinderException e) {
            log.debug("-> Read PayConditionText payConditionId=" + payConditionId + " FAIL");
            return 0;
        }

        int version = 0;
        for (Object object : payConditionTexts) {
            PayConditionText payConditionText = (PayConditionText) object;
            version += payConditionText.getVersion();
        }

        return version;
    }


    private PayConditionTextHome getPayConditionTextHome() {
        return (PayConditionTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITIONTEXT);
    }

    private String buildTextKey(Integer languageId) {
        return "text_" + languageId;
    }

    private String buildLanguageKey(Integer languageId) {
        return "language_" + languageId;
    }

    public boolean isStateful() {
        return false;
    }
}
