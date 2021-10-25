package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * @author Miky
 * @version $Id: CampaignTextImgPK.java 2009-06-23 05:14:55 PM $
 */
public class CampaignTextImgPK implements Serializable {
    public Integer imageStoreId;
    public Integer languageId;
    public Integer templateId;

    public CampaignTextImgPK() {
    }

    public CampaignTextImgPK(Integer imageStoreId, Integer languageId, Integer templateId) {
        this.imageStoreId = imageStoreId;
        this.languageId = languageId;
        this.templateId = templateId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignTextImgPK that = (CampaignTextImgPK) o;

        if (imageStoreId != null ? !imageStoreId.equals(that.imageStoreId) : that.imageStoreId != null) {
            return false;
        }
        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(that.templateId) : that.templateId != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = imageStoreId != null ? imageStoreId.hashCode() : 0;
        result = 31 * result + (languageId != null ? languageId.hashCode() : 0);
        result = 31 * result + (templateId != null ? templateId.hashCode() : 0);
        return result;
    }
}
