package com.piramide.elwis.cmd.financemanager.copycatalog;

import com.piramide.elwis.cmd.admin.copycatalog.CopyCatalog;
import com.piramide.elwis.cmd.admin.copycatalog.util.CopyCatalogUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.admin.Company;
import com.piramide.elwis.domain.catalogmanager.FreeText;
import com.piramide.elwis.domain.catalogmanager.FreeTextHome;
import com.piramide.elwis.domain.catalogmanager.Language;
import com.piramide.elwis.domain.financemanager.InvoiceTemplate;
import com.piramide.elwis.domain.financemanager.InvoiceTemplateHome;
import com.piramide.elwis.domain.financemanager.InvoiceText;
import com.piramide.elwis.dto.financemanager.InvoiceTemplateDTO;
import com.piramide.elwis.dto.financemanager.InvoiceTextDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5.2
 */
public class CopyInvoiceTemplateCatalog implements CopyCatalog {
    private Log log = LogFactory.getLog(this.getClass());

    public void copyCatalog(Company source, Company target, SessionContext sessionContext) {
        log.debug("Execute " + this.getClass().getName() + " ... ");

        InvoiceTemplateHome invoiceTemplateHome = (InvoiceTemplateHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICETEMPLATE);

        Collection sourceInvoiceTemplates = null;
        try {
            sourceInvoiceTemplates = invoiceTemplateHome.findByCompanyId(source.getCompanyId());
        } catch (FinderException e) {
            log.debug("Cannot read invoice templates for source company " + source.getCompanyId(), e);
        }

        if (null != sourceInvoiceTemplates && !sourceInvoiceTemplates.isEmpty()) {
            for (Object obj : sourceInvoiceTemplates) {
                InvoiceTemplate sourceInvoiceTemplate = (InvoiceTemplate) obj;

                InvoiceTemplateDTO targetDTO = new InvoiceTemplateDTO();
                DTOFactory.i.copyToDTO(sourceInvoiceTemplate, targetDTO);
                targetDTO.put("companyId", target.getCompanyId());
                targetDTO.put("templateId", null);

                InvoiceTemplate targetInvoiceTemplate = (InvoiceTemplate) ExtendedCRUDDirector.i.create(targetDTO, new ResultDTO(), false);

                createInvoiceTexts(sourceInvoiceTemplate, targetInvoiceTemplate);
            }
        }
    }

    private void createInvoiceTexts(InvoiceTemplate sourceInvoiceTemplate, InvoiceTemplate targetInvoiceTemplate) {
        Collection sourceInvoiceTexts = sourceInvoiceTemplate.getInvoiceTexts();

        if (null == sourceInvoiceTexts) {
            return;
        }

        for (Object object : sourceInvoiceTexts) {
            InvoiceText sourceInvoiceText = (InvoiceText) object;

            Language sourceLanguage = CopyCatalogUtil.i.getLanguage(sourceInvoiceText.getLanguageId());
            if (null == sourceLanguage) {
                continue;
            }

            Language targetLanguage = CopyCatalogUtil.i.searchTargetLanguage(sourceLanguage, CopyCatalogUtil.i.getLanguages(targetInvoiceTemplate.getCompanyId()));
            if (null == targetLanguage) {
                continue;
            }

            byte[] fileData = sourceInvoiceText.getFinanceFreeText().getValue();

            FreeText targetFreeText = null;
            try {
                targetFreeText = createFreeText(fileData, targetInvoiceTemplate.getCompanyId());
            } catch (CreateException e) {
                log.debug("-> Create invoiceText FreeText companyId=" + targetInvoiceTemplate.getCompanyId() + " FAIL");
                continue;
            }

            if (null != targetFreeText) {
                InvoiceTextDTO targetInvoiceTextDTO = new InvoiceTextDTO();
                targetInvoiceTextDTO.put("templateId", targetInvoiceTemplate.getTemplateId());
                targetInvoiceTextDTO.put("languageId", targetLanguage.getLanguageId());
                targetInvoiceTextDTO.put("companyId", targetInvoiceTemplate.getCompanyId());
                targetInvoiceTextDTO.put("freetextId", targetFreeText.getFreeTextId());
                targetInvoiceTextDTO.put("isDefault", sourceInvoiceText.getIsDefault());

                ExtendedCRUDDirector.i.create(targetInvoiceTextDTO, new ResultDTO(), false);
            }
        }
    }

    private FreeText createFreeText(byte[] fileData, Integer companyId) throws CreateException {
        FreeTextHome freeTextHome = (FreeTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_FREETEXT);
        return freeTextHome.create(fileData, companyId, FreeTextTypes.FREETEXT_TEMPLATE_TEXT);
    }
}
