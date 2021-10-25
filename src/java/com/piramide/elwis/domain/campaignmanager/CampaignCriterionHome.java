/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignCriterionHome.java 7936 2007-10-27 16:08:39Z fernando ${NAME}.java, v 2.0 17-jun-2004 16:22:21 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignCriterionHome extends EJBLocalHome {

    public Collection findAll() throws FinderException;

    CampaignCriterion findByPrimaryKey(Integer key) throws FinderException;

    CampaignCriterion findByCampaignIdAndCriterionValueId(Integer campaignId, Integer campCriterionValueId) throws FinderException;

    CampaignCriterion findByCampaignIdAndCategoryId(Integer campaignId, Integer categoryId) throws FinderException;

    Collection findByManyCampaignIdAndCategoryId(Integer campaignId, Integer categoryId) throws FinderException;

    Collection findByManyCampaignIdAndCriterionValueId(Integer campaignId, Integer campCriterionValueId) throws FinderException;


    public CampaignCriterion create(ComponentDTO dto) throws CreateException;

}
