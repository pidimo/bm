package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;
import java.util.Collection;

/**
 * Alfacentauro Team
 *
 * @author miky
 * @version $Id: MailBean.java 10410 2013-11-25 20:13:29Z miguel ${NAME}.java ,v 1.1 02-02-2005 04:32:28 PM miky Exp $
 */

public abstract class MailBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        updateSubject(dto);
        safeMessageId(dto);
        String actualMailFrom = (String) dto.get("mailFrom");//in order to enable empty "from", let's pick its original value
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        Integer id = PKGenerator.i.nextKey(WebMailConstants.TABLE_MAIL);
        this.setMailId(id);
        this.setMailFrom(actualMailFrom);//let's put its original value
        return null;
    }

    private void updateSubject(ComponentDTO dto) {
        String actualSubject = (String) dto.get("mailSubject");
        if (null == actualSubject || "".equals(actualSubject.trim())) {
            return;
        }

        if (actualSubject.length() > 250) {
            String newSubject = actualSubject.substring(0, 247) + "...";
            dto.put("mailSubject", newSubject);
        }


        //because Mysql 5.1 not support (4 bytes) emoji smileys, we remove these and replace with '?'
        actualSubject = (String) dto.get("mailSubject");
        actualSubject = actualSubject.replaceAll("[^\u0000-\uFFFF]", "?"); //To remove all non-BMP characters (basic multilingual plane). Regular expression range "\u0000 - \uFFFF" is unicode character from 1 byte to 3 bytes

        //actualSubject = actualSubject.replaceAll("\uD83D\uDE0E", ""); //smiling face character
        //actualSubject = actualSubject.replaceAll("\uD83D\uDCA9", "");  //pile of poo character

        dto.put("mailSubject", actualSubject);

    }

    private void safeMessageId(ComponentDTO dto) {
        String currentMessageId = (String) dto.get("messageId");
        if (null == currentMessageId || "".equals(currentMessageId.trim())) {
            return;
        }
        if (currentMessageId.length() > 250) {
            String newMessageId = currentMessageId.substring(0, 250);
            dto.put("messageId", newMessageId);
        }
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {
    }

    public MailBean() {
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

    public abstract Integer getMailId();

    public abstract void setMailId(Integer mailId);

    public abstract String getMailSubject();

    public abstract void setMailSubject(String mailSubject);

    public abstract Byte getMailState();

    public abstract void setMailState(Byte mailState);

    public abstract Integer getMailPriority();

    public abstract void setMailPriority(Integer mailPriority);

    public abstract Integer getFolderId();

    public abstract void setFolderId(Integer folderId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Collection getAttachs();

    public abstract void setAttachs(Collection attachs);

    public abstract Body getBody();

    public abstract void setBody(Body body);

    public abstract Integer getBodyId();

    public abstract void setBodyId(Integer bodyid);

    public abstract Integer getMailSize();

    public abstract void setMailSize(Integer mailSize);

    public abstract String getMailFrom();

    public abstract void setMailFrom(String mailFrom);

    public abstract Collection getMailRecipients();

    public abstract void setMailRecipients(Collection mailRecipients);

    public abstract String getMailPersonalFrom();

    public abstract void setMailPersonalFrom(String mailPersonalFrom);

    public abstract Long getSentDate();

    public abstract void setSentDate(Long sentDate);

    public abstract Integer getIncomingOutgoing();

    public abstract void setIncomingOutgoing(Integer incomingOutgoing);

    public abstract Integer getSignatureId();

    public abstract void setSignatureId(Integer signatureId);

    public abstract Folder getFolder();

    public abstract void setFolder(Folder folder);

    public abstract String getMailAccount();

    public abstract void setMailAccount(String mailAccount);

    public abstract Boolean getHidden();

    public abstract void setHidden(Boolean hidden);

    public abstract Integer ejbSelectCountMessages(Integer folderId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectCountMessages(Integer folderId, Integer companyId) throws FinderException {
        return ejbSelectCountMessages(folderId, companyId);
    }

    public abstract Integer ejbSelectCountUnReadMessages(Integer folderId, Integer companyId) throws FinderException;

    public Integer ejbHomeSelectCountUnReadMessages(Integer folderId, Integer companyId) throws FinderException {
        return ejbSelectCountUnReadMessages(folderId, companyId);
    }

    public abstract Long ejbSelectCalculateFolderSize(Integer folderid, Integer companyId) throws FinderException;

    public Long ejbHomeSelectCalculateFolderSize(Integer folderId, Integer companyId) throws FinderException {
        return ejbSelectCalculateFolderSize(folderId, companyId);
    }

    public abstract Collection ejbSelectGetIncompleteEmails(Integer folderId, Integer companyId) throws FinderException;

    public Collection ejbHomeSelectGetIncompleteEmails(Integer folderId, Integer companyId) throws FinderException {
        return ejbSelectGetIncompleteEmails(folderId, companyId);
    }

    public abstract Collection ejbSelectEmailIdsToSend(Integer folderId, Integer companyId) throws FinderException;

    public Collection ejbHomeSelectEmailIdsToSend(Integer folderId, Integer companyId) throws FinderException {
        return ejbSelectEmailIdsToSend(folderId, companyId);
    }

    public abstract Boolean getSaveEmail();

    public abstract void setSaveEmail(Boolean saveEmail);

    public abstract Boolean getCreateContact();

    public abstract void setCreateContact(Boolean createContact);

    public abstract String getMessageId();

    public abstract void setMessageId(String messageId);

    public abstract Boolean getHaveAttachments();

    public abstract void setHaveAttachments(Boolean attachment);

    public abstract Boolean getIsNewEmail();

    public abstract void setIsNewEmail(Boolean isNewEmail);

    public abstract Boolean getIsCompleteEmail();

    public abstract void setIsCompleteEmail(Boolean isCompleteEmail);

    public abstract String getUidl();

    public abstract void setUidl(String uidl);

    public abstract Boolean getProcessToSent();

    public abstract void setProcessToSent(Boolean processToSent);

    public abstract Boolean getIsDraftTemp();

    public abstract void setIsDraftTemp(Boolean isDraftTemp);
}
