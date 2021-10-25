/**
 * @author ivan
 *
 * Jatun S.R.L. 
 */
package com.piramide.elwis.domain.webmailmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface EmailSourceHome extends EJBLocalHome {
    EmailSource create(Integer mailId, Integer companyId, byte[] source) throws CreateException;

    EmailSource findByPrimaryKey(Integer key) throws FinderException;

    EmailSource findByMailId(Integer mailId, Integer companyId) throws FinderException;
}
