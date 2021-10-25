package com.piramide.elwis.domain.campaignmanager;

import java.io.Serializable;

/**
 * Jatun S.R.L.
 * Campaign generation attach Entity bean primary key class.
 *
 * @author Miky
 * @version $Id: CampaignGenAttachPK.java 9703 2009-09-12 15:46:08Z fernando $
 */
public class CampaignGenAttachPK implements Serializable {
    public Integer attachmentId;
    public Integer generationId;

    public CampaignGenAttachPK() {
    }

    public CampaignGenAttachPK(Integer attachmentId, Integer generationId) {
        this.attachmentId = attachmentId;
        this.generationId = generationId;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final CampaignGenAttachPK that = (CampaignGenAttachPK) o;

        if (attachmentId != null ? !attachmentId.equals(that.attachmentId) : that.attachmentId != null) {
            return false;
        }
        if (generationId != null ? !generationId.equals(that.generationId) : that.generationId != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        int result;
        result = (attachmentId != null ? attachmentId.hashCode() : 0);
        result = 29 * result + (generationId != null ? generationId.hashCode() : 0);
        return result;
    }
}
