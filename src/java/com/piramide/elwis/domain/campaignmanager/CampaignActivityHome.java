package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 13-oct-2006 18:43:21 Miky Exp $
 */

public interface CampaignActivityHome extends EJBLocalHome {
    public CampaignActivity create(ComponentDTO dto) throws CreateException;

    CampaignActivity findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCampaignIdWithoutThisActivity(Integer companyId, Integer campaignId, Integer activityId) throws FinderException;
}
