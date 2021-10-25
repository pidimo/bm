package com.piramide.elwis.utils.webmail;

import com.jatun.commons.email.connection.pop.Mime4JEmailBuilder;
import com.jatun.commons.email.model.Attach;
import com.jatun.commons.email.model.Body;
import com.jatun.commons.email.model.Email;
import com.jatun.commons.email.parser.HtmlEmailDOMParser;
import com.jatun.commons.email.parser.HtmlEmailParser;
import com.piramide.elwis.cmd.utils.WebmailHTMLParser;
import com.piramide.elwis.domain.webmailmanager.*;
import com.piramide.elwis.utils.WebMailConstants;
import com.piramide.elwis.utils.webmail.filter.ProcessAttributesForLinkTAG;
import com.piramide.elwis.utils.webmail.filter.RemoveIdAttributeFromImgTAG;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TimeZone;

/**
 * @author : Ivan
 *         <p/>
 *         Jatun S.R.L
 */
public class EmailSourceUtil {

    public static EmailSourceUtil i = new EmailSourceUtil();

    private EmailSourceUtil() {

    }

    private Log log = LogFactory.getLog(EmailSourceUtil.class);

    public EmailSource createEmailSource(Integer mailId, Integer companyId, byte[] data) throws CreateException {
        EmailSourceHome emailSourceHome =
                (EmailSourceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILSOURCE);

        return emailSourceHome.create(mailId, companyId, data);
    }

    public void buildAndCompleteMail(Mail mail, EmailSource emailSource, UserMail userMail) {
        if (null != mail.getIsCompleteEmail() && mail.getIsCompleteEmail()) {
            return;
        }

        Email email = parseEmailSource(emailSource, WebmailAccountUtil.i.getTimeZone(userMail.getUserMailId()));
        if (null != email) {
            completeEmail(email, mail);
        }

        mail.setIsCompleteEmail(true);
        mail.setIsNewEmail(false);
    }

    /**
     * This method is used in communications
     *
     * @param mailId     Mail identifier
     * @param userMailId userMail
     */
    public void buildAndCompleteMail(Integer mailId, Integer userMailId) {
        UserMail userMail = WebmailAccountUtil.i.getUserMail(userMailId);
        Mail mail = getMail(mailId);
        EmailSource emailSource = getEmailSource(mail.getMailId(), mail.getCompanyId());
        buildAndCompleteMail(mail, emailSource, userMail);
    }

    /**
     * Rebuild the emailsource eml message and update the body and attachments
     * @param mailId mailId
     */
    public void rebuildAndUpdateMail(Integer mailId) {
        Mail mail = getMail(mailId);
        EmailSource emailSource = getEmailSource(mail.getMailId(), mail.getCompanyId());
        if (emailSource != null) {
            Email email = parseEmailSource(emailSource, TimeZone.getDefault());
            if (email != null) {
                Body body = email.getBody();
                if (body == null) {
                    body = new Body("", Body.Type.TEXT);
                }
                processBody(body);

                //delete old attach
                deleteAttachments(mail);
                //create parsed email attach
                if (!email.getAttachParts().isEmpty()) {
                    createAttachments(email.getAttachParts(), mail, body);
                }

                //update the body
                updateDataBaseBody(body, mail);
            }
        }
    }

    private Email parseEmailSource(EmailSource emailSource, TimeZone timeZone) {
        Email email;
        Mime4JEmailBuilder emailBuilder = new Mime4JEmailBuilder();
        try {
            email = emailBuilder.buildEmail(emailSource.getSource(), timeZone);
        } catch (IOException e) {
            log.error("Error in parse eml emailSource..." + emailSource.getMailId());
            email = null;
        }
        return email;
    }

    public EmailSource getEmailSource(Integer mailId, Integer companyId) {
        EmailSourceHome emailSourceHome =
                (EmailSourceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILSOURCE);
        try {
            return emailSourceHome.findByMailId(mailId, companyId);
        } catch (FinderException e) {
            return null;
        }
    }

    public void completeEmail(Email email,
                               Mail dataBaseEmail) {
        com.jatun.commons.email.model.Body body = email.getBody();

        if (body == null) {
            body = new Body("", Body.Type.TEXT);
        }
        processBody(body);

        if (!email.getAttachParts().isEmpty()) {
            createAttachments(email.getAttachParts(), dataBaseEmail, body);
        }

        createDataBaseBody(body, dataBaseEmail);
    }

    private void processBody(Body body) {
        if (body.isHtml()) {
            HtmlEmailParser parser = new HtmlEmailDOMParser();
            parser.addFilter(new ProcessAttributesForLinkTAG());
            parser.addFilter(new RemoveIdAttributeFromImgTAG());

            try {
                parser.parseHtml(body.getContentAsHtml());
                String newContent = parser.getHtml().toString();
                body.setContent(newContent, Body.Type.HTML);
            } catch (Exception e) {
                log.debug("-> Process Body Html content FAIL");
            }
        }
    }

    private void createAttachments(List<Attach> attachments, Mail dataBaseEmail, Body body) {
        AttachHome attachHome =
                (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);

        for (Attach attach : attachments) {
            dataBaseEmail.setHaveAttachments(true);

            byte[] data = attach.getContain();
            if (null == data) {
                continue;
            }

            try {
                com.piramide.elwis.domain.webmailmanager.Attach dataBaseAttach =
                        attachHome.create(dataBaseEmail.getCompanyId(), dataBaseEmail.getMailId(), attach.getFileName(), attach.getUuid(), data.length);
                dataBaseAttach.setVisible(false);
                if (null != attach.getCid() && !"".equals(attach.getCid())) {
                    if (updateBody(dataBaseAttach.getAttachId(), attach.getCid(), body)) {
                        dataBaseAttach.setVisible(true);
                    }
                }

            } catch (CreateException e) {
                log.error("Create Attachment in database FAIL ", e);
            }
        }
    }

    private void deleteAttachments(Mail dataBaseEmail) {
        AttachHome attachHome = (AttachHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_ATTACH);
        Collection attachCollection = null;
        try {
            attachCollection = attachHome.findByMailId(dataBaseEmail.getMailId(), dataBaseEmail.getCompanyId());
        } catch (FinderException e) {
            attachCollection = new ArrayList();
        }

        Object[] attachs = attachCollection.toArray();
        for (int i = 0; i < attachs.length; i++) {
            com.piramide.elwis.domain.webmailmanager.Attach dataBaseAttach = (com.piramide.elwis.domain.webmailmanager.Attach) attachs[i];
            try {
                dataBaseAttach.remove();
            } catch (RemoveException e) {
                log.error("Can't remove attach with id: " + dataBaseAttach.getAttachId());
            }
        }
    }

    private boolean updateBody(Integer attachId, String cid, Body body) {
        boolean result = false;

        if (body.isHtml()) {
            String contain = body.getContentAsHtml();

            String newBodyString = WebmailHTMLParser.changeEmailCidByElwisCid(cid,
                    attachId,
                    contain);
            result = !contain.equals(newBodyString);

            body.setContent(newBodyString, Body.Type.HTML);
        }

        return result;
    }

    private void createDataBaseBody(Body body, Mail dataBaseEmail) {
        BodyHome bodyHome = (BodyHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_BODY);

        String content = body.getContendAsPlainText();
        Integer dataBaseType = Integer.valueOf(WebMailConstants.BODY_TYPE_TEXT);
        if (body.isHtml()) {
            dataBaseType = Integer.valueOf(WebMailConstants.BODY_TYPE_HTML);
            content = body.getContentAsHtml();
        }

        try {
            com.piramide.elwis.domain.webmailmanager.Body dataBaseBody =
                    bodyHome.create(content.getBytes(), dataBaseType, dataBaseEmail.getCompanyId());
            dataBaseEmail.setBodyId(dataBaseBody.getBodyId());
        } catch (CreateException e) {
            log.error("Create Body in database FAIL ", e);
        }
    }

    private void updateDataBaseBody(Body body, Mail dataBaseEmail) {
        BodyHome bodyHome = (BodyHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_BODY);

        String content = body.getContendAsPlainText();
        Integer dataBaseType = Integer.valueOf(WebMailConstants.BODY_TYPE_TEXT);
        if (body.isHtml()) {
            dataBaseType = Integer.valueOf(WebMailConstants.BODY_TYPE_HTML);
            content = body.getContentAsHtml();
        }

        com.piramide.elwis.domain.webmailmanager.Body dataBaseBody = dataBaseEmail.getBody();
        if (dataBaseBody != null) {
            dataBaseBody.setBodyContent(content.getBytes());
            dataBaseBody.setBodyType(dataBaseType);
        } else {
            createDataBaseBody(body,dataBaseEmail);
        }
    }

    private Mail getMail(Integer mailId) {
        MailHome mailHome =
                (MailHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_MAIL);
        try {
            return mailHome.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            return null;
        }
    }
}
