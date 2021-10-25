package com.piramide.elwis.web.common.listener;

import com.piramide.elwis.cmd.campaignmanager.CampaignDocumentGenerateCmd;
import com.piramide.elwis.cmd.campaignmanager.util.VelocityManager;
import com.piramide.elwis.cmd.common.SystemPattern;
import com.piramide.elwis.cmd.common.SystemTemplateValues;
import com.piramide.elwis.cmd.common.config.SystemConstantCmd;
import com.piramide.elwis.cmd.utils.MailManager;
import com.piramide.elwis.cmd.webmailmanager.UpdateMailAccountAutomaticReplyMessageBatchCmd;
import com.piramide.elwis.domain.admin.UserSessionLog;
import com.piramide.elwis.domain.admin.UserSessionLogHome;
import com.piramide.elwis.dto.common.config.SystemConstantDTO;
import com.piramide.elwis.utils.*;
import com.piramide.elwis.utils.configuration.ConfigurationFactory;
import com.piramide.elwis.web.common.accessrightdatalevel.util.AccessRightDataLevelFactory;
import com.piramide.elwis.web.common.dynamicsearch.util.DynamicSearchFactory;
import com.piramide.elwis.web.common.util.JSPHelper;
import com.piramide.elwis.web.financemanager.delegate.InvoicePositionBatchDelegate;
import com.piramide.elwis.web.uimanager.el.Functions;
import net.java.dev.strutsejb.AppLevelException;
import net.java.dev.strutsejb.dto.EJBFactory;
import net.java.dev.strutsejb.dto.ResultDTO;
import net.java.dev.strutsejb.web.BusinessDelegate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Initialization listener
 */
public class InitializeApplicationListener implements ServletContextListener {
    private final static Log log = LogFactory.getLog(InitializeApplicationListener.class);

/* Application Startup Event */

    public void contextInitialized(ServletContextEvent event) {


        log.debug("Initialization Application....");

        initCampaign();

        MailManager.init(ConfigurationFactory.getValue("elwis.temp.folder"));
        VelocityManager.i.initializeVelocityManager();

        initUI(event);

        //initialize system constants
        try {
            initializeSystemConstants();
        } catch (Exception e) {
            log.error("Error initializing system constants " + e);
            throw new ExceptionInInitializerError();
        }

        log.debug("Initialize dynamic search structure");
        DynamicSearchFactory.i.initializeStructure(event.getServletContext());

        log.debug("Initialize access right data level structure");
        AccessRightDataLevelFactory.i.initializeStructure(event.getServletContext());

        bacthRestoreRecurrenceReminders();

        batchForUpdateInvoicePositionsDiscountValue();

        closeUnFinishedUserLogSessions();

        /* Below you should put temporal batch processes */

        batchForUpdateMailAccountsWithOldAutomaticReplyMessages();
    }

    private void initCampaign() {
        CampaignDocumentGenerateCmd.stop = new HashMap();
        (new File(ConfigurationFactory.getValue("elwis.temp.folder") + "/campaign/")).mkdirs();
    }

    private void initUI(ServletContextEvent event) {
        /*add by miky*/
        String resourceFile = null;
        try {
            resourceFile = event.getServletContext().getResource("/WEB-INF/uimanager/xmlstyles/elwis_style.xml").toString();
        } catch (MalformedURLException e) {
            log.error("not be may get resorce to file..." + e);
        }

        InputStream inputStreamStyleSheet = event.getServletContext().getResourceAsStream("/WEB-INF/uimanager/xmlstyles/elwis_style.xml");
        InputStream inputStreamAttributeType = event.getServletContext().getResourceAsStream("/WEB-INF/uimanager/xmlstyles/attributeTypes.xml");

        Map sectionWrapperMap = Functions.readXmlStyleSheetFile(inputStreamStyleSheet, resourceFile);
        List xmlTypes = Functions.readXmlStyleTypes(inputStreamAttributeType, resourceFile);

        //set in the context of aplication
        event.getServletContext().setAttribute(UIManagerConstants.XMLSTYLESHEET_KEY, sectionWrapperMap);
        event.getServletContext().setAttribute(UIManagerConstants.XMLSTYLETYPES_KEY, xmlTypes);

        //init UI bootstrap styles
        initBootstrapUI(event);

        /*end add by miky*/
    }

    private void initBootstrapUI(ServletContextEvent event) {
        String resourceFile = null;
        try {
            resourceFile = event.getServletContext().getResource("/WEB-INF/uimanager/xmlstyles/elwis_style_bootstrap.xml").toString();
        } catch (MalformedURLException e) {
            log.error("not be may get resource to file..." + e);
        }

        InputStream inputStreamStyleSheet = event.getServletContext().getResourceAsStream("/WEB-INF/uimanager/xmlstyles/elwis_style_bootstrap.xml");
        Map sectionWrapperMap = Functions.readXmlStyleSheetFile(inputStreamStyleSheet, resourceFile);

        //set in the context of aplication
        event.getServletContext().setAttribute(UIManagerConstants.XMLSTYLESHEET_BOOTSTRAP_KEY, sectionWrapperMap);
    }

/* Application ShutdownEvent */

    public void contextDestroyed(ServletContextEvent event) {
        //closeUnFinishedUserLogSessions();//In jboss 5.1.0 this throw exceptions. this is not required because is called also from contextInitialized
    }

    /**
     * This method allows to close any user session log which was left open (connected) when the server was stopped
     * or when it is going to start (no sessions alive)
     */
    private void closeUnFinishedUserLogSessions() {
        // for userSession log - update fields
        UserSessionLogHome home = (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);
        try {
            Collection collection = home.findAllConnected(true);
            for (Object aCollection : collection) {
                UserSessionLog sessionLog = (UserSessionLog) aCollection;
                sessionLog.setIsConnected(false);
                sessionLog.setEndConnectionDateTime(System.currentTimeMillis());
            }
        } catch (FinderException e) {
            log.debug("No connected users found");
        }
    }

    private void initializeSystemConstants() throws Exception {
        SystemConstantCmd cmd = null;

        //initialize system languages
        cmd = new SystemConstantCmd();
        cmd.putParam("type", SystemLanguage.SYSTEM_CONSTANT_KEY);
        ResultDTO resultDTO = BusinessDelegate.i.execute(cmd, null);
        List languages = (List) resultDTO.get(SystemLanguage.SYSTEM_CONSTANT_KEY);
        log.debug("System languages recovered = " + languages);
        for (Iterator iterator = languages.iterator(); iterator.hasNext();) {
            SystemConstantDTO dto = (SystemConstantDTO) iterator.next();
            SystemLanguage.systemLanguages.put(dto.get("value"), dto.get("resourceKey"));
        }
        //initialize categoryTypes
        cmd.putParam("type", CategoryTypes.CATEGORYTYPES_CONSTANT_KEY);
        resultDTO = BusinessDelegate.i.execute(cmd, null);
        List categoryTypeList = (List) resultDTO.get(CategoryTypes.CATEGORYTYPES_CONSTANT_KEY);
        log.debug("System categoryType recovered = " + categoryTypeList);
        for (Iterator iterator = categoryTypeList.iterator(); iterator.hasNext();) {
            SystemConstantDTO dto = (SystemConstantDTO) iterator.next();
            CategoryTypes.categorytypes.put(dto.get("value"), dto.get("resourceKey"));
        }

        //initialize system pattern
        for (Iterator iterator = SystemLanguage.systemLanguages.keySet().iterator(); iterator.hasNext();) {
            String isoLangKey = (String) iterator.next();
            Locale locale = new Locale(isoLangKey);
            SystemPattern.datePattern.put(isoLangKey, JSPHelper.getMessage(locale, "datePattern"));
            SystemPattern.decimalPattern.put(isoLangKey, JSPHelper.getMessage(locale, "numberFormat.2DecimalPlaces"));
            SystemPattern.decimalPattern4Digits.put(isoLangKey, JSPHelper.getMessage(locale, "numberFormat.4DecimalPlaces"));

            //initialize system template constant  values
            SystemTemplateValues.invoiceVoucherType.put(isoLangKey, JSPHelper.getMessage(locale, "Invoice.voucherType.invoice"));
            SystemTemplateValues.creditNoteVoucherType.put(isoLangKey, JSPHelper.getMessage(locale, "Invoice.voucherType.creditNote"));
        }

    }

    private void bacthRestoreRecurrenceReminders() {
        try {
            log.info("bacth restoring of reminders with recurrences lost on last app shutdown");
            JMSUtil.sendToJMSQueue(SchedulerConstants.JNDI_BATCHRECURRENCESREMINDERMDB, null, false);
        } catch (NamingException e) {
            log.error("Error trying to found batch reminder recurrences MDB", e);
        } catch (JMSException e) {
            log.error("Error trying to send message to batch reminder recurrences MDB ", e);
        }

    }

    private void batchForUpdateInvoicePositionsDiscountValue() {
        InvoicePositionBatchDelegate.i.updateDiscountValue();
    }

    /**
     * Batch to update mail accounts with old reply messages
     * this not should be removed until bm v6.5 has been released in all production servers
     */
    private void batchForUpdateMailAccountsWithOldAutomaticReplyMessages() {
        UpdateMailAccountAutomaticReplyMessageBatchCmd batchCmd = new UpdateMailAccountAutomaticReplyMessageBatchCmd();
        try {
            BusinessDelegate.i.execute(batchCmd, null);
        } catch (AppLevelException e) {
            log.error("Error in execute update mail account reply messages batch process....", e);
        }
    }
}