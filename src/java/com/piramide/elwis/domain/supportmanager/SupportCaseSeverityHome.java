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
 * Time: 3:50:51 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseSeverityHome extends EJBLocalHome {

    SupportCaseSeverity findByPrimaryKey(Integer key) throws FinderException;

    public SupportCaseSeverity create(ComponentDTO dto) throws CreateException;

    public Collection findSupportCatalogByCompanyId(Integer companyId) throws FinderException;

}
