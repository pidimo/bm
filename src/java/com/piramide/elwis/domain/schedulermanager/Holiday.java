package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 5, 2005
 * Time: 11:17:16 AM
 * To change this template use File | Settings | File Templates.
 */

public interface Holiday extends EJBLocalObject {
    Integer getDay();

    void setDay(Integer day);

    Integer getHolidayId();

    void setHolidayId(Integer holiday);

    Integer getMonth();

    void setMonth(Integer month);

    Boolean getMoveToMonday();

    void setMoveToMonday(Boolean moveToMonday);

    String getTitle();

    void setTitle(String title);

    Integer getOccurrence();

    void setOccurrence(Integer occurrence);

    Integer getCountryId();

    void setCountryId(Integer countryId);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    Integer getVersion();

    void setVersion(Integer version);

    Integer getHolidayType();

    void setHolidayType(Integer miodemid);
}
