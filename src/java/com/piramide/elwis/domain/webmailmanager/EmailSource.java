/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.EJBLocalObject;

public interface EmailSource extends EJBLocalObject {

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getFileSize();

    void setFileSize(Integer fileSize);

    Integer getMailId();

    void setMailId(Integer mailId);

    byte[] getSource();

    void setSource(byte[] source);
}
