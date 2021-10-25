package com.piramide.elwis.domain.schedulermanager;

import javax.ejb.EJBLocalObject;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:35:15 PM
 * To change this template use File | Settings | File Templates.
 */

public interface AppointmentType extends EJBLocalObject {

    Integer getAppointmentTypeId();

    void setAppointmentTypeId(Integer appoinmentTypeId);

    String getName();

    void setName(String name);

    Integer getCompanyId();

    void setCompanyId(Integer companyId);

    String getColor();

    void setColor(String color);

    Integer getVersion();

    void setVersion(Integer version);
}
