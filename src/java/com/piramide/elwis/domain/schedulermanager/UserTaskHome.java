package com.piramide.elwis.domain.schedulermanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: yumi
 * Date: May 31, 2005
 * Time: 2:11:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface UserTaskHome extends EJBLocalHome {

    Collection findAll() throws FinderException;

    UserTask findByPrimaryKey(Integer key) throws FinderException;

    UserTask create(ComponentDTO dto) throws CreateException;

    Integer selectCountByStatus(Integer taskId, Integer status) throws FinderException;
}
