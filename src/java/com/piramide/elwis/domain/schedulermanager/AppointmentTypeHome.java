package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 6, 2005
 * Time: 3:35:14 PM
 * To change this template use File | Settings | File Templates.
 */

public interface AppointmentTypeHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    AppointmentType findByPrimaryKey(Integer key) throws FinderException;

    public AppointmentType create(ComponentDTO dto) throws CreateException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

}
