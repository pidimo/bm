package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.domain.financemanager.FinanceFreeText;
import com.piramide.elwis.domain.financemanager.FinanceFreeTextHome;
import com.piramide.elwis.domain.financemanager.IncomingPayment;
import com.piramide.elwis.domain.financemanager.IncomingPaymentHome;
import com.piramide.elwis.dto.financemanager.IncomingPaymentDTO;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.dto.ResultMessage;
import net.java.dev.strutsejb.ui.CRUDDirector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionContext;
import java.math.BigDecimal;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingPaymentCmd.java 25-feb-2009 9:08:47
 */
public class IncomingPaymentCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("IncomingPaymentCmd executing......................... " + paramDTO);

        if (paramDTO.get("op").equals("read")) {
            read();
        } else if (paramDTO.get("op").equals("create")) {
            paramDTO.put("version", 1);
            create(ctx);
        } else if (paramDTO.get("op").equals("update")) {
            update(ctx);
        } else if (paramDTO.get("op").equals("delete")) {
            delete(ctx);
        } else if (paramDTO.get("op").equals("getLastPaymentDate")) {
            Integer lastPaymentDate = getLastPaymentDate(new Integer(paramDTO.get("incomingInvoiceId").toString()),
                    new Integer(paramDTO.get("companyId").toString()));
            resultDTO.put("lastPaymentDate", lastPaymentDate);
        }
    }

    private void read() {
        log.debug("reading IncomingPayment.......................");
        IncomingPaymentDTO incomingPaymentDTO = new IncomingPaymentDTO();
        incomingPaymentDTO.put("paymentId", Integer.parseInt(paramDTO.get("paymentId").toString()));
        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingPaymentDTO,
                resultDTO);

        if (incomingPaymentDTO.get("notesId") != null) {
            FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
            try {
                FinanceFreeText financeFreeText = financeFreeTextHome.findByPrimaryKey(new Integer(incomingPaymentDTO.get("notesId").toString()));
                resultDTO.put("notes", new String(financeFreeText.getValue()));
            }
            catch (FinderException e) {
                log.debug("error when reading FinanceFreeText for a IncomingInvoice read............  error: " + e.getMessage());
            }
        }
    }

    private void create(SessionContext ctx) {
        log.debug("creating new IncomingPayment......");
        IncomingPaymentDTO incomingPaymentDTO = new IncomingPaymentDTO();
        incomingPaymentDTO.putAll(paramDTO);
        //update IncomingInvoice
        IncomingInvoiceCmd incomingInvoiceCmd = new IncomingInvoiceCmd();
        incomingInvoiceCmd.putParam("op", "addPayment");
        incomingInvoiceCmd.putParam("incomingInvoiceId", Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()));
        incomingInvoiceCmd.putParam("amount", paramDTO.get("amount"));
        incomingInvoiceCmd.putParam("payDate", paramDTO.get("payDate"));
        incomingInvoiceCmd.putParam("incomingInvoiceVersion", paramDTO.get("incomingInvoiceVersion"));
        incomingInvoiceCmd.executeInStateless(ctx);

        if (!incomingInvoiceCmd.getResultDTO().isFailure()) {
            IncomingPayment incomingPayment = (IncomingPayment) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE,
                    incomingPaymentDTO,
                    resultDTO);
            Object notesText = paramDTO.get("notes");
            if (notesText != null) {
                Integer freeTextId = createNote(notesText.toString(), incomingPayment.getCompanyId());
                if (freeTextId != null) {
                    incomingPayment.setNotesId(freeTextId);
                }
            }
        } else {
            for (Iterator iterator = incomingInvoiceCmd.getResultDTO().getResultMessages(); iterator.hasNext();) {
                ResultMessage resultMessage = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(resultMessage);
            }
            resultDTO.setResultAsFailure();
        }
    }

    private void update(SessionContext ctx) {
        log.debug("updating IncomingPayment......");
        ResultDTO incomingInvoiceRDTO = new ResultDTO();
        IncomingPaymentDTO incomingPaymentDTO = new IncomingPaymentDTO();
        incomingPaymentDTO.putAll(paramDTO);
        //reading incomingPayment
        IncomingPaymentDTO incomingPaymentDTO_actual = new IncomingPaymentDTO();
        incomingPaymentDTO_actual.put("paymentId", Integer.parseInt(paramDTO.get("paymentId").toString()));
        incomingPaymentDTO_actual.put("amount", paramDTO.get("amount"));
        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingPaymentDTO_actual,
                resultDTO);
        BigDecimal amount_actual = (BigDecimal) resultDTO.get("amount");
        if (!resultDTO.isFailure()) {
            if (amount_actual.compareTo((BigDecimal) paramDTO.get("amount")) != 0) {
                //updating invoice
                IncomingInvoiceCmd incomingInvoiceCmd = new IncomingInvoiceCmd();
                incomingInvoiceCmd.putParam("op", "updatePayment");
                incomingInvoiceCmd.putParam("incomingInvoiceId", Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()));
                incomingInvoiceCmd.putParam("amount", paramDTO.get("amount"));
                incomingInvoiceCmd.putParam("old_amount", amount_actual);
                incomingInvoiceCmd.putParam("payDate", paramDTO.get("payDate"));
                incomingInvoiceCmd.putParam("incomingInvoiceVersion", paramDTO.get("incomingInvoiceVersion"));
                incomingInvoiceCmd.executeInStateless(ctx);
                incomingInvoiceRDTO = incomingInvoiceCmd.getResultDTO();
            }
            if (!incomingInvoiceRDTO.isFailure()) { //If amount has not changed or no error occured when udpdating openAmount
                //update notes and incomingPayment info..
                Integer freeTextId = null;
                if (paramDTO.get("notesId") != null && paramDTO.get("notesId").toString().length() > 0) {
                    //update note
                    freeTextId = updateNote(Integer.parseInt(paramDTO.get("notesId").toString()), paramDTO.get("notes"));
                } else if (paramDTO.get("notes") != null) {
                    //create note
                    freeTextId = createNote(paramDTO.get("notes").toString(), Integer.parseInt(paramDTO.get("companyId").toString()));
                }
                incomingPaymentDTO.put("paymentId", new Integer(paramDTO.get("paymentId").toString()));
                incomingPaymentDTO.put("notesId", freeTextId);
                //update IncomingPayment
                ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE,
                        incomingPaymentDTO,
                        resultDTO,
                        true,
                        false,
                        true,
                        true);

                if (resultDTO.isFailure()) {
                    ctx.setRollbackOnly();
                }
            } else { //if error..
                for (Iterator iterator = incomingInvoiceRDTO.getResultMessages(); iterator.hasNext();) {
                    ResultMessage resultMessage = (ResultMessage) iterator.next();
                    resultDTO.addResultMessage(resultMessage);
                }
                resultDTO.setResultAsFailure();
            }
        } else {
            resultDTO.setForward("Fail");
        }
    }

    private void delete(SessionContext ctx) {
        log.debug("deleting IncomingPayment................");
        //add amount from openAmount
        IncomingPaymentDTO incomingPaymentDTO_actual = new IncomingPaymentDTO();
        incomingPaymentDTO_actual.put("paymentId", Integer.parseInt(paramDTO.get("paymentId").toString()));
        incomingPaymentDTO_actual.put("amount", paramDTO.get("amount"));
        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingPaymentDTO_actual,
                resultDTO);
        if (!resultDTO.isFailure()) {
            Integer paymentId_read = (Integer) resultDTO.get("paymentId");
            BigDecimal amount_read = (BigDecimal) resultDTO.get("amount");
            Integer incomingInvoiceId_read = (Integer) resultDTO.get("incomingInvoiceId");
            IncomingInvoiceCmd incomingInvoiceCmd = new IncomingInvoiceCmd();
            resultDTO.clear();
            incomingInvoiceCmd.putParam("op", "removePayment");
            incomingInvoiceCmd.putParam("incomingInvoiceId", incomingInvoiceId_read);
            incomingInvoiceCmd.putParam("amount", amount_read);
            incomingInvoiceCmd.executeInStateless(ctx);
            if (!incomingInvoiceCmd.getResultDTO().isFailure()) {
                //delete incomingPayment
                if (paramDTO.get("notesId") != null && paramDTO.get("notesId").toString().length() > 0) {
                    deleteNote(Integer.parseInt(paramDTO.get("notesId").toString()));
                }

                IncomingPaymentDTO incomingPaymentDTO = new IncomingPaymentDTO();
                incomingPaymentDTO.put("paymentId", paymentId_read);
                incomingPaymentDTO.put("amount", amount_read);
                ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_DELETE,
                        incomingPaymentDTO,
                        resultDTO);
                if (resultDTO.isFailure()) {
                    resultDTO.setForward("Fail");
                }
            } else {
                for (Iterator iterator = incomingInvoiceCmd.getResultDTO().getResultMessages(); iterator.hasNext();) {
                    ResultMessage resultMessage = (ResultMessage) iterator.next();
                    resultDTO.addResultMessage(resultMessage);
                }
                resultDTO.setResultAsFailure();
            }
        } else {
            resultDTO.setForward("Fail");
        }
    }

    /**
     * Create a note for a IncomingPayment
     *
     * @param notesText The notesText
     * @return The FinanceFreeTextId
     */
    private Integer createNote(String notesText, Integer companyId) {
        Integer res = null;
        if (notesText != null) {
            FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
            try {
                FinanceFreeText freeText = financeFreeTextHome.create(notesText.getBytes(), companyId, FreeTextTypes.FREETEXT_INCOMINGPAYMENT);
                res = freeText.getFreeTextId();
            } catch (CreateException e) {
                log.debug("Cannot create FinanceFreeText for a IncomingPayment........" + e.getMessage());
            }
        }
        return (res);
    }

    /**
     * Update a note for a IncomingPayment
     *
     * @param notesId   The notesId
     * @param notesText The text (can be null)
     * @return the freeTextId, or null if this was deleted
     */
    private Integer updateNote(Integer notesId, Object notesText) {
        FinanceFreeText financeFreeText = null;
        FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        Integer res = null;
        try {
            financeFreeText = financeFreeTextHome.findByPrimaryKey(notesId);
        }
        catch (FinderException e) {
            log.debug("error when reading FinanceFreeText for a IncomingPayment update............ " + notesId + " error: " + e.getMessage());
        }
        if (financeFreeText != null) {
            if (notesText != null && notesText.toString().length() > 0) {
                //update the note....
                financeFreeText.setValue(notesText.toString().getBytes());
                res = financeFreeText.getFreeTextId();
            } else {
                //delete the note
                try {
                    financeFreeText.remove();
                } catch (RemoveException e) {
                    log.debug("Error when removing FinanceFreeText for a IncomingPayment.. " + notesId + " error: " + e.getMessage());
                }
            }
        }
        return (res);
    }

    /**
     * Removes the note of a IncomingPayment
     *
     * @param notesId The notesId
     */
    private void deleteNote(Integer notesId) {
        FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        try {
            FinanceFreeText financeFreeText = financeFreeTextHome.findByPrimaryKey(notesId);
            financeFreeText.remove();
        }
        catch (FinderException e) {
            log.debug("error when reading FinanceFreeText for a IncomingPayment update............ " + notesId + " error: " + e.getMessage());
        } catch (RemoveException e) {
            log.debug("Error when removing FinanceFreeText for a IncomingPayment.. " + notesId + " error: " + e.getMessage());
        }
    }

    /**
     * Get the last payment date
     *
     * @param incomingInvoiceId The incomingInvoiceId
     * @param companyId         The companyId
     */
    private Integer getLastPaymentDate(Integer incomingInvoiceId, Integer companyId) {
        IncomingPaymentHome incomingPaymentHome = (IncomingPaymentHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INCOMINGPAYMENT);
        Integer res = null;
        try {
            res = incomingPaymentHome.selectMaxPayDate(incomingInvoiceId, companyId);
        } catch (FinderException e) {
            log.debug("Don't existst incomingPayments...... " + e.getMessage());
        }
        return (res);
    }
}
