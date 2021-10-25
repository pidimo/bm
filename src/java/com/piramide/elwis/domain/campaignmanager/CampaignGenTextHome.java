package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * Jatun S.R.L.
 *
 * @author Miky
 * @version $Id: CampaignGenTextHome.java 9695 2009-09-10 21:34:43Z fernando ${NAME}.java, v1.0 30-may-2008 11:15:07 Miky Exp $
 */

public interface CampaignGenTextHome extends EJBLocalHome {
    public CampaignGenText create(ComponentDTO dto) throws CreateException;

    CampaignGenText findByPrimaryKey(Integer key) throws FinderException;
}
