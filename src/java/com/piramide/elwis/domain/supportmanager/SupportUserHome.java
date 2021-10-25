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
 * Time: 3:22:05 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportUserHome extends EJBLocalHome {

    SupportUser findByPrimaryKey(SupportUserPK key) throws FinderException;

    Collection findAllByProduct(Integer produtId, Integer companyId) throws FinderException;

    Collection findAllByCompany(Integer companyId) throws FinderException;

    SupportUser findByProductAndUser(Integer companyId, Integer productId, Integer userId) throws FinderException;

    public SupportUser create(ComponentDTO dto) throws CreateException;

}
