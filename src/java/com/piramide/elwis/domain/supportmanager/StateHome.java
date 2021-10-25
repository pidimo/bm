package com.piramide.elwis.domain.supportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:46:04 PM
 * To change this template use File | Settings | File Templates.
 */

public interface StateHome extends EJBLocalHome {
    State findByPrimaryKey(Integer key) throws FinderException;

    State findByOpenStage(Integer companyId, Integer type) throws FinderException;

    public State create(ComponentDTO dto) throws CreateException;

    public Collection findSupportCatalogByCompanyId(Integer companyId) throws FinderException;
}
