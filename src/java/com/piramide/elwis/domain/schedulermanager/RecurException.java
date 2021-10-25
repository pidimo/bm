package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: RecurException.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 12-04-2005 03:24:18 PM Fernando Montaño Exp $
 */


public interface RecurException extends EJBLocalObject {
    Integer getRecurExceptionId();

    void setRecurExceptionId(Integer recurExceptionId);

    Integer getAppointmentId();

    void setAppointmentId(Integer appointmentId);

    Integer getDateValue();

    void setDateValue(Integer dateValue);

    Appointment getAppointment();

    void setAppointment(Appointment appointment);

    Recurrence getRecurrence();

    void setRecurrence(Recurrence recurrence);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);
}
