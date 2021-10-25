/**
 * @author: ivan
 * Date: 01-11-2006: 01:14:24 PM
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignActivityContactHome extends EJBLocalHome {
    CampaignActivityContact create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.campaignmanager.CampaignActivityContact findByPrimaryKey(CampaignActivityContactPK key) throws FinderException;

    public CampaignActivityContact findByContactId(Integer contactId) throws FinderException;

    CampaignActivityContact findByContactIdAndGenerationIdISNOTNULL(Integer contactId) throws FinderException;

    Collection findByCampaignId(Integer campaignId) throws FinderException;
}
