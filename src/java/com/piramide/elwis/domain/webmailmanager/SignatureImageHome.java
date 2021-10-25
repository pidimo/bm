/**
 * @author Ivan Alban
 * @version 4.3.6
 */
package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface SignatureImageHome extends EJBLocalHome {
    SignatureImage create(ComponentDTO dto) throws CreateException;

    SignatureImage findByPrimaryKey(Integer key) throws FinderException;

    Collection findBySignatureId(Integer signatureId, Integer companyId) throws FinderException;
}
