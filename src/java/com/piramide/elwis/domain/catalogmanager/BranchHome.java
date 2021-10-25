package com.piramide.elwis.domain.catalogmanager;

/**
 * This Class represents the Home interface of Branch Entity Bean
 *
 * @author Ivan
 * @version $Id: BranchHome.java 9579 2009-08-20 23:53:26Z ivan ${NAME}.java, v 2.0 29-abr-2004 11:21:49 Exp $
 */

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface BranchHome extends EJBLocalHome {
    public Branch create(ComponentDTO dto) throws CreateException;

    public Branch findByPrimaryKey(Integer key) throws FinderException;

    public Collection findAll() throws FinderException;

    public Collection findByCompanyId(Integer companyId) throws FinderException;

    public Collection findByBranchName(String branchName, Integer companyId) throws FinderException;
}
