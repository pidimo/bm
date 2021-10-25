package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * @author: ivan
 * Date: 01-11-2006: 01:15:53 PM
 */
public class CampaignActivityContactPK implements Serializable {
    public Integer campaignId;
    public Integer contactId;


    public CampaignActivityContactPK() {
    }


    public CampaignActivityContactPK(Integer campaignId, Integer contactId) {
        this.campaignId = campaignId;
        this.contactId = contactId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampaignActivityContactPK)) {
            return false;
        }

        final CampaignActivityContactPK campaignActivityContactPK = (CampaignActivityContactPK) o;

        if (contactId != null ? !contactId.equals(campaignActivityContactPK.contactId) : campaignActivityContactPK.contactId != null) {
            return false;
        }
        if (campaignId != null ? !campaignId.equals(campaignActivityContactPK.campaignId) : campaignActivityContactPK.campaignId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (contactId != null ? contactId.hashCode() : 0);
        result = 29 * result + (campaignId != null ? campaignId.hashCode() : 0);
        return result;
    }
}
