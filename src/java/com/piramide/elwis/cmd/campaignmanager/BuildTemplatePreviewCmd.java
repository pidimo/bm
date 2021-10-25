package com.piramide.elwis.cmd.campaignmanager;

import com.piramide.elwis.cmd.campaignmanager.util.HtmlTemplateProcessor;
import com.piramide.elwis.cmd.common.ReadTemplateValues;
import com.piramide.elwis.domain.admin.User;
import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.campaignmanager.*;
import com.piramide.elwis.domain.contactmanager.ContactPersonHome;
import com.piramide.elwis.domain.contactmanager.ContactPersonPK;
import com.piramide.elwis.domain.contactmanager.Employee;
import com.piramide.elwis.domain.contactmanager.EmployeeHome;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.CampaignConstants;
import com.piramide.elwis.utils.ContactConstants;
import net.java.dev.strutsejb.EJBCommand;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.ejb.SessionContext;
import java.util.HashMap;
import java.util.Map;

/**
 * This class read template freetext and build template to preview
 *
 * @author: ivan
 */
public class BuildTemplatePreviewCmd extends EJBCommand {
    private Log log = LogFactory.getLog(this.getClass());

    public void executeInStateless(SessionContext sessionContext) {
        log.debug("Build HTML preview of document");
        Integer companyId = new Integer(paramDTO.get("companyId").toString());
        Integer userId = new Integer(paramDTO.get("currentUserId").toString());
        String requestLocale = paramDTO.get("requestLocale").toString();
        Integer languageId = new Integer(paramDTO.get("languageId").toString());
        Integer templateId = new Integer(paramDTO.get("templateId").toString());
        Integer campaignId = new Integer(paramDTO.get("campaignId").toString());

        Integer addressId = null;
        Integer employeeId = null;

        UserHome userHome = (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);
        try {
            User user = userHome.findByPrimaryKey(userId);

            if (AdminConstants.INTERNAL_USER.equals(user.getType().toString())) {
                log.debug("User is internal.");
                addressId = user.getAddressId();
            } else {
                log.debug("Use is external and use responsable campaign to generate preview of template.");
                CampaignHome campaignHome = (CampaignHome) EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN);
                Campaign campaign = campaignHome.findByPrimaryKey(campaignId);
                addressId = campaign.getEmployeeId();
            }
        } catch (FinderException e) {
            log.warn("Cannot find user in elwis ", e.getCause());
        }

        ReadTemplateValues templateValues = null;
        boolean userIsContactPerson = false;
        if (null != addressId) {
            ContactPersonHome contactPersonHome = (ContactPersonHome)
                    EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_CONTACTPERSON);
            EmployeeHome employeeHome = (EmployeeHome) EJBFactory.i.getEJBLocalHome(ContactConstants.JNDI_EMPLOYEE);

            try {
                ContactPersonPK pk = new ContactPersonPK(companyId, addressId);
                contactPersonHome.findByPrimaryKey(pk);
                userIsContactPerson = true;
            } catch (FinderException e) {
                log.warn("Cannot find ContactPerson with addressId = " + companyId + " and contacPersonId = " + addressId);
            }

            try {
                Employee employee = employeeHome.findByPrimaryKey(addressId);
                employeeId = employee.getEmployeeId();
            } catch (FinderException e) {
                log.warn("Cannot find Employee with addressId = " + addressId);
            }

            try {
                templateValues = new ReadTemplateValues(companyId, addressId, employeeId, true, false, requestLocale, true);
            } catch (FinderException e) {
                log.warn("Cannot instanciate ReadTemplateValues Object ", e.getCause());
            }
        }

        if (null != templateValues) {
            Map values = new HashMap();
            if (userIsContactPerson) {
                try {
                    values = templateValues.getFieldValuesAsMap(companyId, addressId, languageId, null);
                } catch (FinderException e) {
                    log.warn("Cannot recover map values.");
                }
            } else {
                try {
                    values = templateValues.getFieldValuesAsMap(addressId, languageId, null);
                } catch (FinderException e) {
                    log.warn("Cannot recover map values.");
                }
            }
            StringBuilder previewText = new StringBuilder();
            String template = "";
            if (!values.isEmpty()) {
                template = readTemplateText(languageId, templateId);
                HtmlTemplateProcessor htmlBuilder = new HtmlTemplateProcessor(new StringBuilder(template));
                previewText = htmlBuilder.buildHtmlDocument(values);
            }

            resultDTO.put("template", new String(previewText));
        }
    }

    /**
     * This method read  <code>CampaignText</code> Object and extracts <code>CampaignFreeText</code> that
     * represents Template text
     *
     * @param languageId language to template preview.
     * @param templateId template identifier.
     * @return <code>String</code> Object that contain template text.
     */
    private String readTemplateText(Integer languageId, Integer templateId) {
        CampaignTextHome campaignTextHome = (CampaignTextHome)
                EJBFactory.i.getEJBLocalHome(CampaignConstants.JNDI_CAMPAIGN_TEXT);
        CampaignTextPK pk = new CampaignTextPK(languageId, templateId);
        String result = "";

        try {
            CampaignText campaignText = campaignTextHome.findByPrimaryKey(pk);
            result = new String(campaignText.getCampaignFreeText().getValue());
        } catch (FinderException e) {
            log.warn("Cannot find CampaignText Object  " + pk);
        }
        return result;
    }

    public boolean isStateful() {
        return false;
    }
}
