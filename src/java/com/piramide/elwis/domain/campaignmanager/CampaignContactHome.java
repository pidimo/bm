/**
 * AlfaCentauro Team
 * @author Yumi
 * @version $Id: CampaignContactHome.java 10484 2014-08-28 22:51:28Z miguel ${NAME}.java, v 2.0 17-jun-2004 16:15:10 Yumi Exp $
 */
package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

public interface CampaignContactHome extends EJBLocalHome {

    CampaignContact findByPrimaryKey(CampaignContactPK key) throws FinderException;

    CampaignContact findByCampaignContactKey(Integer campaignId, Integer campaignContactId) throws FinderException;

    CampaignContact findByAddressIdContactPersonId(Integer companyId, Integer contactPersonId, Integer addressId) throws FinderException;

    Collection findByCampaignIdUNIQUEContactPersonId(Integer companyId, Integer campaignId, Integer contactPersonId) throws FinderException;

    Collection findByCampaignIdContactPersonId(Integer companyId, Integer campaignId, Integer addressId, Integer contactPersonId) throws FinderException;

    Collection findByCampaignIdContactPersonNULL(Integer companyId, Integer campaignId, Integer addressId) throws FinderException;

    Collection findByCampaignKey(Integer campaignId, Integer status) throws FinderException;

    Collection findEnabledCampaignContactsByActivity(Integer activityId) throws FinderException;

    Integer selectCountEnabledCampaignContactsByActivity(Integer activityId) throws FinderException;

    Collection findUserIdsDISTINTCForEnabledCampaignContactsByActivity(Integer activityId) throws FinderException;

    /**
     * get all recipients of an campaign
     */
    Collection findByCampaignIdAndActivityIdNULL(Integer campaignId) throws FinderException;

    Integer selectCountByCampaignIdAndActivityIdNULL(Integer campaignId) throws FinderException;

    CampaignContact findByActivityIdAddressIdContactPersonNULL(Integer activityId, Integer addressId) throws FinderException;

    CampaignContact findByActivityIdAddressIdContactPersonId(Integer activityId, Integer addressId, Integer contactPersonId) throws FinderException;

    Collection findByAddress(Integer addressId) throws FinderException;

    Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;


    Integer selectCountByActivityIdWithoutResponsible(Integer activityId) throws FinderException;

    Collection findByActivityIdWithoutResponsible(Integer activityId) throws FinderException;

    CampaignContact create(ComponentDTO dto) throws CreateException;
}
