/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 26, 2004
 * Time: 9:56:50 AM
 * To change this template use File | Settings | File Templates.
 */
package com.piramide.elwis.service.campaign;

import com.piramide.elwis.cmd.utils.Email;
import com.piramide.elwis.cmd.utils.GenerateDocument;
import com.piramide.elwis.exception.CreateDocumentException;
import com.piramide.elwis.utils.JMSUtil;
import com.piramide.elwis.utils.WebMailConstants;
import org.apache.commons.collections.KeyValue;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DocumentGenerateServiceBean implements SessionBean {
    private final static Log log = LogFactory.getLog("GenerateDocumentService");

    public DocumentGenerateServiceBean() {
    }

    public byte[] renderDocument(List values, String[] names, byte[] wordDoc) throws CreateDocumentException {
        GenerateDocument generateDocument = new GenerateDocument();
        try {
            return generateDocument.renderCampaignDocument(convertToMatrix(values, names.length), names, wordDoc);
        } catch (CreateDocumentException e) {
            log.debug("Throw Exception:" + e);
            //e.printStackTrace();
            //throw new CreateDocumentException(e.getMessage(), e.getArg1());
            throw e;
        }
    }

    public boolean renderDocumentWithMail(String from, Map images, List fields, List values, String[] names, List mails, String subject, List attachs, Integer userId) {
        log.debug("Email render...");
        int i = 0;

        for (Iterator iterator = mails.iterator(); iterator.hasNext();) {
            String mail = (String) iterator.next();
            if (mail != null && mail.length() > 3) {
                try {
                    String[] val = (String[]) values.get(i);
                    StringBuffer body = new StringBuffer();
                    for (Iterator iterator1 = fields.iterator(); iterator1.hasNext();) {
                        Object var = iterator1.next();
                        if (iterator1.hasNext()) {
                            KeyValue entry = (KeyValue) var;
                            body.append(entry.getValue()).append(val[Integer.parseInt(entry.getKey().toString())]);
                        } else {
                            body.append((String) var);
                        }
                    }

                    Email email = new Email(images);
                    //todo to use a constructor with the timezone of the user who is sending the mail
                    email.setSubject(subject);
                    email.setFrom(from);
                    email.setTo(mail);
                    email.setMessage(body.toString());
                    email.setAttachPaths(attachs);
                    email.setBodyType("text/html");
                    log.debug("Email:\n" + email);

                    //if(false)
                    JMSUtil.sendToJMSQueue(WebMailConstants.JNDI_SENDSIMPLEMAILMDB, email, false);
                } catch (NamingException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (JMSException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            i++;
        }
        log.debug("End renderDocumentWithMail");
        return true;
    }


    private Object[][] convertToMatrix(List values, int length) {
        Object[][] matrix = new Object[values.size()][length];

        log.debug("Matrix Function");
        int j = 0;
        for (Iterator iterator = values.iterator(); iterator.hasNext();) {
            matrix[j] = (Object[]) iterator.next();
            j++;
        }
        return matrix;
    }

    public void ejbCreate() throws CreateException {
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException {
    }

    public void ejbRemove() throws EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }
}
