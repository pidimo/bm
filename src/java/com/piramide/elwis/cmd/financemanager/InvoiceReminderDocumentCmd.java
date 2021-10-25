package com.piramide.elwis.cmd.financemanager;

import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.InvoiceDocumentValues;
import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.cmd.utils.DocumentConvertException;
import com.piramide.elwis.cmd.utils.GenerateDocument;
import com.piramide.elwis.cmd.utils.OpenOfficeConverterUtil;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.financemanager.*;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.financemanager.InvoiceReminderDTO;
import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.FinanceConstants;
import com.piramide.elwis.utils.FreeTextTypes;
import com.piramide.elwis.utils.pdfdocument.PdfDocumentUtil;
import com.piramide.elwis.utils.pdfdocument.PdfMetadataDocumentException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.net.ConnectException;
import java.util.Iterator;

/**
 * Jatun S.R.L.
 * cmd to generate and save invoice reminder document
 *
 * @author Miky
 * @version $Id: InvoiceReminderDocumentCmd.java 18-sep-2008 18:49:47 $
 */
public class InvoiceReminderDocumentCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing InvoiceReminderDocumentCmd..... " + paramDTO);

        Integer reminderId = new Integer(paramDTO.get("reminderId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer userAddressId = new Integer(paramDTO.get("userAddressId").toString());
        Integer employeeId = userAddressId; //todo: this should be clarify with HH
        String requestLocale = paramDTO.get("requestLocale").toString();
        Object labelReminder = paramDTO.get("reminderLabel");

        InvoiceReminderDTO reminderDTO = new InvoiceReminderDTO(reminderId);
        InvoiceReminder reminder = (InvoiceReminder) ExtendedCRUDDirector.i.read(reminderDTO, resultDTO, false);

        if (reminder != null && !resultDTO.isFailure()) {
            Invoice invoice = reminder.getInvoice();

            Integer addressId = realInvoiceAddressId(invoice);
            Integer contactPersonId = realInvoiceContactPersonId(invoice);

            Address address = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(addressId), new ResultDTO(), false);
            Address contactPerson = null;
            if (contactPersonId != null) {
                contactPerson = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(contactPersonId), new ResultDTO(), false);
            }

            Address companyAddress = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(companyId), new ResultDTO(), false);
            Address userAddress = (Address) ExtendedCRUDDirector.i.read(new AddressDTO(userAddressId), new ResultDTO(), false);
            Address employeeAddress = userAddress;

            //verifiy if current user is internal and employee, this is required to generate document
            if (!isInternalUser(userId)) {
                addErrorMessage("InvoiceReminder.document.internalUserRequired");
                return;
            }

            //read template
            Integer docLanguageId = getDocumentLanguageId(address, contactPerson, companyAddress, employeeAddress);
            ReminderText reminderText = readInvoiceReminderText(reminder, docLanguageId);
            if (reminderText == null) {
                addErrorMessage("InvoiceReminder.document.default.error");
                return;
            }
            byte[] template = reminderText.getFinanceFreeText().getValue();

            //read values
            Object[] reminderValues = null;
            ReadTemplateValues readTemplateValues = null;
            Integer readLanguageId = (docLanguageId != null ? docLanguageId : reminderText.getLanguageId());
            try {
                readTemplateValues = new ReadTemplateValues(companyId, userAddressId, employeeId, false, false, requestLocale, false);
                reminderValues = readTemplateValues.getInvoiceReminderFieldValues(addressId, contactPersonId, readLanguageId, null, reminder);
            } catch (FinderException e) {
                log.debug("Error in read values for template...", e);
                addErrorMessage("InvoiceReminder.document.generate.error");
                return;
            }
            String[] fieldNames = readTemplateValues.getInvoiceFieldNames();

            //generate document
            Integer defaultLanguageId = InvoiceDocumentValues.getDefaultLanguageId(address, contactPerson, companyAddress, userAddress);
            if (defaultLanguageId == null) {
                defaultLanguageId = readLanguageId;
            }
            String defaultIsoLang = InvoiceDocumentValues.getDefaultIsoLang(address, contactPerson, companyAddress, userAddress);
            if (defaultIsoLang == null) {
                defaultIsoLang = requestLocale;
            }

            InvoiceDocumentValues invoiceDocValues = new InvoiceDocumentValues(defaultIsoLang);

            Object[][] positionValues = invoiceDocValues.getInvoicePositionValues(invoice, defaultLanguageId);
            Object[][] invoiceVatValues = invoiceDocValues.getInvoiceVatValues(invoice);

            GenerateDocument generateDocument = new GenerateDocument();
            byte[] wordDoc = new byte[0];
            try {
                wordDoc = generateDocument.renderInvoiceDocument(reminderValues, fieldNames, positionValues, invoiceVatValues, template);
            } catch (CreateDocumentException e) {
                log.debug("Error in generate document:" + e);
                addErrorMessage(e);
                return;
            }

            //save pdf document
            byte[] pdfDoc = new byte[0];
            try {
                OpenOfficeConverterUtil converterUtil = new OpenOfficeConverterUtil();
                pdfDoc = converterUtil.wordToPdf(wordDoc);

                //add title metadata
                String docTitle = ((labelReminder != null) ? labelReminder + ": " : "") + invoice.getNumber();
                pdfDoc = PdfDocumentUtil.addMetaData(pdfDoc, docTitle);
            } catch (ConnectException e) {
                log.debug("Can't connect to open office..", e);
                addErrorMessage("Document.connect.error");
                return;
            } catch (DocumentConvertException e) {
                log.debug("Can't convert word to pdf...", e);
                addErrorMessage("Document.pdfConvert.error");
                return;
            } catch (PdfMetadataDocumentException e) {
                log.error("Can't add metadata in reminder pdf doc...", e);
                addErrorMessage("InvoiceReminder.document.generate.error");
                return;
            }

            FinanceFreeTextHome freeTextHome = (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
            try {
                if (reminder.getDocumentId() != null) {
                    FinanceFreeText documentText = freeTextHome.findByPrimaryKey(reminder.getDocumentId());
                    documentText.setValue(pdfDoc);
                } else {
                    FinanceFreeText documentText = freeTextHome.create(pdfDoc, companyId, FreeTextTypes.FREETEXT_INVOICEREMINDER);
                    reminder.setDocumentId(documentText.getFreeTextId());
                }
            } catch (CreateException e) {
                log.debug("Error creating the document text.." + e);
                addErrorMessage("InvoiceReminder.document.generate.error");
                return;
            } catch (FinderException e) {
                log.debug("Error in find the document text.." + e);
                addErrorMessage("InvoiceReminder.document.generate.error");
                return;
            }

            resultDTO.put("documentId", reminder.getDocumentId());
        }
    }

    private Integer realInvoiceAddressId(Invoice invoice) {
        return isWithSentAddress(invoice) ? invoice.getSentAddressId() : invoice.getAddressId();
    }

    private Integer realInvoiceContactPersonId(Invoice invoice) {
        return isWithSentAddress(invoice) ? invoice.getSentContactPersonId() : invoice.getContactPersonId();
    }

    private boolean isWithSentAddress(Invoice invoice) {
        return invoice.getSentAddressId() != null;
    }

    private Integer getDocumentLanguageId(Address address, Address contactPerson, Address company, Address employee) {
        Integer defaultLang = null;
        log.debug("getDefaultLang:");
        if (contactPerson != null && contactPerson.getLanguageId() != null) {
            defaultLang = contactPerson.getLanguageId();
        } else if (address.getLanguageId() != null) {
            defaultLang = address.getLanguageId();
        } else if (company.getLanguageId() != null) {
            defaultLang = company.getLanguageId();
        } else if (employee.getLanguageId() != null) {
            defaultLang = employee.getLanguageId();
        }

        log.debug("language by default ..." + defaultLang);
        return defaultLang;
    }

    private ReminderText readInvoiceReminderText(InvoiceReminder reminder, Integer docLanguageId) {

        ReminderText reminderText = null;
        ReminderLevel reminderLevel = reminder.getReminderLevel();
        for (Iterator iterator = reminderLevel.getReminderTexts().iterator(); iterator.hasNext();) {
            ReminderText rText = (ReminderText) iterator.next();
            if (rText.getLanguageId().equals(docLanguageId)) {
                reminderText = rText;
                break;
            } else if (rText.getIsDefault()) {
                log.debug("Find default reminder template:" + rText);
                reminderText = rText;
            }
        }
        return reminderText;
    }

    private void addErrorMessage(CreateDocumentException e) {
        resultDTO.setResultAsFailure();
        if (e.hasArg()) {
            String arg = e.getArg1();
            resultDTO.addResultMessage(e.getMessage(), arg);
        } else {
            resultDTO.addResultMessage(e.getMessage());
        }
    }

    private void addErrorMessage(String resourceKey) {
        resultDTO.addResultMessage(resourceKey);
        resultDTO.setResultAsFailure();
    }

    private boolean isInternalUser(Integer userId) {
        boolean isInternal = false;
        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User user = userHome.findByPrimaryKey(userId);
            isInternal = AdminConstants.INTERNAL_USER.equals(user.getType().toString());
        } catch (FinderException e) {
            log.debug("Not found user ..." + userId);
            return false;
        }
        return isInternal;
    }

    public boolean isStateful() {
        return false;
    }
}
