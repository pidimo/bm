/**
 * Jatun S.R.L.
 * @author Ivan
 */
package com.piramide.elwis.domain.financemanager;

import javax.ejb.EJBLocalObject;

public interface ReminderLevel extends EJBLocalObject {
    Integer getReminderLevelId();

    void setReminderLevelId(Integer reminderLevelId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    java.math.BigDecimal getFee();

    void setFee(java.math.BigDecimal fee);

    Integer getLevel();

    void setLevel(Integer level);

    String getName();

    void setName(String name);

    Integer getNumberOfDays();

    void setNumberOfDays(Integer numberofdays);

    Integer getVersion();

    void setVersion(Integer version);

    java.util.Collection getReminderTexts();

    void setReminderTexts(java.util.Collection reminderText);
}
