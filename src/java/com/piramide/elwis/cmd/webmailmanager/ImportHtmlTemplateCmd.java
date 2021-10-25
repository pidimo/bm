package com.piramide.elwis.cmd.webmailmanager;

import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateProcessor;
import com.piramide.elwis.cmd.common.ExtendedCRUDDirector;
import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.cmd.utils.DocumentTemplateUtil;
import com.piramide.elwis.domain.catalogmanager.*;
import com.piramide.elwis.domain.contactmanager.Address;
import com.piramide.elwis.domain.contactmanager.AddressHome;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.domain.contactmanager.EmployeeHome;
import com.piramide.elwis.dto.catalogmanager.TemplateDTO;
import com.piramide.elwis.utils.CatalogConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.Map;

/**
 * Cmd to import HTML template
 *
 * @author Miky
 * @version $Id: ImportHtmlTemplateCmd.java 2009-05-18 04:38:55 PM $
 */
public class ImportHtmlTemplateCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public boolean isStateful() {
        return false;
    }

    public void executeInStateless(SessionContext ctx) {
        log.debug("Executing ImportHtmlTemplateCmd..... " + paramDTO);

        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        Template template = (Template) ExtendedCRUDDirector.i.read(new TemplateDTO(templateId), resultDTO, false);
        if (resultDTO.isFailure()) {
            return;
        }

        if ("onlyTemplate".equals(getOp())) {
            importOnlyTemplate(template);
        } else {
            importTemplateWithContact(template);
        }
    }

    private void importTemplateWithContact(Template template) {
        log.debug("--- import template with contact ---");

        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userAddressId = new Integer(paramDTO.get("userAddressId").toString());
        Integer addressId = new Integer(paramDTO.get("addressId").toString());
        Integer contactPersonId = null;
        if (paramDTO.get("contactPersonId") != null) {
            contactPersonId = new Integer(paramDTO.get("contactPersonId").toString());
        }

        Integer employeeId = null;
        if (paramDTO.get("employeeId") != null) {
            employeeId = new Integer(paramDTO.get("employeeId").toString());
        }

        Address companyAddress = readAddress(companyId);
        Address currentUser = readAddress(userAddressId);
        Address address = readAddress(addressId);
        Address contactPersonAddress = (contactPersonId != null) ? readAddress(contactPersonId) : null;

        //get user as employee
        if (employeeId == null) {
            Employee employee = readEmployee(userAddressId);
            employeeId = (employee != null) ? employee.getEmployeeId() : null;
        }

        TemplateText templateText = DocumentTemplateUtil.getTemplateTextByLanguage(template, address, contactPersonAddress, currentUser, companyAddress);
        if (templateText != null) {
            processTemplate(companyId, templateText, addressId, contactPersonId, employeeId, userAddressId);
        } else {
            resultDTO.setResultAsFailure();
        }
    }

    private void importOnlyTemplate(Template template) {
        log.debug("--- import only template ---");

        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userAddressId = new Integer(paramDTO.get("userAddressId").toString());

        Integer employeeId = null;
        if (paramDTO.get("employeeId") != null) {
            employeeId = new Integer(paramDTO.get("employeeId").toString());
        }

        //get user as employee
        if (employeeId == null) {
            Employee employee = readEmployee(userAddressId);
            employeeId = (employee != null) ? employee.getEmployeeId() : null;
        }

        TemplateTextHome templateTextHome = (TemplateTextHome) EJBFactory.i.getEJBLocalHome(CatalogConstants.JNDI_TEMPLATETEXT);

        try {
            TemplateText templateText = templateTextHome.findByPrimaryKey(new TemplateTextPK(languageId, template.getTemplateId()));
            processTemplate(companyId, templateText, null, null, employeeId, userAddressId);
        } catch (FinderException e) {
            log.debug("Not found templateText");
            resultDTO.setResultAsFailure();
        }
    }


    private void processTemplate(Integer companyId, TemplateText templateText, Integer addressId, Integer contactPersonId, Integer employeeId, Integer userAddressId) {

        String nullEntityValue = (String) paramDTO.get("msgUnknown");

        boolean isEmail = true;
        String requestLocale = paramDTO.get("requestLocale").toString();

        Integer languageId = templateText.getLanguageId();


        ReadTemplateValues readTemplateValues = null;
        Map templateFieldValueMap;
        try {
            readTemplateValues = new ReadTemplateValues(true, nullEntityValue);
            readTemplateValues.initialize(companyId, userAddressId, employeeId, isEmail, false, requestLocale, false);
            templateFieldValueMap = readTemplateValues.getFieldValuesAsMap(addressId, contactPersonId, languageId, null);

        } catch (FinderException e) {
            log.debug("Error in read values for template...", e);
            resultDTO.setResultAsFailure();
            resultDTO.addResultMessage("Common.invalid.id");
            return;
        }


        FreeText freeText = templateText.getFreeText();
        //create body
        HtmlTemplateProcessor htmlTemplateProcessor = new HtmlTemplateProcessor(new StringBuilder(new String(freeText.getValue())));
        StringBuilder finalMailBody = htmlTemplateProcessor.buildHtmlDocument(templateFieldValueMap);

        resultDTO.put("importedHtml", finalMailBody.toString());
    }

    private Address readAddress(Integer addressId) {
        Address address = null;
        AddressHome addressHome = (AddressHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_ADDRESS);
        try {
            address = addressHome.findByPrimaryKey(addressId);
        } catch (FinderException e) {
            log.debug("Not found Address..." + addressId, e);
        }
        return address;
    }

    private Employee readEmployee(Integer employeeId) {
        Employee employee = null;
        EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);
        try {
            employee = employeeHome.findByPrimaryKey(employeeId);
        } catch (FinderException e) {
            log.debug("Not found Employee.." + employeeId);
        }
        return employee;
    }
}
