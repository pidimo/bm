package com.piramide.elwis.cmd.contactmanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.dto.catalogmanager.PayConditionDTO;
import com.piramide.elwis.dto.catalogmanager.PayConditionTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
public class CopyPayConditionCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");
        PayConditionHome payConditionHome =
                (PayConditionHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITION);

        Collection sourcePayConditions = null;
        try {
            sourcePayConditions = payConditionHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read PayContidions for source company " + source.getCompanyId());
        }
        if (null != sourcePayConditions && !sourcePayConditions.isEmpty()) {
            for (Object obj : sourcePayConditions) {
                PayCondition sourcePayCondition = (PayCondition) obj;

                PayConditionDTO targetDTO = new PayConditionDTO();
                DTOFactory.i.copyToDTO(sourcePayCondition, targetDTO);
                targetDTO.put("companyId", target.getCompanyId());
                targetDTO.put("payConditionId", null);

                try {
                    PayCondition targetPayCondition = payConditionHome.create(targetDTO);
                    createPayConditionTexts(sourcePayCondition, targetPayCondition);

                } catch (CreateException e) {
                    log.debug("Cannot create payCondition");
                }
            }
        }
    }

    private void createPayConditionTexts(PayCondition sourcePaycondition, PayCondition targetPayCondition) {
        PayConditionTextHome payConditionTextHome =
                (PayConditionTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_PAYCONDITIONTEXT);

        Collection sourcePayConditionTexts = sourcePaycondition.getPayConditionTexts();

        if (null == sourcePayConditionTexts) {
            return;
        }

        for (Object object : sourcePayConditionTexts) {
            PayConditionText sourcePayConditionText = (PayConditionText) object;

            Language sourceLanguage = CopyCatalogUtil.i.getLanguage(sourcePayConditionText.getLanguageId());
            if (null == sourceLanguage) {
                continue;
            }

            Language targetLanguage = CopyCatalogUtil.i.searchTargetLanguage(sourceLanguage,
                    CopyCatalogUtil.i.getLanguages(targetPayCondition.getCompanyId()));
            if (null == targetLanguage) {
                continue;
            }

            String sourceText = new String(sourcePayConditionText.getFreeText().getValue());

            FreeText targetFreeText = null;
            try {
                targetFreeText = createFreeText(sourceText, targetPayCondition.getCompanyId());
            } catch (CreateException e) {
                log.debug("-> Create FreeText companyId=" + targetPayCondition.getCompanyId() + " FAIL");
            }

            if (null != targetFreeText) {
                PayConditionTextDTO targetPayConditionTextDTO = new PayConditionTextDTO();
                targetPayConditionTextDTO.put("companyId", targetPayCondition.getCompanyId());
                targetPayConditionTextDTO.put("freetextId", targetFreeText.getFreeTextId());
                targetPayConditionTextDTO.put("languageId", targetLanguage.getLanguageId());
                targetPayConditionTextDTO.put("payConditionId", targetPayCondition.getPayConditionId());

                try {
                    payConditionTextHome.create(targetPayConditionTextDTO);
                } catch (CreateException e) {
                    log.error("-> Create PayConditionText payConditionTextDTO=" +
                            targetPayConditionTextDTO +
                            " FAIL", e);
                }
            }
        }
    }

    private FreeText createFreeText(String text, Integer companyId) throws CreateException {
        FreeTextHome freeTextHome =
                (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);

        return freeTextHome.create(text.getBytes(), companyId, FreeTextTypes.FREETEXT_CATALOG);
    }
}
