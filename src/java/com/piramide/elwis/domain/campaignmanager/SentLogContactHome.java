package com.piramide.elwis.domain.campaignmanager;

import net.java.dev.strutsejb.dto.ComponentDTO;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;
import java.util.Collection;

/**
 * @author Miguel A. Rojas Cardenas
 * @version 4.9
 */
public interface SentLogContactHome extends EJBLocalHome {
    public SentLogContact create(ComponentDTO dto) throws CreateException;

    SentLogContact findByPrimaryKey(Integer key) throws FinderException;

    Collection findByCampaignSentLogId(Integer campaignSentLogId) throws FinderException;

    Collection findByFailedCampaignSentLogId(Integer campaignSentLogId) throws FinderException;

    Integer selectCountByFailedCampaignSentLogId(Integer campaignSentLogId) throws FinderException;

    Integer selectCountByCampaignSentLogIdSuccess(Integer campaignSentLogId) throws FinderException;

    Integer selectCountByCampaignSentLogId(Integer campaignSentLogId) throws FinderException;

    Collection findByCampaignSentLogIdStatus(Integer campaignSentLogId, Integer status) throws FinderException;

    Integer selectCountByGenerationKeySuccess(Long generationKey) throws FinderException;

    Integer selectCountByGenerationKeyFailed(Long generationKey) throws FinderException;

    Collection findByAddress(Integer addressId) throws FinderException;

    Collection findByContactPerson(Integer addressId, Integer contactPersonId) throws FinderException;

    public Integer selectMinSentLogContactIdByStatus(Integer status) throws FinderException;

    public Integer selectCountByCampaignSentLogIdProcessInBackground(Integer campaignSentLogId) throws FinderException;

}
