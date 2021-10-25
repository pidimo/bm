package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.financemanager.InvoiceFreeNumber;
import com.piramide.elwis.domain.financemanager.SequenceRule;
import com.piramide.elwis.domain.financemanager.SequenceRuleHome;
import com.piramide.elwis.dto.financemanager.SequenceRuleDTO;
import com.piramide.elwis.utils.FinanceConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author Ivan
 */
public class SequenceRuleCmd extends EJBCommand {
    private Log log = LogFactory.getLog(SequenceRuleCmd.class);

    public void executeInStateless(SessionContext sessionContext) {
        boolean isRead = true;
        SequenceRuleDTO sequenceRuleDTO = getSequenceRuleDTO();

        if ("create".equals(getOp())) {
            isRead = false;
            create(sequenceRuleDTO);
        }
        if ("delete".equals(getOp())) {
            isRead = false;
            delete(sequenceRuleDTO);
        }
        if ("update".equals(getOp())) {
            isRead = false;
            update(sequenceRuleDTO);
        }
        if ("hasChangedFormat".equals(getOp())) {
            isRead = false;
            String format = (String) paramDTO.get("newFormat");
            hasChangedFormat(sequenceRuleDTO, format);
        }
        if ("searchSequenceRuleByType".equals(getOp())) {
            isRead = false;
            Integer type = (Integer) paramDTO.get("type");
            Integer companyId = (Integer) paramDTO.get("companyId");
            searchSequenceRuleByType(type, companyId);
        }
        if ("hasAvailableFreeNumber".equals(getOp())) {
            isRead = false;
            Integer numberId = getValueAsInteger("numberId");
            availableFreeNumbers(numberId);
        }

        if (isRead) {
            boolean checkReferences = (null != paramDTO.get("withReferences") &&
                    "true".equals(paramDTO.get("withReferences")));
            read(sequenceRuleDTO, checkReferences, sessionContext);
        }
    }

    private void create(SequenceRuleDTO sequenceRuleDTO) {
        sequenceRuleDTO.put("lastNumber", 0);
        if (null != paramDTO.get("maximumNumber") &&
                !"".equals(paramDTO.get("maximumNumber").toString().trim())) {
            sequenceRuleDTO.put("lastNumber", Integer.valueOf(paramDTO.get("maximumNumber").toString()));
        }

        ExtendedCRUDDirector.i.create(sequenceRuleDTO, resultDTO, false);
    }

    private void searchSequenceRuleByType(Integer type, Integer companyId) {
        SequenceRuleHome sequenceRuleHome =
                (SequenceRuleHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_SEQUENCERULE);

        SequenceRuleDTO sequenceRuleDTO = null;
        try {
            SequenceRule sequenceRule = sequenceRuleHome.findByType(type, companyId);
            sequenceRuleDTO = new SequenceRuleDTO();
            DTOFactory.i.copyToDTO(sequenceRule, sequenceRuleDTO);
        } catch (FinderException e) {
            log.debug("-> Execute ");
        }
        resultDTO.put("searchSequenceRuleByType", sequenceRuleDTO);
    }

    private void read(SequenceRuleDTO sequenceRuleDTO, boolean checkReferences, SessionContext ctx) {
        SequenceRule sequenceRule = (SequenceRule) ExtendedCRUDDirector.i.read(sequenceRuleDTO, resultDTO, checkReferences);

        if (sequenceRule != null) {
            if (sequenceRule.getDebitorId() != null) {
                //read debitor address name
                resultDTO.put("debitorName", readAddressName(sequenceRule.getDebitorId(), ctx));
            }
        }
    }

    private void update(SequenceRuleDTO sequenceRuleDTO) {

        SequenceRuleHome sequenceRuleHome =
                (SequenceRuleHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_SEQUENCERULE);
        SequenceRule actualSequenceRule = null;
        try {
            actualSequenceRule = sequenceRuleHome.findByPrimaryKey((Integer) sequenceRuleDTO.get("numberId"));
        } catch (FinderException e) {
            log.debug("-> Read SequenceRule [" + sequenceRuleDTO.get("numberId") + "] Fail");
        }

        if (null == actualSequenceRule) {
            resultDTO.addResultMessage("customMsg.NotFound", sequenceRuleDTO.get("label"));
            resultDTO.setForward("Fail");
            return;
        }

        SequenceRule updatedSequenceRule =
                (SequenceRule) ExtendedCRUDDirector.i.update(sequenceRuleDTO, resultDTO, false, true, false, "Fail");
        if (null == updatedSequenceRule) {
            resultDTO.setForward("Fail");
        } else {
            if ("true".equals(paramDTO.get("confirmChangeFormat"))) {
                processAvailableFreeNumbers(updatedSequenceRule);
            }
        }
    }

    private void delete(SequenceRuleDTO sequenceRuleDTO) {
        ExtendedCRUDDirector.i.delete(sequenceRuleDTO, resultDTO, true, "Fail");
    }

    private boolean hasChangedFormat(SequenceRuleDTO sequenceRuleDTO, String format) {
        SequenceRule sequenceRule =
                (SequenceRule) ExtendedCRUDDirector.i.read(sequenceRuleDTO, resultDTO, false);
        boolean result = true;
        if (null == sequenceRule) {
            resultDTO.put("hasChangedFormat", result);
            return result;
        }

        result = sequenceRule.getFormat().equals(format);
        resultDTO.put("hasChangedFormat", result);
        return result;
    }

    private SequenceRuleDTO getSequenceRuleDTO() {
        SequenceRuleDTO dto = new SequenceRuleDTO();
        dto.put("numberId", getValueAsInteger("numberId"));
        dto.put("label", paramDTO.get("label"));
        dto.put("companyId", paramDTO.get("companyId"));
        dto.put("format", paramDTO.get("format"));
        dto.put("lastDate", paramDTO.get("lastDate"));
        dto.put("lastNumber", paramDTO.get("lastNumber"));
        dto.put("resetType", paramDTO.get("resetType"));
        dto.put("startNumber", paramDTO.get("startNumber"));
        dto.put("type", getValueAsInteger("type"));
        dto.put("version", paramDTO.get("version"));
        dto.put("withReferences", paramDTO.get("withReferences"));
        dto.put("debitorId", getValueAsInteger("debitorId"));
        dto.put("debitorNumber", paramDTO.get("debitorNumber"));

        log.debug("-> Working on SequenceRuleDTO=" + dto + " OK");
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

    private String readAddressName(Integer addressId, SessionContext ctx) {
        LightlyAddressCmd addressCmd = new LightlyAddressCmd();
        addressCmd.putParam("addressId", addressId);

        addressCmd.executeInStateless(ctx);

        ResultDTO customResultDTO = addressCmd.getResultDTO();
        return (String) customResultDTO.get("addressName");
    }

    private void availableFreeNumbers(Integer numberId) {
        Collection invoiceFreeNumbers = new ArrayList();
        SequenceRule sequenceRule =(SequenceRule) ExtendedCRUDDirector.i.read(new SequenceRuleDTO(numberId), resultDTO, false);

        if (sequenceRule != null) {
            invoiceFreeNumbers = sequenceRule.getInvoiceFreeNumbers();
        }

        resultDTO.put("availableFreeNumberSize", Integer.valueOf(invoiceFreeNumbers.size()));
    }

    private void processAvailableFreeNumbers(SequenceRule sequenceRule) {
        Collection invoiceFreeNumbers = sequenceRule.getInvoiceFreeNumbers();

        if (paramDTO.get("deleteAllNumbers") != null) {
            Object[] obj = invoiceFreeNumbers.toArray();
            for (int i = 0; i < obj.length; i++) {
                InvoiceFreeNumber invoiceFreeNumber = (InvoiceFreeNumber) obj[i];
                try {
                    invoiceFreeNumber.remove();
                } catch (RemoveException e) {
                    log.debug("Error in delete InvoiceFreeNumber:" + invoiceFreeNumber.getFreeNumberId(), e);
                }
            }
        }

        if (paramDTO.get("updateNumberFormat") != null) {
            for (Iterator iterator = invoiceFreeNumbers.iterator(); iterator.hasNext();) {
                InvoiceFreeNumber invoiceFreeNumber = (InvoiceFreeNumber) iterator.next();
                invoiceFreeNumber.setRuleFormat(sequenceRule.getFormat());
            }
        }
    }

    public boolean isStateful() {
        return false;
    }
}
