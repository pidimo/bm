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
 * Time: 2:31:58 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportCaseHome extends EJBLocalHome {
    SupportCase findByPrimaryKey(Integer key) throws FinderException;

    Collection findAllByProductWhithNotCloseStage(Integer companyId, Integer productId) throws FinderException;

    Collection findAllByNotCloseStage(Integer companyId) throws FinderException;

    Collection findByAddress(Integer addressId) throws FinderException;

    Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;

    public SupportCase create(ComponentDTO dto) throws CreateException;

    String selectMaxSupportCaseNumber(Integer companyId) throws FinderException;
}
