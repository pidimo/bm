/**
 * @author: ivan
 * Date: 30-10-2006: 03:55:54 PM
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignTemplateHome extends EJBLocalHome {
    public CampaignTemplate create(ComponentDTO dto) throws CreateException;

    com.piramide.elwis.domain.campaignmanager.CampaignTemplate findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByCampaignId(Integer campaignId) throws FinderException;

    public Collection findByCampaignIdAndDocumentType(Integer campaignId, Integer documentType) throws FinderException;
}
