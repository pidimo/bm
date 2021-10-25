package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: Sep 23, 2005
 * Time: 2:57:25 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserSessionLogHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    public UserSessionLog create(ComponentDTO dto) throws CreateException;

    UserSessionLog findByPrimaryKey(Integer key) throws FinderException;

    UserSessionLog findBySessionId(String key) throws FinderException;

    Collection findAllConnected(Boolean isConnected) throws FinderException;

    Collection selectLoadLoggedUserIds() throws FinderException;

    Collection findConnectedUsers(Integer companyId) throws FinderException;
}
