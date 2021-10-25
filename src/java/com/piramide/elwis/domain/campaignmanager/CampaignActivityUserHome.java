package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignActivityUserHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 25-oct-2006 15:54:31 Miky Exp $
 */

public interface CampaignActivityUserHome extends EJBLocalHome {
    CampaignActivityUser findByPrimaryKey(CampaignActivityUserPK key) throws FinderException;

    CampaignActivityUser create(ComponentDTO dto) throws CreateException;
}
