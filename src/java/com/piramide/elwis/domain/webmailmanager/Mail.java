package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: Mail.java 10320 2013-02-26 20:02:58Z miguel ${NAME}.java ,v 1.1 02-02-2005 04:32:28 PM miky Exp $
 */

public interface Mail extends EJBLocalObject {
    Integer getMailId();

    void setMailId(Integer mailId);

    String getMailSubject();

    void setMailSubject(String mailSubject);

    Byte getMailState();

    void setMailState(Byte mailState);

    Integer getMailPriority();

    void setMailPriority(Integer mailPriority);

    Integer getFolderId();

    void setFolderId(Integer folderId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Collection getAttachs();

    void setAttachs(Collection attachs);

    Body getBody();

    void setBody(Body body);

    Integer getBodyId();

    void setBodyId(Integer bodyid);

    Integer getMailSize();

    void setMailSize(Integer mailSize);

    String getMailFrom();

    void setMailFrom(String mailFrom);

    Collection getMailRecipients();

    void setMailRecipients(Collection mailRecipients);

    String getMailPersonalFrom();

    void setMailPersonalFrom(String mailPersonalFrom);

    Long getSentDate();

    void setSentDate(Long sentDate);

    Integer getIncomingOutgoing();

    void setIncomingOutgoing(Integer incomingOutgoing);

    Integer getSignatureId();

    void setSignatureId(Integer signatureId);

    Folder getFolder();

    void setFolder(Folder folder);

    String getMailAccount();

    void setMailAccount(String mailAccount);

    Boolean getHidden();

    void setHidden(Boolean hidden);

    Boolean getSaveEmail();

    void setSaveEmail(Boolean saveEmail);

    Boolean getCreateContact();

    void setCreateContact(Boolean createContact);

    String getMessageId();

    void setMessageId(String messageId);

    Boolean getHaveAttachments();

    void setHaveAttachments(Boolean attachment);

    Boolean getIsNewEmail();

    void setIsNewEmail(Boolean isNewEmail);

    Boolean getIsCompleteEmail();

    void setIsCompleteEmail(Boolean isCompleteEmail);

    String getUidl();

    void setUidl(String uidl);

    Boolean getProcessToSent();

    void setProcessToSent(Boolean processToSent);

    Boolean getIsDraftTemp();

    void setIsDraftTemp(Boolean isDraftTemp);
}
