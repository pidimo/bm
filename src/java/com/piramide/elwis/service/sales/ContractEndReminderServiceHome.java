/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ${NAME}.java  06-nov-2009 17:55:46$
 */
package com.piramide.elwis.service.sales;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;

public interface ContractEndReminderServiceHome extends EJBLocalHome {
    com.piramide.elwis.service.sales.ContractEndReminderService create() throws CreateException;
}
