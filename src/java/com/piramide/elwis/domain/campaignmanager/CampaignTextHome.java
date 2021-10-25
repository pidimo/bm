/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignTextHome.java 9761 2009-09-29 19:43:01Z miguel ${NAME}.java, v 2.0 03-ago-2004 20:17:19 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignTextHome extends EJBLocalHome {
    public CampaignText create(ComponentDTO dto) throws CreateException;

    CampaignText findByPrimaryKey(CampaignTextPK key) throws FinderException;

    /**
     * @param campaignId
     * @return
     * @throws FinderException
     * @deprecated
     */
    CampaignText findDefaultCampaign(Integer campaignId) throws FinderException;

    CampaignText findDefaultTemplate(Integer campaignId) throws FinderException;

    Collection findByTemplateId(Integer templateId, Integer companyId) throws FinderException;

    CampaignText findDefaultByTemplate(Integer templateId, Integer companyId) throws FinderException;

    CampaignText findByDocumentFreeTextId(Integer freeTextId) throws FinderException;
}
