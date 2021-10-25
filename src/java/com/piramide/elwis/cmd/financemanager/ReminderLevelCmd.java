package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.financemanager.ReminderLevelDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class ReminderLevelCmd extends EJBCommand {
    private Log log = LogFactory.getLog(ReminderLevelCmd.class);

    public void executeInStateless(SessionContext ctx) {
        boolean isRead = true;
        ReminderLevelDTO reminderLevelDTO = getReminderLevelDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(reminderLevelDTO, ctx);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(reminderLevelDTO);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(reminderLevelDTO);
        }
        if ("getNextReminderLevel".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            getNextReminderLevel(invoiceId);
        }
        if ("getReminderLevel".equals(getOp())) {
            isRead = false;
            Integer invoiceId = (Integer) paramDTO.get("invoiceId");
            getReminderLevel(invoiceId);
        }
        if ("getReminderLevelName".equals(getOp())) {
            isRead = false;
            Integer reminderLevel = (Integer) paramDTO.get("reminderLevel");
            Integer companyId = (Integer) paramDTO.get("companyId");
            getReminderLevelName(reminderLevel, companyId);
        }
        if (isRead) {
            boolean checkReferences = null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences").toString().trim());
            read(reminderLevelDTO, checkReferences);
        }
    }

    private String getReminderLevelName(Integer level, Integer companyId) {
        ReminderLevelHome reminderLevelHome =
                (ReminderLevelHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERLEVEL);
        try {
            ReminderLevel reminderLevel = reminderLevelHome.findByLevel(level, companyId);
            resultDTO.put("getReminderLevelName", reminderLevel.getName());
            return reminderLevel.getName();
        } catch (FinderException e) {
            log.debug("-> Read InvoiceLevel name levelId=" + level + " companyId=" + companyId + " FAIL");
        }
        resultDTO.put("getReminderLevelName", "");
        return "";
    }

    private ReminderLevelDTO getReminderLevel(Integer invoiceId) {
        ReminderLevelDTO dto = new ReminderLevelDTO();

        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        Invoice invoice;
        try {
            invoice = invoiceHome.findByPrimaryKey(invoiceId);
            resultDTO.put("reminderDate", invoice.getReminderDate());
            resultDTO.put("invoiceDate", invoice.getInvoiceDate());
        } catch (FinderException e) {
            log.debug("->Read Invoice invoiceId=" + invoiceId + " FAIL");
            resultDTO.put("getReminderLevel", null);
            resultDTO.put("reminderDate", null);
            resultDTO.put("invoiceDate", null);
            return null;
        }

        if (null == invoice.getReminderLevel()) {
            resultDTO.put("getReminderLevel", null);
            return null;
        }

        ReminderLevelHome reminderLevelHome =
                (ReminderLevelHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERLEVEL);

        try {
            ReminderLevel reminderLevel = reminderLevelHome.findByLevel(invoice.getReminderLevel(),
                    invoice.getCompanyId());
            DTOFactory.i.copyToDTO(reminderLevel, dto);
        } catch (FinderException e) {
            log.debug("-> Read ReminderLevel level=" + invoice.getReminderLevel() +
                    " companyId=" + invoice.getCompanyId() + " FAIL");
            resultDTO.put("getReminderLevel", null);
            return null;
        }

        resultDTO.put("getReminderLevel", dto);
        return dto;
    }

    private ReminderLevelDTO getNextReminderLevel(Integer invoiceId) {
        ReminderLevelDTO dto = new ReminderLevelDTO();

        InvoiceHome invoiceHome =
                (InvoiceHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICE);
        Invoice invoice;
        try {
            invoice = invoiceHome.findByPrimaryKey(invoiceId);
        } catch (FinderException e) {
            log.debug("->Read Invoice invoiceId=" + invoiceId + " FAIL");
            resultDTO.put("getNextReminderLevel", dto);
            resultDTO.put("invoiceLevel", 0);
            return dto;
        }

        Integer actualLevel = invoice.getReminderLevel();
        if (null == actualLevel) {
            actualLevel = 0;
        }

        ReminderLevelHome reminderLevelHome =
                (ReminderLevelHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_REMINDERLEVEL);

        try {
            Collection reminderLevels = reminderLevelHome.findByCompanyId(invoice.getCompanyId());
            Iterator iterator = reminderLevels.iterator();
            while (iterator.hasNext()) {
                ReminderLevel reminderLevel = (ReminderLevel) iterator.next();
                if (reminderLevel.getLevel() > actualLevel) {
                    DTOFactory.i.copyToDTO(reminderLevel, dto);
                    break;
                }
            }
        } catch (FinderException e) {
            log.debug("-> Read ReminderLevels companyId=" + invoice.getCompanyId());
            resultDTO.put("getNextReminderLevel", null);
            resultDTO.put("invoiceLevel", actualLevel);
            return null;
        }

        resultDTO.put("getNextReminderLevel", dto);
        resultDTO.put("invoiceLevel", actualLevel);
        return dto;
    }

    private void read(ReminderLevelDTO reminderLevelDTO, boolean checkReferences) {
        ExtendedCRUDDirector.i.read(reminderLevelDTO, resultDTO, checkReferences);
    }

    private void create(ReminderLevelDTO reminderLevelDTO, SessionContext ctx) {
        ReminderLevel reminderLevel =
                (ReminderLevel) ExtendedCRUDDirector.i.create(reminderLevelDTO, resultDTO, false);

        ReminderTextCmd reminderTextCmd = new ReminderTextCmd();
        reminderTextCmd.setOp("create");
        reminderTextCmd.putParam("isDefault", "true");
        reminderTextCmd.putParam("template", paramDTO.get("template"));
        reminderTextCmd.putParam("reminderLevelId", reminderLevel.getReminderLevelId());
        reminderTextCmd.putParam("languageId", paramDTO.get("languageId"));
        reminderTextCmd.putParam("companyId", reminderLevel.getCompanyId());

        reminderTextCmd.executeInStateless(ctx);
    }

    private void update(ReminderLevelDTO reminderLevelDTO) {
        ExtendedCRUDDirector.i.update(reminderLevelDTO, resultDTO, false, true, false, "Fail");

    }

    private void delete(ReminderLevelDTO reminderLevelDTO) {
        ReminderLevel reminderLevel =
                (ReminderLevel) ExtendedCRUDDirector.i.read(reminderLevelDTO, resultDTO, true);

        if (null == reminderLevel) {
            return;
        }

        Collection reminderTexts = reminderLevel.getReminderTexts();
        Iterator iterator = reminderTexts.iterator();
        while (iterator.hasNext()) {
            ReminderText reminderText = (ReminderText) iterator.next();
            try {
                reminderText.remove();
                iterator = reminderTexts.iterator();
            } catch (RemoveException e) {
                log.debug("->Delete ReminderText reminderLevelId=" +
                        reminderLevel.getReminderLevelId() + " FAIL");
            }
        }

        try {
            reminderLevel.remove();
        } catch (RemoveException e) {
            log.debug("->Delete ReminderLevel reminderLevelId=" + reminderLevel.getReminderLevelId() + " FAIL");
        }
    }

    private ReminderLevelDTO getReminderLevelDTO() {
        ReminderLevelDTO dto = new ReminderLevelDTO();
        dto.put("reminderLevelId", getValueAsInteger("reminderLevelId"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("fee", paramDTO.get("fee"));
        dto.put("level", paramDTO.get("level"));
        dto.put("name", paramDTO.get("name"));
        dto.put("numberOfDays", paramDTO.get("numberOfDays"));
        dto.put("version", paramDTO.get("version"));
        dto.put("withReferences", paramDTO.get("withReferences"));

        log.debug("->Working InvoiceTextDTO" + dto + " OK");

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
