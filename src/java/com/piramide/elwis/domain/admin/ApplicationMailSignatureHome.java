/**
 * @author : Ivan
 *
 * Jatun S.R.L
 */
package com.piramide.elwis.domain.admin;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface ApplicationMailSignatureHome extends EJBLocalHome {
    ApplicationMailSignature create(String languageIso, byte[] htmlSignature, byte[] textSignature) throws CreateException;

    ApplicationMailSignature findByPrimaryKey(String key) throws FinderException;
}
