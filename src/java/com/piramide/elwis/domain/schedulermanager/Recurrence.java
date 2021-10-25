package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;
import java.util.Collection;

/**
 * AlfaCentauro Team
 *
 * @author Fernando Montaño
 * @version $Id: Recurrence.java 9121 2009-04-17 00:28:59Z fernando ${NAME}.java, v 2.0 25-04-2005 11:26:13 AM Fernando Montaño Exp $
 */


public interface Recurrence extends EJBLocalObject {
    Integer getAppointmentId();

    void setAppointmentId(Integer appointmentId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getRecurEvery();

    void setRecurEvery(Integer recurEvery);

    Integer getRangeType();

    void setRangeType(Integer rangeType);

    Integer getRangeValue();

    void setRangeValue(Integer rangeValue);

    Integer getRuleType();

    void setRuleType(Integer ruleType);

    String getRuleValue();

    void setRuleValue(String ruleValue);

    Integer getRuleValueType();

    void setRuleValueType(Integer ruleValueType);

    Appointment getAppointment();

    void setAppointment(Appointment appointment);

    Collection getExceptions();

    void setExceptions(Collection exceptions);
}
