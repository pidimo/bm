package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public interface CampaignSentLogHome extends EJBLocalHome {
    public CampaignSentLog create(ComponentDTO dto) throws CreateException;

    CampaignSentLog findByPrimaryKey(Integer key) throws FinderException;

    Integer selectTotalSentByGenerationKey(Long generationKey) throws FinderException;
}
