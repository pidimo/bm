/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignCriterionValueHome.java 2075 2004-08-03 14:55:47Z mauren ${NAME}.java, v 2.0 28-jul-2004 16:55:41 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignCriterionValueHome extends EJBLocalHome {

    //    public CampaignCriterionValue create(Integer companyId) throws CreateException;
    CampaignCriterionValue findByPrimaryKey(Integer key) throws FinderException;

    public Collection findByTableId(Integer key) throws FinderException;
}
