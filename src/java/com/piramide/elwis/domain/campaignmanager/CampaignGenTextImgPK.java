package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * @author Miky
 * @version $Id: CampaignGenTextImgPK.java 2009-06-26 05:58:43 PM $
 */
public class CampaignGenTextImgPK implements Serializable {
    public Integer campaignGenTextId;
    public Integer imageStoreId;

    public CampaignGenTextImgPK() {
    }

    public CampaignGenTextImgPK(Integer campaignGenTextId, Integer imageStoreId) {
        this.campaignGenTextId = campaignGenTextId;
        this.imageStoreId = imageStoreId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignGenTextImgPK that = (CampaignGenTextImgPK) o;

        if (campaignGenTextId != null ? !campaignGenTextId.equals(that.campaignGenTextId) : that.campaignGenTextId != null) {
            return false;
        }
        if (imageStoreId != null ? !imageStoreId.equals(that.imageStoreId) : that.imageStoreId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = campaignGenTextId != null ? campaignGenTextId.hashCode() : 0;
        result = 31 * result + (imageStoreId != null ? imageStoreId.hashCode() : 0);
        return result;
    }
}
