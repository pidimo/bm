/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: ${NAME}.java  06-nov-2009 17:55:46$
 */
package com.piramide.elwis.service.sales;

import javax.ejb.EJBLocalObject;
import java.util.Date;

public interface ContractEndReminderService extends EJBLocalObject {

   public void performContractEndReminder(Date timeOfCall, long intervalBetweenChecks); 
}
