/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-05-25 02:38:51 PM $
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

public interface ImageStore extends EJBLocalObject {
    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    byte[] getFileData();

    void setFileData(byte[] fileData);

    String getFileName();

    void setFileName(String fileName);

    String getSessionId();

    void setSessionId(String sessionId);

    Integer getImageStoreId();

    void setImageStoreId(Integer imageStoreId);

    Integer getImageType();

    void setImageType(Integer imageType);
}
