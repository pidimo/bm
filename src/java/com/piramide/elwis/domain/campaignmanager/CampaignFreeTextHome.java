/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignFreeTextHome.java 2204 2004-08-11 20:26:32Z fernando ${NAME}.java, v 2.0 28-jul-2004 15:09:36 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface CampaignFreeTextHome extends EJBLocalHome {

    public CampaignFreeText create(byte[] value, Integer companyId, Integer type) throws CreateException;

    CampaignFreeText findByPrimaryKey(Integer key) throws FinderException;
}
