package com.piramide.elwis.domain.contactmanager;

import javax.ejb.EJBLocalObject;

/**
 * @author Yumi
 * @version $Id: ContactAttach.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v 2.0 13-may-2004 18:25:50 Yumi Exp $
 */

public interface ContactAttach extends EJBLocalObject {

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getContactId();

    void setContactId(Integer contactId);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    String getFileName();

    void setFileName(String fileName);

    Integer getFileSize();

    void setFileSize(Integer fileSize);

    String getContentType();

    void setContentType(String contentType);
}
