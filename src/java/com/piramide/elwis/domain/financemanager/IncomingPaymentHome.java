/**
 * Jatun S.R.L.
 * @author alvaro
 * @version $Id: ${NAME}.java 18-feb-2009 9:46:34
 */
package com.piramide.elwis.domain.financemanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface IncomingPaymentHome extends EJBLocalHome {
    com.piramide.elwis.domain.financemanager.IncomingPayment findByPrimaryKey(Integer key) throws FinderException;

    public IncomingPayment create(ComponentDTO dto) throws CreateException;

    Integer selectMaxPayDate(Integer incomignInvoiceId, Integer companyId) throws FinderException;
}
