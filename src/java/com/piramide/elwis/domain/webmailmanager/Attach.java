package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Alvaro
 * @version $Id: Attach.java 10225 2012-05-29 17:48:18Z miguel ${NAME}, 02-02-2005 04:27:32 PM alvaro Exp $
 */

public interface Attach extends EJBLocalObject {
    Integer getAttachId();

    void setAttachId(Integer attachId);

    String getAttachName();

    void setAttachName(String attachName);

    byte[] getAttachFile();

    void setAttachFile(byte[] attachFile);

    Integer getMailId();

    void setMailId(Integer attachMailId);

    Integer getCompanyId();

    void setCompanyId(Integer attachCompanyId);

    Boolean getVisible();

    void setVisible(Boolean visible);

    Integer getSize();

    void setSize(Integer size);

    String getEmlAttachUUID();

    void setEmlAttachUUID(String emlAttachUUID);
}
