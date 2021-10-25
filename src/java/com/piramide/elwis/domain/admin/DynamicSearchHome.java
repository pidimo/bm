package com.piramide.elwis.domain.admin;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 5.1
 */
public interface DynamicSearchHome extends EJBLocalHome {
    DynamicSearch create(ComponentDTO dto) throws CreateException;
    com.piramide.elwis.domain.admin.DynamicSearch findByPrimaryKey(Integer key) throws FinderException;

    public DynamicSearch findByNameModuleUser(String name, String module, Integer userId) throws FinderException;
}
