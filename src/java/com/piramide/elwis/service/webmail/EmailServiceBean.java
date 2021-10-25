package com.piramide.elwis.service.webmail;

import com.piramide.elwis.domain.admin.UserHome;
import com.piramide.elwis.domain.admin.UserSessionLogHome;
import com.piramide.elwis.domain.webmailmanager.UserMailHome;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomEvent;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomEventHandler;
import com.piramide.elwis.service.webmail.downloadlog.MyCustomListener;
import com.piramide.elwis.utils.AdminConstants;
import com.piramide.elwis.utils.JMSUtil;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.jms.DeleteEmailMessage;
import com.piramide.elwis.utils.webmail.jms.UserMessage;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import javax.ejb.*;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jatun S.R.L
 *
 * @author ivan
 */
public class EmailServiceBean implements SessionBean, MyCustomListener {
    private final static Log LOG = LogFactory.getLog(EmailServiceBean.class);

    private SessionContext ctx;

    public EmailServiceBean() {
    }

    public void sentEmails(Integer userMailId) {
        UserMessage userMessage = new UserMessage(userMailId, true);

        sentJMSUserMessageToSentEmail(userMessage);
    }

    public void sentEmailsInBackGround() {
        List<Integer> loggedUserIds = getLoggedApplicationUserIds();
        List<Integer> emailUserIds = loadEnabledUserMailIdsToProcessInBackground();

        UserMessage message;
        for (Integer id : emailUserIds) {
            message = new UserMessage(id, loggedUserIds.contains(id));

            sentJMSUserMessageToSentEmail(message);
        }
    }

    public void downloadEmailInBackground() {
        List<Integer> loggedUserIds = getLoggedApplicationUserIds();
        List<Integer> emailUserIds = loadEnabledUserMailIdsToProcessInBackground();

        long millisKey = System.currentTimeMillis();
        DownloadLogFactory.i.log(this.getClass(), " ");
        DownloadLogFactory.i.log(this.getClass(), "Starting download in background process: " + new DateTime(millisKey));
        DownloadLogFactory.i.mdbObserverPreviousLog("Previous items still in the queue...");
        DownloadLogFactory.i.log(this.getClass(), "List of userIds to process:" + emailUserIds);
        DownloadLogFactory.i.log(this.getClass(), "User to process:" + emailUserIds.size());

        UserMessage message;
        for (Integer id : emailUserIds) {
            message = new UserMessage(id, loggedUserIds.contains(id));
            message.setMillisKey(millisKey);

            sentJMSUserMessageToDownloadEmail(message);
        }
    }

    public void deleteEmailsFromPOPServer(DeleteEmailMessage deleteEmailMessage) {
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_QUEUE_DELETE_EMAIL, deleteEmailMessage, false);
        } catch (NamingException e) {
            LOG.error("Cannot queue the JMS UserMessage to delete emails from pop server emails.", e);
        } catch (JMSException e) {
            LOG.error("Cannot queue the JMS UserMessage to delete emails from pop server emails.", e);
        }
    }

    private List<Integer> loadEnabledUserMailIdsToProcessInBackground() {
        List<Integer> applicationUserIds = getActiveApplicationUserIds();

        List<Integer> emailUserIds = getEnabledUserMailToBackgroundDownloadIds();

        applicationUserIds.retainAll(emailUserIds);

        return applicationUserIds;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getEnabledUserMailToBackgroundDownloadIds() {
        UserMailHome userMailHome =
                (UserMailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_USERMAIL);

        List<Integer> result = new ArrayList<Integer>();
        try {
            result.addAll(userMailHome.selectBackgroundUserMailIds());
        } catch (FinderException e) {
            // No results found
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getActiveApplicationUserIds() {
        UserHome userHome =
                (UserHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USER);

        List<Integer> result = new ArrayList<Integer>();
        try {
            result.addAll(userHome.selectActiveApplicationUserIds());
        } catch (FinderException e) {
            // No results found
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    private List<Integer> getLoggedApplicationUserIds() {
        UserSessionLogHome userSessionLogHome =
                (UserSessionLogHome) EJBFactory.i.getEJBLocalHome(AdminConstants.JNDI_USERSESSIONLOG);
        List<Integer> result = new ArrayList<Integer>();
        try {
            result.addAll(userSessionLogHome.selectLoadLoggedUserIds());
        } catch (FinderException e) {
            // No results found
        }

        return result;
    }

    private void sentJMSUserMessageToSentEmail(UserMessage userMessage) {
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_QUEUE_SEARCH_EMAIL_USER_IN_BACKGROUND, userMessage, false);
        } catch (NamingException e) {
            LOG.error("Cannot queue the JMS UserMessage to send emails.", e);
        } catch (JMSException e) {
            LOG.error("Cannot queue the JMS UserMessage to send emails.", e);
        }
    }

    private void sentJMSUserMessageToDownloadEmail(UserMessage userMessage) {
        try {
            JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_QUEUE_DOWNLOAD_EMAILS_USER_IN_BACKGROUND, userMessage, false);
            queueDownloadLog(userMessage);
        } catch (NamingException e) {
            LOG.error("Cannot queue the JMS UserMessage to download emails. userMailId=" + userMessage.getUserMailId(), e);
        } catch (JMSException e) {
            LOG.error("Cannot queue the JMS UserMessage to download emails. userMailId=" + userMessage.getUserMailId(), e);
        }
    }

    private void queueDownloadLog(UserMessage userMessage) {
        MyCustomEvent myCustomEvent = new MyCustomEvent(this, "QUEUE_USERMAIL", userMessage.getMillisKey(), userMessage.getUserMailId());
        fire(myCustomEvent);
    }

    public void fire(MyCustomEvent event) {
        MyCustomEventHandler.fire(event);
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
        this.ctx = sessionContext;
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }
}
