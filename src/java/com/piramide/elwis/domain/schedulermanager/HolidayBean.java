package com.piramide.elwis.domain.schedulermanager;

import com.piramide.elwis.dto.common.ExtendedDTOFactory;
import com.piramide.elwis.utils.PKGenerator;
import com.piramide.elwis.utils.SchedulerConstants;
import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.*;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Jul 5, 2005
 * Time: 11:17:14 AM
 * To change this template use File | Settings | File Templates.
 */

public abstract class HolidayBean implements EntityBean {
    EntityContext entityContext;

    public Integer ejbCreate(ComponentDTO dto) throws CreateException {
        ExtendedDTOFactory.i.copyFromDTO(dto, this, false);
        setHolidayId(PKGenerator.i.nextKey(SchedulerConstants.TABLE_HOLIDAY));
        setVersion(new Integer(1));
        return null;
    }

    public void ejbPostCreate(ComponentDTO dto) throws CreateException {

    }

    public void setEntityContext(EntityContext entityContext) throws EJBException {
        this.entityContext = entityContext;
    }

    public void unsetEntityContext() throws EJBException {
        this.entityContext = null;
    }

    public void ejbRemove() throws RemoveException, EJBException {
    }

    public void ejbActivate() throws EJBException {
    }

    public void ejbPassivate() throws EJBException {
    }

    public void ejbLoad() throws EJBException {
    }

    public void ejbStore() throws EJBException {
    }

    public abstract Integer getDay();

    public abstract void setDay(Integer day);

    public abstract Integer getHolidayId();

    public abstract void setHolidayId(Integer holiday);

    public abstract Integer getMonth();

    public abstract void setMonth(Integer month);

    public abstract Boolean getMoveToMonday();

    public abstract void setMoveToMonday(Boolean moveToMonday);

    public abstract String getTitle();

    public abstract void setTitle(String title);

    public abstract Integer getOccurrence();

    public abstract void setOccurrence(Integer occurrence);

    public abstract Integer getCountryId();

    public abstract void setCountryId(Integer countryId);

    public abstract Integer getCompanyId();

    public abstract void setCompanyId(Integer companyId);

    public abstract Integer getVersion();

    public abstract void setVersion(Integer version);

    public abstract Integer getHolidayType();

    public abstract void setHolidayType(Integer holidayType);
}
