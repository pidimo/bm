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
 * @version $Id: CampaignGenerationHome.java 12581 2016-08-26 22:42:33Z miguel ${NAME}.java, v1.0 30-may-2008 11:03:25 Miky Exp $
 */

public interface CampaignGenerationHome extends EJBLocalHome {
    public CampaignGeneration create(ComponentDTO dto) throws CreateException;

    CampaignGeneration findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByGenerationTime(Integer activityId, Integer templateId, Long generationTime) throws FinderException;

    public Collection findByGenerationTime(Integer templateId, Long generationTime) throws FinderException;

    public Collection findByTemplateId(Integer templateId) throws FinderException;

    public Collection findByCampaignId(Integer campaignId) throws FinderException;
}
