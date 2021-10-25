package com.piramide.elwis.domain.webmailmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.2
 */
public interface EmailAccountErrorDetailHome extends EJBLocalHome {
    EmailAccountErrorDetail create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.webmailmanager.EmailAccountErrorDetail findByPrimaryKey(Integer key) throws FinderException;

    Collection findByEmailAccountErrorId(Integer emailAccountErrorId, Integer companyId) throws FinderException;

    Collection findByEmailAccountErrorIdMailIdentifier(Integer emailAccountErrorId, Integer mailIdentifier) throws FinderException;

}
