/**
 * @author Ivan Alban
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface InvoicePositionBatchService extends EJBLocalObject {
    public void updateDiscount();
}
