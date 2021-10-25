package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.ReminderTextDTO;
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
public class ReminderTextCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ReminderTextCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        boolean isRead = true;

        ReminderTextDTO reminderTextDTO = getReminderTextDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("template");
            Integer companyId = (Integer) paramDTO.get("companyId");
            create(reminderTextDTO, file, companyId);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            ArrayByteWrapper file = (ArrayByteWrapper) paramDTO.get("template");
            update(reminderTextDTO, file);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(reminderTextDTO);
        }
        if (isRead) {
            boolean enableLastElementValidation = null != paramDTO.get("enableLastElementValidation") &&
                    "true".equals(paramDTO.get("enableLastElementValidation").toString().trim());
            read(reminderTextDTO, enableLastElementValidation);
        }
    }

    private void update(ReminderTextDTO reminderTextDTO, ArrayByteWrapper file) {
        //read old default reminderText
        ReminderText defaultReminderText = null;
        try {
            defaultReminderText =
                    getDefaultReminderText((Integer) reminderTextDTO.get("reminderLevelId"),
                            (Integer) reminderTextDTO.get("companyId"));
        } catch (FinderException e) {
            log.debug("-> ReminderLevel reminderLevelId=" + reminderTextDTO.get("reminderLevelId") +
                    " not defined default ReminderText object");
        }

        //remove primaryKey values because this values cannot updated
        reminderTextDTO.remove("reminderLevelId");
        reminderTextDTO.remove("languageId");
        ReminderText updatedReminderText =
                (ReminderText) ExtendedCRUDDirector.i.update(reminderTextDTO, resultDTO, false, true, false, "FAIL");

        //reminderText was deleted
        if (null == updatedReminderText) {
            resultDTO.setForward("Fail");
            return;
        }

        //Version error
        if (resultDTO.isFailure()) {
            return;
        }

        if (null != defaultReminderText &&
                updatedReminderText.getIsDefault() &&
                !updatedReminderText.equals(defaultReminderText)) {
            defaultReminderText.setIsDefault(false);
        }

        if (null != file) {
            try {
                FinanceFreeText freeText = getFreeText(updatedReminderText.getFreetextId());
                freeText.setValue(file.getFileData());
                log.debug("->Update FreeText data OK");
            } catch (FinderException e) {
                log.debug("-> Read FreeText freetextId=" + updatedReminderText.getFreetextId() + " FAIL", e);
            }
        }
    }

    private void read(ReminderTextDTO reminderTextDTO, boolean enableIsLastElementValidation) {
        ReminderText reminderText =
                (ReminderText) ExtendedCRUDDirector.i.read(reminderTextDTO, resultDTO, false);

        if (null == reminderText) {
            resultDTO.setForward("Fail");
            return;
        }

        if (enableIsLastElementValidation &&
                isLastElement(reminderText.getReminderLevelId(), reminderText.getCompanyId())) {
            resultDTO.addResultMessage("TemplateFile.error.CannotDeleteTemplate");
            resultDTO.setForward("Fail");
        }
    }

    private void delete(ReminderTextDTO reminderTextDTO) {
        ReminderText reminderText =
                (ReminderText) ExtendedCRUDDirector.i.read(reminderTextDTO, resultDTO, false);

        if (null == reminderText) {
            resultDTO.setForward("Fail");
            return;
        }

        if (reminderText.getIsDefault()) {
            resultDTO.addResultMessage("TemplateFile.error.default");
            resultDTO.setForward("Fail");
            return;
        }

        if (isLastElement(reminderText.getReminderLevelId(), reminderText.getCompanyId())) {
            resultDTO.addResultMessage("TemplateFile.error.CannotDeleteTemplate");
            resultDTO.setForward("Fail");
            return;
        }

        try {
            reminderText.remove();
        } catch (RemoveException e) {
            log.debug("-> Delete ReminderText FAIL", e);
        }
    }

    private void create(ReminderTextDTO reminderTextDTO,
                        ArrayByteWrapper file,
                        Integer companyId) {
        //remove this key because in creation primary key for entity is null
        reminderTextDTO.remove(reminderTextDTO.getPrimKeyName());

        FinanceFreeTextHome financeFreeTextHome =
                (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        FinanceFreeText freeText = null;
        try {
            freeText = financeFreeTextHome.create(file.getFileData(), companyId, FreeTextTypes.FREETEXT_TEMPLATE_TEXT);
        } catch (CreateException e) {
            log.debug("-> Create ReminderText Fail", e);
        }
        if (null != freeText) {
            reminderTextDTO.put("freetextId", freeText.getFreeTextId());
            if ((Boolean) reminderTextDTO.get("isDefault")) {
                updateDefaultReminderText((Integer) reminderTextDTO.get("reminderLevelId"), companyId);
            }

            ExtendedCRUDDirector.i.create(reminderTextDTO, resultDTO, false);
        }
    }


    private FinanceFreeText getFreeText(Integer freeTextId) throws FinderException {
        FinanceFreeTextHome freeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        return freeTextHome.findByPrimaryKey(freeTextId);
    }

    private boolean isLastElement(Integer reminderLevelId, Integer companyId) {
        ReminderTextHome reminderTextHome =
                (ReminderTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERTEXT);

        try {
            Collection reminderTexts = reminderTextHome.findReminderTextByReminderLevelId(reminderLevelId, companyId);
            if (reminderTexts.size() == 1) {
                return true;
            }

        } catch (FinderException e) {
            log.debug("-> Read reminterText objects reminderLevelId=" + reminderLevelId +
                    "Fail");
            return true;
        }

        return false;
    }

    private void updateDefaultReminderText(Integer reminderLevelId, Integer companyId) {
        ReminderText defultReminderText;
        try {
            defultReminderText = getDefaultReminderText(reminderLevelId, companyId);
            defultReminderText.setIsDefault(false);
            log.debug("-> Update default RemindetText OK");
        } catch (FinderException e) {
            log.debug("-> ReminderLevel reminderLevelId=" +
                    reminderLevelId +
                    " not defined default ReminderText object");
        }
    }

    private ReminderText getDefaultReminderText(Integer reminderTextId, Integer companyId) throws FinderException {
        ReminderTextHome reminderTextHome =
                (ReminderTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERTEXT);

        return reminderTextHome.findDefaultReminderText(reminderTextId, companyId);
    }

    private ReminderTextDTO getReminderTextDTO() {
        Integer reminderLevelId = null;
        Integer languageId = null;
        boolean isDefault = null != paramDTO.get("isDefault") &&
                "true".equals(paramDTO.get("isDefault"));

        if (null != paramDTO.get("reminderLevelId") &&
                !"".equals(paramDTO.get("reminderLevelId").toString().trim())) {
            try {
                reminderLevelId = Integer.valueOf(paramDTO.get("reminderLevelId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse reminderLevelId" + paramDTO.get("reminderLevelId") + " FAIL");
            }
        }

        if (null != paramDTO.get("languageId") &&
                !"".equals(paramDTO.get("languageId").toString().trim())) {
            try {
                languageId = Integer.valueOf(paramDTO.get("languageId").toString());
            } catch (NumberFormatException e) {
                log.debug("-> Parse languageId" + paramDTO.get("languageId") + " FAIL");
            }
        }
        ReminderTextPK reminderTextPK = new ReminderTextPK();
        reminderTextPK.reminderLevelId = reminderLevelId;
        reminderTextPK.languageId = languageId;


        ReminderTextDTO dto = new ReminderTextDTO();
        dto.put("reminderLevelId", reminderLevelId);
        dto.put("languageId", languageId);
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("isDefault", isDefault);
        dto.put("version", paramDTO.get("version"));
        dto.put(dto.getPrimKeyName(), reminderTextPK);

        log.debug("->Working ReminderTextDTO" + dto + " OK");

        return dto;
    }

    public boolean isStateful() {
        return false;
    }
}
