package com.piramide.elwis.cmd.utils;

import com.piramide.elwis.utils.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.File;

/**
 * Mail manager for read from pop server, send email using external smtp server or
 * using local smpt server
 *
 * @author ivan
 * @version $Id: MailManager.java 9695 2009-09-10 21:34:43Z fernando ${CLASS_NAME}.java,v 1.2 01-03-2005 10:09:22 AM ivan Exp $
 */
public class MailManager {
    private Log log = LogFactory.getLog(this.getClass());
    public static String PATH = "";
    public static String CACHE_FOLDER_NAME = "/webmail-cache/";

    /**
     * Cannot avalilable smtp server
     */
    public static final int NOT_FEATURE_SMTP = 1;

    public static final int CANNOT_CONNECT_SMTP = 2;

    /**
     * connection was established
     */
    public static final int OK = 0;


    /**
     * Singleton instance
     */
    public static MailManager i = new MailManager();

    private MailManager() {
    }

    public static void init(String p) {
        PATH = p;
        File f = new File(PATH + CACHE_FOLDER_NAME);
        f.mkdir();
    }

    public boolean testSMTPServer() {
        log.debug("testSMTPServer()");

        Session session = null;
        try {
            InitialContext initialContext = new InitialContext();
            session = (Session) initialContext.lookup(Constants.JNDI_MAILSESSION);
        } catch (NamingException e) {
            log.debug("Cannot locate Send mail Service....");
            return false;
        }

        Transport transport = null;
        try {
            transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            log.debug("No existe ekl proveedor....");
            return false;
        }

        try {
            transport.connect();
            transport.close();
        } catch (MessagingException e) {
            log.debug("no esta disponible el servidor ");
            return false;
        }

        return true;
    }
}

