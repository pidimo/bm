package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.contactmanager.AddressToSupplierCmd;
import com.piramide.elwis.cmd.contactmanager.LightlyAddressCmd;
import com.piramide.elwis.domain.financemanager.FinanceFreeText;
import com.piramide.elwis.domain.financemanager.FinanceFreeTextHome;
import com.piramide.elwis.domain.financemanager.IncomingInvoice;
import com.piramide.elwis.domain.financemanager.IncomingPayment;
import com.piramide.elwis.dto.financemanager.IncomingInvoiceDTO;
import com.piramide.elwis.utils.ArrayByteWrapper;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.DTOFactory;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 *
 * @author alvaro
 * @version $Id: IncomingInvoiceCmd.java 20-feb-2009 17:08:23
 */
public class IncomingInvoiceCmd extends EJBCommand {

    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("IncomingInvoiceCmd execute................................." + paramDTO);
        String op = getOp();
        if (op.equals("create")) {
            paramDTO.put("openAmount", paramDTO.get("amountGross")); //default when creating
            paramDTO.put("version", 1);
            create(ctx);
        } else if (op.equals("update")) {
            update(ctx);
        } else if (op.equals("delete")) {
            delete();
        } else if (op.equals("read")) {
            read(ctx);
        } else if (op.equals("lightRead")) {
            lightRead();
        } else if (op.equals("addPayment")) {
            addPayment(Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()),
                    (BigDecimal) paramDTO.get("amount"),
                    Integer.parseInt(paramDTO.get("payDate").toString()),
                    Integer.parseInt(paramDTO.get("incomingInvoiceVersion").toString()));
        } else if (op.equals("updatePayment")) {
            updatePayment(Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()),
                    (BigDecimal) paramDTO.get("amount"),
                    (BigDecimal) paramDTO.get("old_amount"),
                    Integer.parseInt(paramDTO.get("payDate").toString()),
                    Integer.parseInt(paramDTO.get("incomingInvoiceVersion").toString()));
        } else if (op.equals("removePayment")) {
            removePayment(Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()),
                    (BigDecimal) paramDTO.get("amount"));
        }

    }

    private void create(SessionContext ctx) {
        log.debug("creating new IncomingInvoice......");
        ArrayByteWrapper documentWrapper = (ArrayByteWrapper) paramDTO.get("fileWrapper");

        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.putAll(paramDTO);
        //convert the address to a supplier (if isn't yet)
        convertAddressToSupplier(Integer.parseInt(paramDTO.get("supplierId").toString()),
                Integer.parseInt(paramDTO.get("companyId").toString()),
                ctx);

        IncomingInvoice incomingInvoice = (IncomingInvoice) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_CREATE,
                incomingInvoiceDTO,
                resultDTO);
        Object notesText = paramDTO.get("notes");
        if (notesText != null) {
            Integer freeTextId = createNote(notesText.toString(), incomingInvoice.getCompanyId());
            if (freeTextId != null) {
                incomingInvoice.setNotesId(freeTextId);
            }
        }

        //create invoice document
        createOrUpdateInvoiceDocument(documentWrapper, incomingInvoice);
    }

    private void update(SessionContext ctx) {
        log.debug("updating IncomingInvoice......");
        ArrayByteWrapper documentWrapper = (ArrayByteWrapper) paramDTO.get("fileWrapper");

        IncomingInvoice incomingInvoice = readIncomingInvoice(new Integer(paramDTO.get("incomingInvoiceId").toString()));
        //Calculate amounts
        BigDecimal paymentsAmountTotal = calculateIncomingPayments(incomingInvoice);
        BigDecimal amountGross = (BigDecimal) paramDTO.get("amountGross");
        BigDecimal calculatedOpenAmount = amountGross.subtract(paymentsAmountTotal);
        //verifiying
        if (calculatedOpenAmount.floatValue() >= 0) {
            //convert the address to a supplier (if isn't yet)
            convertAddressToSupplier(Integer.parseInt(paramDTO.get("supplierId").toString()),
                    Integer.parseInt(paramDTO.get("companyId").toString()),
                    ctx);

            IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
            incomingInvoiceDTO.putAll(paramDTO);
            incomingInvoiceDTO.put("incomingInvoiceId", new Integer(paramDTO.get("incomingInvoiceId").toString()));

            incomingInvoiceDTO.put("openAmount", calculatedOpenAmount);
            if (calculatedOpenAmount.floatValue() == 0) {
                incomingInvoiceDTO.put("paidUntil", getLastPaymentDate(incomingInvoice.getIncomingInvoiceId(),
                        incomingInvoice.getCompanyId(), ctx));
            } else {
                incomingInvoiceDTO.put("paidUntil", null);
            }

            //update IncomingInvoice
            incomingInvoice = (IncomingInvoice) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_UPDATE,
                    incomingInvoiceDTO,
                    resultDTO,
                    true,
                    false,
                    true,
                    true);

            if (!resultDTO.isFailure()) {
                Integer freeTextId = createOrUpdateFreeText(paramDTO.get("notesId"),
                        paramDTO.get("notes"),
                        new Integer(paramDTO.get("companyId").toString()));
                incomingInvoice.setNotesId(freeTextId);

                //update document
                createOrUpdateInvoiceDocument(documentWrapper, incomingInvoice);
            }


            //read
            HashMap details = readDetails(Integer.parseInt(resultDTO.get("supplierId").toString()),
                    resultDTO.get("notesId"),
                    ctx);
            resultDTO.putAll(details);
        } else {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage(new ResultMessage("Finance.incomingInvoice.openAmountError"));
        }
    }

    private void delete() {
        log.debug("deleting IncomingInvoice................");

        //First remove payments (if exists)
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", new Integer(paramDTO.get("incomingInvoiceId").toString()));
        IncomingInvoice incomingInvoice = (IncomingInvoice) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
        if (!resultDTO.isFailure() && incomingInvoice != null) {
            ArrayList<IncomingPayment> paymentsList = new ArrayList<IncomingPayment>(incomingInvoice.getIncomingPayments());
            if (paymentsList.size() > 0) {
                for (int i = 0; i < paymentsList.size(); i++) {
                    try {
                        paymentsList.get(i).remove();
                    } catch (RemoveException e) {
                        log.error("Error removing incomingPayment..........." + e.getMessage());
                    }
                }
            }

            if (paramDTO.get("notesId") != null && paramDTO.get("notesId").toString().length() > 0) {
                deleteNote(Integer.parseInt(paramDTO.get("notesId").toString()));
            }
            //Remove IncomingInvoice....
            ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_DELETE,
                    incomingInvoiceDTO,
                    resultDTO,
                    true,
                    true,
                    true,
                    true);
        }

    }

    private void read(SessionContext ctx) {
        log.debug("reading IncomingInvoice.......................");
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()));
        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
        HashMap details = readDetails(Integer.parseInt(incomingInvoiceDTO.get("supplierId").toString()),
                incomingInvoiceDTO.get("notesId"), ctx);
        resultDTO.putAll(details);
    }

    /**
     * Reads only the IncomingInvoice entity
     */
    private void lightRead() {
        log.debug("lightly reading IncomingInvoice.......................");
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", Integer.parseInt(paramDTO.get("incomingInvoiceId").toString()));
        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
    }

    private void createOrUpdateInvoiceDocument(ArrayByteWrapper documentWrapper, IncomingInvoice incomingInvoice) {
        if (documentWrapper != null && incomingInvoice != null) {

            if (incomingInvoice.getInvoiceDocument() != null) {
                //update the document
                incomingInvoice.getInvoiceDocument().setValue(documentWrapper.getFileData());

            } else {
                //create the document
                FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
                try {
                    FinanceFreeText freeText = financeFreeTextHome.create(documentWrapper.getFileData(), incomingInvoice.getCompanyId(), FreeTextTypes.FREETEXT_INCOMINGINVOICE);
                    //set the freetext id
                    incomingInvoice.setDocumentId(freeText.getFreeTextId());
                } catch (CreateException e) {
                    log.debug("Cannot create document FreeText for a IncomingInvoice........" + e.getMessage());
                }
            }
        }
    }

    private void addPayment(Integer incomingInvoiceId,
                            BigDecimal amount,
                            Integer payDate,
                            Integer incomingInvoiceVersion) {
        //read incomingInvoice
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", incomingInvoiceId);

        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
        if (!resultDTO.isFailure()) { //read OK and version OK
            BigDecimal openAmount = (BigDecimal) resultDTO.get("openAmount");
            openAmount = openAmount.subtract(amount);
            if (openAmount.floatValue() >= 0) {
                incomingInvoiceDTO.putAll(resultDTO);
                incomingInvoiceDTO.put("version", incomingInvoiceVersion);
                incomingInvoiceDTO.put("openAmount", openAmount);
                if (openAmount.floatValue() == 0) {
                    incomingInvoiceDTO.put("paidUntil", payDate);
                }
                ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE,
                        incomingInvoiceDTO,
                        resultDTO,
                        true,
                        true,
                        true,
                        true);
            } else if (openAmount.floatValue() < 0) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage(new ResultMessage("Finance.incomingInvoice.openAmountError"));
            }
        }
    }

    private void updatePayment(Integer incomingInvoiceId, BigDecimal amount, BigDecimal old_amount, Integer payDate,
                               Integer incomingInvoiceVersion) {
        //read incomingInvoice
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", incomingInvoiceId);
        incomingInvoiceDTO.put("version", incomingInvoiceVersion);
        IncomingInvoice incomingInvoice_actual = (IncomingInvoice) ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
        BigDecimal openAmount = incomingInvoice_actual.getOpenAmount();
        openAmount = (openAmount.add(old_amount)).subtract(amount);
        if (openAmount.floatValue() >= 0) {
            incomingInvoiceDTO = new IncomingInvoiceDTO();
            DTOFactory.i.copyToDTO(incomingInvoice_actual, incomingInvoiceDTO);
            incomingInvoiceDTO.put("openAmount", openAmount);
            if (openAmount.floatValue() == 0) {
                incomingInvoiceDTO.put("paidUntil", payDate);
            } else {
                incomingInvoiceDTO.put("paidUntil", null);
            }
            ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE,
                    incomingInvoiceDTO,
                    resultDTO,
                    true,
                    false,
                    true,
                    true);
            if (resultDTO.isFailure()) {
                resultDTO.setForward("FailFromInvoice");
                resultDTO.setResultAsFailure();
            }

        } else if (openAmount.floatValue() < 0) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage(new ResultMessage("Finance.incomingInvoice.openAmountError"));
        }
    }

    private void removePayment(Integer incomingInvoiceId, BigDecimal amount) {
        //read incomingInvoice
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", incomingInvoiceId);

        ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
        if (!resultDTO.isFailure()) { //read OK and version OK
            BigDecimal openAmount = (BigDecimal) resultDTO.get("openAmount");
            openAmount = openAmount.add(amount);
            if (openAmount.floatValue() >= 0) {
                incomingInvoiceDTO.putAll(resultDTO);
                incomingInvoiceDTO.put("openAmount", openAmount);
                if (openAmount.floatValue() > 0) {
                    incomingInvoiceDTO.put("paidUntil", null);
                }
                ExtendedCRUDDirector.i.doCRUD(CRUDDirector.OP_UPDATE,
                        incomingInvoiceDTO,
                        resultDTO,
                        true,
                        true,
                        true,
                        true);
                if (resultDTO.isFailure()) {
                    resultDTO.setForward("FailFromInvoice");
                }
            } else if (openAmount.floatValue() < 0) {
                resultDTO.setResultAsFailure();
                resultDTO.addResultMessage(new ResultMessage("Finance.incomingInvoice.openAmountError"));
            }
        }
    }

    /**
     * Create a note for a IncomingInvoice
     *
     * @param notesText The notesText
     * @return The FinanceFreeTextId
     */
    private Integer createNote(String notesText, Integer companyId) {
        Integer res = null;
        if (notesText != null) {
            FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
            try {
                FinanceFreeText freeText = financeFreeTextHome.create(notesText.getBytes(), companyId, FreeTextTypes.FREETEXT_INCOMINGINVOICE);
                res = freeText.getFreeTextId();
            } catch (CreateException e) {
                log.debug("Cannot create FinanceFreeText for a IncomingInvoice........" + e.getMessage());
            }
        }
        return (res);
    }

    /**
     * Update a note for a IncomingInvoice
     *
     * @param notesId   The notesId
     * @param notesText The text (can be null)
     * @return the freeTextId, or null if this was deleted
     */
    private Integer updateNote(Integer notesId, Object notesText) {
        log.debug("Updating a notes................." + notesText);
        FinanceFreeText financeFreeText = null;
        FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
        Integer res = null;
        try {
            financeFreeText = financeFreeTextHome.findByPrimaryKey(notesId);
        }
        catch (FinderException e) {
            log.debug("error when reading FinanceFreeText for a IncomingInvoice update............ " + notesId + " error: " + e.getMessage());
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
                    log.debug("Error when removing FinanceFreeText for a IncomingInvoice.. " + notesId + " error: " + e.getMessage());
                }
            }
        }
        return (res);
    }

    /**
     * Removes the note of a IncomingInvoice
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
            log.debug("error when reading FinanceFreeText for a IncomingInvoice update............ " + notesId + " error: " + e.getMessage());
        } catch (RemoveException e) {
            log.debug("Error when removing FinanceFreeText for a IncomingInvoice.. " + notesId + " error: " + e.getMessage());
        }
    }

    /**
     * Convert an address to a supplier (if the address is not a supplier yet)
     *
     * @param addressId The addressId
     * @param companyId The companyId
     * @param ctx       The SessionContext
     */
    private void convertAddressToSupplier(Integer addressId, Integer companyId, SessionContext ctx) {
        AddressToSupplierCmd addressToSupplierCmd = new AddressToSupplierCmd();
        addressToSupplierCmd.putParam("addressId", addressId);
        addressToSupplierCmd.putParam("companyId", companyId);
        addressToSupplierCmd.executeInStateless(ctx);
    }

    /**
     * Read an IncomingInvoice
     *
     * @param incomingInvoiceId The incomingInvoiceId
     * @return The incomingInvoice Bean
     */
    private IncomingInvoice readIncomingInvoice(Integer incomingInvoiceId) {
        IncomingInvoiceDTO incomingInvoiceDTO = new IncomingInvoiceDTO();
        incomingInvoiceDTO.put("incomingInvoiceId", incomingInvoiceId);
        IncomingInvoice incomingInvoice = (IncomingInvoice) ExtendedCRUDDirector.i.doCRUD(ExtendedCRUDDirector.OP_READ,
                incomingInvoiceDTO,
                resultDTO);
        return (incomingInvoice);
    }

    /**
     * Calculates the incomingInvoice payments amount
     *
     * @param incomingInvoice The IncomingInvoice
     * @return The sum of its payments
     */
    private BigDecimal calculateIncomingPayments(IncomingInvoice incomingInvoice) {
        BigDecimal paymentsAmountTotal = new BigDecimal(0);
        if (incomingInvoice.getIncomingPayments().size() > 0) {
            for (Iterator iterator = incomingInvoice.getIncomingPayments().iterator(); iterator.hasNext();) {
                IncomingPayment incomingPayment = (IncomingPayment) iterator.next();
                paymentsAmountTotal = paymentsAmountTotal.add(incomingPayment.getAmount());
            }
        }
        return (paymentsAmountTotal);
    }

    /**
     * Create or update a freetext depending of the parameter values
     *
     * @param notesId   The freeTextId
     * @param notes     the freeText content
     * @param companyId The companyId
     * @return The freeTextId
     */
    private Integer createOrUpdateFreeText(Object notesId, Object notes, Integer companyId) {
        log.debug("create o update NOte.............. notesId: " + notesId + " notes: " + notes + " companyId: " + companyId);
        Integer freeTextId = null;
        if (notesId != null && notesId.toString().length() > 0) {
            //update note
            freeTextId = updateNote(Integer.parseInt(notesId.toString()), notes);
        } else if (notes != null) {
            //create note
            freeTextId = createNote(notes.toString(), companyId);
        }
        return (freeTextId);
    }

    /**
     * Get the last paymentDate
     *
     * @param incomingInvoiceId IncomingInvoiceId
     * @param companyId         companyId
     * @param ctx               SessionContext
     * @return The last payment date, null if can't be read
     */
    private Integer getLastPaymentDate(Integer incomingInvoiceId, Integer companyId, SessionContext ctx) {
        IncomingPaymentCmd incomingPaymentCmd = new IncomingPaymentCmd();
        incomingPaymentCmd.putParam("incomingInvoiceId", incomingInvoiceId);
        incomingPaymentCmd.putParam("companyId", companyId);
        incomingPaymentCmd.setOp("getLastPaymentDate");
        incomingPaymentCmd.executeInStateless(ctx);
        log.debug("Last payment date........................... " + incomingPaymentCmd.getResultDTO());
        return ((Integer) incomingPaymentCmd.getResultDTO().get("lastPaymentDate"));
    }

    /**
     * Read supplier and note of a IncomingInvoice
     *
     * @param supplierId supplierId
     * @param notesId    notesId
     * @param ctx        SessionContext
     * @return read data
     */
    private HashMap readDetails(Integer supplierId, Object notesId, SessionContext ctx) {
        log.debug("reading details....................................... supplierId: " + supplierId + " notesId: " + notesId);
        HashMap result = new HashMap();
        //read supplier
        LightlyAddressCmd cmd = new LightlyAddressCmd();
        cmd.putParam("addressId", supplierId);
        cmd.executeInStateless(ctx);

        ResultDTO resultDTOCmd = cmd.getResultDTO();
        if (!resultDTOCmd.isFailure() && !"Fail".equals(resultDTOCmd.getForward())) {
            result.put("supplierName", resultDTOCmd.get("addressName"));
        }

        if (notesId != null) {
            FinanceFreeTextHome financeFreeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
            try {
                FinanceFreeText financeFreeText = financeFreeTextHome.findByPrimaryKey(new Integer(notesId.toString()));
                result.put("notes", new String(financeFreeText.getValue()));
            }
            catch (FinderException e) {
                log.debug("error when reading FinanceFreeText for a IncomingInvoice read............  error: " + e.getMessage());
            }
        }
        log.debug("reading details results: " + result);
        return (result);
    }
}
