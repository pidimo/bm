package com.piramide.elwis.domain.supportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author : alejandro
 *         Date: Aug 11, 2005
 *         Time: 2:41:42 PM
 */

public interface SupportCaseTypeHome extends EJBLocalHome {
    SupportCaseType findByPrimaryKey(Integer key) throws FinderException;

    public SupportCaseType create(ComponentDTO dto) throws CreateException;

    public Collection findSupportCatalogByCompanyId(Integer companyId) throws FinderException;
}
