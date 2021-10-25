package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 6.5
 */
public interface DemoAccountHome extends EJBLocalHome {
    DemoAccount create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.admin.DemoAccount findByPrimaryKey(Integer key) throws FinderException;
}
