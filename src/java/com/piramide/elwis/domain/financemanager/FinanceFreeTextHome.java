/**
 * Jatun S.R.L
 *
 * @author ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface FinanceFreeTextHome extends EJBLocalHome {

    FinanceFreeText findByPrimaryKey(Integer key) throws FinderException;

    FinanceFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;
}
