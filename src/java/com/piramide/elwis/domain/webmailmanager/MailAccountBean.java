/**
 * Jatun s.r.l.
 *
 * @author : ivan
 */
package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

public abstract class MailAccountBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setMailAccountId(PKGenerator.i.nextKey(WebMailConstants.TABLE_MAILACCOUNT));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public MailAccountBean() {
    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract String getEmail();

    public abstract void setEmail(String email);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyid);

    public abstract String getLogin();

    public abstract void setLogin(String login);

    public abstract Integer getMailAccountId();

    public abstract void setMailAccountId(Integer mailAccountId);

    public abstract String getPassword();

    public abstract void setPassword(String password);

    public abstract String getServerName();

    public abstract void setServerName(String serverName);

    public abstract String getServerPort();

    public abstract void setServerPort(String serverPort);

    public abstract Integer getUserMailId();

    public abstract void setUserMailId(Integer userMailId);

    public abstract Integer getUseSSLConnection();

    public abstract void setUseSSLConnection(Integer useSSLConection);

    public abstract Integer getAccountType();

    public abstract void setAccountType(Integer accountType);

    public abstract java.util.Collection getSignatures();

    public abstract void setSignatures(java.util.Collection signatures);

    public abstract Boolean getDefaultAccount();

    public abstract void setDefaultAccount(Boolean defaultAccount);

    public abstract String getPrefix();

    public abstract void setPrefix(String prefix);

    public abstract String getSmtpServer();

    public abstract void setSmtpServer(String smtpServer);

    public abstract String getSmtpPort();

    public abstract void setSmtpPort(String smtpPort);

    public abstract Boolean getSmtpAuthentication();

    public abstract void setSmtpAuthentication(Boolean smtpAuthentication);

    public abstract Integer getSmtpSSL();

    public abstract void setSmtpSSL(Integer smtpSSL);

    public abstract Long getLastDownloadTime();

    public abstract void setLastDownloadTime(Long lastDownloadTime);

    public abstract Boolean getUsePOPConfiguration();

    public abstract void setUsePOPConfiguration(Boolean usePOPConfiguration);

    public abstract String getSmtpLogin();

    public abstract void setSmtpLogin(String smtpLogin);

    public abstract String getSmtpPassword();

    public abstract void setSmtpPassword(String smtpPassword);

    public abstract Boolean getKeepEmailOnServer();

    public abstract void setKeepEmailOnServer(Boolean keepEmailOnServer);

    public abstract Boolean getCreateInCommunication();

    public abstract void setCreateInCommunication(Boolean createInCommunication);

    public abstract Boolean getCreateOutCommunication();

    public abstract void setCreateOutCommunication(Boolean createOutCommunication);

    public abstract Boolean getAutomaticForward();

    public abstract void setAutomaticForward(Boolean automaticForward);

    public abstract String getForwardEmail();

    public abstract void setForwardEmail(String forwardEmail);

    public abstract Boolean getAutomaticReply();

    public abstract void setAutomaticReply(Boolean automaticReply);

    public abstract String getAutomaticReplyMessageSubject();

    public abstract void setAutomaticReplyMessageSubject(String automaticReplyMessageSubject);

    public abstract String getAutomaticReplyMessage();

    public abstract void setAutomaticReplyMessage(String automaticReplyMessage);

    public abstract Integer getRemoveEmailOnServerAfterOf();

    public abstract void setRemoveEmailOnServerAfterOf(Integer removeEmailOnServerAfterOf);

    public abstract Integer getReplyMessageHtmlId();

    public abstract void setReplyMessageHtmlId(Integer replyMessageHtmlId);

    public abstract Integer getReplyMessageTextId();

    public abstract void setReplyMessageTextId(Integer replyMessageTextId);
}
