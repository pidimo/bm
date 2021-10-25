package com.piramide.elwis.domain.supportmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: alejandro
 * Date: Aug 11, 2005
 * Time: 3:26:11 PM
 * To change this template use File | Settings | File Templates.
 */

public interface SupportContactHome extends EJBLocalHome {
    SupportContact findByPrimaryKey(SupportContactPK key) throws FinderException;

    Collection findBySupportCaseActivity(Integer activityId, Integer caseId) throws FinderException;

    Collection findAllSupportContactByCase(Integer caseId) throws FinderException;

    public SupportContact create(Integer activityId, Integer caseId, Integer companyId, Integer contactId) throws CreateException;

    SupportContact findByContactId(Integer contactId, Integer companyId) throws FinderException;
}
