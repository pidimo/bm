package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.financemanager.InvoiceTemplate;
import com.piramide.elwis.domain.financemanager.InvoiceText;
import com.piramide.elwis.dto.financemanager.InvoiceTemplateDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceTemplateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(InvoiceTemplateCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        InvoiceTemplateDTO invoiceTemplateDTO = getInvoiceTemplateDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(invoiceTemplateDTO, ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(invoiceTemplateDTO);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(invoiceTemplateDTO);
        }
        if (isRead) {
            boolean checkReferences = null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences").toString().trim());
            read(invoiceTemplateDTO, checkReferences);
        }
    }

    private void read(InvoiceTemplateDTO dto, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(dto, resultDTO, checkReferences);
    }

    private void create(InvoiceTemplateDTO invoiceTemplateDTO, SessionContext ctx) {
        InvoiceTemplate invoiceTemplate =
                (InvoiceTemplate) ExtendedCRUDDirector.i.create(invoiceTemplateDTO, resultDTO, false);

        InvoiceTextCmd invoiceTextCmd = new InvoiceTextCmd();
        invoiceTextCmd.setOp("create");
        invoiceTextCmd.putParam("isDefault", "true");
        invoiceTextCmd.putParam("template", paramDTO.get("template"));
        invoiceTextCmd.putParam("templateId", invoiceTemplate.getTemplateId());
        invoiceTextCmd.putParam("languageId", paramDTO.get("languageId"));
        invoiceTextCmd.putParam("companyId", invoiceTemplate.getCompanyId());
        invoiceTextCmd.executeInStateless(ctx);
    }

    private void update(InvoiceTemplateDTO invoiceTemplateDTO) {
        ExtendedCRUDDirector.i.update(invoiceTemplateDTO, resultDTO, false, true, false, "Fail");
    }

    private void delete(InvoiceTemplateDTO invoiceTemplateDTO) {
        InvoiceTemplate invoiceTemplate =
                (InvoiceTemplate) ExtendedCRUDDirector.i.read(invoiceTemplateDTO, resultDTO, true);
        if (null == invoiceTemplate) {
            return;
        }

        Collection invoiceTexts = invoiceTemplate.getInvoiceTexts();

        Iterator iterator = invoiceTexts.iterator();
        while (iterator.hasNext()) {
            InvoiceText invoiceText = (InvoiceText) iterator.next();
            try {
                invoiceText.remove();
                iterator = invoiceTexts.iterator();
            } catch (RemoveException e) {
                log.debug("->Delete InvoiceText templateId=" + invoiceTemplate.getTemplateId() + " FAIL");
            }
        }

        try {
            invoiceTemplate.remove();
        } catch (RemoveException e) {
            log.debug("->Delete InvoiceTemplate templateId=" + invoiceTemplate.getTemplateId() + " FAIL");
        }
    }

    private InvoiceTemplateDTO getInvoiceTemplateDTO() {
        InvoiceTemplateDTO dto = new InvoiceTemplateDTO();
        dto.put("templateId", getValueAsInteger("templateId"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("title", paramDTO.get("title"));
        dto.put("version", paramDTO.get("version"));
        dto.put("withReferences", paramDTO.get("withReferences"));

        log.debug("->Working InvoiceTemplate" + dto + " OK");
        return dto;
    }

    private Integer getValueAsInteger(String key) {
        Integer value = null;
        if (null != paramDTO.get(key) &&
                !"".equals(paramDTO.get(key).toString().trim())) {
            try {
                value = Integer.valueOf(paramDTO.get(key).toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse " + key + "=" + paramDTO.get(key) + " FAIL");
            }
        }

        return value;
    }

    public boolean isStateful() {
        return false;
    }
}
