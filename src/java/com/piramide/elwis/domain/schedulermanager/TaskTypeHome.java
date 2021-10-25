package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Jun 29, 2005
 * Time: 2:14:05 PM
 * To change this template use File | Settings | File Templates.
 */

public interface TaskTypeHome extends EJBLocalHome {
    Collection findAll() throws FinderException;

    TaskType findByPrimaryKey(Integer key) throws FinderException;

    TaskType create(ComponentDTO dto) throws CreateException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;
}
