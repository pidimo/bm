package com.piramide.elwis.cmd.catalogmanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.catalogmanager.PayCondition;
import com.piramide.elwis.domain.catalogmanager.PayConditionText;
import com.piramide.elwis.dto.catalogmanager.PayConditionDTO;
import net.java.dev.strutsejb.EJBCommand;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;


/**
 * @author Ivan
 * @version $Id: PayConditionCmd.java 9703 2009-09-12 15:46:08Z fernando ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

public class PayConditionCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;

        PayConditionDTO payConditionDTO = getPayConditionDTO();
        if ("create".equals(getOp())) {
            isRead = false;
            Integer languageId = Integer.valueOf(paramDTO.get("languageId").toString());
            String text = (String) paramDTO.get("payConditionText");
            create(payConditionDTO, languageId, text, ctx);
        }
        if ("update".equals(getOp())) {
            update(payConditionDTO);
            isRead = false;
        }
        if ("delete".equals(getOp())) {
            delete(payConditionDTO);
            isRead = false;
        }
        if ("isValidPayCondition".equals(getOp())) {
            isRead = false;
            Integer payConditionId = (Integer) paramDTO.get("payConditionId");
            isValidPayCondition(payConditionId);
        }
        if (isRead) {
            boolean checkReferences = null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences").toString().trim());
            read(payConditionDTO, checkReferences);
        }
    }

    public boolean isStateful() {
        return false;
    }

    private void delete(PayConditionDTO payConditionDTO) {
        PayCondition payCondition =
                (PayCondition) ExtendedCRUDDirector.i.read(payConditionDTO, resultDTO, true);

        if (null == payCondition) {
            resultDTO.setForward("Fail");
            return;
        }

        Collection payConditionTexts = payCondition.getPayConditionTexts();
        Iterator iterator = payConditionTexts.iterator();
        while (iterator.hasNext()) {
            PayConditionText payConditionText = (PayConditionText) iterator.next();
            try {
                payConditionText.remove();
                iterator = payConditionTexts.iterator();
            } catch (RemoveException e) {
                log.debug("-> Delete PayConditionText payConditionId=" +
                        payCondition.getPayConditionId() +
                        " FAIL", e);
                return;
            }
        }

        try {
            payCondition.remove();
        } catch (RemoveException e) {
            log.error("-> Delete PayCondition payConditionId=" + payCondition.getPayConditionId() + " FAIL", e);

        }
    }

    private void update(PayConditionDTO payConditionDTO) {
        ExtendedCRUDDirector.i.update(payConditionDTO, resultDTO, true, true, false, "Fail");
    }

    private void create(PayConditionDTO payConditionDTO,
                        Integer languageId,
                        String text,
                        SessionContext ctx) {
        PayCondition payCondition = (PayCondition) ExtendedCRUDDirector.i.create(payConditionDTO,
                resultDTO, false);

        if (null != payCondition) {
            PayConditionTextCmd payConditionTextCmd = new PayConditionTextCmd();
            payConditionTextCmd.setOp("create");
            payConditionTextCmd.putParam("companyId", payCondition.getCompanyId());
            payConditionTextCmd.putParam("payConditionId", payCondition.getPayConditionId());
            payConditionTextCmd.putParam("languageId", languageId);
            payConditionTextCmd.putParam("payConditionText", text);
            payConditionTextCmd.executeInStateless(ctx);
        }
    }

    private void read(PayConditionDTO payConditionDTO, boolean chekReferences) {
        ExtendedCRUDDirector.i.read(payConditionDTO, resultDTO, chekReferences);
    }

    private PayConditionDTO getPayConditionDTO() {
        Integer payConditionId = null;
        if (null != paramDTO.get("payConditionId") &&
                !"".equals(paramDTO.get("payConditionId").toString().trim())) {
            try {
                payConditionId = Integer.valueOf(paramDTO.get("payConditionId").toString().trim());
            } catch (NumberFormatException e) {
                log.debug("-> Parse payConditionId=" +
                        paramDTO.get("payConditionId") +
                        " FAIL");
            }
        }
        PayConditionDTO dto = new PayConditionDTO();
        dto.put("payConditionId", payConditionId);
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("discount", ("".equals(paramDTO.get("discount")) ? null : paramDTO.get("discount")));
        dto.put("payConditionName", paramDTO.get("payConditionName"));
        dto.put("payDays", ("".equals(paramDTO.get("payDays")) ? null : paramDTO.get("payDays")));
        dto.put("payDaysDiscount", ("".equals(paramDTO.get("payDaysDiscount")) ? null : paramDTO.get("payDaysDiscount")));
        dto.put("version", paramDTO.get("version"));
        dto.put("withReferences", paramDTO.get("withReferences"));
        return dto;
    }

    private boolean isValidPayCondition(Integer payConditionId) {

        PayConditionDTO payConditionDTO = new PayConditionDTO();
        payConditionDTO.put("payConditionId", payConditionId);
        PayCondition payCondition =
                (PayCondition) ExtendedCRUDDirector.i.read(payConditionDTO, resultDTO, false);

        if (null == payCondition) {
            resultDTO.put("isValidPayCondition", false);
            return false;
        }

        if (null == payCondition.getPayDays()) {
            resultDTO.put("isValidPayCondition", false);
            return false;
        }

        resultDTO.put("isValidPayCondition", true);
        return true;
    }
}