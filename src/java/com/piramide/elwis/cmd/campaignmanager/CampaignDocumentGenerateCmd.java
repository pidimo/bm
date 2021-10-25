package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.common.CalculateDocumentValues;
import com.piramide.elwis.cmd.common.CompanyInfoValues;
import com.piramide.elwis.cmd.common.EJBCommandUtil;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.utils.CampaignResultPageList;
import com.piramide.elwis.cmd.utils.CampaignTemplateManager;
import com.piramide.elwis.cmd.utils.VariableConstants;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.dto.admin.UserDTO;
import com.piramide.elwis.dto.campaignmanager.AttachDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignActivityDTO;
import com.piramide.elwis.dto.campaignmanager.CampaignDTO;
import com.piramide.elwis.dto.contactmanager.AddressDTO;
import com.piramide.elwis.dto.contactmanager.EmployeeDTO;
import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.service.campaign.DocumentGenerateService;
import com.piramide.elwis.service.campaign.DocumentGenerateServiceHome;
import com.piramide.elwis.utils.*;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.EJBFactoryException;
import net.java.dev.strutsejb.dto.ResultDTO;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * AlfaCentauro Team
 *
 * @author Tayes
 * @version $Id: CampaignDocumentGenerateCmd.java 9703 2009-09-12 15:46:08Z fernando $
 */

public class CampaignDocumentGenerateCmd extends EJBCommand {
    private final static Log log = LogFactory.getLog("GenerateDocument");

    private static final int ADDRESS = 0;
    private static final int CONTACT_PERSON = 1;
    public static Map stop;

    private String session;
    private boolean isStop;

    List values;
    List idValues; //to create communications
    int row = 0;
    private ArrayByteWrapper wordDoc;

    public CampaignDocumentGenerateCmd() {
        isStop = false;
    }

    public void executeInStateless(SessionContext ctx) {
        Integer campaignId;
        Integer userAddressId;
        Integer companyId;
        Integer languageId;
        Integer templateId;
        String language;
        Integer telecomTypeId = null;
        Integer activityId;
        int page;
        String responsibleType;

        try {
            companyId = (Integer) paramDTO.get("companyId");
            userAddressId = (Integer) paramDTO.get(Constants.USER_ADDRESSID);
            language = paramDTO.getAsString("languageName");
            campaignId = new Integer(paramDTO.getAsInt("campaignId"));
            languageId = new Integer(paramDTO.getAsInt("languageId"));
            templateId = new Integer(paramDTO.getAsInt("templateId"));
            activityId = new Integer(paramDTO.getAsInt("activityId"));
            if (paramDTO.containsKey("telecomTypeId")) {
                telecomTypeId = new Integer(paramDTO.getAsInt("telecomTypeId"));
            }
            page = paramDTO.getAsInt("page");
            responsibleType = paramDTO.getAsString("responsibleType");

        } catch (NumberFormatException e) {
            e.printStackTrace();
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }
        session = paramDTO.getAsString("session");
        log.debug("Find Entities...");
        Campaign campaign = null;
        Address company = null;
        Address user = null;
        Employee employee = null;
        CampaignActivity activity = null;
        User activityUser = null;
        Employee sendEmployee = null;
        try {
            campaign = (Campaign) EJBFactory.i.findEJB(new CampaignDTO(campaignId));
            company = (Address) EJBFactory.i.findEJB(new AddressDTO(companyId));
            user = (Address) EJBFactory.i.findEJB(new AddressDTO(userAddressId));
            activity = (CampaignActivity) EJBFactory.i.findEJB(new CampaignActivityDTO(activityId));
            activityUser = (User) EJBFactory.i.findEJB(new UserDTO(activity.getUserId()));
            sendEmployee = (Employee) EJBFactory.i.findEJB(new EmployeeDTO(activityUser.getAddressId())); //activity user as employee
            employee = sendEmployee;
        } catch (EJBFactoryException e) {
            resultDTO.setResultAsFailure();
            //resultDTO.addResultMessage("Common.invalid.id");
            log.debug("Fail... Entity...\n:" + e);
            return;
        }

        //if sender employee is current user
        if (CampaignConstants.CURRENT_USER_SEND.equals(responsibleType)) {
            log.debug("Sender employee is current user.....");
            try {
                Employee userAsEmployee = (Employee) EJBFactory.i.findEJB(new EmployeeDTO(userAddressId));
                sendEmployee = userAsEmployee;
                employee = userAsEmployee;
            } catch (EJBFactoryException e) {
                log.debug("Current user not is internal user...");
            }
        }

        log.debug("After load entities....");
        boolean isEmail = paramDTO.getAsBool("email");
        // If isValidTemplate
        CampaignTextHome campaignTextHome = (CampaignTextHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        CampaignFreeText freeText = null;
        DocumentGenerateService documentGenerateService = null;
        CompanyInfoValues companyInfoValues = new CompanyInfoValues();
        if (isEmail) {
            companyInfoValues.setEndLineAsHTMLDocument(); //this it must be first ,is required to html documents only;
        }
        companyInfoValues.setCompanyInfoValues(company, employee);

        String[] fieldNames = companyInfoValues.getFieldNames(false);
        Object[] companyTelecoms = companyInfoValues.getCompanyTelecoms();
        try {
            log.debug("Validating template...");
            freeText = campaignTextHome.findByPrimaryKey(new CampaignTextPK(languageId, templateId)).getCampaignFreeText();
            DocumentGenerateServiceHome documentGenerateServiceHome = (DocumentGenerateServiceHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_DOCUMENT_GENERATESERVICE);
            documentGenerateService = documentGenerateServiceHome.create();

            if (!isEmail) {
                List valuesTestList = new ArrayList();
                String[] valuesTest = new String[fieldNames.length];
                for (int i = 0; i < fieldNames.length; i++) {
                    valuesTest[i] = "";
                }
                valuesTestList.add(valuesTest);
                documentGenerateService.renderDocument(valuesTestList, fieldNames, freeText.getValue());
            }
        } catch (FinderException e) {
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Template.error.find");
            //e.printStackTrace();
        } catch (CreateDocumentException e) {
            log.debug("CreateDocumentException:" + e);
            resultDTO.setResultAsFailure();
            if (e.hasArg()) {
                resultDTO.addResultMessage(e.getMessage(), e.getArg1());
            } else {
                resultDTO.addResultMessage(e.getMessage());
            }
            //e.printStackTrace();
        } catch (CreateException e) {
            //e.printStackTrace();
            resultDTO.setResultAsFailure();
        }

        if (resultDTO.isFailure()) {
            log.debug("Template is invalid or another thing...");
            return;
        }

        CampaignResultPageList resultPageList = new CampaignResultPageList(templateId, campaignId, companyId);
        List recipients = resultPageList.getResultPage(languageId, page);
        log.debug("Recipients:" + recipients + "-Size" + recipients.size());

        values = new ArrayList();
        idValues = new ArrayList();
        List emails = new ArrayList();
        List noHasEmail = new ArrayList();
        int failedContactPerson = 0;
        int failedAddress = 0;
        int rowProcess = 0;
        for (Iterator iterator = recipients.iterator(); iterator.hasNext();) {

            // Verify if was Cancelled generation of documents....
            if (((Boolean) stop.get(session)).booleanValue()) {
                isStop = true;
                stop.remove(session);
                log.debug("Generation of Documents.... was cancelled!!!");
                break;
            }
            Integer[] ids = (Integer[]) iterator.next();
            rowProcess++;
            log.debug("ID Results:Address=" + ids[ADDRESS] + " ContactPerson=" + ids[CONTACT_PERSON] + "- ROW:" + rowProcess);
            Address contactPerson = null;
            Address organization = null;
            boolean isPERSON = false;
            try {
                organization = (Address) EJBFactory.i.findEJB(new AddressDTO(ids[ADDRESS]));
                isPERSON = ContactConstants.ADDRESSTYPE_PERSON.equals(organization.getAddressType());
            } catch (EJBFactoryException e) {
                log.debug(" organization not searched .. ");
                failedAddress++;
                continue;
            }
            if (!isPERSON) {
                try {
                    if (ids[CONTACT_PERSON] != null) {
                        contactPerson = (Address) EJBFactory.i.findEJB(new AddressDTO(ids[CONTACT_PERSON]));
                    }
                } catch (EJBFactoryException e) {
                    failedContactPerson++;
                    continue;
                }
            }

            //calculate values to contact responsible
            if (CampaignConstants.CONTACT_RESPONSIBLE.equals(responsibleType)) {
                Employee contactEmployee = getActivityContactResponsible(activityId, ids[ADDRESS], ids[CONTACT_PERSON]);
                if (contactEmployee != null) {
                    employee = contactEmployee;
                } else {
                    employee = sendEmployee;
                }

                //set new values
                companyInfoValues = new CompanyInfoValues();
                if (isEmail) {
                    companyInfoValues.setEndLineAsHTMLDocument(); //this it must be first ,is required to html documents only;
                }
                companyInfoValues.setCompanyInfoValues(company, employee);
                companyTelecoms = companyInfoValues.getCompanyTelecoms();
            }

            log.debug("Invoke calculate....:" + contactPerson + " - " + organization);
            CalculateDocumentValues documentValues = new CalculateDocumentValues(isPERSON);
            if (isEmail) {
                documentValues.setEndLineAsHTMLDocument(); //this it must be first,is required to html documents only;
            }
            documentValues.setIsoLang(languageId.toString()); /// add BY ME. For salutation:set ISO value of the generate language.
            documentValues.setIsCampaign("true");
            documentValues.setTelecomTypes(companyInfoValues.getTelecomTypes());
            documentValues.setEmail(isEmail);
            try {
                //documentValues.calculateValues(organization, contactPerson, company, employee, user, paramDTO.getAsString("locale"), campaign.getStartDateField(), telecomTypeId); //todo: mikyFix, preguntar a alex
                documentValues.calculateValues(organization, contactPerson, company, employee, user, paramDTO.getAsString("locale"), new Date(), telecomTypeId);
            } catch (FinderException e) {
                log.debug(" ... ERROR not document calculate values....");
            }

            log.debug("IS EMAIL:" + isEmail);

            Object[] addressValues = documentValues.getValues(companyInfoValues.getCompany_employeeValues(), companyTelecoms, false);

            if (isEmail) {
                if (documentValues.getEmail() != null && documentValues.getEmail().trim().length() > 3) {
                    values.add(addressValues);
                    idValues.add(getIdsOfSendData(organization, contactPerson, employee));
                    emails.add(documentValues.getEmail());
                } else {
                    if (addressValues[VariableConstants.FIELD_PERSON_NAME] != null) {
                        noHasEmail.add(addressValues[VariableConstants.FIELD_PERSON_NAME]);
                    } else {
                        noHasEmail.add(addressValues[VariableConstants.FIELD_ADDRESS_NAME]);
                    }
                }
            } else {
                values.add(addressValues);
                idValues.add(getIdsOfSendData(organization, contactPerson, employee));
            }
        }
        //log.debug("after for...");
        log.debug("FAILED-Address:" + failedAddress + "- FAILED-ContactPErson:" + failedContactPerson + " - ROW-PROCESS:" + rowProcess);
        log.debug("noHasEmail:" + noHasEmail);
        log.debug("Genetate Process");
        log.debug("Values:" + values.size() + " - Emails:" + emails.size());

        try {
            if (isEmail) {
                CampaignTemplateManager templateManager = new CampaignTemplateManager(isEmail);
                if (!CampaignResultPageList.existCampaignTemplateFolder(templateId, campaignId)) {
                    new File(CampaignResultPageList.pathCampaignTemplateFolder(templateId, campaignId)).mkdir();
                }


                if (!CampaignResultPageList.existCampaignTemplateCacheFile(templateId, campaignId, languageId)) {
                    String template = CampaignResultPageList.pathCampaignTemplateHtml(templateId, campaignId, languageId);
                    log.debug("Creating cache template:" + template);

                    FileOutputStream stream = new FileOutputStream(template);
                    stream.write(freeText.getValue());
                    stream.close();
                    //new GenerateDocument().convertDocument(template, CampaignResultPageList.pathCampaignTemplateHtml(templateId, campaignId, languageId));
                    //new File(template).delete();
                }
                /*new GenerateDocument().convertDocument(freeText.getValue(),
                        CampaignResultPageList.pathCampaignTemplateHtml(campaignId, languageId));*/
                log.debug("Send emails");
                List fields = templateManager.loadTemplateCache(templateId, campaignId, languageId, fieldNames);
                Map images = templateManager.loadImageCache(templateId, campaignId);
                log.debug("Fields:" + fields.size());
                log.debug("MAils:" + emails);
                if (fields.size() > 0) {

                    /* Logica para attachs  */

                    String listIdAttach = (String) paramDTO.get("listIdAttach");
                    List attachs = new ArrayList();
                    String pathCampaign = CampaignResultPageList.pathCampaignFolder(templateId, campaignId, true);

                    if (listIdAttach != null && listIdAttach.trim().length() > 0) {
                        String[] attachIDs = listIdAttach.split(",");


                        for (int i = 0; i < attachIDs.length; i++) {
                            String attachID = attachIDs[i].trim();
                            Attach attach = (Attach) ExtendedCRUDDirector.i.read(new AttachDTO(new Integer(attachID)), new ResultDTO(), false);
                            if (attach != null) {
                                String pathToFile = pathCampaign + attach.getFilename();
                                FileOutputStream stream = new FileOutputStream(pathToFile);
                                stream.write(attach.getCampaignFreeText().getValue());
                                stream.close();
                                attachs.add(pathToFile);
                            }
                        }
                    }

                    String subject = paramDTO.getAsString("subject");
                    Integer userId = EJBCommandUtil.i.getValueAsInteger(this, "userId");
                    boolean success = documentGenerateService.renderDocumentWithMail(paramDTO.getAsString("from"), images, fields,
                            values, fieldNames, emails, subject, attachs, userId);
                    log.debug("RenderdocumentStatus:" + success);
                    resultDTO.put("success", Boolean.toString(success));
                    resultDTO.put("noHasEmail", noHasEmail);
                }


            } else {
                wordDoc = new ArrayByteWrapper(documentGenerateService.renderDocument(values, fieldNames, freeText.getValue()));
            }
        } catch (CreateDocumentException e) {
            log.debug("Se tiro sin razon alguna....");
        } catch (Exception e) {
            if (e instanceof com.sun.star.connection.NoConnectException) {
                resultDTO.addResultMessage("Campaign.error.openOfficeServer");
                resultDTO.put("NoConnectException", null);
            } else {
                resultDTO.addResultMessage("Campaign.error.emilGenerate");
            }
            resultDTO.setResultAsFailure();
            log.error("Can't generate template or send email", e);
            return;
        }
        //resultDTO.put("", values);

        if (failedAddress > 0) {
            resultDTO.put("failedAddress", new Integer(failedAddress));
        }

        if (failedContactPerson > 0) {
            resultDTO.put("failedContactPerson", new Integer(failedContactPerson));
        }

    }

    /**
     * get responsible of an activity contact as Employee
     *
     * @param activityId
     * @param addressId
     * @param contactPersonId
     * @return Employee
     */
    private Employee getActivityContactResponsible(Integer activityId, Integer addressId, Integer contactPersonId) {
        Employee contactEmployee = null;
        CampaignContactHome campaignContactHome = (CampaignContactHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGNCONTACT);
        CampaignContact activityContact = null;
        try {
            if (contactPersonId != null) {
                activityContact = campaignContactHome.findByActivityIdAddressIdContactPersonId(activityId, addressId, contactPersonId);
            } else {
                activityContact = campaignContactHome.findByActivityIdAddressIdContactPersonNULL(activityId, addressId);
            }
        } catch (FinderException e) {
            log.debug("Fail find activity contact......." + e);
        }

        if (activityContact != null) {
            User contactUser = null;
            try {
                if (activityContact.getUserId() != null) {
                    contactUser = (User) EJBFactory.i.findEJB(new UserDTO(activityContact.getUserId()));
                    contactEmployee = (Employee) EJBFactory.i.findEJB(new EmployeeDTO(contactUser.getAddressId()));
                }
            } catch (EJBFactoryException e) {
                log.debug("Fail find contact employee......." + e);
            }
        }
        return contactEmployee;
    }


    public boolean isStateful() {
        return false;
    }

    public ArrayByteWrapper getWordDoc() {
        return wordDoc;
    }

    /**
     * save ids of send data (address, contact person , employee) to create comunications
     *
     * @param address
     * @param contactPerson
     * @param employee
     * @return Map
     */
    private Map getIdsOfSendData(Address address, Address contactPerson, Employee employee) {
        Map idsMap = new HashMap();
        idsMap.put("idAddress", address.getAddressId());
        idsMap.put("idContactPerson", contactPerson != null ? contactPerson.getAddressId() : null);
        idsMap.put("idEmployee", employee.getEmployeeId());

        return idsMap;
    }

    /**
     * create communications to recipients of document generation
     *
     * @param documentGenerateService
     * @param fieldNames
     * @param freeText
     * @param activity
     * @param ctx
     */
    private void createCommunicationsToDocumentGenerate(DocumentGenerateService documentGenerateService, String[] fieldNames, CampaignFreeText freeText, CampaignActivity activity, SessionContext ctx) {
        log.debug("Creating communication................");

        for (int i = 0; i < values.size(); i++) {
            Object[] addressValues = (Object[]) values.get(i);
            Map ids = (Map) idValues.get(i);

            List valueList = new ArrayList();
            valueList.add(addressValues);

            ArrayByteWrapper doc = new ArrayByteWrapper();
            try {
                doc = new ArrayByteWrapper(documentGenerateService.renderDocument(valueList, fieldNames, freeText.getValue()));
            } catch (CreateDocumentException e) {
                log.debug("Se tiro sin razon alguna....");
            }

            ////////revise exception management
            ////if is canceled in generation

            DocumentCommunicationMassiveCmd cmd = new DocumentCommunicationMassiveCmd();

            cmd.putParam("freeText", doc); //todo:revise in creation
            cmd.putParam("addressId", ids.get("idAddress"));
            cmd.putParam("dateStart", DateUtils.dateToInteger(new Date()));
            cmd.putParam("companyId", activity.getCompanyId());
            cmd.putParam("contactPersonId", ids.get("idContactPerson"));
            cmd.putParam("employeeId", ids.get("idEmployee"));
            cmd.putParam("note", activity.getTitle()); //subject
            cmd.putParam("status", "0");
            cmd.putParam("type", CommunicationTypes.LETTER);
            cmd.putParam("inOut", 0);
            cmd.putParam("isAction", new Boolean(false));

            //cmd.putParam("dateFinish", paramDTO.get("dateFinish"));
            //cmd.putParam("freeTextId", paramDTO.get("freeTextId"));
            //cmd.putParam("processId", paramDTO.get("processId"));
            //cmd.putParam("templateId", paramDTO.get("templateId"));
            //cmd.putParam("contactNumber", paramDTO.get("contactNumber"));
            //cmd.putParam("probability", paramDTO.get("probability"));

            log.debug("DTOOOOOOOOOOOO:::::" + cmd.getParamDTO());
///            cmd.executeInStateless(ctx);

        }
    }
}
