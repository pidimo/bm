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
 * Time: 3:33:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ReminderHome extends EJBLocalHome {
    public Collection findAll() throws FinderException;

    Reminder findByPrimaryKey(Integer key) throws FinderException;

    public Reminder create(ComponentDTO dto) throws CreateException;

    public Collection findAllBetweenDate(Long initial, Long ending) throws FinderException;

    public Collection findAllLesserThanDate(Long initialTime) throws FinderException;
}
