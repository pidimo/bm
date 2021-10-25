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
 * Time: 3:46:24 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserOfGroupHome extends EJBLocalHome {

    UserOfGroup findByPrimaryKey(UserOfGroupPK key) throws FinderException;

    Collection findByUserGroupId(Integer groupId) throws FinderException;

    UserOfGroup create(ComponentDTO dto) throws CreateException;
}
