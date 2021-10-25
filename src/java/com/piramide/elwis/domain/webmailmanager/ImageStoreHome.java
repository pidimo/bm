/**
 *
 * @author Miky
 * @version $Id: ${NAME}.java 2009-05-25 02:38:51 PM $
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface ImageStoreHome extends EJBLocalHome {
    ImageStore findByPrimaryKey(Integer key) throws FinderException;

    ImageStore create(byte[] fileData, String fileName, Integer companyId, String sessionId, Integer type) throws CreateException;

    public Collection findBySessionId(String sessionId, Integer imageType) throws FinderException;
}
