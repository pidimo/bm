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
import com.piramide.elwis.dto.financemanager.InvoiceDTO;
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
 * Cmd to generate invoice document, save this and update in invoice
 *
 * @author Miky
 * @version $Id: InvoiceDocumentCmd.java 17-sep-2008 14:07:24 $
 */
public class InvoiceDocumentCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing InvoiceDocumentCmd..... " + paramDTO);

        Integer invoiceId = new Integer(paramDTO.get("invoiceId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userId = new Integer(paramDTO.get("userId").toString());
        Integer userAddressId = new Integer(paramDTO.get("userAddressId").toString());
        Integer employeeId = userAddressId; //todo: this should be clarify with HH
        String requestLocale = paramDTO.get("requestLocale").toString();

        InvoiceDTO invoiceDTO = new InvoiceDTO(invoiceId);
        Invoice invoice = (Invoice) ExtendedCRUDDirector.i.read(invoiceDTO, resultDTO, false);
        if (invoice != null && !resultDTO.isFailure()) {

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
                addErrorMessage("Invoice.document.internalUserRequired");
                return;
            }

            //read template
            Integer docLanguageId = getDocumentLanguageId(address, contactPerson, companyAddress, employeeAddress);
            InvoiceText invoiceText = readInvoiceText(invoice, docLanguageId);
            if (invoiceText == null) {
                addErrorMessage("Invoice.document.default.error");
                return;
            }
            byte[] template = invoiceText.getFinanceFreeText().getValue();

            //read values
            Object[] invoiceValues = null;
            ReadTemplateValues readTemplateValues = null;
            Integer readLanguageId = (docLanguageId != null ? docLanguageId : invoiceText.getLanguageId());
            try {
                readTemplateValues = new ReadTemplateValues(companyId, userAddressId, employeeId, false, false, requestLocale, false);
                invoiceValues = readTemplateValues.getInvoiceFieldValues(addressId, contactPersonId, readLanguageId, null, invoice);
            } catch (FinderException e) {
                log.debug("Error in read values for template...", e);
                addErrorMessage("Invoice.document.generate.error");
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

                log.info("Start word document generation..." + invoiceId);

                wordDoc = generateDocument.renderInvoiceDocument(invoiceValues, fieldNames, positionValues, invoiceVatValues, template);

                log.info("End word document generation..." + invoiceId);
            } catch (CreateDocumentException e) {
                log.debug("Error in generate docyment:" + e);
                addErrorMessage(e);
                return;
            }

            //save pdf document
            byte[] pdfDoc = new byte[0];
            try {

                log.info("Start convert word to pdf with open office..." + invoiceId);

                OpenOfficeConverterUtil converterUtil = new OpenOfficeConverterUtil();
                pdfDoc = converterUtil.wordToPdf(wordDoc);
                //add title metadata
                pdfDoc = PdfDocumentUtil.addMetaData(pdfDoc, invoice.getNumber());

                log.info("End convert word to pdf with open office..." + invoiceId);
            } catch (ConnectException e) {
                log.debug("Can't connect to open office..", e);
                addErrorMessage("Document.connect.error");
                return;
            } catch (DocumentConvertException e) {
                log.debug("Can't convert word to pdf...", e);
                addErrorMessage("Document.pdfConvert.error");
                return;
            } catch (PdfMetadataDocumentException e) {
                log.error("Can't add metadata in pdf doc...", e);
                addErrorMessage("Invoice.document.generate.error");
                return;
            }

            FinanceFreeTextHome financeFreeTextHome =
                    (FinanceFreeTextHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_FREETEXT);
            try {
                if (invoice.getDocumentId() != null) {
                    FinanceFreeText documentText = financeFreeTextHome.findByPrimaryKey(invoice.getDocumentId());
                    documentText.setValue(pdfDoc);
                } else {
                    FinanceFreeText documentText = financeFreeTextHome.create(pdfDoc, companyId, FreeTextTypes.FREETEXT_INVOICE);
                    invoice.setDocumentId(documentText.getFreeTextId());
                }
            } catch (CreateException e) {
                log.debug("Error creating the document text.." + e);
                addErrorMessage("Invoice.document.generate.error");
                return;
            } catch (FinderException e) {
                log.debug("Error creating the document text.." + e);
                addErrorMessage("Invoice.document.generate.error");
                return;
            }


            resultDTO.put("documentId", invoice.getDocumentId());
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
        return defaultLang;
    }

    private InvoiceText readInvoiceText(Invoice invoice, Integer docLanguageId) {
        //read template
        InvoiceTemplateHome templateHome = (InvoiceTemplateHome) EJBFactory.i.getEJBLocalHome(FinanceConstants.JNDI_INVOICETEMPLATE);
        InvoiceText invoiceText = null;

        try {
            InvoiceTemplate invoiceTemplate = templateHome.findByPrimaryKey(invoice.getTemplateId());
            for (Iterator iterator = invoiceTemplate.getInvoiceTexts().iterator(); iterator.hasNext();) {
                InvoiceText iText = (InvoiceText) iterator.next();
                if (iText.getLanguageId().equals(docLanguageId)) {
                    invoiceText = iText;
                    break;
                } else if (iText.getIsDefault()) {
                    invoiceText = iText;
                }
            }
        } catch (FinderException e) {
            log.debug("Not found invoice template..." + e);
        }
        return invoiceText;
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
