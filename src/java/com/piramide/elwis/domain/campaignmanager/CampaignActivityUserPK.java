package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 * Campaign activity user Entity bean primary key class.
 *
 * @author Miky
 * @version $Id: CampaignActivityUserPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignActivityUserPK implements Serializable {
    public Integer activityId;
    public Integer userId;

    public CampaignActivityUserPK() {

    }

    public CampaignActivityUserPK(Integer userId, Integer activityId) {
        this.userId = userId;
        this.activityId = activityId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CampaignActivityUserPK that = (CampaignActivityUserPK) o;

        if (activityId != null ? !activityId.equals(that.activityId) : that.activityId != null) {
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (activityId != null ? activityId.hashCode() : 0);
        result = 29 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }
}
