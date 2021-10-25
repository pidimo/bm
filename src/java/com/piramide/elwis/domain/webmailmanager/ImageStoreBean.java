/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-05-25 02:38:51 PM $
 */
package com.piramide.elwis.domain.webmailmanager;

import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.WebMailConstants;

import javax.ejb.*;

public abstract class ImageStoreBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(byte[] fileData, String fileName, Integer companyId, String sessionId, Integer type) throws CreateException {
        this.setImageStoreId(PKGenerator.i.nextKey(WebMailConstants.TABLE_IMAGESTORE));
        this.setFileData(fileData);
        this.setFileName(fileName);
        this.setCompanyId(companyId);
        this.setSessionId(sessionId);
        this.setImageType(type);
        return null;
    }

    public void ejbPostCreate(byte[] fileData, String fileName, Integer companyId, String sessionId, Integer type) throws CreateException {

    }

    public ImageStoreBean() {
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

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract byte[] getFileData();

    public abstract void setFileData(byte[] fileData);

    public abstract String getFileName();

    public abstract void setFileName(String fileName);

    public abstract String getSessionId();

    public abstract void setSessionId(String sessionId);

    public abstract Integer getImageStoreId();

    public abstract void setImageStoreId(Integer imageStoreId);

    public abstract Integer getImageType();

    public abstract void setImageType(Integer imageType);
}
