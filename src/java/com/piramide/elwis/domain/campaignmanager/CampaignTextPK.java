package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * @author Yumi
 * @version $Id: CampaignTextPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignTextPK implements Serializable {
    public Integer languageId;
    public Integer templateId;

    public CampaignTextPK() {
    }

    public CampaignTextPK(Integer languageId, Integer templateId) {
        this.languageId = languageId;
        this.templateId = templateId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CampaignTextPK that = (CampaignTextPK) o;

        if (languageId != null ? !languageId.equals(that.languageId) : that.languageId != null) {
            return false;
        }
        if (templateId != null ? !templateId.equals(that.templateId) : that.templateId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (languageId != null ? languageId.hashCode() : 0);
        result = 31 * result + (templateId != null ? templateId.hashCode() : 0);
        return result;
    }
}
