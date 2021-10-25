package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.InvoiceTextDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceTextCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;

        InvoiceTextDTO invoiceTextDTO = getInvoiceTextDTO();
        if ("create".equals(getOp())) {
            isRead = false;
            ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("template");
            Integer companyId = (Integer) paramDTO.get("companyId");
            create(invoiceTextDTO, file, companyId);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("template");
            update(invoiceTextDTO, file);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(invoiceTextDTO);
        }
        if (isRead) {
            boolean enableLastElementValidation = null != paramDTO.get("enableLastElementValidation") &&
                    "true".equals(paramDTO.get("enableLastElementValidation").toString().trim());
            read(invoiceTextDTO, enableLastElementValidation);
        }
    }

    private void delete(InvoiceTextDTO invoiceTextDTO) {
        InvoiceText invoiceText =
                (InvoiceText) ExtendedCRUDDirector.i.read(invoiceTextDTO, resultDTO, false);

        if (null == invoiceText) {
            resultDTO.setForward("Fail");
            return;
        }

        if (invoiceText.getIsDefault()) {
            resultDTO.addResultMessage("TemplateFile.error.default");
            resultDTO.setForward("Fail");
            return;
        }

        if (isLastElement(invoiceText.getTemplateId(), invoiceText.getCompanyId())) {
            resultDTO.addResultMessage("TemplateFile.error.CannotDeleteTemplate");
            resultDTO.setForward("Fail");
            return;
        }

        try {
            invoiceText.remove();
        } catch (RemoveException e) {
            log.debug("-> Delete InvoiceText FAIL", e);
        }
    }

    private void read(InvoiceTextDTO invoiceTextDTO, boolean enableIsLastElementValidation) {
        InvoiceText invoiceText =
                (InvoiceText) ExtendedCRUDDirector.i.read(invoiceTextDTO, resultDTO, false);

        if (null == invoiceText) {
            resultDTO.setForward("Fail");
            return;
        }

        if (enableIsLastElementValidation &&
                isLastElement(invoiceText.getTemplateId(), invoiceText.getCompanyId())) {

            resultDTO.addResultMessage("TemplateFile.error.CannotDeleteTemplate");
            resultDTO.setForward("Fail");
        }
    }

    private void update(InvoiceTextDTO invoiceTextDTO, ArrayByteWrapper file) {
        //read old default templateText
        InvoiceText defaultInvoiceText = null;
        try {
            defaultInvoiceText =
                    getDefaultInvoiceText((Integer) invoiceTextDTO.get("templateId"), (Integer) invoiceTextDTO.get("companyId"));
        } catch (FinderException e) {
            log.debug("-> Template templateId=" + invoiceTextDTO.get("templateId") +
                    " not defined default TemplateText object");
        }

        //remove primaryKey values because this values cannot updated
        invoiceTextDTO.remove("templateId");
        invoiceTextDTO.remove("languageId");
        InvoiceText updatedInvoiceText =
                (InvoiceText) ExtendedCRUDDirector.i.update(invoiceTextDTO, resultDTO, false, true, false, "Fail");

        //InvoiceText was deleted
        if (null == updatedInvoiceText) {
            resultDTO.setForward("Fail");
            return;
        }

        //Version error
        if (resultDTO.isFailure()) {
            return;
        }

        if (null != defaultInvoiceText &&
                updatedInvoiceText.getIsDefault() &&
                !updatedInvoiceText.equals(defaultInvoiceText)) {
            defaultInvoiceText.setIsDefault(false);
        }

        if (null != file) {
            try {
                FinanceFreeText freeText = getFreeText(updatedInvoiceText.getFreetextId());
                freeText.setValue(file.getFileData());
                log.debug("->Update FreeText data OK");
            } catch (FinderException e) {
                log.debug("-> Read FreeText freetextId=" + updatedInvoiceText.getFreetextId() + " FAIL", e);
            }
        }
    }

    private void create(InvoiceTextDTO invoiceTextDTO,
                        ArrayByteWrapper file,
                        Integer companyId) {
        //remove this key because in creation primary key for entity is null
        invoiceTextDTO.remove(invoiceTextDTO.getPrimKeyName());

        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        FinanceFreeText freeText = null;
        try {
            freeText = financeFreeTextHome.create(file.getFileData(), companyId, FreeTextTypes.FREETEXT_TEMPLATE_TEXT);
        } catch (CreateException e) {
            log.error("-> Execute FinanceFreeTextHome.create() Fail", e);
        }
        if (null != freeText) {
            invoiceTextDTO.put("freetextId", freeText.getFreeTextId());
            if ((Boolean) invoiceTextDTO.get("isDefault")) {
                updateDefaultInvoiceText((Integer) invoiceTextDTO.get("templateId"), companyId);
            }

            ExtendedCRUDDirector.i.create(invoiceTextDTO, resultDTO, false);
        }
    }

    private void updateDefaultInvoiceText(Integer templateId, Integer companyId) {
        try {
            InvoiceText defaultInvoiceText = getDefaultInvoiceText(templateId, companyId);
            defaultInvoiceText.setIsDefault(false);
            log.debug("-> Update default InvoiceText OK");
        } catch (FinderException e) {
            log.debug("-> Template templateId=" + templateId + " not defined default TemplateText object");
        }
    }


    private InvoiceText getDefaultInvoiceText(Integer templateId, Integer companyId) throws FinderException {
        InvoiceTextHome invoiceTextHome =
                (InvoiceTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICETEXT);
        return invoiceTextHome.findDefaultInvoiceText(templateId, companyId);
    }

    private InvoiceTextDTO getInvoiceTextDTO() {
        Integer languageId = null;
        Integer templateId = null;
        boolean isDefault = null != paramDTO.get("isDefault") &&
                "true".equals(paramDTO.get("isDefault").toString().trim());

        if (null != paramDTO.get("languageId") &&
                !"".equals(paramDTO.get("languageId").toString().trim())) {
            try {
                languageId = Integer.valueOf(paramDTO.get("languageId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse languageId=" + paramDTO.get("languageId") + " FAIL");
            }
        }

        if (null != paramDTO.get("templateId") &&
                !"".equals(paramDTO.get("templateId").toString().trim())) {
            try {
                templateId = Integer.valueOf(paramDTO.get("templateId").toString().trim());
            } catch (NumberFormatException e) {
                log.debug("-> Parse languageId=" + paramDTO.get("templateId") + " FAIL");
            }
        }

        InvoiceTextPK invoiceTextPK = new InvoiceTextPK();
        invoiceTextPK.templateId = templateId;
        invoiceTextPK.languageId = languageId;

        InvoiceTextDTO dto = new InvoiceTextDTO();
        dto.put("languageId", languageId);
        dto.put("templateId", templateId);
        dto.put("isDefault", isDefault);
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("version", paramDTO.get("version"));
        dto.put(dto.getPrimKeyName(), invoiceTextPK);

        log.debug("->Working InvoiceTextDTO" + dto + " OK");
        return dto;
    }

    private FinanceFreeText getFreeText(Integer freeTextId) throws FinderException {
        FinanceFreeTextHome freeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        return freeTextHome.findByPrimaryKey(freeTextId);
    }

    private boolean isLastElement(Integer templateId, Integer companyId) {
        InvoiceTextHome invoiceTemplateHome =
                (InvoiceTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICETEXT);
        try {
            Collection invoiceTexts =
                    invoiceTemplateHome.findInvoiceTextsByTemplateId(templateId,
                            companyId);

            if (invoiceTexts.size() == 1) {
                return true;
            }
        } catch (FinderException e) {
            log.debug("-> Read Template templateId=" + templateId + " FAIL", e);
            return true;
        }

        return false;
    }

    public boolean isStateful() {
        return false;
    }
}
