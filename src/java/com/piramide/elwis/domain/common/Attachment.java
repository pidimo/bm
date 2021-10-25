package com.piramide.elwis.domain.common;

import javax.ejb.EJBLocalObject;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: Attachment.java 8277 2008-06-12 00:56:40Z miguel ${NAME}.java, v1.0 05-jun-2008 17:10:04 Miky Exp $
 */

public interface Attachment extends EJBLocalObject {
    Integer getAttachmentId();

    void setAttachmentId(Integer attachmentId);

    Integer getAttachmentType();

    void setAttachmentType(Integer attachmentType);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getContentType();

    void setContentType(String contentType);

    String getFileName();

    void setFileName(String fileName);

    Integer getFileSize();

    void setFileSize(Integer fileSize);

    Integer getFreeTextId();

    void setFreeTextId(Integer freeTextId);

    Integer getVersion();

    void setVersion(Integer version);

    FileFreeText getFileFreeText();

    void setFileFreeText(FileFreeText fileFreeText);

    public void setFileFreeText(EJBLocalObject fileFreeText);
}
