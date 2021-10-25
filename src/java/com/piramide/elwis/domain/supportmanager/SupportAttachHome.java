package com.piramide.elwis.domain.supportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:29:08 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportAttachHome extends EJBLocalHome {
    SupportAttach findByPrimaryKey(Integer key) throws FinderException;

    public SupportAttach create(ComponentDTO dto) throws CreateException;
}
