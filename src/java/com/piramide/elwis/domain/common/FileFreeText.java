package com.piramide.elwis.domain.common;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Mar 16, 2005
 * Time: 10:20:48 AM
 * To change this template use File | Settings | File Templates.
 */

public interface FileFreeText extends EJBLocalObject {
    Integer getFileId();

    void setFileId(Integer fileId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    byte[] getValue();

    void setValue(byte[] value);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getType();

    void setType(Integer type);
}
