/**
 * @author Ivan Alban
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface InvoicePositionBatchServiceHome extends EJBLocalHome {
    InvoicePositionBatchService create() throws CreateException;
}
