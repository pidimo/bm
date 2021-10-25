package com.piramide.elwis.utils.webmail;

import com.jatun.commons.email.connection.pop.Mime4JEmailBuilder;
import com.piramide.elwis.domain.webmailmanager.Attach;
import com.piramide.elwis.domain.webmailmanager.EmailSource;
import com.piramide.elwis.domain.webmailmanager.EmailSourceHome;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.EJBFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.FinderException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Util to get the attach data, this can be from attach table or the related email source table
 *
 * @author Miguel A. Rojas Cardenas
 * @version 4.7
 */
public class WebmailAttachUtil {
    private Log log = LogFactory.getLog(WebmailAttachUtil.class);

    public static WebmailAttachUtil i = new WebmailAttachUtil();

    private WebmailAttachUtil() {
    }

    public byte[] getAttachData(Attach attach) {
        byte[] attachData = new byte[0];

        if (attach.getEmlAttachUUID() != null) {
            EmailSource emailSource = findEmailSource(attach.getMailId());
            if (emailSource != null) {

                Mime4JEmailBuilder emailBuilder = new Mime4JEmailBuilder();
                try {
                    ByteArrayOutputStream byteArrayOutputStream = emailBuilder.findAttachment(emailSource.getSource(), attach.getEmlAttachUUID());
                    if (byteArrayOutputStream != null) {
                        attachData = byteArrayOutputStream.toByteArray();
                    }
                } catch (IOException e) {
                    log.debug("Can't read attach content from eml..", e);
                }
            }
        } else {
            attachData = attach.getAttachFile();
        }
        return attachData;
    }

    private EmailSource findEmailSource(Integer mailId) {
        EmailSource emailSource = null;
        EmailSourceHome emailSourceHome = (EmailSourceHome) EJBFactory.i.getEJBLocalHome(WebMailConstants.JNDI_EMAILSOURCE);
        try {
            emailSource = emailSourceHome.findByPrimaryKey(mailId);
        } catch (FinderException e) {
            log.debug("Not found EmailSource:" + mailId);
        }
        return emailSource;
    }


}
