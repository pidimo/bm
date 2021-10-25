package com.piramide.elwis.cmd.common;

import com.piramide.elwis.cmd.contactmanager.ContactCreateCmd;
import com.piramide.elwis.cmd.contactmanager.ContactReadCmd;
import com.piramide.elwis.cmd.contactmanager.ContactUpdateCmd;
import com.piramide.elwis.cmd.utils.DocumentTemplateUtil;
import com.piramide.elwis.cmd.utils.GenerateDocument;
import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.catalogmanager.Template;
import com.piramide.elwis.domain.catalogmanager.TemplateHome;
import com.piramide.elwis.domain.catalogmanager.TemplateText;
import com.piramide.elwis.domain.contactmanager.*;
import com.piramide.elwis.domain.salesmanager.*;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.Constants;
import com.piramide.elwis.utils.ContactConstants;
import com.piramide.elwis.utils.SalesConstants;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.EJBContext;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Collection;
import java.util.Iterator;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: GenerateDocumentCmd.java 10391 2013-10-18 21:38:17Z miguel $
 */

public class GenerateDocumentCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog("GenerateDocument");
    boolean isUpdate = false;
    private static final String ELWIS_IDENTIFIER = "[ELWIS-DATA]";


    public void executeInStateless(SessionContext ctx) {
        log.debug("-> Execute GenerateDocumentCmd OK");
        Address currentUser;
        Address company;

        try {
            currentUser = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO.getAsInt(Constants.USER_ADDRESSID)));
            company = (Address) EJBFactory.i.findEJB(new AddressDTO(paramDTO.getAsInt("companyId")));
        } catch (NumberFormatException e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }
        EJBCommand cmd = null;
        if ("create".equals(getOp())) {
            log.debug("[Create]Begin:" + paramDTO);
            // Execute ContactCreateCmd command before generate document
            cmd = new ContactCreateCmd();
        } else if ("update".equals(getOp())) {
            log.debug("[UPDATE]Begin:" + paramDTO);
            isUpdate = true;
            boolean isRead = false;//"1".equals(paramDTO.get("status")); //Por alguna razon se usaba status, esto era lo que provocaba
            //que no se actulice la communication
            if (isRead || "true".equals(paramDTO.get("view"))) {
                cmd = new ContactReadCmd();
                //if(!"true".equals(paramDTO.get("view")))
                if (isRead) {
                    resultDTO.put("update", "readonly");
                }
            } else {
                cmd = new ContactUpdateCmd();
            }
        }
        cmd.putParam(paramDTO);
        try {
            log.debug("Execute Dyna CMD");
            cmd.executeInStateless(ctx);
        } catch (AppLevelException e) {
            resultDTO.setResultAsFailure();
            log.error("Can't execute cmd:" + cmd.getClass());
            return;
        }

        Contact contact = (Contact) cmd.getResultDTO().get("contact");
        log.debug("CONTACT:" + contact);
        if (contact == null) {
            resultDTO.addResultMessage("msg.NotFound", paramDTO.get("note"));
            resultDTO.put("actionForward", "/Communication/List.do");
            resultDTO.setForward("Fail");
            return;
        }
        resultDTO.put("fid", contact.getContactFreeText().getFreeTextId());
        resultDTO.put("contactId", contact.getContactId());

        if (cmd.getResultDTO().isFailure()) {
            log.debug("Fail cmd...");
            resultDTO.setResultAsFailure();

            for (Iterator iterator = cmd.getResultDTO().getResultMessages(); iterator.hasNext();) {
                ResultMessage message = (ResultMessage) iterator.next();
                resultDTO.addResultMessage(message);
            }
            return;
        }
        buildDocument(company, currentUser, contact, ctx);
    }

    private void buildDocument(Address company, Address currentUser, Contact contact, EJBContext ctx) {
        try {
            //Set Contact status to:GENERATED
            contact.setStatus("1");
            documentCreateOrUpdate(company, currentUser, contact, (Integer) paramDTO.get("telecomId"));
            log.debug("[Create]Document is not Null");
        } catch (CreateDocumentException e) {
            log.debug(" ........Cant'n Create document:" + e);
            resultDTO.setResultAsFailure();
            if (e.hasArg()) {
                String arg = e.getArg1();
                resultDTO.addResultMessage(e.getMessage(), arg);
            } else {
                resultDTO.addResultMessage(e.getMessage());
            }
            ctx.setRollbackOnly();

        }
    }

    private void documentCreateOrUpdate(Address company, Address currentUser, Contact contact, Integer telecomId) throws CreateDocumentException {
        log.debug("[Func-CreateDocument]:Init");
        boolean isPERSON = false;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        Address contactPerson = null;
        log.debug("Contact:" + contact + " - Address:" + contact.getAddressId());
        Address address = null;

        try {
            address = addressHome.findByPrimaryKey(contact.getAddressId());
            isPERSON = ContactConstants.ADDRESSTYPE_PERSON.equals(address.getAddressType());
            if (contact.getContactPersonId() != null && !isPERSON) {
                contactPerson = addressHome.findByPrimaryKey(contact.getContactPersonId());
            }
        } catch (Exception e) {
            throw new CreateDocumentException("Document.error.generate.invalidAddress");
        }
        log.debug("FREETEXT     :" + contact.getContactFreeText().getValue());
        CompanyInfoValues companyInfoValues = new CompanyInfoValues();
        companyInfoValues.setCompanyInfoValues(company, contact.getEmployee());
        if (contact.getContactFreeText().getValue() == null) {
            ContactFreeText freeText = contact.getContactFreeText();


            TemplateHome templateHome = (TemplateHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATE);
            Template template = null;
            try {
                template = templateHome.findByPrimaryKey(contact.getTemplateId());
            } catch (FinderException e) {
                log.error("Cannot find template");
                throw new CreateDocumentException("Document.error.find.template");
            }

            log.debug("Verify size of templates");
            if (template.getTemplateText().size() < 1) {
                throw new CreateDocumentException("Document.error.templates", template.getDescription());
            }

            byte[] documentContent = calculateAddressByLanguage(template, address, contactPerson, currentUser, company);

            log.debug("[Func-CreateDocument]:Invoke:calculateValues");

            CalculateDocumentValues documentValues = new CalculateDocumentValues(isPERSON);
            documentValues.setTelecomTypes(companyInfoValues.getTelecomTypes());
            documentValues.setCaseAsSalesProcess(contact.getIsAction());
            documentValues.setMainAdditionalAddress(findAdditionalAddress(contact.getAdditionalAddressId()));

            log.debug("End of Initialize");
            try {
                documentValues.calculateValues(address, contactPerson, company, contact.getEmployee(), currentUser, paramDTO.getAsString("locale"), contact.getDateStartField(), telecomId);
            } catch (FinderException e) {
                log.debug("error calculate values ... ");
            }
            log.debug("After calculate...");
            byte[] finalDoc = null;
            GenerateDocument document = new GenerateDocument();
            document.setAction(contact.getIsAction());
            log.debug("Contact:" + contact.getContactId() + " - " + contact.getProcessId());
            if (contact.getIsAction().booleanValue()) {
                documentValues.initNumberFormat(10, 2);
                ActionHome actionHome = (ActionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTION);
                ActionPK actionPk = new ActionPK();
                actionPk.contactId = contact.getContactId();
                actionPk.processId = contact.getProcessId();
                try {
                    log.debug("Find Action!!!! - ACTIONPK:" + actionPk.contactId + " - " + actionPk.processId);
                    Action action = actionHome.findByPrimaryKey(actionPk);
                    ActionPositionHome actionPositionHome = (ActionPositionHome) EJBFactory.i.getEJBLocalHome(SalesConstants.JNDI_ACTIONPOSITION);
                    log.debug("Second Finder");
                    Collection collection = actionPositionHome.findByProcessAndContactId(contact.getProcessId(), contact.getContactId());
                    Object[][] matrix = null;
                    if (!collection.isEmpty()) {
                        matrix = new Object[collection.size()][VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length];
                    }
                    int j = 0;
                    // Calculate the action positions for generate document
                    for (Iterator iterator = collection.iterator(); iterator.hasNext();) {
                        ActionPosition actionPosition = (ActionPosition) iterator.next();
                        log.debug("Find ActionPosition");
                        documentValues.calculateSaleProcessActionPositionValues(actionPosition, action);
                        System.arraycopy(documentValues.getActionPositionValues(), 0, matrix[j++], 0, VariableConstants.SALESPROCESS_ACTIONPOSITION_FIELDS.length);
                    }

                    boolean doProcess = matrix != null;
                    log.debug("Before Calculate");
                    documentValues.calculateSalesProcessActionValues(action);
                    CreateDocumentException cde = null;
                    // As the Action has actionpositions, then generate as salesprocess document
                    if (doProcess) {
                        try {
                            log.debug("Generate documents");
                            finalDoc = document.renderSalesProcessDocument(documentValues.getValues(companyInfoValues.getCompany_employeeValues(),
                                    companyInfoValues.getCompanyTelecoms(),
                                    true),
                                    companyInfoValues.getFieldNames(true),
                                    matrix, documentContent);
                        } catch (CreateDocumentException ex) {
                            doProcess = false;
                            cde = ex;
                        }
                    }
                    String msg = null;
                    if (cde != null) {
                        msg = cde.getArg1();
                    }

                    // Otherwise genetare as normal document
                    if (!doProcess || "salesprocess".equals(msg)) {
                        try {
                            log.debug("Tratando de generar nuevo doc");
                            documentValues.setEmptySaleProcessValues();
                            finalDoc = document.renderNormalDocument(documentValues.getValues(companyInfoValues.getCompany_employeeValues(),
                                    companyInfoValues.getCompanyTelecoms(),
                                    false),
                                    companyInfoValues.getFieldNames(false),
                                    documentContent);
                        } catch (CreateDocumentException e1) {
                            throw e1;
                        }
                    } else if (cde != null) {
                        throw cde;
                    }


                } catch (FinderException e) {
                    e.printStackTrace();
                }
            } else {
                documentValues.setEmptySaleProcessValues();
                finalDoc = document.renderNormalDocument(documentValues.getValues(companyInfoValues.getCompany_employeeValues(),
                        companyInfoValues.getCompanyTelecoms(),
                        false),
                        companyInfoValues.getFieldNames(false),
                        documentContent);
            }
            freeText.setValue(finalDoc);
            // Because need control by document....
            freeText.setVersion(new Integer(freeText.getVersion().intValue() + 1));
            log.debug("[Func-CreateDocument]:Find template");
        }

        /*else {      // No more update fields support ... :)
    if (!resultDTO.containsValue("readonly")) {

        CalculateDocumentValues documentValues = new CalculateDocumentValues(isPERSON, true);
        documentValues.setTelecomTypes(companyInfoValues.getTelecomTypes());
        documentValues.calculateValues(address, contactPerson, company, contact.getEmployee(), currentUser, paramDTO.getAsString("locale"), contact.getDateStartField(), telecomId);

        String[] fieldNames = companyInfoValues.getFieldNames(false);
        Object[] values = documentValues.getValues(companyInfoValues.getCompany_employeeValues(),
                                                   companyInfoValues.getCompanyTelecoms(),
                                                   false);

        StringBuffer update = new StringBuffer(ELWIS_IDENTIFIER);
        int i = -1;
        for (i = 0; i < fieldNames.length - 1; i++) {
            update.append(fieldNames[i]).append("=").append(values[i]);
            update.append("|");
        }

        if (i > 0) update.append(fieldNames[i]).append("=").append(values[i]);
        if (update.length() > 0)
            resultDTO.put("update", new String(update));
    }
}*/
    }

    private byte[] calculateAddressByLanguage(Template template, Address address, Address contactPerson, Address currentUser, Address company) {

        TemplateText templateText = DocumentTemplateUtil.getTemplateTextByLanguage(template, address, contactPerson, currentUser, company);
        if (templateText != null) {
            return templateText.getFreeText().getValue();
        } else {
            return null;
        }
    }

    private AdditionalAddress findAdditionalAddress(Integer additionalAddressId) {
        AdditionalAddress additionalAddress = null;
        AdditionalAddressHome additionalAddressHome = (AdditionalAddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDITIONALADDRESS);

        if (additionalAddressId != null) {
            try {
                additionalAddress = additionalAddressHome.findByPrimaryKey(additionalAddressId);
            } catch (FinderException e) {
                log.debug("Not found additional address " + additionalAddressId, e);
            }
        }
        return additionalAddress;
    }

    public boolean isStateful() {
        return false;
    }
}
