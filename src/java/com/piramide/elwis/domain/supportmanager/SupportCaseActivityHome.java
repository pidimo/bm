package com.piramide.elwis.domain.supportmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 2:52:17 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseActivityHome extends EJBLocalHome {
    SupportCaseActivity findByPrimaryKey(Integer key) throws FinderException;

    SupportCaseActivity findByTypeOpen(Integer caseId, Integer typeOpen) throws FinderException;

    public SupportCaseActivity create(ComponentDTO dto) throws CreateException;
}
