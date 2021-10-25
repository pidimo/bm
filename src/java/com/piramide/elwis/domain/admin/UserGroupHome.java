package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Apr 22, 2005
 * Time: 3:42:34 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserGroupHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    UserGroup findByPrimaryKey(Integer key) throws FinderException;

    UserGroup create(ComponentDTO dto) throws CreateException;

    Collection findByCompanyId(Integer companyId) throws FinderException;

    Collection findByUserGroupName(String groupName, Integer companyId) throws FinderException;

    Collection findByCompanyAndType(Integer companyId, Integer groupType) throws FinderException;
}
