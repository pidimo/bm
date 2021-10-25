package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * AlfaCentauro Team
 *
 * @author Yumi
 * @version $Id: CampaignContactPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignContactPK implements Serializable {
    public Integer campaignContactId;
    public Integer campaignId;

    public CampaignContactPK() {
    }

    public CampaignContactPK(Integer campaignContactId, Integer campaignId) {
        this.campaignContactId = campaignContactId;
        this.campaignId = campaignId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignContactPK)) {
            return false;
        }

        final CampaignContactPK campaignContactPK = (CampaignContactPK) o;

        if (campaignContactId != null ? !campaignContactId.equals(campaignContactPK.campaignContactId) : campaignContactPK.campaignContactId != null) {
            return false;
        }
        if (campaignId != null ? !campaignId.equals(campaignContactPK.campaignId) : campaignContactPK.campaignId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (campaignContactId != null ? campaignContactId.hashCode() : 0);
        result = 29 * result + (campaignId != null ? campaignId.hashCode() : 0);
        return result;
    }
}
