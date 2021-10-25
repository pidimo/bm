package com.piramide.elwis.web.contactmanager.form;

import com.piramide.elwis.cmd.financemanager.InvoiceReadCmd;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;

import javax.servlet.http.HttpServletRequest;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class InvoiceForm extends com.piramide.elwis.web.financemanager.form.InvoiceForm {

    @Override
    protected void readCopyOfInvoice(HttpServletRequest request) {
        String copyOfInvoiceId = (String) getDto("copyOfInvoiceId");

        setDto("hasRelation", "");
        if (FinanceConstants.InvoiceType.CreditNote.equal(getDto("type").toString())) {
            copyOfInvoiceId = (String) getDto("creditNoteOfId");
            setDto("hasRelation", "true");
        }

        if (null == copyOfInvoiceId ||
                "".equals(copyOfInvoiceId)) {
            setDto("hasRelation", "");
            return;
        }

        InvoiceReadCmd invoiceReadCmd = new InvoiceReadCmd();
        invoiceReadCmd.putParam("invoiceId", Integer.valueOf(copyOfInvoiceId));

        try {
            ResultDTO resultDTO = BusinessDelegate.i.execute(invoiceReadCmd, request);
            if (resultDTO.isFailure()) {
                return;
            }

            setDto("currencyId", null != resultDTO.get("currencyId") ?
                    resultDTO.get("currencyId").toString() : "");
            setDto("payConditionId", null != resultDTO.get("payConditionId") ?
                    resultDTO.get("payConditionId").toString() : "");
            setDto("templateId", null != resultDTO.get("templateId") ?
                    resultDTO.get("templateId").toString() : "");
            setDto("notes", resultDTO.get("notes"));
            setDto("netGross", null != resultDTO.get("netGross") ?
                    resultDTO.get("netGross").toString() : "");

            setDto("title", null != resultDTO.get("title") ? resultDTO.get("title") : "");

            if (resultDTO.get("sequenceRuleId") != null && !"".equals(resultDTO.get("sequenceRuleId"))) {
                setDto("sequenceRuleId", resultDTO.get("sequenceRuleId"));
            } else {
                updateSequenceRule(request);
            }
        } catch (AppLevelException e) {
            log.error("-> Execute " + InvoiceReadCmd.class.getName() + " FAIL", e);
        }
    }
}
